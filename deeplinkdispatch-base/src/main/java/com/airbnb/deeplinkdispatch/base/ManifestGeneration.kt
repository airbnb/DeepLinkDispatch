package com.airbnb.deeplinkdispatch.base

/**
 * Constants related to manifest and asset generation for DeepLinkDispatch.
 *
 * These constants are shared between:
 * - The KSP processor that generates the manifest and match index assets
 * - The Gradle plugin that reads and merges the generated manifest and assets
 */
object ManifestGeneration {
    /**
     * KSP argument key to enable asset-based match index generation.
     *
     * When this argument is set to "true", the KSP processor will:
     * - Write the binary match index as an asset file instead of encoding it as a string
     * - Generate a registry class that loads the index from assets via AssetManager
     *
     * This is automatically set by ManifestGenerationPlugin when applied to library modules.
     */
    const val OPTION_USE_ASSET_BASED_MATCH_INDEX = "deepLink.useAssetBasedMatchIndex"

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

    /**
     * The asset directory prefix where the KSP processor writes the binary match index files.
     *
     * The full path will be: build/generated/ksp/<variant>/resources/assets/<MATCH_INDEX_ASSET_PATH_PREFIX>/<module>.bin
     *
     * This path is used by:
     * - DeepLinkProcessor in the processor to write the match index via XFiler.writeResource()
     * - ManifestGenerationPlugin to locate and merge the generated assets
     * - Generated registry classes at runtime to load the match index via AssetManager
     */
    const val MATCH_INDEX_ASSET_PATH_PREFIX = "deeplinkdispatch"

    /**
     * File extension for match index asset files.
     */
    const val MATCH_INDEX_ASSET_EXTENSION = ".bin"

    /**
     * Generates the asset path for a given module name's match index.
     *
     * @param moduleName The module name (will be lowercased)
     * @return The asset path relative to the assets directory
     */
    @JvmStatic
    fun getMatchIndexAssetPath(moduleName: String): String =
        "$MATCH_INDEX_ASSET_PATH_PREFIX/${moduleName.lowercase()}$MATCH_INDEX_ASSET_EXTENSION"

    /**
     * Generates the full resource path (including assets/ prefix) for writing the match index via XFiler.
     *
     * @param moduleName The module name (will be lowercased)
     * @return The resource path for XFiler.writeResource()
     */
    @JvmStatic
    fun getMatchIndexResourcePath(moduleName: String): String = "assets/${getMatchIndexAssetPath(moduleName)}"
}
