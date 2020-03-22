plugins {
    java
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.sherter.google-java-format") version "0.8"
    id("com.google.cloud.tools.jib") version "2.1.0"
    id("com.palantir.docker-run") version "0.25.0"
    id("com.palantir.git-version") version "0.12.2"
    id("com.cherryperry.gradle-file-encrypt") version "1.4.0"
}

group = "de.pandemieduell"
version = versionDetails.gitHash

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.data:spring-data-mongodb")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

val dockerImage = "pandemieduell/server:${project.version}"
val dockerUser: String? by project
val dockerPassword: String? by project
val deploymentSrc = "${project.rootDir}/src/deployment/kubernetes"
val deploymentKubeConfig = "$deploymentSrc/deployment-account/kubeconfig.secret.yaml"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    to {
        image = dockerImage
        if (dockerUser != null) {
            auth {
                username = dockerUser
                password = dockerPassword
            }
        }
    }
}

dockerRun {
    name = project.name
    image = dockerImage
}

gradleFileEncrypt {
    files = arrayOf(deploymentKubeConfig)
}

task("lint") {
    group = "verification"
    dependsOn(tasks["verifyGoogleJavaFormat"])
}

task("fixStyle") {
    group = "verification"
    dependsOn(tasks["googleJavaFormat"])
}

task("buildDocker") {
    group = "build"
    dependsOn("jibDockerBuild")
}

task("push") {
    group = "publishing"
    dependsOn("jib")
}

val writeDeploymentParameters by tasks.creating {
    group = "deployment"
    val templateFiles = fileTree(deploymentSrc) {
        include("**/*.tpl.*")
    }.files
    val parameters = mapOf(
        "DOCKER_IMAGE" to dockerImage
    )

    fun File.outputFile() = file(path.replace(".tpl", ".filled"))

    inputs.files(templateFiles)
    inputs.properties(parameters)
    outputs.files(templateFiles.map { it.outputFile() })

    doLast {
        templateFiles.forEach { templateFile ->
            var filledText = templateFile.readText()
            for ((key, value) in parameters) {
                filledText = filledText.replace("$$key", value)
            }
            templateFile.outputFile().writeText(filledText)
        }
    }
}

fun kubectlDeployTask(
    kustomization: String,
    commonTag: Pair<String, String>,
    kubeconfig: String? = null,
    block: Exec.() -> Unit = {}
) = tasks.creating(Exec::class) {
    group = "deployment"
    dependsOn(writeDeploymentParameters)

    inputs.files(fileTree(kustomization) {
        exclude("*.encrypted")
        exclude("*.tpl.*")
    })

    commandLine(
        "kubectl", "apply",
        "--kustomize", ".",
        "--prune",
        "--selector", "${commonTag.first}=${commonTag.second}",
        "--wait"
    )
    workingDir(kustomization)
    if (kubeconfig != null) {
        environment("KUBECONFIG", kubeconfig)
    }
    block()
}

val deployDeploymentAccount by kubectlDeployTask(
    kustomization = "$deploymentSrc/deployment-account",
    commonTag = "component" to "deployment-account"
)
val deployDatabase by kubectlDeployTask(
    kustomization = "$deploymentSrc/mongodb",
    commonTag = "component" to "mongo",
    kubeconfig = deploymentKubeConfig
)
val deployServer by kubectlDeployTask(
    kustomization = "$deploymentSrc/server",
    commonTag = "component" to "server",
    kubeconfig = deploymentKubeConfig
) {
    mustRunAfter(deployDatabase)
}

task("deploy") {
    group = "deployment"
    dependsOn(deployDatabase, deployServer)
}

val Project.versionDetails
    get() = (this.extra["versionDetails"] as groovy.lang.Closure<*>)() as com.palantir.gradle.gitversion.VersionDetails
