package com.tapadootest.app.classes

import android.content.res.Resources

var isCustomerApp = true

//demo
const val SHARE_URL = "http://tpbookserver.herokuapp.com"

const val BASE_URL = "$SHARE_URL/"

//const val DEEP_LINKING_URL = "$SHARE_URL/dl/bbb"

//const val DOWNLOAD_INVOICE: String = BASE_URL + "Client/Subscriptions/Invoice/{orderId}/pdf"

var isEnglish = true

const val English_Regular_Font = "fonts/SFPRODISPLAYREGULAR.OTF"

const val Arabic_Regular_Font = "fonts/Cairo-Regular.ttf"

const val English_Bold_Font = "fonts/SFPRODISPLAYBOLD.OTF"

const val Arabic_Bold_Font = "fonts/Cairo-Bold.ttf"

const val PUSH_NOTIFICATION = "pushNotification"

const val PAGE_SIZE = 10


// ******* app specific constants ********

const val LinearTopBar = 1.1
const val LoginTopBar = 1.2
const val SearchTopBar = 1.3
const val CalendarTopBar = 1.4
const val AppBarImage = 2.0
const val HideAllViews = 3.0

//Edit text configuration
var showAnimatedET = false
var showLabelOnEditText = true

//For Text Fragment
const val AboutUs = 1
const val TermsAndConditions = 2
const val PrivacyPolicy = 3

//Dp to pixel
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

// for saving state of CustomEditTexts
const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
const val SUPER_STATE_KEY = "SUPER_STATE_KEY"


const val PARTNER_NAME = "partner_name"
const val PARTNER_ID = "partner_id"
const val BOOKING_DATE = "booking_date"
const val END_BOOKING_DATE = "end_booking_date"
const val IS_CALL = "IS_CALL"
const val IS_VIDEO = "IS_VIDEO"
const val TIMER = "TIMER"


const val CHAT = "chat"
const val CHAT_ID = "chatId"
const val APPOINTMENT = "appointment"
const val GROUP = "Group"
const val CALL = "Call"

var Chat_URL: String = "$SHARE_URL/chatHub"

const val  ACTION_KEY_CHANNEL_NAME = "ecHANEL"
const val  ACTION_KEY_ENCRYPTION_KEY = "xdL_encr_key_"
const val  ACTION_KEY_ENCRYPTION_MODE = "tOK_edsx_Mode"

const val ACCEPT_LANGUAGE = "Accept-Language"
const val AUTHORIZATION = "Authorization"
const val OffsetHours = "OffsetHours"

var splashInitFirebase = false