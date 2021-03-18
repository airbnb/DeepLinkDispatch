Releasing
========

 1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
 4. `git commit -am "Prepare for release X.Y.Z."` (where X.Y.Z is the new version)
 5. `git tag -a X.Y.X -m "Version X.Y.Z"` (where X.Y.Z is the new version)
 6. `./gradlew clean assemble sourcesJar androidSourcesJar javadocsJar androidJavadocsJar uploadArchives --no-daemon --no-parallel`
 7. Update the `gradle.properties` to the next SNAPSHOT version.
 8. `git commit -am "Prepare next development version."`
 9. `git push && git push --tags`
 10. Visit [Sonatype Nexus](https://oss.sonatype.org/) and promote the artifact.
 11. Go to https://github.com/airbnb/DeepLinkDispatch/releases and create the release (via pushed tag)