package com.airbnb.deeplinkdispatch.metadata.writers

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import java.io.PrintWriter

/**
 * Implement this interface if you want to provide own metadata writer.
 */
interface Writer {
    /**
     * Compose metadata with help of provided environment, writer and collection of
     * found deeplink elements.
     */
    fun write(
        env: XProcessingEnv,
        writer: PrintWriter,
        elements: List<DeepLinkAnnotatedElement>,
    )

    companion object {
        private const val PARAM = "@param"
        private const val RETURN = "@return"

        // Strips off {@link #PARAM} and {@link #RETURN}.
        internal fun formatJavaDoc(str: String?): String? {
            var result = str
            if (result != null) {
                val paramPos = result.indexOf(PARAM)
                if (paramPos != -1) {
                    result = result.substring(0, paramPos)
                }
                val returnPos = result.indexOf(RETURN)
                if (returnPos != -1) {
                    result = result.substring(0, returnPos)
                }
                result = result.trim { it <= ' ' }
            }
            return result
        }
    }
}
