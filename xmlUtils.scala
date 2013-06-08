package com.gjos.scalahelpers

import scala.xml._
import io.Source

object xmlUtils {
  def xmlFromUrl(targetUrl: String): Node = {
    val url = new java.net.URL(targetUrl)
    val connection = url.openConnection()
    val stream = connection.getInputStream()
    val htmlDoc = Source.fromInputStream(stream, "UTF-8").getLines.mkString
    stream.close()
    xmlFromString(htmlDoc)
  }
  
  def xmlFromString(s: String): Node = makeANewXmlParserBecauseScalasDefaultParserTriesToLookForADTDWhenParsingAStringButThenFindsOutThereIsNoDTDBecauseItsAFuckingString.loadString(s)
  
  // If you are reading this, I apologize. Someone at work told me that rather than writing elaborate comments, I should make the code explain itself.
  // I apologize again for also making an elaborate comment.
  // TODO: make this cleaner? And Scarlett.
  private val makeANewXmlParserBecauseScalasDefaultParserTriesToLookForADTDWhenParsingAStringButThenFindsOutThereIsNoDTDBecauseItsAFuckingString = {
    val parser = javax.xml.parsers.SAXParserFactory.newInstance()
    parser.setNamespaceAware(false)
    parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    XML.withSAXParser(parser.newSAXParser())
  }
}