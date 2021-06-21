package com.knoldus.models

case class User(id: Option[UserId], username: String, password: String, location: String, gender: Int)

case class Users(users: Seq[User])


