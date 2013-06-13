Html2Scalatags
==========

A library for converting HTML pages and fragments into Scalatags
----------

For more information on scalatags, please visit [https://github.com/lihaoyi/scalatags](https://github.com/lihaoyi/scalatags)

**Simple usage example**

	import com.gjos.scala.html2stags
	val page = html2Scalatags.scalaTagsFromUrl("http://xkcd.com/")
    println(scalatagsPrinter.print(page))
	
**Output (this is a String)**

	html.xmlns("http://www.w3.org/1999/xhtml").attr("version" -> "-//W3C//DTD XHTML 1.1//EN", "xml:lang" -> "en")(
	  head(
		link.rel("stylesheet").href("http://imgs.xkcd.com/static/styles_short_beta.css").title("Default").attr("type" -> "text/css")(), 
		title(
		  "xkcd: Council of 300"
		), 
		meta.attr("http-equiv" -> "X-UA-Compatible", "content" -> "IE=edge")(), 
		link.rel("shortcut icon").href("http://imgs.xkcd.com/s/919f273.ico").attr("type" -> "image/x-icon")(), 
		link.rel("icon").href("http://imgs.xkcd.com/s/919f273.ico").attr("type" -> "image/x-icon")(), 
		link.rel("alternate").title("Atom 1.0").href("/atom.xml").attr("type" -> "application/atom+xml")(), 
		link.rel("alternate").title("RSS 2.0").href("/rss.xml").attr("type" -> "application/rss+xml")(), 
		link.rel("apple-touch-icon-precomposed").href("http://imgs.xkcd.com/static/apple-touch-icon-precomposed.png")(), 
		script.attr("type" -> "text/javascript")(
		  """
			|  var _gaq = _gaq || [];
			|  _gaq.push([\'_setAccount\', \'UA-25700708-7\']);
			|  _gaq.push([\'_setDomainName\', \'xkcd.com\']);
			|  _gaq.push([\'_setAllowLinker\', true]);
			|  _gaq.push([\'_trackPageview\']);
			|  (function() {
			|    var ga = document.createElement(\'script\'); ga.type = \'text/javascript\'; ga.async = true;
			|    ga.src = (\'https:\' == document.location.protocol ? \'https://ssl\' : \'http://www\') + \'.google-analytics.com/ga.js\';
			|    var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(ga, s);
			|  })();
		  """.stripMargin
		)
	  ), 
	  body(
	   "etc."
	  )
	)
