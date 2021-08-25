package com.airbnb.deeplinkdispatch

import org.junit.Assert.assertTrue
import org.junit.Test

class DeepLinkMatchResultTests {

    private val concrete = DeepLinkMatchResult(
        deeplinkEntry = DeepLinkEntry(
            type = MatchType.Activity,
            uriTemplate = "scheme://host/one/two/three",
            className = this.javaClass.name,
            method = null
        ),
        parameterMap = emptyMap()
    )
    private val parmSecondPathElement = DeepLinkMatchResult(
        deeplinkEntry = DeepLinkEntry(
            type = MatchType.Activity,
            uriTemplate = "scheme://host/one/{param}/three",
            className = this.javaClass.name,
            method = null
        ),
        parameterMap = emptyMap()
    )
    private val parmFirstPathElement = DeepLinkMatchResult(
        deeplinkEntry = DeepLinkEntry(
            type = MatchType.Activity,
            uriTemplate = "scheme://host/{param}/two/three",
            className = this.javaClass.name,
            method = null
        ),
        parameterMap = emptyMap()
    )
    private val cpsSecondPathSegment = DeepLinkMatchResult(
        deeplinkEntry = DeepLinkEntry(
            type = MatchType.Activity,
            uriTemplate = "scheme://host/one/<config>/three",
            className = this.javaClass.name,
            method = null
        ),
        parameterMap = emptyMap()
    )
    private val cpsFirstPathSegment = DeepLinkMatchResult(
        deeplinkEntry = DeepLinkEntry(
            type = MatchType.Activity,
            uriTemplate = "scheme://host/<config>/two/three",
            className = this.javaClass.name,
            method = null
        ),
        parameterMap = emptyMap()
    )

    @Test
    fun testSameness() {
        assertTrue(concrete.compareTo(concrete) == 0)
        assertTrue(parmSecondPathElement.compareTo(parmSecondPathElement) == 0)
        assertTrue(parmFirstPathElement.compareTo(parmFirstPathElement) == 0)
        assertTrue(cpsSecondPathSegment.compareTo(cpsSecondPathSegment) == 0)
        assertTrue(cpsFirstPathSegment.compareTo(cpsFirstPathSegment) == 0)
    }

    @Test
    fun testEarlierLaterPlaceholder() {
        assertTrue(parmSecondPathElement.compareTo(parmFirstPathElement) == 1)
        assertTrue(parmFirstPathElement.compareTo(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.compareTo(cpsFirstPathSegment) == 1)
        assertTrue(parmFirstPathElement.compareTo(cpsSecondPathSegment) == -1)
    }

    @Test
    fun testEarlierLaterCps() {
        assertTrue(cpsSecondPathSegment.compareTo(cpsFirstPathSegment) == 1)
        assertTrue(cpsFirstPathSegment.compareTo(cpsSecondPathSegment) == -1)
        assertTrue(cpsSecondPathSegment.compareTo(parmFirstPathElement) == 1)
        assertTrue(cpsFirstPathSegment.compareTo(parmSecondPathElement) == -1)
    }

    @Test
    fun testPlaceholderWinOverCps() {
        assertTrue(cpsSecondPathSegment.compareTo(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.compareTo(cpsSecondPathSegment) == 1)
    }
}