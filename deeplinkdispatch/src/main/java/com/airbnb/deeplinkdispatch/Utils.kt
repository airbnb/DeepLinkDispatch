package com.airbnb.deeplinkdispatch

import android.os.Bundle

/**
 * Ensure that every key-to-be-replaced declared by all registries have a corresponding key in
 * the user's injected mapping of configurablePathSegmentReplacements. If not, throw an exception
 * and tell the user which keys aren't present.
 * @param registries
 * @param configurablePathSegmentReplacements
 */
fun validateConfigurablePathSegmentReplacements(
    registries: List<BaseRegistry>,
    configurablePathSegmentReplacements: Map<ByteArray, ByteArray>,
) = DeepLinkDispatch.validationExecutor.run {
    // Collect all path segment keys across all registries
    val keysUnion = registries.flatMap { it.pathSegmentReplacementKeysInRegistry }.toSet()
    val missingKeys =
        keysUnion
            .filter { key ->
                !configurablePathSegmentReplacements.keys.any { it.contentEquals(key) }
            }.joinToString(",\n") { String(it) }

    require(missingKeys.isEmpty()) {
        "Keys not found in BaseDeepLinkDelegate's mapping of " +
            "PathVariableReplacementValues. Missing keys are:\n$missingKeys.\nKeys in mapping " +
            "are:\n${configurablePathSegmentReplacements.keys.joinToString(",\n") { String(it) }}."
    }
}

/**
 * Returns a shallow copy of this Bundle that is missing the items for which the predicate returned
 * false
 */
fun Bundle.filter(predicate: (key: String, value: Any?) -> Boolean): Bundle {
    val output = Bundle(this)
    this.keySet()?.forEach { key ->
        if (!predicate(key, this.get(key))) {
            output.remove(key)
        }
    }
    return output
}
