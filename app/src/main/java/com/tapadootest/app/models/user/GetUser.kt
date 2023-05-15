package com.tapadootest.app.models.user


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class GetUser {
    @SerializedName("Birthdate")
    @Expose
    var birthdate: String = ""

    @SerializedName("Age")
    @Expose
    var age: String = ""

    @SerializedName("ConfirmPassword")
    @Expose
    var confirmPassword: String = ""

    @SerializedName("UserId")
    @Expose
    var userId: String = ""

    @SerializedName("BloodPressureHigh")
    @Expose
    var bloodPressureHigh: String = ""

    @SerializedName("BloodPressureLow")
    @Expose
    var bloodPressureLow: String = ""

//    @SerializedName("Country")
//    @Expose
//    var country: Country = Country()

    @SerializedName("CountryId")
    @Expose
    var countryId: Int = 0

    @SerializedName("NotificationCount")
    @Expose
    var notificationCount: Int = 0

    @SerializedName("Email")
    @Expose
    var email: String = ""

    @SerializedName("Gender")
    @Expose
    var gender: Int = 0

    @SerializedName("GenderText")
    @Expose
    var genderText: String = ""

    @SerializedName("Height")
    @Expose
    var height: String = ""

    @SerializedName("Name")
    @Expose
    var name: String = ""

    @SerializedName("Password")
    @Expose
    var password: String = ""

    @SerializedName("Phone")
    @Expose
    var phone: String = ""

    @SerializedName("Picture")
    @Expose
    var picture: String = ""

    @SerializedName("RegisterDate")
    @Expose
    var registerDate: String = ""

    @SerializedName("UserType")
    @Expose
    var userType: Int = 0

    @SerializedName("Weight")
    @Expose
    var weight: String = ""

    @SerializedName("Disabled")
    @Expose
    var disabled = false

    @SerializedName("IsProfileComplete")
    @Expose
    var isProfileComplete = false

}