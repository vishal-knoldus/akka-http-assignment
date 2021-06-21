package com.knoldus.UserRoutesTest

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.JsonSupport.JsonSupportProtocol
import com.knoldus.Main.QuickstartServer.userRoutes
import com.knoldus.models.User
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec
import spray.json.DefaultJsonProtocol.listFormat
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class UserRoutesSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with JsonSupportProtocol {
  var users = List(
    User(Some(1), "Meenakshi", "123", "Mathura", 1),
    User(Some(2), "Aditi", "122", "Delhi", 1)
  )

  " A enroll-user" should {
    "return all the users " in {
      Get("users/enroll-user") ~> userRoutes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[List[User]] shouldBe users

      }
    }
    "return a user by hitting the query parameter endpoint" in {
      Get("users/enroll-user?id=2") ~> userRoutes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Option[User]] shouldBe Some(User(Some(2), "Aditi", "122", "Delhi", 1))
      }
    }
    "return a user by calling the endpoint with the id in the path " in {
       Get("users/enroll-user/2") ~> userRoutes ~> check{
         response.status shouldBe StatusCodes.OK
         val strictEntityFuture = response.entity.toStrict(1 second)
         val strictEntity = Await.result(strictEntityFuture,1 second)
         strictEntity.contentType shouldBe ContentTypes.`application/json`

         val user = strictEntity.data.utf8String.parseJson.convertTo[Option[User]]
         user shouldBe Some(User(Some(2), "Aditi", "122", "Delhi", 1))
       }
    }
     "insert a user into the database " in {
       val newUser = User(Some(3),"Rahul" , "124" ,"Noida" , 2)
       Post("users/enroll-user" , newUser) ~> userRoutes ~> check{
         status shouldBe StatusCodes.OK
         assert(users.contains(newUser))
       }
     }
    "Delete a user" in {
      Delete("users/enroll-user") ~> userRoutes ~> check{
        status shouldBe StatusCodes.OK
      }
    }
    "Update a user " in {
      Put("users/enroll-user/1/Sakshi") ~> userRoutes ~> check{
      status shouldBe StatusCodes.OK
        responseAs[Option[User]] shouldBe Some(User(Some(1),"Sakshi" ,"123","Mathura" , 1))
      }
    }

  }
}