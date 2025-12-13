Publishing a new Main release to Maven
========

1. Change the version in `gradle.properties` to a non-SNAPSHOT version based on Major.Minor.Patch naming scheme
2. `git commit -am "Prepare for release X.Y.Z."` (where X.Y.Z is the new version)
3. `git tag -a X.Y.X -m "Version X.Y.Z"` (where X.Y.Z is the new version)
4. Add your sonatype login information under gradle properties mavenCentralUsername and mavenCentralPassword in your local user gradle.properties file
5. Make sure you have a gpg signing key configured (https://vanniktech.github.io/gradle-maven-publish-plugin/central/#secrets)
6. Run `./gradlew publishAllPublicationsToMavenCentral` to build the artifacts and publish them to maven
7. Update the `gradle.properties` to the next SNAPSHOT version.
8. `git commit -am "Prepare next development version."`
9. `git push && git push --tags`
10. Merge to master and create a new release through the Github web UI with release notes

Publishing a release to an internal repository
========

To publish an internal release to an Artifactory repository:

1. Set credential values for ARTIFACTORY_USERNAME and ARTIFACTORY_PASSWORD in your local gradle.properties
2. Set values for ARTIFACTORY_RELEASE_URL (and optionally ARTIFACTORY_SNAPSHOT_URL if you are publishing a snapshot)
3. ./gradlew publishAllPublicationsToAirbnbArtifactoryRepository -PdoNotSignRelease=true
4. "-PdoNotSignRelease=true" is optional, but we don't need to sign artifactory releases and this allows everyone to publish without setting up a gpg key

If you need to publish to a different repository, look at the configuration in 'publishing.gradle'
to see how to configure additional repositories.

Maven Local Installation
=======================

If testing changes locally, you can install to mavenLocal via `./gradlew publishToMavenLocal`
