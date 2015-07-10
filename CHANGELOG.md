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