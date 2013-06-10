package com.gjos.scala.html2stags

import scalatags._
import com.gjos.scala.json.slashEscaper._

object scalatagsPrinter extends Xml2ScalatagMaps{
  private type SeqStag = SeqSTag[STag]
  private val indent = "  "
  private val newline = "\r\n"
  
  def print(stag: STag, indentLevel: Int = 0): String = {
    val indents = indent * indentLevel
    val linedent = newline + indents
    val stringRepr = stag match {
      case htag: HtmlTag  => {
         
        val printedChilds = htag.children.map(print(_, indentLevel+1)).map(_.trim)
        
        val formattedChilds = if(printedChilds.exists(_.nonEmpty)) {
          printedChilds.mkString("(" + linedent + indent, ", " + linedent + indent, linedent + ")") 
        } else if(myTagMap.contains(htag.tag)) "()" else ""
          
        val formattedClasses = if(htag.classes.nonEmpty) htag.classes.map(_.toString).mkString(".cls(", ", ", ")") else ""
        val formattedStyles = if(htag.styles.nonEmpty) htag.styles.mkString(".css((", "), (", "))") else ""
        
        val otherNamedAttributes = for((k,v) <- htag.attrMap if myAttrMap.contains(k)) yield {
          '.' + k + "(\"" + escape(v.toString) + "\")"
        }
        val otherUnnamedAttributes = htag.attrMap.filterKeys(!myAttrMap.contains(_)).map{ case (k,v) => escape(k) + "\" -> \"" + escape(v.toString) }
    
        val formattedOtherAttributes = if(htag.attrMap.nonEmpty) otherNamedAttributes.mkString + otherUnnamedAttributes.mkString(".attr(\"", "\", \"", "\")") else ""
        val formattedAttributes = formattedClasses + formattedStyles + formattedOtherAttributes
        htag.tag.trim + formattedAttributes + formattedChilds      
      }
      case stags: SeqStag         => {
          val printedChilds = stags.children.map(print(_, indentLevel)).map(_.trim).filter(_.nonEmpty).filter(_!="\"\"")
          if(printedChilds.exists(_.nonEmpty)) {
            printedChilds.mkString(", " + linedent ) 
          } else ""
      }
      case xtag: XmlSTag          => xtag.x.toString
      case otag: ObjectSTag       => otag.x.toString
      case strtag: StringSTag     => strtag.x.toString
      case other                  => other.toString
    }
    if(stringRepr.nonEmpty && indentLevel>0) linedent + stringRepr else stringRepr
  }
}