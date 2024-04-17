package com.ubaya.hobbyapp.model

data class Users(
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String?,
    val email: String,
    val password: String,
)
data class News(
    var id: Int,
    var title: String,
    var summary: String,
    var writer: String,
    var content: String,
    var images: String
)