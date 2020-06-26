package com.airbnb.deeplinkdispatch

import org.junit.Assert.assertTrue
import org.junit.Test

class DeepLinkEntryTests {

    private val concrete = DeepLinkEntry("scheme://host/one/two/three", DeepLinkEntry.Type.CLASS, this.javaClass, null)
    private val parmSecondPathElement = DeepLinkEntry("scheme://host/one/{param}/three", DeepLinkEntry.Type.CLASS, this.javaClass, null)
    private val parmFirstPathElement = DeepLinkEntry("scheme://host/{param}/two/three", DeepLinkEntry.Type.CLASS, this.javaClass, null)
    private val cpsSecondPathSegment = DeepLinkEntry("scheme://host/one/<config>/three", DeepLinkEntry.Type.CLASS, this.javaClass, null)
    private val cpsFirstPathSegment = DeepLinkEntry("scheme://host/<config>/two/three", DeepLinkEntry.Type.CLASS, this.javaClass, null)

    @Test fun testSameness(){
        assertTrue(concrete.moreConcreteThan(concrete) == 0)
        assertTrue(parmSecondPathElement.moreConcreteThan(parmSecondPathElement) == 0)
        assertTrue(parmFirstPathElement.moreConcreteThan(parmFirstPathElement) == 0)
        assertTrue(cpsSecondPathSegment.moreConcreteThan(cpsSecondPathSegment) == 0)
        assertTrue(cpsFirstPathSegment.moreConcreteThan(cpsFirstPathSegment) == 0)
    }

    @Test fun testEarlierLaterPlaceholder(){
        assertTrue(parmSecondPathElement.moreConcreteThan(parmFirstPathElement) == 1)
        assertTrue(parmFirstPathElement.moreConcreteThan(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.moreConcreteThan(cpsFirstPathSegment) == 1)
        assertTrue(parmFirstPathElement.moreConcreteThan(cpsSecondPathSegment) == -1)
    }

    @Test fun testEarlierLaterCps(){
        assertTrue(cpsSecondPathSegment.moreConcreteThan(cpsFirstPathSegment) == 1)
        assertTrue(cpsFirstPathSegment.moreConcreteThan(cpsSecondPathSegment) == -1)
        assertTrue(cpsSecondPathSegment.moreConcreteThan(parmFirstPathElement) == 1)
        assertTrue(cpsFirstPathSegment.moreConcreteThan(parmSecondPathElement) == -1)
    }

    @Test fun testPlaceholderWinOverCps(){
        assertTrue(cpsSecondPathSegment.moreConcreteThan(parmSecondPathElement) == -1)
        assertTrue(parmSecondPathElement.moreConcreteThan(cpsSecondPathSegment) == 1)
    }
}