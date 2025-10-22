# DeepLinkDispatch Build Plugins

## Manifest Generation Plugin

### Overview

The `ManifestGenerationPlugin` automatically generates and merges Android manifest intent filters for DeepLinkDispatch deep links.

### Requirements

**IMPORTANT: This plugin ONLY works with KSP (Kotlin Symbol Processing). It does NOT work with KAPT.**

### How It Works

1. **KSP Phase**: The DeepLinkDispatch annotation processor (running via KSP) generates an `AndroidManifest.xml` file containing intent filters for all `@DeepLink` and `@DeepLinkSpec` annotations that have `activityClasFqn` set.

2. **Manifest Merging**: AGP performs its standard manifest merging from all source sets.

3. **Post-Processing**: This plugin merges the KSP-generated manifest with the AGP-merged manifest.

4. **Packaging**: The final merged manifest is packaged into the APK/AAR.

### Usage

Apply the plugin in your `build.gradle`:

```gradle
plugins {
    id 'com.android.application' // or 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.devtools.ksp'  // REQUIRED: Must use KSP, not KAPT
    id 'com.airbnb.deeplinkdispatch.buildsrc.manifest-generation'
}
```

Then annotate your deeplinks with `activityClasFqn`:

```kotlin
@DeepLink(
    value = ["http://example.com/users/{id}"],
    activityClasFqn = "com.example.app.UserActivity"
)
class UserActivity : Activity()
```

Or use `@DeepLinkSpec`:

```java
@DeepLinkSpec(
    prefix = {"http://example.com"},
    activityClasFqn = "com.example.app.DeepLinkActivity"
)
public @interface WebDeepLink {
    String[] value();
}
```

### Why KSP Only?

The plugin relies on:
1. KSP task execution order and outputs
2. Specific task dependencies between KSP and manifest processing
3. KSP's ability to generate files during annotation processing

KAPT has different task names, execution characteristics, and lifecycle that are incompatible with this implementation.

**If you're using KAPT**, you must manually add intent filters to your `AndroidManifest.xml`.

### Benefits

- **No manual manifest maintenance**: Intent filters are automatically generated from annotations
- **Single build**: Works on the first clean build (no chicken-and-egg problem)
- **Type-safe**: Uses the same annotations as your deeplink definitions
- **DRY principle**: Define your deeplinks once in code, not in both code and manifest

### Troubleshooting

#### Build fails with "Could not configure manifest generation"

Make sure you're using KSP, not KAPT:
```gradle
// Correct - uses KSP
dependencies {
    ksp 'com.airbnb:deeplinkdispatch-processor:x.y.z'
}

// Incorrect - uses KAPT (not supported)
dependencies {
    kapt 'com.airbnb:deeplinkdispatch-processor:x.y.z'
}
```

#### Intent filters not appearing in APK

1. Verify `activityClasFqn` is set on your annotations
2. Run `./gradlew clean assembleDebug` to ensure a fresh build
3. Check `build/generated/deeplinkdispatch/AndroidManifest.xml` exists and contains intent filters
4. Verify you're using KSP, not KAPT

### Testing

The plugin includes basic unit tests that verify the plugin structure and can be instantiated correctly.

Full integration testing is performed through the `sample` app, which demonstrates:
- Clean builds work correctly (no chicken-and-egg problem)
- Generated manifest entries appear in the final APK
- Multiple activities can have intent filters
- Works with both `@DeepLink` and `@DeepLinkSpec` annotations

To test the plugin functionality:
```bash
# Build the sample app from clean state
./gradlew clean :sample:assembleDebug

# Verify the generated manifest is included
cat sample/build/intermediates/packaged_manifests/debug/AndroidManifest.xml | grep deepLink_third
```

### Technical Details

The plugin hooks into the Gradle build as follows:

```
processManifest → kspKotlin → manifestMergeTask → packageApk
                      ↓              ↑
                  generates    depends on
                  manifest
```

This ordering ensures:
- KSP generates the manifest after initial manifest processing
- Our merge task runs after KSP completes
- Final packaging includes the merged result
- No circular dependencies

### License

Same as DeepLinkDispatch main project (Apache 2.0).
