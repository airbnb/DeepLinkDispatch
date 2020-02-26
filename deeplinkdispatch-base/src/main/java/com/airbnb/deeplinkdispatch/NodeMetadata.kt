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
class NodeMetadata(var metadata: Byte) {
    constructor(uriComponentType: Byte, uriComponentValue: String) :
            this(metadata = uriComponentType or uriComponentValue.transformationType())

    fun isComponentTypeMismatch(comparisonType: Byte): Boolean = metadata and comparisonType == zero

    @JvmField
    val isComponentParam = metadata and MetadataMasks.ComponentParamMask != zero
    @JvmField
    val isConfigurablePathSegment = metadata and MetadataMasks.ConfigurablePathSegmentMask != zero
    @JvmField
    val isValueLiteralValue = !(isComponentParam || isConfigurablePathSegment)

    companion object {
        /**
         * Using a type declaration seems like the only way to declare a literal Byte in Kotlin.
         * We want to be sure to avoid any unnecessary type conversions.
         */
        private const val zero: Byte = 0

        /**
         * Transformation types:
         * @return an ASCII encoding as a character for the flag for whichever transformation type
         * applies.
         */
        fun String.transformationType(): Byte {
            return when {
                validateIfConfigurablePathSegment(this) -> {
                    MetadataMasks.ConfigurablePathSegmentMask
                }
                validateIfComponentParam(this) -> {
                    MetadataMasks.ComponentParamMask
                }
                else -> {
                    0
                }
            }
        }

        @JvmStatic
        fun isComponentTypeRoot(metadata: Byte): Boolean =
                metadata and MetadataMasks.ComponentTypeRootMask != zero

        @JvmStatic
        fun isComponentTypeScheme(metadata: Byte): Boolean =
                metadata and MetadataMasks.ComponentTypeSchemeMask != zero

        @JvmStatic
        fun isComponentTypeHost(metadata: Byte): Boolean =
                metadata and MetadataMasks.ComponentTypeHostMask != zero

        @JvmStatic
        fun isComponentTypePathSegment(metadata: Byte): Boolean =
                metadata and MetadataMasks.ComponentTypePathSegmentMask != zero

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
        fun isComponentParam(nodeMetadata: Byte): Boolean =
                nodeMetadata and MetadataMasks.ComponentParamMask != zero

        /**
         * A Configurable Path Segment represents a path segment which will lookup a dynamic (runtime)
         * provided replacement value when it is visited. The entire path segment will be replaced.
         */
        @JvmStatic
        fun isConfigurablePathSegment(nodeMetadata: Byte): Boolean =
                nodeMetadata and MetadataMasks.ConfigurablePathSegmentMask != zero


    }
}

/**
 * Masks for [NodeMetadata] bitwise operations.
 */
object MetadataMasks {
    /**
     * Root is just used to start the tree. It does not correspond to a normal URI component.
     */
    const val ComponentTypeRootMask: Byte = 1
    const val ComponentTypeSchemeMask: Byte = 2
    const val ComponentTypeHostMask: Byte = 4
    const val ComponentTypePathSegmentMask: Byte = 8
    /**
     * Component params are uri components (host, path segment) that also contain a param. The
     * param both matches a range of values and can record the value from said range.
     */
    const val ComponentParamMask: Byte = 16
    /**
     * ConfigurablePathSegmentMask represents a path segment which will lookup a dynamic (runtime) provided
     * replacement value when it is visited. The replacement value will replace the entire path
     * segment (content between slashes).
     */
    const val ConfigurablePathSegmentMask: Byte = 32
}