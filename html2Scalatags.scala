package com.gjos.scala.html2stags

import scalatags._
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
  private def nodeToStag(node: Node): STag = node match {
    case element:Element    => myTagMap.get(element.tagName()) match {
      // Just use the XML tag as a string
      case None                   => StringSTag(element.toString)
      // Recurse on children, and surround the child-HtmlTags with the HtmlTag for this element
      case Some(surroundWithTag)  => addAttributes(surroundWithTag(nodesToStags(element.childNodes().toSeq)), element)
    }
    // I guess there's not problem with using comments
    case c: Comment         => XmlSTag(new scala.xml.Comment(c.getData()))
    // Content of style and script tags. 
    case data: DataNode     => StringSTag(data.getWholeData())
    // Just get the text
    case t: TextNode        => StringSTag(t.text())
    // I don't want your metadata!
    case _: DocumentType | _: XmlDeclaration | _ => StringSTag("")
  }
  
  /*
   * Converts a sequence of XML elements to a sequence of scala tags. 
   * Empty tags are filtered, e.g.: <a><b>bla</b></a> is actually ("<a>", "", "<b>", "foo", "</b>", "", "</a>")
   */
  private def nodesToStags(nodes: Seq[Node]): Seq[STag] = nodes.map(nodeToStag(_))
  
  
  def scalaTagsFromUrl(url: String): STag = nodeToStag(htmlUtils.htmlFromUrl(url))
  def scalaTagsFromFullHtmlString(s: String): STag = nodeToStag(htmlUtils.parseFullHtmlFromString(s))
  def scalaTagsFromPartialHtmlString(s: String): STag = nodeToStag(htmlUtils.parsePartialHtmlFromString(s))
  
}