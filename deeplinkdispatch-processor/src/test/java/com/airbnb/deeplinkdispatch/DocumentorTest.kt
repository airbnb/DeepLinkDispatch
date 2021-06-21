package com.airbnb.deeplinkdispatch

import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XTypeElement
import com.google.common.base.Charsets
import com.google.common.collect.ImmutableList
import com.google.common.io.Files
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File
import java.io.IOException
import java.net.MalformedURLException

class DocumentorTest {
    @Mock
    private val processingEnv: XProcessingEnv? = null

    @Mock
    private val messager: XMessager? = null

    @Mock
    private val options: Map<String, String>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(processingEnv!!.messager).thenReturn(messager)
        Mockito.`when`(processingEnv.options).thenReturn(options)
    }

    @Test
    fun testInitWithRightFilePath() {
        Mockito.`when`(options!![Documentor.DOC_OUTPUT_PROPERTY_NAME]).thenReturn(FILE_PATH)
        val documentor = Documentor(processingEnv!!)
        assertThat(documentor.file).isNotNull
    }

    @Test
    fun testInitWithoutFilePath() {
        val documentor = Documentor(processingEnv!!)
        assertThat(documentor.file).isNull()
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        Mockito.`when`(options!![Documentor.DOC_OUTPUT_PROPERTY_NAME]).thenReturn(FILE_PATH)
        val documentor = Documentor(processingEnv!!)
        documentor.write(getElements())
        val actual = Files.toString(File(FILE_PATH), Charsets.UTF_8)
        val expected = ("airbnb://example.com/{foo}/bar\\n|#|\\nSample doc\\n|#|\\nDocClass\\"
                + "n|##|\\nairbnb://example.com/{foo}/bar\\n|#|\\n\\n|#|\\nDocClass#DocMethod\\n|##|\\n")
        Assert.assertEquals(expected, actual)
    }

    @Throws(MalformedURLException::class)
    private fun getElements(): List<DeepLinkAnnotatedElement> {
        val element1 = Mockito.mock(
            XTypeElement::class.java
        )
        val name1 = Mockito.mock(
            String::class.java
        )
        Mockito.`when`(element1.docComment)
            .thenReturn("Sample doc \n @param empty \n @return nothing")
        Mockito.`when`(element1.qualifiedName).thenReturn(name1)
        Mockito.`when`(name1.toString()).thenReturn("DocClass")
        val element2 = Mockito.mock(
            XTypeElement::class.java
        )
        val element2Enclosed = Mockito.mock(
            XTypeElement::class.java
        )
        val name2 = Mockito.mock(
            String::class.java
        )
        val name2Enclosed = Mockito.mock(
            String::class.java
        )
        Mockito.`when`(element2.qualifiedName).thenReturn(name2)
        Mockito.`when`(name2.toString()).thenReturn("DocMethod")
        Mockito.`when`(element2.enclosingTypeElement).thenReturn(element2Enclosed)
        Mockito.`when`(element2Enclosed.qualifiedName).thenReturn(name2Enclosed)
        Mockito.`when`(name2Enclosed.toString()).thenReturn("DocClass")
        val deepLinkElement1 = DeepLinkAnnotatedElement(
            "airbnb://example.com/{foo}/bar",
            element1, DeepLinkEntry.Type.CLASS
        )
        val deepLinkElement2 = DeepLinkAnnotatedElement(
            "airbnb://example.com/{foo}/bar",
            element2, DeepLinkEntry.Type.METHOD
        )
        return ImmutableList.of(deepLinkElement1, deepLinkElement2)
    }

    companion object {
        private val FILE_PATH = System.getProperty("user.dir") + "/doc/deeplinks.txt"
    }
}