package com.airbnb.deeplinkdispatch

import android.os.Bundle

/**
 * Regex pattern for valid URL path characters according to RFC 3986.
 * Allowed characters:
 * - Unreserved: a-z, A-Z, 0-9, -, ., _, ~
 * - Sub-delimiters: !, $, &, ', (, ), *, +, ,, ;, =
 * - Path specific: :, @, /
 * - Percent-encoded: % followed by two hex digits
 */
private val URL_PATH_SAFE_PATTERN = Regex("^[a-zA-Z0-9\\-._~!$&'()*+,;=:@/%]*$")

/**
 * Checks if a string contains only valid URL path characters.
 * Also validates that percent-encoded sequences are valid (% followed by exactly 2 hex digits).
 */
private fun isUrlPathSafe(value: String): Boolean {
    if (!URL_PATH_SAFE_PATTERN.matches(value)) {
        return false
    }
    // Validate percent-encoding: % must be followed by exactly 2 hex digits
    var i = 0
    while (i < value.length) {
        if (value[i] == '%') {
            if (i + 2 >= value.length ||
                !value[i + 1].isHexDigit() ||
                !value[i + 2].isHexDigit()
            ) {
                return false
            }
            i += 3
        } else {
            i++
        }
    }
    return true
}

private fun Char.isHexDigit(): Boolean = this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'

/**
 * Ensure that every key-to-be-replaced declared by all registries have a corresponding key in
 * the user's injected mapping of configurablePathSegmentReplacements. If not, throw an exception
 * and tell the user which keys aren't present.
 * @param registries
 * @param configurablePathSegmentReplacements
 */
fun validateConfigurablePathSegmentReplacements(
    registries: List<BaseRegistry>,
    configurablePathSegmentReplacements: Map<String, String>,
) = DeepLinkDispatch.validationExecutor.run {
    // Collect all path segment keys across all registries
    val keysUnion = registries.flatMap { it.pathSegmentReplacementKeysInRegistry }.toSet()
    val missingKeys =
        keysUnion
            .filter { key ->
                !configurablePathSegmentReplacements.keys.any { it.contentEquals(String(key)) }
            }.joinToString(",\n") { String(it) }
    val nonUrlPathSafeValues =
        configurablePathSegmentReplacements.entries.filter {
            !isUrlPathSafe(it.value)
        }
    require(nonUrlPathSafeValues.isEmpty()) {
        """
        Replacement value for configurable path segment(s) contains characters that are not URL path safe.

        Allowed characters are: a-z, A-Z, 0-9, - . _ ~ ! $ & ' ( ) * + , ; = : @ / and percent-encoded characters (%XX).

        ${nonUrlPathSafeValues.joinToString { "key: ${it.key} -> value: '${it.value}'" }}
        """.trimIndent()
    }
    val nonEmptyConfigurablePathSegmentValuesWithoutLeadingSlash =
        configurablePathSegmentReplacements.entries.filter {
            it.value.isNotEmpty() &&
                !it.value.startsWith("/")
        }
    require(nonEmptyConfigurablePathSegmentValuesWithoutLeadingSlash.isEmpty()) {
        """
        Replacement value for configurable path segment(s) was not empty and did not start with a '/' which is not allowed.

        This is required because an empty replacement will remove the whole path segment which starts with an '/'.

        ${nonEmptyConfigurablePathSegmentValuesWithoutLeadingSlash.joinToString { "key: ${it.key} -> value: ${it.value}" }}
        """.trimIndent()
    }
    require(missingKeys.isEmpty()) {
        "Keys not found in BaseDeepLinkDelegate's mapping of " +
            "PathVariableReplacementValues. Missing keys are:\n$missingKeys.\nKeys in mapping " +
            "are:\n${configurablePathSegmentReplacements.keys.joinToString(",\n") { it }}."
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
