plugins {
    java
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.sherter.google-java-format") version "0.8"
    id("com.google.cloud.tools.jib") version "2.1.0"
    id("com.palantir.docker-run") version "0.25.0"
    id("com.palantir.git-version") version "0.12.2"
}

group = "de.pandemieduell"
version = versionDetails.gitHash
val dockerImage = "docker.pkg.github.com/pandemieduell/server/${project.name}:${project.version}"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


jib {
    to {
        image = dockerImage
    }
}

dockerRun {
    name = project.name
    image = dockerImage
}

task("lint") {
    dependsOn(tasks["verifyGoogleJavaFormat"])
}

task("fixStyle") {
    dependsOn(tasks["googleJavaFormat"])
}

task("buildDocker") {
    dependsOn("jibDockerBuild")
}

val Project.versionDetails
    get() = (this.extra["versionDetails"] as groovy.lang.Closure<*>)() as com.palantir.gradle.gitversion.VersionDetails
