package com.gjos.scalahelpers

object html2ScalatagsExample extends App {
  val page = html2Scalatags.scalaTagsFromUrl("http://isarjenrobben.geblesseerd.com/")
  
  println(scalatagsPrinter.print(page))
}