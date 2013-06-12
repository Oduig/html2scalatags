package com.gjos.scala.html2stags

import scalatags._
import com.gjos.scala.json.slashEscaper._
import com.gjos.scala.json.htmlEscaper

/*
 * Formats a scalatag into a string which can be put directly into scala code
 */
object scalatagsPrinter extends Xml2ScalatagMaps{
  private type SeqStag = SeqSTag[STag]
  private val indent = "  "
  private val newline = "\r\n"
    
  
  /*
   * Print a scalatag with an optional indent level
   */
  def print(stag: STag, indentLevel: Int = 0): String = {
    val indents = indent * indentLevel
    val indentsNxt = indent * (indentLevel+1)
    val linedent = newline + indents
    val linedentNxt = newline + indentsNxt
    
    /*
     * Unescapes HTML codes and replaces them with regularly escaped tokens. 
     * This is necessary because when executed, scalatags escapes &s into &amps; and regular characters into html codes
     */ 
    def surroundWithQuotesIfNonEmpty(s: String): String = {
      if(s.trim.nonEmpty) {
        val out = escape(htmlEscaper.unescape(s))
        val lines = out.split("\n").filter(_.trim.nonEmpty)
        if(lines.size > 1){
          lines.mkString("\"\"\"\n" + indentsNxt + "|", "\n" + indentsNxt + "|", "\n" + indents + "\"\"\".stripMargin")
        } else '"' + out + '"'
      } else ""
    }      
    
    // A scalatag can be several things, so match it and return a string in each case
    stag match {
      // HtmlTag is a tag which may contain attributes and children
      case htag: HtmlTag  => {
        // First, print children
        val printedChilds = htag.children.map(print(_, indentLevel+1)).filter(_.trim.nonEmpty)
        
        // Format the children. If all children are empty, give a shorter representation
        val formattedChilds = if(printedChilds.nonEmpty) {
          printedChilds.mkString("(" + linedentNxt, ", " + linedentNxt, linedent + ")") 
        } else if(myTagMap.contains(htag.tag)) "()" else ""
          
        // Format the tag's classes
        val formattedClasses = if(htag.classes.nonEmpty) htag.classes.map(_.toString).mkString(".cls(", ", ", ")") else ""
        // Format the tag's custom css
        val formattedStyles = if(htag.styles.nonEmpty) htag.styles.mkString(".css((", "), (", "))") else ""
        
        // Gather all the attributes that are defined by a special tag in scalatags
        val otherNamedAttributes = for((k,v) <- htag.attrMap if myAttrMap.contains(k)) yield {
          '.' + k + "(\"" + escape(v.toString) + "\")"
        }
        // Gather all the attributes that are not defined by a special tag in scalatags (uses attr("key" -> "value", ...) )
        val otherUnnamedAttributes = htag.attrMap.filterKeys(!myAttrMap.contains(_)).map{ case (k,v) => escape(k) + "\" -> \"" + escape(v.toString) }
    
        // Format these attributes
        val formattedOtherAttributes = otherNamedAttributes.mkString + (if(otherUnnamedAttributes.nonEmpty) otherUnnamedAttributes.mkString(".attr(\"", "\", \"", "\")") else "")
        val formattedAttributes = formattedClasses + formattedStyles + formattedOtherAttributes
        // Return the trimmed tag, formatted attributes and formatted children
        htag.tag.trim + formattedAttributes + formattedChilds      
      }
      // A child of an HtmlTag is often a Seq of STags, which does not really make sense to me.
      // It would make sense if a child was an STag, and all children together were Seq[STag]. 
      // But so be it. We have to parse it anyway.
      case stags: SeqStag         => {
          // Print all children, filter the empty ones and the ones that are printed as two quotes ""
          val printedChilds = stags.children.map(print(_, indentLevel)).filter(_.trim.nonEmpty)
          // Again, print the list only if there's any relevant data in there
          printedChilds.mkString(", " + linedent) 
      }
      // Strings should be printed with quotes around them, unless they are simply empty nodes
      case strtag: StringSTag     => surroundWithQuotesIfNonEmpty(strtag.x.toString)
      // Trivial cases
      case xtag: XmlSTag          => xtag.x.toString
      case otag: ObjectSTag       => otag.x.toString
      case other                  => other.toString
    } 
  }
}