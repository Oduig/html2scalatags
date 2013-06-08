package com.gjos.scala.html2stags

import scalatags._
import com.gjos.scala.json.slashEscaper

object scalatagsPrinter extends Xml2ScalatagMaps{
  def print(stag: STag, indentLevel: Int = 0): String = stag match {
    case htag: HtmlTag  => {
      val indents = "  " * indentLevel
      val newline = "\r\n"
        
      val printedChilds = htag.children.map(print(_, indentLevel+1))
      
      val formattedChilds = if(printedChilds.nonEmpty) {
        printedChilds.mkString("(", ", ", newline + indents + ")") 
      } else if(myTagMap.contains(htag.tag)) "()" else ""
        
      val formattedClasses = if(htag.classes.nonEmpty) htag.classes.map(_.toString).mkString(".cls(", ", ", ")") else ""
      val formattedStyles = if(htag.styles.nonEmpty) htag.styles.mkString(".css((", "), (", "))") else ""
      
      val otherNamedAttributes = for((k,v) <- htag.attrMap if myAttrMap.contains(k)) yield {
        '.' + k + "(\"" + slashEscaper.escape(v.toString) + "\")"
      }
      val otherUnnamedAttributes = htag.attrMap.filterKeys(!myAttrMap.contains(_)).map{ case (k,v) => slashEscaper.escape(k) + "\" -> \"" + slashEscaper.escape(v.toString) }
  
      val formattedOtherAttributes = if(htag.attrMap.nonEmpty) otherNamedAttributes.mkString + otherUnnamedAttributes.mkString(".attr(\"", "\", \"", "\")") else ""
      val formattedAttributes = formattedClasses + formattedStyles + formattedOtherAttributes
      (if(indentLevel==0) "" else newline) + indents + htag.tag.trim + formattedAttributes + formattedChilds      
    }
  }
    
}