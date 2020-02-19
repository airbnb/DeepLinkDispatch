package com.airbnb.deeplinkdispatch

/**
 * Ensure that every key-to-be-replaced declared by all registries have a corresponding key in
 * the user's injected mapping of configurablePathSegmentReplacements. If not, throw an exception
 * and tell the user which keys aren't present.
 * @param registries
 * @param configurablePathSegmentReplacements
 */
fun validateConfigurablePathSegmentReplacements(
        registries: List<BaseRegistry>, configurablePathSegmentReplacements: Map<String, String>
) = DeepLinkDispatch.validationExecutor.run {
    //Collect all path segment keys across all registries
    val keysUnion = mutableSetOf<String>()
    for (registry in registries) {
        keysUnion.addAll(registry.pathSegmentReplacementKeysInRegistry)
    }
    val missingKeys = keysUnion.filter { key -> key !in configurablePathSegmentReplacements.keys }
            .joinToString(",\n")

    require(missingKeys.isEmpty()) { "Keys not found in BaseDeepLinkDelegate's mapping of " +
            "PathVariableReplacementValues. Missing keys are:\n$missingKeys.\nKeys in mapping " +
            "are:\n${configurablePathSegmentReplacements.keys.joinToString(",\n" )}."
    }

    keysUnion.filter { configurablePathSegmentReplacements[it].isNullOrEmpty() }.apply {
        require(this.isEmpty()) {"All path segment replacements must have a non-empty replacement " +
                "value. Please inject a valid mapping into BaseDeepLinkDelegate. These keys were " +
                "missing values:\n${this.joinToString(",\n")}."}
    }
}
