## Project setup

If you want to execute `docker` locally without root privileges (and without security) issues, you can install [Podman](https://podman.io/) and add an alias: `sudo ln -s /usr/bin/podman /usr/bin/docker`.

## Tasks

 * Build the project: `./gradlew assemble`
 * Test the project: `./gradlew test`
 * Check the code style: `./gradlew lint`
 * Fix the code style: `./gradlew fixStyle` 
 * Build the docker container to your local docker installation: `./gradlew buildDocker`
 * Start/stop the docker container in your local docker installation: `./gradlew dockerRun` / `./gradlew dockerStop`
 * Remove the docker container from your local docker installation: `./gradlew dockerRemoveContainer`
