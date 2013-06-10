package com.gjos.scala.html2stags

/*
 * This class was created to abstract from HtmlCleaner's java syntax for TagNodes
 */
object tagNodeFuncions {
  import org.htmlcleaner._
  import scala.collection.JavaConversions._
  
  def getAttributes(t: TagNode): Map[String, String] = t.getAttributes().toMap
  
  //TODO: Make plain strings work?
  def getChildren(t: TagNode): Seq[TagNode] = t.getChildren().toSeq
  
  def getLabel(t: TagNode): String = t.getName()
}