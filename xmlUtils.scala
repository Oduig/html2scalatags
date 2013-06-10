package com.gjos.scala.html2stags

import io.Source
import com.gjos.scala.json.htmlEscaper._
// These three libraries are so bad that I can't decide which one to take
import scala.xml._
import org.htmlcleaner._
import org.jsoup.Jsoup

// If you are reading this, I apologize. Someone at work told me that rather than writing elaborate comments, I should make the code explain itself.
// I apologize again, for not only following this advice, but also making an elaborate comment.
// TODO: make this cleaner? And Scarlett.
 object xmlUtils {  
  // washes any filth right off
  private val laundry = new ScalaHtmlCleaner()
  laundry.getProperties().setOmitDoctypeDeclaration(true)
  // not really a DRY press, but at least its a press
  private val presser = new SimpleHtmlSerializer(laundry.getProperties())
  
  private val alternativeXmlParserBecauseScalasDefaultParserTriesToLookForADTDWhenParsingAStringButThenFindsOutThereIsNoDTDBecauseItsAFuckingString = {
    val parser = javax.xml.parsers.SAXParserFactory.newInstance()
    // This library tries to be clever, then fails at it and throws errors all around
    parser.setNamespaceAware(false)
    // I don't want your 'features'
    parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
    parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    parser.setValidating(false)
    // Don't know what this does, but at least now both of us are unaware
    parser.setXIncludeAware(false)
    XML.withSAXParser(parser.newSAXParser())
  }
  def fixMyStringSoTheExtendedFuckingMarkupLanguageParserCanDoItsFuckingJob(s: String): String = unescape(presser.getAsString(laundry.clean(escape(s))))//unescape(Jsoup.parse(escape(s)).toString())

  
  def xmlFromUrl(targetUrl: String): Node = {
    val url = new java.net.URL(targetUrl)
    val connection = url.openConnection()
    val stream = connection.getInputStream()
    val htmlDoc = Source.fromInputStream(stream, "UTF-8").getLines.mkString
    stream.close()
    xmlFromString(htmlDoc)
  }

  def xmlFromString(s: String): Node = {
    val lookinGood = fixMyStringSoTheExtendedFuckingMarkupLanguageParserCanDoItsFuckingJob(s)
    println(lookinGood)
    alternativeXmlParserBecauseScalasDefaultParserTriesToLookForADTDWhenParsingAStringButThenFindsOutThereIsNoDTDBecauseItsAFuckingString.loadString(lookinGood)
  }
  
}