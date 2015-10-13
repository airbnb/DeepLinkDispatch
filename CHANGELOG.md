# 1.4.0 (10/12/2015)

* [PR 49](https://github.com/airbnb/DeepLinkDispatch/pull/49) URL schemes with a number in them are
seen as malformed

* [PR 46](https://github.com/airbnb/DeepLinkDispatch/pull/46) Removes `@DeepLinks` annotation. We
can achieve the same with just `@DeepLink`

* [PR 45](https://github.com/airbnb/DeepLinkDispatch/pull/45) Fixes a `NullPointerException` in
`DeepLinkEntry` when given an invalid Uri.

* [PR 35](https://github.com/airbnb/DeepLinkDispatch/pull/35)
* [PR 39](https://github.com/airbnb/DeepLinkDispatch/pull/39)

More relaxed path matching Regex. DeepLinkDispatch now accepts paths with `#!+%~$,.` characters.

# 1.3.0 (07/23/2015)

* [PR 26](https://github.com/airbnb/DeepLinkDispatch/pull/26) Failure/Success callbacks are now
fired via a Broadcast that you can subscribe to using a Broadcast receiver instead of having the
application implement an interface.

* [PR 29](https://github.com/airbnb/DeepLinkDispatch/pull/29) Fixes bug where `@DeepLinks` was not
being added as a supported annotation type

* [PR 30](https://github.com/airbnb/DeepLinkDispatch/pull/30) Apply a stricter regex when scanning
DeepLinkRegistry to prevent early return from matching.

# 1.2.0 (07/10/2015)

* Breaking change: Uri scheme is now required when declaring deep links with `@DeepLink` and
`@DeepLinks`. This allows for multiple schemes to be handled (eg.: myapp:// and http://

* Compilation will now fail if DeepLinkDispatch finds an invalid Uri.

# 1.1.0 (06/20/2015)

* Specify paths with one URL parameters instead of a host and path parameter.
* Add hooks in the Application class to handle successful and unsuccessful deep linking.
* Use JavaPoet instead of JavaWriter.

# 1.0.1

* Initial version.