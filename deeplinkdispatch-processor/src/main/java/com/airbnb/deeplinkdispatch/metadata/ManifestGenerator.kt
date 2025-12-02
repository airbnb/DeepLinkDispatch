package com.airbnb.deeplinkdispatch.metadata

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
 * The Gradle plugin reads from: build/generated/ksp/<variant>/resources/deeplinkdispatch/AndroidManifest.xml
 */
internal class ManifestGenerator(
    private val processingEnv: XProcessingEnv,
) {
    private val messager: XMessager = processingEnv.messager

    // Track if we've already written the manifest to avoid duplicate writes in multi-round processing
    private var manifestWritten = false

    fun write(elements: List<DeepLinkAnnotatedElement>) {
        // Skip if we've already written the manifest in a previous round
        if (manifestWritten) {
            return
        }
        if (elements.isNullOrEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "No deep links. Manifest generation: Manifest not generated.",
            )
            return
        }

        // Only generate manifest if at least one element has activityClassFqn set
        val elementsWithActivity = elements.filter { it.activityClassFqn != null }
        if (elementsWithActivity.isEmpty()) {
            messager.printMessage(
                Diagnostic.Kind.WARNING,
                "No deep links with activityClassFqn. Manifest generation: Manifest not generated.",
            )
            return
        }

        // Generate manifest content to a string first
        val manifestContent =
            StringWriter().use { stringWriter ->
                PrintWriter(stringWriter, true).use { writer ->
                    val docWriter: Writer = ManifestWriter()
                    docWriter.write(processingEnv, writer, elements)
                }
                stringWriter.toString()
            }

        // Write to KSP's resource output via filer API (this gets cached by Gradle)
        try {
            processingEnv.filer
                .writeResource(
                    filePath = Path.of(ManifestGeneration.MANIFEST_RESOURCE_PATH),
                    originatingElements = emptyList(),
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
