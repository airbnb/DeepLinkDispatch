package com.airbnb.deeplinkdispatch.metadata

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.base.ManifestGeneration
import com.airbnb.deeplinkdispatch.metadata.writers.ManifestWriter
import com.airbnb.deeplinkdispatch.metadata.writers.Writer
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Path
import javax.tools.Diagnostic

/**
 * This generates the input manifest for the manifest merger in the Gradle plugin.
 *
 * It creates a manifest file that contains all the intent filters from the annotated elements.
 * The manifest is written to KSP's resource output directory using the filer API, which ensures
 * proper caching by Gradle.
 *
 * This generator supports multi-round processing by accumulating elements across rounds and
 * writing the final manifest only when [writeAccumulatedManifest] is called (typically in the
 * processor's finish callback).
 *
 * The Gradle plugin reads from: build/generated/ksp/<variant>/resources/deeplinkdispatch/AndroidManifest.xml
 */
internal class ManifestGenerator(
    private val processingEnv: XProcessingEnv,
) {
    private val messager: XMessager = processingEnv.messager

    // Accumulate elements across processing rounds
    private val accumulatedElements = mutableListOf<DeepLinkAnnotatedElement>()
    private val accumulatedOriginatingElements = mutableSetOf<XElement>()

    // Track if we've already written the manifest to avoid duplicate writes
    private var manifestWritten = false

    /**
     * Accumulates deep link elements from the current processing round.
     * Call this during each processing round, then call [writeAccumulatedManifest]
     * when processing is complete.
     */
    fun addElements(
        elements: List<DeepLinkAnnotatedElement>,
        originatingElements: List<XElement>,
    ) {
        if (elements.isNotEmpty()) {
            accumulatedElements.addAll(elements)
            accumulatedOriginatingElements.addAll(originatingElements)
        }
    }

    /**
     * Writes the accumulated manifest from all processing rounds.
     * Should be called once when all processing rounds are complete (in the finish callback).
     */
    fun writeAccumulatedManifest() {
        // Skip if we've already written the manifest
        if (manifestWritten) {
            return
        }

        if (accumulatedElements.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "No deep links. Manifest generation: Manifest not generated.",
            )
            return
        }

        // Only generate manifest if at least one element has activityClassFqn set
        val elementsWithActivity = accumulatedElements.filter { it.activityClassFqn != null }
        if (elementsWithActivity.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "No deep links have activityClassFqn set. By setting activityClassFqn" +
                    " and applying the deep link dispatch gradle plugin you can enable" +
                    " `AndroidManifest.xml` generation for your deeplinks.",
            )
            return
        }

        // Generate manifest content to a string first
        val manifestContent =
            StringWriter().use { stringWriter ->
                PrintWriter(stringWriter, true).use { writer ->
                    val docWriter: Writer = ManifestWriter()
                    docWriter.write(processingEnv, writer, accumulatedElements)
                }
                stringWriter.toString()
            }

        // Write to KSP's resource output via filer API (this gets cached by Gradle)
        try {
            processingEnv.filer
                .writeResource(
                    filePath = Path.of(ManifestGeneration.MANIFEST_RESOURCE_PATH),
                    originatingElements = accumulatedOriginatingElements.toList(),
                    mode = XFiler.Mode.Aggregating,
                ).use { outputStream ->
                    outputStream.write(manifestContent.toByteArray(Charsets.UTF_8))
                }
            manifestWritten = true
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                " Manifest generation: Generated at KSP resource output: ${ManifestGeneration.MANIFEST_RESOURCE_PATH}",
            )
        } catch (e: Exception) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                " Manifest generation failed: ${e.message}",
            )
        }
    }
}
