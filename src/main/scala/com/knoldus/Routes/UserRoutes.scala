package com.knoldus.Routes

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{delete, get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete

import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout
import com.knoldus.JsonSupport.JsonSupportProtocol
import com.knoldus.UserRegistryActor
import com.knoldus.UserRegistryActor.{ActionPerformed, CreateUser, DeleteUser, GetAllUsers, GetUser, UpdateUser}
import com.knoldus.models.{User, Users}


trait UserRoutes extends JsonSupportProtocol {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[UserRoutes])

  def userRegistryActor: ActorRef
  implicit lazy val timeout = Timeout(5.seconds)

  def userRoutes: Route = pathPrefix("users") {
    pathPrefix("enroll-user") {
      pathEnd {
        concat(
          get {
            val users: Future[Users] =
              (userRegistryActor ? GetAllUsers).mapTo[Users]
            complete(users)
          },
          post {
            entity(as[User]) { user =>
              val userCreated = (userRegistryActor ? CreateUser(user)).mapTo[ActionPerformed]
              onSuccess(userCreated) { performed =>
                log.info(s"Created User [${user.username}]: ${performed.action}")
                complete(StatusCodes.Created, performed.action)
              }
            }
          })
      } ~
        path(IntNumber) { id =>
          get {
            val maybeUser: Future[User] = (userRegistryActor ? GetUser(id)).mapTo[User]
            rejectEmptyResponse {
              complete(maybeUser)
            }
          } ~
            delete {
              val userDeleted: Future[ActionPerformed] = (userRegistryActor ? DeleteUser(id)).mapTo[ActionPerformed]
              onSuccess(userDeleted) { performed =>
                log.info(s"Deleted user $id", performed.action)
                complete(StatusCodes.OK, performed)
              }
            }

        } ~
        path(IntNumber/Segment){ (id , name:String) =>
          put {
            val updateUser : Future[ActionPerformed] = (userRegistryActor ? UpdateUser(id , name)).mapTo[ActionPerformed]
            onSuccess(updateUser){ performed =>
              log.info(s"Updated user $id", performed.action)
              complete(StatusCodes.OK, performed)
            }
          }


        }
    }
  }
}
