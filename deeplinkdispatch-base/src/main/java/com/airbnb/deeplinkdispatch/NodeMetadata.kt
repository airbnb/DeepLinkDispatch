@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.Utils.validateIfComponentParam
import com.airbnb.deeplinkdispatch.base.Utils.validateIfConfigurablePathSegment
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * Wraps a Byte [metadata] so that we can easily specify and read different
 * combinations/permutations of flags with bitmasks. Each [TreeNode] has a NodeMetadata instance
 * as the first byte of its header.
 */
class NodeMetadata(
    var metadata: Byte,
) {
    constructor(uriComponentType: Byte, uriComponentValue: String) :
        this(metadata = uriComponentType or uriComponentValue.transformationType())

    fun isComponentTypeMismatch(comparisonType: Byte): Boolean = metadata and comparisonType == ZERO

    @JvmField
    val isComponentParam = metadata and MetadataMasks.COMPONENT_PARAM_MASK != ZERO

    @JvmField
    val isConfigurablePathSegment = metadata and MetadataMasks.CONFIGURABLE_PATH_SEGMENT_MASK != ZERO

    @JvmField
    val isValueLiteralValue = !(isComponentParam || isConfigurablePathSegment)

    companion object {
        /**
         * Using a type declaration seems like the only way to declare a literal Byte in Kotlin.
         * We want to be sure to avoid any unnecessary type conversions.
         */
        private const val ZERO: Byte = 0

        /**
         * Transformation types:
         * @return an ASCII encoding as a character for the flag for whichever transformation type
         * applies.
         */
        fun String.transformationType(): Byte =
            when {
                validateIfConfigurablePathSegment(this) -> {
                    MetadataMasks.CONFIGURABLE_PATH_SEGMENT_MASK
                }
                validateIfComponentParam(this) -> {
                    MetadataMasks.COMPONENT_PARAM_MASK
                }
                else -> {
                    0
                }
            }

        @JvmStatic
        fun isComponentTypeRoot(metadata: Byte): Boolean = metadata and MetadataMasks.COMPONENT_TYPE_ROOT_MASK != ZERO

        @JvmStatic
        fun isComponentTypeScheme(metadata: Byte): Boolean = metadata and MetadataMasks.COMPONENT_TYPE_SCHEME_MASK != ZERO

        @JvmStatic
        fun isComponentTypeHost(metadata: Byte): Boolean = metadata and MetadataMasks.COMPONENT_TYPE_HOST_MASK != ZERO

        @JvmStatic
        fun isComponentTypePathSegment(metadata: Byte): Boolean = metadata and MetadataMasks.COMPONENT_TYPE_PATH_SEGMENT_MASK != ZERO

        /**
         * In DLD, any component of a URI can contain a placeholder that will be substituted.
         * The placeholder does not need to occupy the entire component.
         * <p>E.g.</p>
         * <ul>
         * <li>myScheme://app{Placeholder}Name/</li>
         * <li>Placeholder: One</li>
         * <li>result: myScheme://appOneName/</li>
         * </ul>
         * */
        @JvmStatic
        fun isComponentParam(nodeMetadata: Byte): Boolean = nodeMetadata and MetadataMasks.COMPONENT_PARAM_MASK != ZERO

        /**
         * A Configurable Path Segment represents a path segment which will lookup a dynamic (runtime)
         * provided replacement value when it is visited. The entire path segment will be replaced.
         */
        @JvmStatic
        fun isConfigurablePathSegment(nodeMetadata: Byte): Boolean = nodeMetadata and MetadataMasks.CONFIGURABLE_PATH_SEGMENT_MASK != ZERO
    }
}

/**
 * Masks for [NodeMetadata] bitwise operations.
 */
object MetadataMasks {
    /**
     * Root is just used to start the tree. It does not correspond to a normal URI component.
     */
    const val COMPONENT_TYPE_ROOT_MASK: Byte = 1
    const val COMPONENT_TYPE_SCHEME_MASK: Byte = 2
    const val COMPONENT_TYPE_HOST_MASK: Byte = 4
    const val COMPONENT_TYPE_PATH_SEGMENT_MASK: Byte = 8

    /**
     * Component params are uri components (host, path segment) that also contain a param. The
     * param both matches a range of values and can record the value from said range.
     */
    const val COMPONENT_PARAM_MASK: Byte = 16

    /**
     * ConfigurablePathSegmentMask represents a path segment which will lookup a dynamic (runtime) provided
     * replacement value when it is visited. The replacement value will replace the entire path
     * segment (content between slashes).
     */
    const val CONFIGURABLE_PATH_SEGMENT_MASK: Byte = 32
}
