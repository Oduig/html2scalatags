package com.gjos.scalahelpers

import scalatags._
import scala.xml._

object html2Scalatags extends Xml2ScalatagMaps{  
  /*
   * Adds an attribute to a scalatag
   */
  private def addAttribute(htag: HtmlTag, attrName: String, value: String) = attrMap.get(attrName) match {
    case None           => htag.attr(attrName -> value)
    case Some(addAttr)  => addAttr(htag, value)
  }
  
  /*
   * Takes an XML node, and adds all the attributes from the node to the scalatag
   */
  private def addAttributes(htag: HtmlTag, node: Node): HtmlTag = {
    def iter(t: HtmlTag, attribs: List[(String, String)]): HtmlTag = attribs match {
      case Nil                  => t
      case (key, value)::rest   => iter(addAttribute(t, key, value), rest)
    } 
    iter(htag, node.attributes.asAttrMap.toList)
  }
  
  /*
   * Converts an XML node to a scala tag
   */
  private def nodeToStag(node: Node): STag = tagMap.get(node.label) match {
      // Just use the XML tag as a string
      case None                   => {
        val trimmed = node.toString.trim
        if(trimmed.nonEmpty) '"' + slashEscaper.escape(htmlUnescaper.unescape(trimmed)) + '"' else ""
      }
      // Recurse on children, and surround the child-HtmlTags with the HtmlTag for this node
      case Some(surroundWithTag)  => surroundWithTag(nodesToStags(node.nonEmptyChildren)) match {
        case htag: HtmlTag  => addAttributes(htag, node)
        case stag           => stag
      }
  }
  
  /*
   * Converts a sequence of XML nodes to a sequence of scala tags. 
   * Empty tags are filtered, e.g.: <a><b>bla</b></a> is actually ("<a>", "", "<b>", "foo", "</b>", "</a>")
   */
  private def nodesToStags(nodes: Seq[Node]): Seq[STag] = nodes.map(nodeToStag(_))
  
  
  def scalaTagsFromUrl(url: String): STag = nodeToStag(xmlUtils.xmlFromUrl(url))
  def scalaTagsFromString(s: String): STag = nodeToStag(xmlUtils.xmlFromString(s))
}