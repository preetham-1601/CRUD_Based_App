package com.example.crudapp

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


open class PostRVModel {
    // creating getter and setter methods.
    // creating variables for our different fields.
    var postTitle: String? = null
    var postImg: String? = null
    var postDescription: String? = null
    var postId: String? = null


    // creating an empty constructor.
    constructor() {}

    constructor(
        postId: String?,
        postTitle: String?,
        postImg: String?,
        postDescription: String?

    ) {
        this.postTitle = postTitle
        this.postImg = postImg
        this.postDescription = postDescription
        this.postId = postId

    }
}
