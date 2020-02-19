@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.NodeMetadata.*
import kotlin.experimental.and

/**
 * Use a UByte for [flag] so that we can easily specify and read different
 * combinations/permutations of flags with a bit mask.
 */
enum class NodeMetadata(val flag: UByte) {
    /**
     * Root is just used to start the tree. It does not correspond to a normal URI component.
     */
    IsComponentTypeRoot(1u),
    IsComponentTypeScheme(2u),
    IsComponentTypeHost(4u),
    IsComponentTypePathSegment(8u),
    /**
     * Component params are uri components (host, path segment) that also contain a param. The
     * param both matches a range of values and can record the value from said range.
     */
    IsComponentParam(16u),
    /**
     * IsConfigurablePathSegment type represents a path segment which will lookup a dynamic (runtime) provided
     * replacement value when it is visited. The replacement value will replace the entire path
     * segment (content between slashes).
     */
    IsConfigurablePathSegment(32u)
}

object NodeMetadataConverters {
    @JvmStatic
    fun isComponentTypeRoot(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeRoot.flag.toByte() == IsComponentTypeRoot.flag.toByte()

    @JvmStatic
    fun isComponentTypeScheme(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeScheme.flag.toByte() == IsComponentTypeScheme.flag.toByte()

    @JvmStatic
    fun isComponentTypeHost(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeHost.flag.toByte() == IsComponentTypeHost.flag.toByte()

    @JvmStatic
    fun isComponentTypePathSegment(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypePathSegment.flag.toByte() == IsComponentTypePathSegment.flag.toByte()

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
            nodeMetadata and IsComponentParam.flag.toByte() == IsComponentParam.flag.toByte()

    /**
     * A Configurable Path Segment represents a path segment which will lookup a dynamic (runtime)
     * provided replacement value when it is visited. The entire path segment will be replaced.
     */
    @JvmStatic
    fun isConfigurablePathSegment(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsConfigurablePathSegment.flag.toByte() == IsConfigurablePathSegment.flag.toByte()
}