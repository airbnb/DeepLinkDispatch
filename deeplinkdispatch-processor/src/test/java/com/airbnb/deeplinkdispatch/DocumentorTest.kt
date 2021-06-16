package com.airbnb.deeplinkdispatch

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
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class DocumentorTest {
    @Mock
    private val processingEnv: ProcessingEnvironment? = null

    @Mock
    private val messager: Messager? = null

    @Mock
    private val options: Map<String, String>? = null

    @Mock
    private val elements: Elements? = null
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(processingEnv!!.messager).thenReturn(messager)
        Mockito.`when`(processingEnv.options).thenReturn(options)
        Mockito.`when`(processingEnv.elementUtils).thenReturn(elements)
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
            TypeElement::class.java
        )
        val name1 = Mockito.mock(
            Name::class.java
        )
        Mockito.`when`(elements!!.getDocComment(element1))
            .thenReturn("Sample doc \n @param empty \n @return nothing")
        Mockito.`when`(element1.simpleName).thenReturn(name1)
        Mockito.`when`(name1.toString()).thenReturn("DocClass")
        val element2 = Mockito.mock(
            TypeElement::class.java
        )
        val element2Enclosed = Mockito.mock(
            TypeElement::class.java
        )
        val name2 = Mockito.mock(
            Name::class.java
        )
        val name2Enclosed = Mockito.mock(
            Name::class.java
        )
        Mockito.`when`(element2.simpleName).thenReturn(name2)
        Mockito.`when`(name2.toString()).thenReturn("DocMethod")
        Mockito.`when`(element2.enclosingElement).thenReturn(element2Enclosed)
        Mockito.`when`(element2Enclosed.simpleName).thenReturn(name2Enclosed)
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