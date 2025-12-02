package com.airbnb.deeplinkdispatch.base

/**
 * Constants related to manifest generation for DeepLinkDispatch.
 *
 * These constants are shared between:
 * - The KSP processor that generates the manifest
 * - The Gradle plugin that reads and merges the generated manifest
 */
object ManifestGeneration {
    /**
     * The resource path where the KSP processor writes the generated AndroidManifest.xml.
     *
     * The full path will be: build/generated/ksp/<variant>/resources/<MANIFEST_RESOURCE_PATH>
     *
     * This path is used by:
     * - ManifestGenerator in the processor to write the manifest via XFiler.writeResource()
     * - ManifestGenerationPlugin to locate and merge the generated manifest
     */
    const val MANIFEST_RESOURCE_PATH = "deeplinkdispatch/AndroidManifest.xml"
}
