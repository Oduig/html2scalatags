package com.gjos.scala.html2stags

import scalatags._
import com.gjos.scala.json.slashEscaper
import com.gjos.scala.json.htmlEscaper
import org.jsoup.nodes._
import scala.collection.JavaConversions._

object html2Scalatags extends Xml2ScalatagMaps{  
  /*
   * Adds an attribute to a scalatag
   */
  private def addAttribute(htag: HtmlTag, attrName: String, value: String) = myAttrMap.get(attrName) match {
    case None           => htag.attr(attrName -> value)
    case Some(addAttr)  => addAttr(htag, value)
  }
  
  /*
   * Takes an XML element, and adds all the attributes from the element to the scalatag
   */
  private def addAttributes(htag: HtmlTag, element: Element): HtmlTag = {
    def iter(t: HtmlTag, attribs: List[Attribute]): HtmlTag = attribs match{
      case Nil          => t
      case attr::rest   => iter(addAttribute(t, attr.getKey(), attr.getValue()), rest)
    } 
    iter(htag, element.attributes().asList().toList)
  }
  
  /*
   * Converts an XML element to a scala tag
   */
  private def elementToStag(element: Element): STag = myTagMap.get(element.tagName()) match {
      // Just use the XML tag as a string
      case None                   => {
        val trimmed = element.toString.trim
        if(trimmed.nonEmpty) '"' + slashEscaper.escape(htmlEscaper.unescape(trimmed)) + '"' else ""
      }
      // Recurse on children, and surround the child-HtmlTags with the HtmlTag for this element
      case Some(surroundWithTag)  => addAttributes(surroundWithTag(elementsToStags(element.children().toSeq)), element)
  }
  
  /*
   * Converts a sequence of XML elements to a sequence of scala tags. 
   * Empty tags are filtered, e.g.: <a><b>bla</b></a> is actually ("<a>", "", "<b>", "foo", "</b>", "", "</a>")
   */
  private def elementsToStags(elements: Seq[Element]): Seq[STag] = elements.map(elementToStag(_))
  
  
  def scalaTagsFromUrl(url: String): STag = elementToStag(htmlUtils.htmlFromUrl(url))
  def scalaTagsFromFullHtmlString(s: String): STag = elementToStag(htmlUtils.parseFullHtmlFromString(s))
  def scalaTagsFromPartialHtmlString(s: String): STag = elementToStag(htmlUtils.parsePartialHtmlFromString(s))
  
}