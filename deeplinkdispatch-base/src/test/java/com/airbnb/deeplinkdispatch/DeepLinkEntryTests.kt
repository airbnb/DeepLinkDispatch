package com.airbnb.deeplinkdispatch

import org.junit.Assert.assertTrue
import org.junit.Test

class DeepLinkEntryTests {

    private val concrete = DeepLinkEntry("scheme://host/one/two/three", this.javaClass, null)
    private val parmSecondPathElement = DeepLinkEntry("scheme://host/one/{param}/three", this.javaClass, null)
    private val parmFirstPathElement = DeepLinkEntry("scheme://host/{param}/two/three", this.javaClass, null)
    private val cpsSecondPathSegment = DeepLinkEntry("scheme://host/one/<config>/three", this.javaClass, null)
    private val cpsFirstPathSegment = DeepLinkEntry("scheme://host/<config>/two/three", this.javaClass, null)

    @Test fun testSameness(){
        assertTrue(concrete.compareTo(concrete) == 0)
        assertTrue(parmSecondPathElement.compareTo(parmSecondPathElement) == 0)
        assertTrue(parmFirstPathElement.compareTo(parmFirstPathElement) == 0)
        assertTrue(cpsSecondPathSegment.compareTo(cpsSecondPathSegment) == 0)
        assertTrue(cpsFirstPathSegment.compareTo(cpsFirstPathSegment) == 0)
    }

    @Test fun testEarlierLaterPlaceholder(){
        assertTrue(parmSecondPathElement.compareTo(parmFirstPathElement) == 1)
        assertTrue(parmFirstPathElement.compareTo(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.compareTo(cpsFirstPathSegment) == 1)
        assertTrue(parmFirstPathElement.compareTo(cpsSecondPathSegment) == -1)
    }

    @Test fun testEarlierLaterCps(){
        assertTrue(cpsSecondPathSegment.compareTo(cpsFirstPathSegment) == 1)
        assertTrue(cpsFirstPathSegment.compareTo(cpsSecondPathSegment) == -1)
        assertTrue(cpsSecondPathSegment.compareTo(parmFirstPathElement) == 1)
        assertTrue(cpsFirstPathSegment.compareTo(parmSecondPathElement) == -1)
    }

    @Test fun testPlaceholderWinOverCps(){
        assertTrue(cpsSecondPathSegment.compareTo(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.compareTo(cpsSecondPathSegment) == 1)
    }
}