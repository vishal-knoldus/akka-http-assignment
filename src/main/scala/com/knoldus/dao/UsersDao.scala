package com.knoldus.dao

import com.knoldus.models.User
import com.knoldus.models.UserId
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

object UsersDao extends BaseDao {

  def findAll: Future[Seq[User]] = usersTable.result
  def create(user: User): Future[UserId] = usersTable.returning(usersTable.map(_.id)) += user
  def findById(userId: UserId): Future[User] = usersTable.filter(_.id === userId).result.head

  def delete(userId: UserId): Future[Int] = usersTable.filter(_.id === userId).delete
  def update(userId: UserId , name:String) : Future[Int] = {
    usersTable.filter(_.id === userId).map(user =>
      (user.username)).update(name)
  }

}