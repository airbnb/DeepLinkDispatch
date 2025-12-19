package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.base.MatchIndex.ALLOWED_VALUES_DELIMITER
import com.airbnb.deeplinkdispatch.base.MatchIndex.VARIABLE_DELIMITER

private val placeholderRegex = ("\\" + MatchIndex.VARIABLE_DELIMITER[0] + "(.*?)\\" + MatchIndex.VARIABLE_DELIMITER[1]).toRegex()

internal val allowedPlaceholderRegex =
    "(?<=${"\\" + MatchIndex.ALLOWED_VALUES_DELIMITER[0]})(.*)(?=${"\\" + MatchIndex.ALLOWED_VALUES_DELIMITER[1]})".toRegex()

/**
 * Get all the placeholders that follow the format {placeholder_name} or {placeholder_name(allowed|values)} from a [String].
 */
fun String.placeholders(): List<String>? =
    placeholderRegex
        .findAll(this)
        .map { it.groupValues[0] }
        .toList()
        .takeUnless { it.isEmpty() }

/**
 * Placeholder used in the PATTERN_SIMPLE_GLOB to match any character.
 *
 * See: https://developer.android.com/reference/android/os/PatternMatcher#PATTERN_SIMPLE_GLOB
 */
const val SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR = "..*"

/**
 * Get all the allowed values from a placeholder that follows the format {placeholder_name(allowed|values)} from a [String].

 */
internal fun String.placeholderValues(): List<String> =
    allowedPlaceholderRegex.find(this)?.let { matchResult ->
        matchResult.value.split(MatchIndex.ALLOWED_VALUES_SEPARATOR)
    } ?: listOf(SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR)

/**
 * Rewrite a [String] with all the allowed values from a placeholder that follows the format {placeholder_name(allowed|values)} sorted alphabetically.
 */
internal fun String.orderPlaceholderValues(): String =
    allowedPlaceholderRegex.replace(this) { matchResult ->
        matchResult.value
            .split(MatchIndex.ALLOWED_VALUES_SEPARATOR)
            .sorted()
            .joinToString(separator = MatchIndex.ALLOWED_VALUES_SEPARATOR.toString())
    }

/**
 * For the placeholder name given in `placeholderName` retrieve all possible values or an empty set
 * if there aren't any.
 */
fun String.allPossiblePlaceholderValues(placeholderName: String): List<String> =
    placeholders()
        ?.filter { it.startsWith("${VARIABLE_DELIMITER[0]}$placeholderName${ALLOWED_VALUES_DELIMITER[0]}") }
        ?.map { it.placeholderValues() }
        ?.flatten() ?: emptyList()

/**
 * Get all the possible values from a [String] that contains placeholders that follow the format {placeholder_name(allowed|values)}.
 * e.g. "http{scheme(|s)}://{host_prefix(|de.|ro.|www.)}airbnb.com/guests" will return a list of all possible values for the scheme and host_prefix placeholders.
 */
fun String.allPossibleValues(): List<String> =
    placeholders()
        ?.firstOrNull()
        ?.let { firstPlaceholder ->
            firstPlaceholder.placeholderValues()?.mapNotNull { placeholderValue ->
                val placeholderReplacedString = this.replace(firstPlaceholder, placeholderValue)
                val remainingPlaceholdesReplacedValues = placeholderReplacedString.allPossibleValues()
                remainingPlaceholdesReplacedValues.ifEmpty {
                    listOf(placeholderReplacedString)
                }
            }
        }?.flatten() ?: listOf(this)
