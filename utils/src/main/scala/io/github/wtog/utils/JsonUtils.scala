package io.github.wtog.utils

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
  * @author : tong.wang
  * @since : 9/24/19 11:05 PM
  * @version : 1.0.0
  */
object JsonUtils {

  private lazy val mapper: ObjectMapper with ScalaObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.setSerializationInclusion(Include.NON_NULL)
    mapper.setSerializationInclusion(Include.NON_ABSENT)
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.registerModule(DefaultScalaModule)

    mapper
  }

  def toJson[T](t: T): String = mapper.writeValueAsString(t)

  def toMap(t: Any): Map[String, Any] = mapper.convertValue[Map[String, Any]](t)

  def parseFrom[T: Manifest](json: String): T = mapper.readValue[T](json)

  def parseFrom[T: Manifest](bytes: Array[Byte]): T = mapper.readValue[T](bytes)

  def parseFrom[T: Manifest](map: Map[String, Any]): T = mapper.convertValue[T](map)
}
