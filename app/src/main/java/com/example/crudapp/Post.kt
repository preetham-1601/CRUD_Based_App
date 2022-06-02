package com.example.crudapp

data class Post(
    val postId: String ?= null,
    val postTitle: String?= null,
    val postImage: String?= null,
    val postDescription: String?= null

)