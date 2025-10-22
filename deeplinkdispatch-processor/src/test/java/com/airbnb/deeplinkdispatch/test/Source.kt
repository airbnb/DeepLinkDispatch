package com.airbnb.deeplinkdispatch.test

import com.tschuchort.compiletesting.SourceFile
import java.io.File

sealed class Source {
    abstract val contents: String

    abstract fun toKotlinSourceFile(srcRoot: File): SourceFile

    class JavaSource(
        private val qName: String,
        override val contents: String,
    ) : Source() {
        override fun toKotlinSourceFile(srcRoot: File): SourceFile {
            val outFile =
                srcRoot
                    .resolve(qName.replace(".", "/") + ".java")
                    .also {
                        it.parentFile.mkdirs()
                        it.writeText(contents.trimIndent())
                    }
            return SourceFile.fromPath(outFile)
        }
    }

    class KotlinSource(
        private val relativePath: String,
        override val contents: String,
    ) : Source() {
        override fun toKotlinSourceFile(srcRoot: File): SourceFile {
            val outFile =
                srcRoot.resolve(relativePath).also {
                    it.parentFile.mkdirs()
                    it.writeText(contents.trimIndent())
                }
            return SourceFile.fromPath(outFile)
        }
    }
}
