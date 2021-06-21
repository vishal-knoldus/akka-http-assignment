package com.knoldus.JsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.UserRegistryActor.ActionPerformed
import com.knoldus.models.{User, Users}
import spray.json.DefaultJsonProtocol

trait JsonSupportProtocol extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)

  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat5(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
