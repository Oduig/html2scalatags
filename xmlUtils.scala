package com.gjos.scala.html2stags

import io.Source
import org.htmlcleaner._

 object xmlUtils {  
  private val cleaner = new HtmlCleaner()

  def xmlFromUrl(targetUrl: String): TagNode = {
    val url = new java.net.URL(targetUrl)
    val connection = url.openConnection()
    val stream = connection.getInputStream()
    val htmlDoc = Source.fromInputStream(stream, "UTF-8").getLines.mkString
    stream.close()
    xmlFromString(htmlDoc)
  }

  def xmlFromString(s: String): TagNode = {
    cleaner.clean(s)
  }
  
}