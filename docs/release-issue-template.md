# Release Akka Serverless Java/Scala SDKs

### Prepare

- [ ] Make sure all important PRs have been merged
- [ ] Check that the [latest build](https://app.circleci.com/pipelines/github/lightbend/akkaserverless-java-sdk) successfully finished
- [ ] Make sure a version of the proxy that supports the protocol version the SDK expects has been deployed to production

You can see the proxy version on prod [on grafana](https://lightbendcloud.grafana.net/d/ebzw4ARnz/prod-akka-serverless-operations-dashboard?orgId=1) or using [various other methods](https://github.com/lightbend/akkaserverless/wiki/Versioning-and-how-to-determine-what-version-is-running).

### Cutting the release 

- [ ] Edit the [draft release notes](https://github.com/lightbend/akkaserverless-java-sdk/releases) and create the appropriate tag
    - CI will automatically publish to Sonatype OSSRH (Maven Central) based on the tag
    - CI will update the docs/current branch

### Check availability

- [ ] Check that [`docs/current`](https://github.com/lightbend/akkaserverless-java-sdk/commits/docs/current) has been updated
- [ ] Check the release on [Maven Central](https://repo1.maven.org/maven2/com/akkaserverless/akkaserverless-scala-sdk-testkit_2.13/)

### Fix and publish docs

- [ ] Update the [supported version in the main docs](https://github.com/lightbend/akkaserverless-docs/blob/master/docs/modules/ROOT/partials/include.adoc#L21) if relevant (affects [Supported Versions](https://developer.lightbend.com/docs/akka-serverless/setting-up/index.html#_supported_languages))
- [ ] Add an item to the [Release Notes](https://github.com/lightbend/akkaserverless-docs/blob/master/docs/modules/release-notes/pages/index.adoc) in the documentation
- [ ] Release the Akka Serverless documentation to get the SDK docs updates published

### Update to the latest version
 
- [ ] Update the sdk version in samples and maven plugin
    - [ ] Update the `akkaserverless-sdk.version` in the `samples/*/pom.xml` files to the released version.
    - [ ] Update the `akkaserverless-sdk.version` default value in the `samples/scala-*/project/plugins.sbt` files to the release version
    - [ ] version in `maven-java/**/pom.xml`
 
### Check docs update

- [ ] After quite a while TechHub shows [updated docs](https://developer.lightbend.com/docs/akka-serverless/index.html) (see the timestamp on the bottom of the page)

### Announcements

- [ ] Announce in [the forum](https://discuss.lightbend.com/c/akka-serverless/40)
    - tag with Akka Serverless, releases and announcement
- [ ] Inform Lightbend marketing and engineering if it's a noteworthy release
- [ ] Close this issue
