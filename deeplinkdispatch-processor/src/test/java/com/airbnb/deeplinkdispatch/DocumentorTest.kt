package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XMemberContainer
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.squareup.javapoet.ClassName
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.IOException
import java.net.MalformedURLException

class DocumentorTest {
    private val processingEnv = mockk<XProcessingEnv>()

    private val messager = mockk<XMessager>(relaxed = true)

    private val options = mockk<Map<String, String>>()

    init {
        every { processingEnv.messager } returns messager
        every { processingEnv.options } returns options
    }

    @Test
    fun testInitWithRightFilePath() {
        every { options[Documentor.DOC_OUTPUT_PROPERTY_NAME] } returns FILE_PATH
        val documentor = Documentor(processingEnv)
        assertThat(documentor.file).isNotNull
    }

    @Test
    fun testInitWithoutFilePath() {
        every { options[Documentor.DOC_OUTPUT_PROPERTY_NAME] } returns null
        val documentor = Documentor(processingEnv)
        assertThat(documentor.file).isNull()
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        every { options[Documentor.DOC_OUTPUT_PROPERTY_NAME] } returns FILE_PATH
        val documentor = Documentor(processingEnv)
        documentor.write(getElements())
        val actual = File(FILE_PATH).readText()
        val expected = (
            "airbnb://example.com/{foo}/bar\\n|#|\\nSample doc\\n|#|\\nDocClass\\" +
                "n|##|\\nairbnb://example.com/{foo}/bar\\n|#|\\n\\n|#|\\nDocClass#DocMethod\\n|##|\\n"
            )
        Assert.assertEquals(expected, actual)
    }

    @Throws(MalformedURLException::class)
    private fun getElements(): List<DeepLinkAnnotatedElement> {
        val classElement = mockk<XTypeElement>()

        every { classElement.docComment } returns "Sample doc \n @param empty \n @return nothing"
        every { classElement.className } returns ClassName.get("", "DocClass")

        val methodElement = mockk<XMethodElement>(relaxed = true)

        val element2Enclosed = mockk<XMemberContainer>(relaxed = true)
        every { element2Enclosed.className } returns ClassName.get("", "DocClass")

        every { methodElement.name } returns "DocMethod"
        every { methodElement.enclosingElement } returns element2Enclosed
        val deepLinkElement1 = DeepLinkAnnotatedElement.ActivityAnnotatedElement(
            "airbnb://example.com/{foo}/bar",
            classElement
        )
        val deepLinkElement2 = DeepLinkAnnotatedElement.MethodAnnotatedElement(
            "airbnb://example.com/{foo}/bar",
            methodElement
        )
        return listOf(deepLinkElement1, deepLinkElement2)
    }

    companion object {
        private val FILE_PATH = System.getProperty("user.dir") + "/doc/deeplinks.txt"
    }
}
