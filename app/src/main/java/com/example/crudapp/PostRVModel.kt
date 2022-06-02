package com.example.crudapp

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


open class PostRVModel : Parcelable {
    // creating getter and setter methods.
    // creating variables for our different fields.
    var postTitle: String? = null
    var postDescription: String? = null
    var postId: String? = null
    var postImg: String? = null

    // creating an empty constructor.
    constructor() {}
    protected constructor(`in`: Parcel) {
        postTitle = `in`.readString()
        postId = `in`.readString()
        postDescription = `in`.readString()
        postImg = `in`.readString()
    }

    constructor(
        postId: String?,
        postTitle: String?,
        postDescription: String?,
        postImg: String?
    ) {
        this.postTitle = postTitle
        this.postId = postId
        this.postDescription = postDescription
        this.postImg = postImg
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(postTitle)
        dest.writeString(postId)
        dest.writeString(postDescription)
        dest.writeString(postImg)
    }

    companion object CREATOR : Creator<PostRVModel> {
        override fun createFromParcel(parcel: Parcel): PostRVModel {
            return PostRVModel(parcel)
        }

        override fun newArray(size: Int): Array<PostRVModel?> {
            return arrayOfNulls(size)
        }
    }


}
