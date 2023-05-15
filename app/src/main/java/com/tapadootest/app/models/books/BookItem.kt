package com.tapadootest.app.models.books


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class BookItem {
    @SerializedName("author")
    @Expose
    var author: String? = ""

    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String? = ""

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("isbn")
    @Expose
    var isbn: String? = ""

    @SerializedName("price")
    @Expose
    var price: Double = 0.0

    @SerializedName("title")
    @Expose
    var title: String? = ""

    @SerializedName("description")
    @Expose
    var description: String? = ""
}