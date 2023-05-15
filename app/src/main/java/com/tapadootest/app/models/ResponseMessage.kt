package com.tapadootest.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseMessage {

    @SerializedName("Status")
    @Expose
    val status = 0

    @SerializedName("Message")
    @Expose
    val message: String = ""

    @SerializedName("Value")
    @Expose
    val value: String? = ""

    @SerializedName("Value2")
    @Expose
    val value2: String? = ""

}