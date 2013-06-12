package com.gjos.scala.html2stags

import org.jsoup.Jsoup
import org.jsoup.nodes._

object htmlUtils {  

  def htmlFromUrl(urlString: String): Element = {
    val url = new java.net.URL(urlString)
    // Wait at most 30 seconds
    Jsoup.parse(url, 30000).child(0)
  }
        
  def parseFullHtmlFromString(fullHtml: String): Element = Jsoup.parse(fullHtml).child(0)

  // Assumes that the input s goes into a body tag
  def parsePartialHtmlFromString(partialBody: String): Element = Jsoup.parseBodyFragment(partialBody).child(0)
  
}