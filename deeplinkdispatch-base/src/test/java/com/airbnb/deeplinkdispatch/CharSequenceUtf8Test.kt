package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.chunkOnModifiedUtf8ByteSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharSequenceUtf8Test {
    private val random1000Utf8Chars =
        "\uDB3E\uDE1F_!쉚\uDAF6\uDD90\uDAE2\uDF90ݽ\uD986\uDFFBÖ\uDA0D\uDF36\uD967\uDCC1ޑ捯Ԉyᔑ\u061CԂ" +
            "\uD95A\uDE4B!\u0099稵ɰSˇǃ\uD86C\uDD23q⑈\uD8E3\uDEE1\u1AE2͂A \uDBDD\uDF23˹젙⛍ܬעц揕\uD869\uDEE5g܋\uD90F\uDE62\uD9DD" +
            "\uDC415Ӳ[;\uD946\uDF32و䊳y灲\uDBCC\uDD09i\uDB03\uDCCB\uDA64\uDDB8Ҧ\uD88D\uDF05甆㕾\uDB7E\uDE7AŇ쉟\uDA03\uDD8A\uDBF1" +
            "\uDF96D\uD882\uDC8F䶫ï\u05EE\uDA7B\uDE52\uD9B9\uDC92Ȉ\uDBCE\uDE68\uDA0C\uDD10qᄿ2-Ϋڈ箸X\uDA21\uDCF2湒f빱榶\uD909\uDE10ԅȲʶ" +
            "\uDA5E\uDC5B罗Bƀ\uD933\uDEC5᱅\uDBF8\uDECC䧢Ěڶ\uDA94\uDCFA?\uDB15\uDEBE˓ҧFk]R\uD9DA\uDDE4ݯ\uE546˽)S팮7\uD975\uDE71Է唀ѭ" +
            "\uE1FA郺МJŤ\uD964\uDCF5軃e\uD945\uDEDC5ၔތ�摴\uDA29\uDE9DÃꊳf\uE676\uDA5E\uDF35Ὑ븼ڷ\uEE15{䬴\uD883\uDCD4îﻹ�奼kᄷAtA\uF287j7G㘿꩓׃Kڱ" +
            "\"\u2BB9\uDAB5\uDEA8\uDB00\uDC8Eޛ\uD92D\uDDD1ŗ\uDB8F\uDE4E\uD9D2\uDDFA#\uF1F5ާ\uD8CF\uDEE6輰\uD8A5\uDD0B0\uD9D7\uDD6A訋^\uDB44" +
            "\uDE37\uD9FC\uDF8EyȒѽ͖\uD8D9\uDC91xܑ݂u+|鈎\uF38EÃ͌襖\uDBA2\uDF69*l\uD806\uDD942]\uDAC8\uDC50\uDB00\uDE2C3\uD923\uDE4C\u052B▲JR" +
            "秶Ȍ*\uDA46\uDEF4염oF\uD99A\uDF730\uDAEE\uDF6C�\uD9EC\uDF9Bk㡃\uDBB5\uDC66桊㥠;櫗觢ܲŁⱪ\uDB32\uDCD1\uD823\uDEE7렙*t\uD849\uDC99EʞÂ" +
            "\uD931\uDC97镪4sᬂ\uD832\uDE33ױ\uD87D\uDC86tЩ粁d\u0B91ݚ㯷䫀ꇎڋ6cغ\uDBFD\uDC2CдȜi^ᵵ\uDABF\uDE7EV2슴\u0529bt{掛_Ӡt\uD826" +
            "\uDC9Cโ\uDAFF\uDE65j˔f[גȵ뵡nʆΆ\uDB08\uDD05ᇔ诊\uD980\uDD9F\uD9A3\uDD28\uD9AA\uDF79͡໓⢝ͪˣ_в�6�Ⱥڞt\uD8C6\uDD9D�\"ق\uDA72" +
            "\uDCF9\uD865\uDE23A\uE38D┕۱ߺG睓濄ɮ㻩>ꬉ\uD88C\uDCF4Әq\uD8C7\uDE91\uD9F0\uDF48\uDBD9\uDEB2ߧԌꪈ\uD910\uDDE1\uD9C4\uDD6Bȕ" +
            "\uD98D\uDF2D\uFDDB`֦[cɝਮ⁼ﯞө܈f\uF2E1ヘ~\uDB41\uDC0E%Ě1#뀭嬒h,\uDB74\uDE1D䝻-藾M\uD836\uDD1E惼\uDBEA\uDC82%\uDBD5\uDF38ް8 ܇`" +
            "5{j͈�\uD883\uDF7F٦聆\uD9B2\uDC8Eա֤j)\uDA29\uDC61g乿4Ӏݛ\uDB33\uDCEA뤮葐\uE84Dԃ\uDBFC\uDFBAܧۑ裞찷\uDBB3\uDFF5c혅\uDABB\uDD10@" +
            "\uD932\uDC80B\uDA75\uDFA8\uD8DB\uDD28\uD82C\uDF53\uD9A6\uDD64\uD873\uDEFA-棕Ŵ\uDB63\uDEF6&e6\uD8E8\uDE1A϶·ބ쮋Üauނи˺둝" +
            "\uD901\uDFFDⓌꩀw\uD94C\uDE20�˿o于㯬у\uDBE8\uDD46ٝ詖\uD81A\uDF50霐F\uD9A5\uDF21埏킅⇂5艥]ωގ\uD872\uDC79\u07B3٥ؤ8γ\uD8DE\uDE67ҟe" +
            "\uD872\uDCB7gԦc\uA7CF\uDA9E\uDDAEǿ\uD93F\uDEE0o\uDBE3\uDE13Ǥ\uDA59\uDE55Z镑߷恀\uD830\uDC51\uDA4C\uDD56xŁ\uF2D7\uE6FA떝" +
            "\u08DF酯\uD89A\uDE8A顝ꍾ琘\uD86F\uDE4D\uDA5C\uDDDBO\uDA9E\uDF0Fܩ蛫γ)܄쉷\u05EDË\uD84E\uDD4A\uDA31\uDF7FȻ̹\uD9DC\uDE22ӥ" +
            "%\u008A\uDBB6\uDFECŎ\uEF66洺\"č+\uD9C1\uDF9C崉Pꗇǯ\uDA41\uDF37\uE979չꃲ܅8\uE767ꍺ\uDBC6\uDE73Ұ미,2\uD994\uDEB0GҒa鲸\uD98D" +
            "\uDE2F}뺈껱\uD9CB\uDE65\uDB27\uDEE0\uDA33\uDDE1K\uD84B\uDCA3\uDA66\uDE5E\uDB7B\uDED9zŧ\uDB71\uDED57\uD850\uDC51\u05F9" +
            "\uF656ȣfބ\uD9B0\uDD47壛ѫI(G`l\uDA58\uDFB4Nﶚ|ùv裒̾芠h2Þ'ݛb\uD92D\uDC7B\uD85B\uDE3B\uD8D8\uDFAF.飚ťŔ\uDBB8\uDCDF" +
            "⨹ﯸᣧ\uD97F\uDDFC\u038D\uDBEA\uDCDEʾNkė[Dิ汔\uD869\uDD94핌/\uDB15\uDE65ĕ\uF743\uF32Fﵫ\uDB76\uDDE0|\uDBBB\uDFCF㲚˶{" +
            "߱\uDA22\uDFD6词\uD9CA\uDC32\uDB43\uDED7مn晳=ܨz\uDBE3\uDEE6\uD8BD\uDD72\uD94B\uDD51\uDA6D\uDDF9\uDAF0\uDC30" +
            "\uDAF9\uDC82�Юٺ怎]ƪ\uD94F\uDF2BDݭǑ\uD962\uDE72ϧ\uDA4E\uDC58\uEF4Fė\uE401\uDBD4\uDF95\uE327瑎ᐚ/ì䏆ϡ˺\uD904" +
            "\uDE99\uD810\uDF80╜柩\uD9CC\uDC70h\uD883\uDF4C㑾Țð\uDB8E\uDCB4敪΅\uD960\uDEF9\uD9BC\uDE80珍͕̿}櫾 \uDA34\uDE3F" +
            "Ԕ\uDB85\uDFF1⟌\uD999\uDEB2爤\uD9D5\uDCCDb.帩\uD8FB\uDCA5맀㹈ؔϴ\uDBFB\uDE2BEɯض-߫?׆϶炢\uD93A\uDC8Fއqڻ율t\uD8AC" +
            "\uDD36飯#蘉疼뭔Ӛ\uD81D\uDC40Ῠ\uDB9C\uDC58܆'\uD9FB\uDD02\uF87DAӊTΟz\uD84E\uDF1F\uD980\uDFBBV\uDBC1\uDFB1ҖKܙ" +
            "\uDA56\uDF10ꐚ\uF215棄O\uDB2A\uDEADD\uDBD5\uDC36ZY\uD86E\uDEAE\uD9E4\uDC9B\u05EDk䩦Q批촣\uD8CE\uDFD2式\uDAF0" +
            "\uDD65\uDBD7\uDFC3O\$݈䇂\uD9C5\uDFF2褹s\uE44A,ф7:V\uD9C0\uDE1D淍i�쑚ۀƂ\uD90F\uDC72ͼΦ}߮ᙪZ\u0090씴鑒hd\uE3F0|Bw1" +
            "\uDBB5\uDCDBN4ڔ\uD938\uDD48ڿ⣖\uD838\uDC85ζ\u0BD5\u05EF\uD8ED\uDFCE홲웼≼\u0558\uD867\uDD6B\uDBF8\uDD4Bډ൧8d" +
            "\uD8B9\uDC81ۅ䇒\uD97F\uDE1B\uDB0C\uDF2Dꚕ׀Ĳ\uDBC7\uDE9F\uDA24\uDE73cķo\uDBCA\uDD24Ǭ݈/\uD8E9\uDFAB\uD9B1\uDDCBɏ;(Ȅ甠" +
            "鵤\uDABF\uDE28NUp뺒Ѭ㿆\u05CCȴ\uD937\uDFD4ճs\uDB7F\uDE3D⍺\uD891\uDD46㽔\uD994\uDE7BIŧɃ卩;Z\uD923\uDF65\\\uDB02\uDF6D" +
            "\uDB70\uDEFCԋ\uD995\uDFDA汢Ƶ\uD9AB\uDF47ѢꘫƑ\uDAC0\uDEACᔶ\uD813\uDECD\uDB6D\uDC8Eá\uD961\uDEF4\uD9B8\uDECFﯣφ\u2D9A잫j" +
            "뇏N�䧥п2\uDAEE\uDF1C֔Ըעރ\uDBC7\uDE56\uD951\uDE1B˄\uDAFB\uDD94ʚ˩dT[櫸Ŗw\uDB04\uDE03\$δ\uDAEA\uDE0B\uD8CE\uDDFD\uD80A" +
            "\uDCFDʯΛڦԍ\uDB31\uDFDA䰏V䲋띟`\uDA05\uDEA3\uD84B\uDC0C㒵֜\uE128\uD948\uDFA7JT蛴\uE9952\uD867\uDC29j㦂\uDA3A\uDEDE\uD848" +
            "\uDD6A⚺\uD84C\uDEA9ӑ۴\u31E9ٔӜ}ŭ㪢XѸӳbŎ \uD8AE\uDD28˜\uDB25\uDFBC굑ǈү"

    @Test(expected = IllegalArgumentException::class)
    fun testChunkTooSmall() {
        "\u0000".chunkOnModifiedUtf8ByteSize(2)
    }

    private val chunkSize = 5

    @Test fun testOneNullByte() {
        val source = "\u0000"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 1)
        assertEquals(testResult[0].length, 1)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun testChunk() {
        val source = "\u0000\u0000\u0000"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 2)
        assertEquals(testResult[0].length, 2)
        assertEquals(testResult[1].length, 1)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun testExactly5() {
        val source = "\u0000123"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 1)
        assertEquals(testResult[0].length, 4)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun test5CharsOverflow() {
        val source = "1234\u0000"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 2)
        assertEquals(testResult[0].length, 4)
        assertEquals(testResult[1].length, 1)
        assertTrue(compareSourceAndChunked(source, testResult))
    }

    @Test fun test3ByteCharFit() {
        val source = "€\u0000"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 1)
        assertEquals(testResult[0].length, 2)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun test3ByteCharOverflow() {
        val source = "1€\u0000"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 2)
        assertEquals(testResult[0].length, 2)
        assertEquals(testResult[1].length, 1)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun testNormalAscii() {
        val source = "0123456789"
        val testResult = source.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertEquals(testResult.size, 2)
        assertEquals(testResult[0].length, chunkSize)
        assertEquals(testResult[1].length, chunkSize)
        assertTrue(compareSourceAndChunked(source, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun testLargeRandomUTf8StringChunk5() {
        val testResult = random1000Utf8Chars.chunkOnModifiedUtf8ByteSize(chunkSize)
        assertTrue(compareSourceAndChunked(random1000Utf8Chars, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(chunkSize))
    }

    @Test fun testLargeRandomUTf8StringChunk10() {
        val testResult = random1000Utf8Chars.chunkOnModifiedUtf8ByteSize(10)
        assertTrue(compareSourceAndChunked(random1000Utf8Chars, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(10))
    }

    @Test fun testLargeRandomUTf8StringChunk100() {
        val testResult = random1000Utf8Chars.chunkOnModifiedUtf8ByteSize(100)
        assertTrue(compareSourceAndChunked(random1000Utf8Chars, testResult))
        assertTrue(testResult.checkModifiedUtf8ByteArraySize(100))
    }

    fun compareSourceAndChunked(
        source: CharSequence,
        chunked: List<CharSequence>,
    ): Boolean = source == chunked.joinToString(separator = "")

    fun List<CharSequence>.checkModifiedUtf8ByteArraySize(maxByteArraySize: Int): Boolean =
        all { charSequence ->
            (charSequence.toString().toByteArray().size) + charSequence.count { char -> char == '\u0000' } <= maxByteArraySize
        }
}
