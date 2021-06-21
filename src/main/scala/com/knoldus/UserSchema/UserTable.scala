package com.knoldus.UserSchema
import slick.jdbc.MySQLProfile.api._
import com.knoldus.models.{User, UserId}

class UsersTable(tag: Tag) extends Table[User](tag, "users") {

  def id = column[UserId]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")
  def location = column[String]("location")
  def gender = column[Int]("gender")

  //Add id to *
  def * = (id.?, username, password, location, gender) <> ((User.apply _).tupled, User.unapply)
}