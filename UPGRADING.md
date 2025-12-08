# Upgrading

When ugrading to a newer version of DeepLink Dispatch the following notes should be taken into account.

## To V7.x

* Replacements given for configurable path segments must lead with a `/` as a `""` value will remove the whole path segment.
  * e.g. `testPath/<configurable-path-segment>/morePath` the mapping for `configurable-path-segment` must now contain a `/` at the begining (e.g. `/pathReplacement`) leading the path to be `testPath/pathReplacement/morePath` because `""` is a valid value and setting it in the mapping would lead to a path like `testPath/morePath`. 

## To v5.x

* The concept of a `Loader` was renamed to `Registry`. e.g. all the generated `*ModuleLoader` classes will become `*ModuleRegistry` classes after the update. If you reference those you need to update all references.
* Several (mostly internal) classes (e.g. `com.airbnb.deeplinkdispatch.Parser`) were removed, if you referenced them you need to change your code.
* Several (mostly internal) classes (e.g. `com.airbnb.deeplinkdispatch.SchemeHostAndPath`) have a different interface, if you were using them you need to adopt your code.
