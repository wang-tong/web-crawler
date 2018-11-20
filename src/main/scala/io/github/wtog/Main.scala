package io.github.wtog

import io.github.wtog.processor.PageProcessor
import io.github.wtog.spider.Spider
import io.github.wtog.utils.ClassUtils

import scala.io.StdIn

/**
 * @author : tong.wang
 * @since : 6/14/18 11:40 PM
 * @version : 1.0.0
 */
object Main {

  def main(args: Array[String]): Unit = {

    val processorList = ClassUtils.loadClasses("io.github.wtog.processor.impl", classOf[PageProcessor])
      .toList.zip(Stream from 1)

    System.getenv("PASS_PLATFORM") match {
      case "openshift" ⇒
        processorList.foreach { case (processor, _) ⇒ Spider(name = processor.getClass.getSimpleName, pageProcessor = processor).start() }
      case _ ⇒
        println("show page processor list: ")
        println("\t0. all")
        for ((service, order) ← processorList) {
          println(s"\t${order}. ${service.getClass.getSimpleName}")
        }

        println("\nchoose number to execute.")
        println("input like 1,2,3 means to execute 1 and 2 and 3 processor")

        val chooseNumber = StdIn.readLine()
        val chosen = chooseNumber.split(",").distinct

        val executeProcessor = if (chosen.isEmpty || chooseNumber.contains("0")) {
          println("execute all processor")
          processorList
        } else {
          processorList.filter {
            case (_, order) ⇒ chosen.contains(order.toString)
          }
        }

        val spiders = executeProcessor.map { case (processor, _) ⇒ Spider(name = processor.getClass.getSimpleName, pageProcessor = processor) }

        spiders.foreach(_.start())
    }
  }
}
