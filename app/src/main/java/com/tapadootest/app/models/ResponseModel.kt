package com.tapadootest.app.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseModel {
    @SerializedName("Status")
    @Expose
    var status: Int = 0

    @SerializedName("Message")
    @Expose
    var message: String = ""

    @SerializedName("success")
    @Expose
    var success: Boolean = false
}