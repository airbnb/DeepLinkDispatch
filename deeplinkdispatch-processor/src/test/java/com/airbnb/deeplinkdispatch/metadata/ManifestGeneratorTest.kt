package com.airbnb.deeplinkdispatch.metadata

import androidx.room.compiler.codegen.toJavaPoet
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.squareup.javapoet.ClassName
import com.squareup.kotlinpoet.javapoet.KotlinPoetJavaPoetPreview
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.ByteArrayOutputStream

/**
 * Tests for ManifestGenerator's multi-round accumulation behavior.
 *
 * The ManifestGenerator is designed to support KSP's multi-round processing by:
 * 1. Accumulating elements across multiple calls to addElements()
 * 2. Writing the final manifest only when writeAccumulatedManifest() is called
 *
 * This ensures that elements from later processing rounds are included in the final manifest.
 */
class ManifestGeneratorTest {
    private val messager = mockk<XMessager>(relaxed = true)
    private val filer = mockk<XFiler>(relaxed = true)
    private val processingEnv = mockk<XProcessingEnv>()
    private val outputStream = ByteArrayOutputStream()

    init {
        every { processingEnv.messager } returns messager
        every { processingEnv.filer } returns filer
        every { filer.writeResource(any(), any(), any()) } returns outputStream
    }

    /**
     * Test that elements from multiple calls to addElements() are accumulated
     * and all appear in the final manifest output.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultiRoundAccumulation() {
        val generator = ManifestGenerator(processingEnv)

        val element1 =
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.TestActivity",
            )
        val element2 =
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.TestActivity",
            )

        val originatingElement1 = mockk<XElement>()
        val originatingElement2 = mockk<XElement>()

        // Simulate first processing round
        generator.addElements(listOf(element1), listOf(originatingElement1))

        // Simulate second processing round
        generator.addElements(listOf(element2), listOf(originatingElement2))

        // Simulate finish callback - write accumulated manifest
        generator.writeAccumulatedManifest()

        // Verify output contains both paths
        val output = outputStream.toString(Charsets.UTF_8)
        assertThat(output).contains("/path1")
        assertThat(output).contains("/path2")
        assertThat(output).contains("com.example.TestActivity")
    }

    /**
     * Test that writeAccumulatedManifest() only writes once even if called multiple times.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testWriteOnlyOnce() {
        val generator = ManifestGenerator(processingEnv)

        val element =
            createActivityElement(
                uri = "https://example.com/path",
                activityClassFqn = "com.example.TestActivity",
            )

        generator.addElements(listOf(element), listOf(mockk()))
        generator.writeAccumulatedManifest()
        generator.writeAccumulatedManifest() // Second call should be no-op

        // Verify filer.writeResource was only called once
        verify(exactly = 1) { filer.writeResource(any(), any(), any()) }
    }

    /**
     * Test that empty accumulator produces no output (only warning).
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testEmptyAccumulatorWarnsAndSkipsWrite() {
        val generator = ManifestGenerator(processingEnv)

        generator.writeAccumulatedManifest()

        // Verify no file write was attempted
        verify(exactly = 0) { filer.writeResource(any(), any(), any()) }
    }

    /**
     * Test that elements without activityClassFqn don't trigger manifest generation.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testElementsWithoutActivityClassFqnSkipManifestGeneration() {
        val generator = ManifestGenerator(processingEnv)

        val element =
            createActivityElement(
                uri = "https://example.com/path",
                activityClassFqn = null, // No activity class
            )

        generator.addElements(listOf(element), listOf(mockk()))
        generator.writeAccumulatedManifest()

        // Verify no file write was attempted (warning is issued instead)
        verify(exactly = 0) { filer.writeResource(any(), any(), any()) }
    }

    /**
     * Test that originating elements from all rounds are included in the filer call.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testOriginatingElementsFromAllRoundsIncluded() {
        val originatingElementsSlot = slot<List<XElement>>()

        every {
            filer.writeResource(
                filePath = any(),
                originatingElements = capture(originatingElementsSlot),
                mode = any(),
            )
        } returns outputStream

        val generator = ManifestGenerator(processingEnv)

        val element1 =
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.TestActivity",
            )
        val element2 =
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.TestActivity",
            )

        val originatingElement1 = mockk<XElement>()
        val originatingElement2 = mockk<XElement>()
        val originatingElement3 = mockk<XElement>()

        // Simulate multiple processing rounds
        generator.addElements(listOf(element1), listOf(originatingElement1, originatingElement2))
        generator.addElements(listOf(element2), listOf(originatingElement3))

        generator.writeAccumulatedManifest()

        // Verify all originating elements are included
        assertThat(originatingElementsSlot.captured).containsExactlyInAnyOrder(
            originatingElement1,
            originatingElement2,
            originatingElement3,
        )
    }

    /**
     * Test that duplicate originating elements are deduplicated.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testDuplicateOriginatingElementsAreDeduplicated() {
        val originatingElementsSlot = slot<List<XElement>>()

        every {
            filer.writeResource(
                filePath = any(),
                originatingElements = capture(originatingElementsSlot),
                mode = any(),
            )
        } returns outputStream

        val generator = ManifestGenerator(processingEnv)

        val element1 =
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.TestActivity",
            )
        val element2 =
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.TestActivity",
            )

        val sharedOriginatingElement = mockk<XElement>()
        val uniqueOriginatingElement = mockk<XElement>()

        // Add same originating element in both rounds
        generator.addElements(listOf(element1), listOf(sharedOriginatingElement))
        generator.addElements(listOf(element2), listOf(sharedOriginatingElement, uniqueOriginatingElement))

        generator.writeAccumulatedManifest()

        // Verify duplicates are removed
        assertThat(originatingElementsSlot.captured).hasSize(2)
        assertThat(originatingElementsSlot.captured).containsExactlyInAnyOrder(
            sharedOriginatingElement,
            uniqueOriginatingElement,
        )
    }

    /**
     * Test that mode is Aggregating (appropriate for multi-round processors).
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testUsesAggregatingMode() {
        val modeSlot = slot<XFiler.Mode>()

        every {
            filer.writeResource(
                filePath = any(),
                originatingElements = any(),
                mode = capture(modeSlot),
            )
        } returns outputStream

        val generator = ManifestGenerator(processingEnv)

        val element =
            createActivityElement(
                uri = "https://example.com/path",
                activityClassFqn = "com.example.TestActivity",
            )

        generator.addElements(listOf(element), listOf(mockk()))
        generator.writeAccumulatedManifest()

        assertThat(modeSlot.captured).isEqualTo(XFiler.Mode.Aggregating)
    }

    /**
     * Test that multiple elements from the same round are handled correctly.
     */
    @Test
    @OptIn(KotlinPoetJavaPoetPreview::class)
    fun testMultipleElementsInSameRound() {
        val generator = ManifestGenerator(processingEnv)

        val element1 =
            createActivityElement(
                uri = "https://example.com/path1",
                activityClassFqn = "com.example.Activity1",
            )
        val element2 =
            createActivityElement(
                uri = "https://example.com/path2",
                activityClassFqn = "com.example.Activity2",
            )
        val element3 =
            createActivityElement(
                uri = "https://example.com/path3",
                activityClassFqn = "com.example.Activity1",
            )

        // All elements in same round
        generator.addElements(listOf(element1, element2, element3), listOf(mockk()))
        generator.writeAccumulatedManifest()

        val output = outputStream.toString(Charsets.UTF_8)
        assertThat(output).contains("com.example.Activity1")
        assertThat(output).contains("com.example.Activity2")
        assertThat(output).contains("/path1")
        assertThat(output).contains("/path2")
        assertThat(output).contains("/path3")
    }

    @OptIn(KotlinPoetJavaPoetPreview::class)
    private fun createActivityElement(
        uri: String,
        activityClassFqn: String?,
        intentFilterAttributes: Set<String> = emptySet(),
        actions: Set<String> = setOf("android.intent.action.VIEW"),
        categories: Set<String> = setOf("android.intent.category.DEFAULT", "android.intent.category.BROWSABLE"),
    ): DeepLinkAnnotatedElement.ActivityAnnotatedElement {
        val typeElement = mockk<XTypeElement>()
        every { typeElement.asClassName().toJavaPoet() } returns ClassName.get("", "TestClass")
        every { typeElement.docComment } returns null

        return DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            uri = uri,
            activityClassFqn = activityClassFqn,
            intentFilterAttributes = intentFilterAttributes,
            actions = actions,
            categories = categories,
            element = typeElement,
        )
    }
}
