package com.airbnb.deeplinkdispatch

class TestRegistry(
    matchArray: ByteArray,
) : BaseRegistry(matchArray, emptyArray())

@kotlin.ExperimentalUnsignedTypes
fun testRegistry(entries: List<DeepLinkEntry>): TestRegistry {
    val root = Root()
    entries.forEach {
        root.addToTrie(it)
    }
    return TestRegistry(root.toUByteArray().toByteArray())
}
