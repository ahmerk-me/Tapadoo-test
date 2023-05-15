package com.tapadootest.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cells.library.networking.TapadooAPICall
import com.google.gson.JsonObject
import com.tapadootest.app.models.user.GetUser
import com.tapadootest.app.models.user.RegisterRequest
import com.tapadootest.app.R
import com.tapadootest.app.classes.*
import com.tapadootest.app.models.ResponseMessage
import com.tapadootest.app.view.activity.MainActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAuthViewModel : ViewModel() {

    var registerUser: MutableLiveData<ResponseMessage> = MutableLiveData()
    var loginUser: MutableLiveData<ResponseMessage> = MutableLiveData()
    var editProfile: MutableLiveData<ResponseMessage> = MutableLiveData()
    var getUser: MutableLiveData<GetUser> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()
    var dataLogout: MutableLiveData<ResponseMessage> = MutableLiveData()
    var errorText = "Some Error Occurred"
    var doLogin = false


    fun loginUser(email: String?, password: String?, act: MainActivity) {

        var isAllValid = true
        isError.value = false
        isLoading.value = true

        if (email != null && password != null) {

            Log.e("12345 ", " Email ===>$email==== Password ====>$password")

            doLogin = true

            if (email.isNotEmpty()) {

                if (password.isNotEmpty()) {

//                    if (isValidEmail(email)) {

                    if (!isPasswordValid(password)) {
                        errorText = act.getString(R.string.NotValidPassword)
                        isAllValid = false
                    }
//                    } else {
//                        errorText = act.getString(R.string.EmailIsInvalidLabel)
//                        isAllValid = false
//                    }
                } else {
                    errorText = act.getString(R.string.PasswordBlank)
                    isAllValid = false
                }
            } else {
                errorText = act.getString(R.string.IsRequiredLabel)
                    .replace("aaa", act.getString(R.string.emailOrMobile))
                isAllValid = false
            }
        } else doLogin = false

        if (isAllValid) {
            loginApi(email, password, act)
        } else {
            isError.value = true
            isLoading.value = false
        }
    }


    private fun loginApi(email: String?, password: String?, act: MainActivity) {

        //Start loading from the function call and not here
        var j = JsonObject()
        j.addProperty("UserName", email)
        j.addProperty("Password", password)

        val apiCall: Call<ResponseMessage>? = TapadooAPICall.apiInterface()?.login(j)

        apiCall?.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {

                if (response.isSuccessful) {

                    if (response.body()!!.status > 0) {

                        isError.value = false

                        sessionManager?.token = response.body()?.message.toString()

                        sessionManager?.LoginSession()

                        loginUser.value = response.body()!!

                        MainActivity.listener.onRestartApp()

                    } else if (response.body()?.status == -2) {

//                        val fragment = VerificationFragment(act)
//
//                        val bundle = Bundle()
//
//                        bundle.putString("mobile", response.body()?.value)
//
//                        bundle.putString("code", response.body()?.value2.toString())
//
//                        bundle.putString("password", password)
//
//                        fragment.arguments = bundle
//
//                        Navigator.loadFragment(act, fragment, R.id.content_home, true, "home")

                    } else {

                        errorText = response.body()!!.message
                        isError.value = true
                        Log.e(
                            "11111 ",
                            "response.body()!!.message ===>>> " + response.body()!!.message
                        )

                    }

                }

                isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {

//                isError.value = true
//                errorText = t.toString()

                isLoading.value = false

            }

        })
    }


    fun registerUser(registerRequest: RegisterRequest, act: MainActivity) {

        isError.value = false

        if (registerRequest.email.isEmpty() && registerRequest.name.isEmpty() &&
            registerRequest.phone.isEmpty() && registerRequest.countryId == 0 &&
            registerRequest.password.isEmpty()
        ) {
            errorText = act.getString(R.string.FillAllFields)
            isError.value = true
            return
        }

        if (!isValidEmail(registerRequest.email)) {
            errorText = act.getString(R.string.EmailIsInvalidLabel)
            isError.value = true
            return
        }

        if (registerRequest.phone.length != 8) {
            errorText = act.getString(R.string.MobileLengthError)
            isError.value = true
            return
        }

        if (!isPasswordValid(registerRequest.password)) {
            errorText = act.getString(R.string.NotValidPassword)
            isError.value = true
            return
        }

//        if (!registerRequest.isTermsChecked) {
//            errorText = act.getString(R.string.AcceptTermsMessage)
//            isError.value = true
//            return
//        }

        if (isError.value == false)
            registerApi(registerRequest, act)

    }


    private fun registerApi(body: RegisterRequest, act: MainActivity) {

        isLoading.value = true

        val apiCall: Call<ResponseMessage>? = TapadooAPICall.apiInterface()?.register(body)

        apiCall?.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {

                if (response.isSuccessful) {

//                    errorText = response.body()!!.message
//
//                    isError.value = true

                    Snackbar.make(act.binding.root, response.body()!!.message, Snackbar.LENGTH_LONG)
                        .show()

                    if (response.body()!!.status > 0) {

                        isError.value = false

                        registerUser.value = response.body()

                        if (registerUser.value?.status ?: 0 > 0) {

//                            val fragment = VerificationFragment(act)
//
//                            val bundle = Bundle()
//
//                            bundle.putString("code", body.countryCode)
//
//                            bundle.putString("mobile", body.phone)
//
//                            bundle.putString("password", body.password)
//
//                            fragment.arguments = bundle
//
//                            Navigator.loadFragment(act, fragment, R.id.content_home, true, "home")

                        }

                    } else {

                        Log.e(
                            "11111 ",
                            "response.body()!!.message ===>>> " + response.body()!!.message
                        )

                    }

                }

                isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {

                isLoading.value = false

            }

        })

    }


    fun editUser(getUser: GetUser?, act: MainActivity) {

        doLogin = false

        var isAllValid = true
        isError.value = false
        isLoading.value = true

        if (!getUser?.name.isNullOrEmpty() && !getUser?.email.isNullOrEmpty() && !getUser?.phone.isNullOrEmpty()) {

            if (!isValidEmail(getUser?.email)) {
                errorText = act.getString(R.string.EmailIsInvalidLabel)
                isAllValid = false
            } else if (getUser?.phone?.length != 8) {
                errorText = act.getString(R.string.MobileLengthError)
                isAllValid = false
            }

        } else {
            errorText = act.getString(R.string.FillAllFields)
            isAllValid = false
        }

        if (isAllValid) {
            editProfileApi(getUser, act)
        } else {
            isError.value = true
            isLoading.value = false
        }

    }


    private fun editProfileApi(body: GetUser?, act: MainActivity) {

        //Start loading from the function call and not here

        val apiCall: Call<ResponseMessage>? =
            body?.let { TapadooAPICall.apiInterface()?.editProfile(it) }

        apiCall?.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {

                if (response.isSuccessful) {

                    Snackbar.make(act.binding.root, response.body()!!.message, Snackbar.LENGTH_LONG)
                        .show()

                    if (response.body()!!.status > 0) {

                        editProfile.value = response.body()!!

                        act.supportFragmentManager.popBackStack()

                    }

                }

                isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {

//                isError.value = true
//                errorText = t.toString()

                isLoading.value = false

            }

        })
    }


    fun myProfile() {

        myProfile(null)

    }


    fun myProfile(userId: String?) {

        doLogin = false

        isLoading.value = true

        val apiCall: Call<GetUser?>? = TapadooAPICall.apiInterface()?.myProfile(userId)

        apiCall?.enqueue(object : Callback<GetUser?> {
            override fun onResponse(
                call: Call<GetUser?>,
                response: Response<GetUser?>
            ) {

                if (response.isSuccessful) {

                    getUser.value = response.body()!!

                }

                isLoading.value = false

            }

            override fun onFailure(call: Call<GetUser?>, t: Throwable) {

//                isError.value = true
//                errorText = t.toString()

                isLoading.value = false

            }

        })

    }


    fun forgotPasswordApi(email: String?, act: MainActivity) {

        isLoading.value = true

        val apiCall: Call<ResponseMessage?>? = TapadooAPICall.apiInterface()?.forgotPassword(email)

        apiCall?.enqueue(object : Callback<ResponseMessage?> {
            override fun onResponse(
                call: Call<ResponseMessage?>?,
                response: Response<ResponseMessage?>?
            ) {

                if (response?.isSuccessful == true) {

                    errorText = response.body()?.message.toString()

                    isError.value = true

                    if (response.body()?.status ?: 0 > 0) {

                        act.supportFragmentManager.popBackStack()

                    }

                }

                isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage?>?, t: Throwable) {

                isError.value = true
                errorText = t.toString()

                isLoading.value = false

            }

        })

    }


    fun logoutApi() {

        isLoading.value = true

        val apiCall: Call<ResponseMessage?>? =
            TapadooAPICall.apiInterface()?.logout(2, LanguageSessionManager.regId) //this is firebase token
//        val apiCall: Call<ResponseMessage?>? =
//            CellsAPICall.apiInterface()?.logout(2, sessionManager?.token)

        apiCall?.enqueue(object : Callback<ResponseMessage?> {
            override fun onResponse(
                call: Call<ResponseMessage?>?,
                response: Response<ResponseMessage?>?
            ) {

                if (response?.isSuccessful == true) {

                    sessionManager?.token = ""

                    sessionManager?.LogoutSession()

                    dataLogout.value = response.body()

                }

                isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage?>?, t: Throwable) {

//                isError.value = true
//                errorText = t.toString()

                isLoading.value = false

            }

        })
    }


    fun registerInBackground(firebaseToken: String?, context: Context) {

        val jsonObject = JsonObject()

        jsonObject.addProperty("DeviceType", 2)

        jsonObject.addProperty("DeviceId", getIMEI(context))

        jsonObject.addProperty("Token", firebaseToken)

        jsonObject.addProperty("lang", LanguageSessionManager.language)

        if (isCustomerApp) {
            jsonObject.addProperty("UserType", "3")
        } else {
            jsonObject.addProperty("UserType", "2")
        }

        val apiCall: Call<ResponseMessage?>? =
            TapadooAPICall.apiInterface()?.updateDeviceToken(jsonObject)

        apiCall?.enqueue(object : Callback<ResponseMessage?> {
            override fun onResponse(
                call: Call<ResponseMessage?>?,
                response: Response<ResponseMessage?>?
            ) {

                if (response?.isSuccessful == true) {

                    if (response.body()!!.status > 0) {

                        Log.e("11111", " Firebase token registered ***********")

                        LanguageSessionManager.regId = firebaseToken

//                        todo: uncomment when API is fixed for updatedevicetoken
                        sessionManager!!.token = response.body()?.value2.toString()
//                        sessionManager!!.token = regId.toString()

                    }

                }
                //token expire
                else if (response?.code() == 401) {

                }

            }

            override fun onFailure(call: Call<ResponseMessage?>?, t: Throwable) {

            }

        })

    }


    fun addUserProfile(getUser: GetUser?, act: MainActivity) {
        editProfileApi(getUser, act)
    }


}