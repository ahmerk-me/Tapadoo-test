package com.tapadootest.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomDialog : Cloneable {

    public override fun clone(): Any {
        return super.clone()
    }

    var id: Int? = null

    var isSelected: Boolean = false

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    var systemName: String? = null

}