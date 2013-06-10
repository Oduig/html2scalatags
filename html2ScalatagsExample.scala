package com.gjos.scala.html2stags

trait html2ScalatagsExample {
  def main(args: Array[String]){
    val page = html2Scalatags.scalaTagsFromUrl("http://google.nl/")
    println(scalatagsPrinter.print(page))
  }
}