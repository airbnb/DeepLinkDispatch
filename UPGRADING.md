# Upgrading

When ugrading to a newer version of Deeplink Dispatch the following notes should be taken into account.

## To v5.x

* The concept of a `Loader` was renamed to `Registry`. e.g. all the generated `*ModuleLoader` classes will become `*ModuleRegistry` classes after the update. If you reference those you need to update all references.
* Several (mostly internal) classes (e.g. `com.airbnb.deeplinkdispatch.Parser`) were removed, if you referenced them you need to change your code.
* Several (mostly internal) classes (e.g. `com.airbnb.deeplinkdispatch.SchemeHostAndPath`) have a different interface, if you were using them you need to adopt your code.