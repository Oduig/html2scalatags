package com.gjos.scala.html2stags

trait html2ScalatagsExample {
  def main(args: Array[String]){
    val page = html2Scalatags.scalaTagsFromUrl("http://google.com/")
    println(scalatagsPrinter.print(page))
  }
}