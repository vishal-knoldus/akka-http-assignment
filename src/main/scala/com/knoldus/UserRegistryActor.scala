package com.knoldus
import akka.actor.{Actor, ActorLogging,Props}
import com.knoldus.JsonSupport.JsonSupportProtocol
import com.knoldus.dao.UsersDao
import com.knoldus.models.{User, UserId, Users}
import scala.util.{Failure, Success}

object UserRegistryActor {
  final case class ActionPerformed(action: String)
  final case object GetAllUsers
  final case class CreateUser(user: User)
  final case class GetUser(name: UserId)
  final case class DeleteUser(name: UserId)
  final case class UpdateUser(id:UserId , name:String)
  def props: Props = Props[UserRegistryActor]
}

class UserRegistryActor extends JsonSupportProtocol with Actor with ActorLogging {
  import UserRegistryActor._
  import context.dispatcher

  def receive: Receive = {
    case GetAllUsers =>
      val mysender = sender
      val allUsers = UsersDao.findAll
      allUsers.onComplete {
        case Success(usr) => mysender ! Users(usr)
        case Failure(failureUsr) => println("Data not found to find all Users in Database")
      }
    case CreateUser(user) =>
      UsersDao.create(user)
      sender() ! ActionPerformed(s"User ${user.username} created.")

    case GetUser(id) =>
      val user = UsersDao.findById(id)
      val userSender = sender
      user.onComplete {
        case Success(usr) => userSender ! usr
        case Failure(failureUsr) => println(s"$id user not found")
      }
    case DeleteUser(id) =>
      val user = UsersDao.delete(id)
      val delSender = sender
      user.onComplete {
        case Success(del) => delSender ! ActionPerformed(s"User $id deleted")
        case Failure(delUser) => println(s"Unable to Delete user $id")
      }
    case UpdateUser(id,name) =>
      val user = UsersDao.update(id,name)
      val updateSender = sender
      user.onComplete{
        case Success(update) => updateSender ! update
        case Failure(updateUser) => println(s"$id user not found")
      }
  }
}
