package com.gjos.scala.html2stags

import scalatags._
import scala.xml._
import com.gjos.scala.json.slashEscaper
import com.gjos.scala.json.htmlEscaper
import tagNodeFuncions._
import org.htmlcleaner._

object html2Scalatags extends Xml2ScalatagMaps{  
  /*
   * Adds an attribute to a scalatag
   */
  private def addAttribute(htag: HtmlTag, attrName: String, value: String) = myAttrMap.get(attrName) match {
    case None           => htag.attr(attrName -> value)
    case Some(addAttr)  => addAttr(htag, value)
  }
  
  /*
   * Takes an XML node, and adds all the attributes from the node to the scalatag
   */
  private def addAttributes(htag: HtmlTag, node: TagNode): HtmlTag = {
    def iter(t: HtmlTag, attribs: List[(String, String)]): HtmlTag = attribs match {
      case Nil                  => t
      case (key, value)::rest   => iter(addAttribute(t, key, value), rest)
    } 
    iter(htag, getAttributes(node).toList)
  }
  
  /*
   * Converts an XML node to a scala tag
   */
  private def nodeToStag(node: TagNode): STag = myTagMap.get(getLabel(node)) match {
      // Just use the XML tag as a string
      case None                   => {
        val trimmed = node.toString.trim
        if(trimmed.nonEmpty) '"' + slashEscaper.escape(htmlEscaper.unescape(trimmed)) + '"' else ""
      }
      // Recurse on children, and surround the child-HtmlTags with the HtmlTag for this node
      case Some(surroundWithTag)  => addAttributes(surroundWithTag(nodesToStags(getChildren(node))), node)
  }
  
  /*
   * Converts a sequence of XML nodes to a sequence of scala tags. 
   * Empty tags are filtered, e.g.: <a><b>bla</b></a> is actually ("<a>", "", "<b>", "foo", "</b>", "</a>")
   */
  private def nodesToStags(nodes: Seq[TagNode]): Seq[STag] = nodes.map(nodeToStag(_))
  
  
  def scalaTagsFromUrl(url: String): STag = nodeToStag(xmlUtils.xmlFromUrl(url))
  def scalaTagsFromString(s: String): STag = nodeToStag(xmlUtils.xmlFromString(s))
}