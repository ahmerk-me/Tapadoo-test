package com.tapadootest.app.classes

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tapadootest.app.R
import com.tapadootest.app.models.CustomDialog
import com.tapadootest.app.view.activity.MainActivity
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


var sessionManager: SessionManager? = null

var languageSessionManager: LanguageSessionManager? = null

var cancelBeforeHours = 24

private const val PERMISSION_CODE = 23

fun getPixels(dipValue: Int, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dipValue.toFloat(),
        r.displayMetrics
    ).toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertLocalDateToDateString(localDate: LocalDate, format: String): String {

    val formatter = DateTimeFormatter.ofPattern(format)
    return localDate.format(formatter)
}

fun convertDateToStringNew(date: Date?, format: String?): String? {
    var dateStr: String? = null
    val df: DateFormat = SimpleDateFormat(format, Locale.US)
    try {
        dateStr = date?.let { df.format(it) }
    } catch (ex: Exception) {
        println(ex)
    }
    return dateStr
}

fun convertDateToUTCstringNew(date: Date?, format: String?): String {
    var dateStr = ""
    val df: DateFormat = SimpleDateFormat(format, Locale.US)
//            df.timeZone = TimeZone.getTimeZone("UTC")
    try {
        dateStr = df.format(date ?: "")
    } catch (ex: Exception) {
        println(ex)
    }
    return dateStr
}

fun convertStringToDateNew(date: String, format: String): Date? {
    var resultDate: Date? = null
    val df: DateFormat = SimpleDateFormat(format, Locale.US)
    try {
        resultDate = df.parse(date)
    } catch (ex: Exception) {
        println(ex)
    }
    return resultDate
}

fun convertStringDateToFormat(
    date: String,
    existingFormat: String,
    requiredFormat: String
): String? {

    var convertedDate: Date? = null
    val df: DateFormat = SimpleDateFormat(existingFormat, Locale.US)
    try {
        convertedDate = df.parse(date)
    } catch (ex: Exception) {
        println(ex)
    }

    return convertDateToStringNew(convertedDate!!, requiredFormat)
}

fun DisableLayout(layout: ViewGroup) {
    layout.isEnabled = false
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is ViewGroup) {
            DisableLayout(child)
        } else {
            child.isEnabled = false
        }
    }
}

fun EnableLayout(layout: ViewGroup) {
    layout.isEnabled = true
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is ViewGroup) {
            EnableLayout(child)
        } else {
            child.isEnabled = true
        }
    }
}

fun isValidEmail(target: CharSequence?): Boolean {
    return if (target == null) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }
}

fun isPasswordValid(password: String): Boolean {

    return password.length >= 6
}

fun clearStack(act: FragmentActivity) {
    val fm = act.supportFragmentManager
    Log.d(
        "BackStackEntryCount() ",
        " fm.getBackStackEntryCount() " + fm.backStackEntryCount
    )

    val limit = 0
    for (i in 0 until fm.backStackEntryCount - limit) {
        fm.popBackStack()
    }
} // clearing back stack

fun logoutSession(mSessionManager: SessionManager) {

    mSessionManager.LogoutSession()
    mSessionManager.userCode = ""
    mSessionManager.token = ""
    mSessionManager.userName = ""
    mSessionManager.userFullName = ""
}

fun hideKeyboard(activity: Activity) {
    try {
        val inputManager: InputMethodManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView: View? = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun showKeyboard(activity: Activity, view: View) {
    try {
        val inputManager: InputMethodManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView: View? = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.showSoftInput(
                view,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun showListEmptyMessage(
    textView: TextView,
    msg: String,
    act: Activity,
    showMsg: Boolean
) {

    textView.text = msg

    textView.visibility = if (showMsg) VISIBLE else GONE

}

fun getAddress(location: String): String {

    return "test address hard coded!!!"
}


//We are calling this method to check the permission status
fun isWriteExternalStorageAllowed(act: FragmentActivity?): Boolean {
    //Getting the permission status
    val result =
        ContextCompat.checkSelfPermission(act!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //If permission is granted returning true
    return result == PackageManager.PERMISSION_GRANTED

    //If permission is not granted returning false
}

//Requesting permission
fun requestWriteExternalStoragePermission(act: FragmentActivity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            act,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    ) {
        //If the user has denied the permission previously your code will come to this block
        //Here you can explain why you need this permission
        //Explain here why you need this permission

        //checkMyPermission();
    }

    //And finally ask for the permission
    ActivityCompat.requestPermissions(
        act,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        PERMISSION_CODE
    )
}

fun getIMEI(context: Context): String? {
    return (Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            + "-" + context.applicationContext.packageName)
}


fun persistImage(bitmap: Bitmap?, name: String?, act: Context): String? {
    val filesDir = act.filesDir
    val imageFile = File(filesDir, name)
    val videoThumbnailPath = imageFile.path
    Log.d("videoThumbnailPath", "" + videoThumbnailPath)
    val os: OutputStream
    try {
        os = FileOutputStream(imageFile)
        if (os != null && bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        }
    } catch (e: java.lang.Exception) {

        //Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
    }
    return videoThumbnailPath
}


fun getFileName(path: String): String? {
    val index = path.lastIndexOf('/')
    return path.substring(index + 1)
}


fun createThumbnail(activity: Activity?, path: String?): Bitmap? {
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    var bitmap: Bitmap? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(activity, Uri.parse(path))
        bitmap =
            mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    } finally {
        mediaMetadataRetriever?.release()
    }
    return bitmap
}


fun getVideoThumbnail(act: FragmentActivity?, path: String?): Bitmap? {
    var bitmap: Bitmap? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        bitmap = createThumbnail(act, path)
        if (bitmap != null) {
            return bitmap
        }
    } else {
        bitmap =
            ThumbnailUtils.createVideoThumbnail(path!!, MediaStore.Images.Thumbnails.MICRO_KIND)
        if (bitmap != null) {
            return bitmap
        }
    }
    // MediaMetadataRetriever is available on API Level 8 but is hidden until API Level 10
    var clazz: Class<*>? = null
    var instance: Any? = null
    try {
        clazz = Class.forName("android.media.MediaMetadataRetriever")
        instance = clazz.newInstance()
        val method = clazz.getMethod("setDataSource", String::class.java)
        method.invoke(instance, path)
        // The method name changes between API Level 9 and 10.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
            bitmap = clazz.getMethod("captureFrame").invoke(instance) as Bitmap
        } else {
            val data = clazz.getMethod("getEmbeddedPicture").invoke(instance) as ByteArray
            if (data != null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            }
            if (bitmap == null) {
                bitmap = clazz.getMethod("getFrameAtTime").invoke(instance) as Bitmap
            }
        }
    } catch (e: java.lang.Exception) {
        bitmap = null
    } finally {
        try {
            if (instance != null) {
                clazz!!.getMethod("release").invoke(instance)
            }
        } catch (ignored: java.lang.Exception) {
        }
    }
    return bitmap
}


fun getVideoThumbnailPath(current_path_video: String, context: FragmentActivity): String? {
    return persistImage(
        getVideoThumbnail(context, current_path_video),
        getFileName(current_path_video)?.replace(".mp4", ".jpg"), context
    )
}

fun showSnackBar(mainLayout: View?, message: String?) {
    val snackbar = Snackbar.make(mainLayout!!, message!!, Snackbar.LENGTH_LONG)
    val view = snackbar.view
    val params = view.layoutParams as FrameLayout.LayoutParams

//        params.gravity = Gravity.TOP;
    view.layoutParams = params
    snackbar.show()
}


fun showErrorMessage(response: ResponseBody?, mainLayout: View?) {
    if (response != null) {
        if (response.byteStream() != null) {

//                TypedInput body = response.getBody();
            var outResponse = ""
            try {
                val reader = BufferedReader(InputStreamReader(response.byteStream()))
                val out = StringBuilder()
                val newLine = System.getProperty("line.separator")
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    out.append(line)
                    out.append(newLine)
                }
                outResponse = out.toString()
                Log.d("outResponse", "" + outResponse)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            if (outResponse != null) {
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(outResponse)
                    if (jsonObject.has("errors")) {
                        outResponse = jsonObject.getString("errors").replace("\"".toRegex(), "")
                        if (outResponse.split(",".toRegex()).toTypedArray().isNotEmpty()) {
                            if (outResponse.split(",".toRegex())
                                    .toTypedArray()[0].split(":".toRegex())
                                    .toTypedArray().size > 1
                            ) {
                                outResponse = outResponse.split(",".toRegex())
                                    .toTypedArray()[0].split(":".toRegex())
                                    .toTypedArray()[1].replace("\\[".toRegex(), "")
                                    .replace("\\]".toRegex(), "").replace("\\}".toRegex(), "")
                                Log.d("outResponse", "3 $outResponse")
                                Snackbar.make(mainLayout!!, outResponse, Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

// url = file path or whatever suitable URL you want.
fun getMimeType(url: String?): String? {
    return MimeTypeMap.getFileExtensionFromUrl(Uri.encode(getFileName(url ?: "")))
        ?.run {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase(Locale.getDefault()))
        }
        ?: url // You might set it to */*
}


fun getHeaders(): Map<String, String>? {

    val map: MutableMap<String, String> = HashMap()

    map["Authorization"] = sessionManager!!.token

    map["Accept-Language"] = LanguageSessionManager.language

    map["Content-Type"] = "application/json"

    return map

}


fun getSelectedItems(list: ArrayList<CustomDialog?>): ArrayList<CustomDialog?> {

    return ArrayList(list.filter { it?.isSelected == true })

}


fun getFormattedPrice(context: Context?, price: Double): String? {
    val format1 = java.lang.String.format(
        Locale("en"), "%.3" + "f", price
    )
    val format2 =
        NumberFormat.getNumberInstance(Locale.US).format(java.lang.Double.valueOf(format1))
    return format2.replace(".000", "") +
            " " + context?.getString(R.string.KD)
}


fun getFormattedPrice(context: Context?, price: String): String? {
    val c = ""

//        if (Math.abs(Double.parseDouble(price) / 1000000) > 1) {
//
//            c =  "m";
//
//        } else if (Math.abs(Double.parseDouble(price) / 1000) > 1) {
//
//            c = "k";
//
//        }
    return price + c + " " + context?.getString(R.string.KD)
}


fun setWebViewContent(content: String, webView: WebView) {
    webView.webViewClient = WebViewClient()
    webView.settings.javaScriptEnabled = true
    webView.settings.javaScriptCanOpenWindowsAutomatically = true
    webView.settings.pluginState = WebSettings.PluginState.ON
    webView.webChromeClient = WebChromeClient()
    val layoutParams: RelativeLayout.LayoutParams =
        webView.layoutParams as RelativeLayout.LayoutParams
//    layoutParams.setMargins(20, 20, 20, 20)
//    webView.layoutParams = layoutParams
    var mainHTMLText = ""
    val sb = StringBuffer(content)

//        content = sb.replace(content.lastIndexOf("\""), content.lastIndexOf("\"") + 1, "").toString();

//        mainHTMLText = content.replaceFirst("\"", "");
    mainHTMLText = content
    Log.d("mainHTMLText", "" + mainHTMLText)
    var finalHTML = ""
    if (LanguageSessionManager.language.equals("en", true)) {
        mainHTMLText = mainHTMLText.replace("font-".toRegex(), "f_nt")
        finalHTML =
            "<html><head><style type='text/css'>@font-face {font-family: 'Montserrat-Regular';src: url('" + English_Regular_Font + "');}" +
                    " body {background-color: " +
                    "transparent;border: 0px;margin: 10px;padding: 0px;font-family: 'Montserrat-Regular'; font-size: 15px;width: 100%;" +
                    "line-height: 150%; color:" + "#616C7D" + ";}</style></head><body dir='LTR'><div style='color: " + "#616C7D" + ";font-size:18px;text-align: center;font-weight: bold;'>" + "</div>" +
                    mainHTMLText + "<style type='text/css'> body {width:95%;} iframe { width: 100%;} img {width: 100%;} table {width: 100%; }td {word-wrap: break-word;}</style></body></html>"
    } else {
        finalHTML =
            "<html><head><style type='text/css'>@font-face {font-family: 'DroidSansArabic';src: url('" + Arabic_Regular_Font + "');}" +
                    " body {background-color: " +
                    "transparent;border: 0px;margin: 10px;padding: 0px;font-family: 'DroidSansArabic'; font-size: 15px;width: 100%;" +
                    "color:" + "#616C7D" + ";}</style></head><body dir='RTL'><div style='color: " + "#616C7D" + ";font-size:18px;text-align: center;'>" + "</div>" +
                    mainHTMLText + "<style type='text/css'> body {width:95%;} iframe { width: 100%;} img {width: 100%;} table {width: 100%;} td {word-wrap: break-word;}</style></body></html>"
    }
    Log.d("finalHTML", "" + finalHTML)
    webView.setBackgroundColor(Color.parseColor("#ffffff"))
    webView.loadDataWithBaseURL("file:///android_asset/", finalHTML, "text/html", "UTF-8", null)
}


fun convertToMMMDDYYYY(date: String): String {

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    return dateFormat.format(dateFormat1.parse(date))

}


fun convertFromDDMMYYYYToMMMDDYYYY(date: String): String {

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    return dateFormat.format(dateFormat1.parse(date))

}


fun convertFromMMMDDYYYYToDDMMYYYY(date: String): String {

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    return dateFormat1.format(dateFormat.parse(date))

}

fun convertSpToPixels(sp: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
    )
}


fun getAgeFromDOB(dobString: String?): Int {
    if (dobString.isNullOrEmpty()) {
        return 0
    } else {
        var date: Date? = null
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        try {
            date = sdf.parse(dobString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date == null) return 0
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.time = date
        val year = dob[Calendar.YEAR]
        val month = dob[Calendar.MONTH]
        val day = dob[Calendar.DAY_OF_MONTH]
        dob[year, month + 1] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}


fun openUrl(act: MainActivity, url: String?) {

    if (!url.isNullOrEmpty()) {

        if (URLUtil.isValidUrl(url)) {

//            try {
//
//                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//
//                act.startActivity(myIntent)
//
//            } catch (e: ActivityNotFoundException) {

            val bundle1 = Bundle()

            bundle1.putString("id", url)

            bundle1.putBoolean("isUrl", true)

//            val fragment: Fragment = AboutUsFragment(act, ComingFrom.ContactUs)
//
//            fragment.arguments = bundle1
//
//            Navigator.loadFragment(act, fragment, R.id.content_home, true, "home")

//            }

        }

    }
}


fun getCurrentTimezoneOffset(): String? {
    val tz = TimeZone.getDefault()
    val cal = GregorianCalendar.getInstance(tz)
    val offsetInMillis = tz.getOffset(cal.timeInMillis)
    return "" + (if (offsetInMillis >= 0) "" else "-") + (Math.abs(offsetInMillis / 3600000) + offsetInMillis / 60000.0 % 60.0 / 60.0)
}


fun convertFtInchToCm(ft: String, inch: String): String {
    return ((ft.toDouble() / 0.397) + (inch.toDouble() / 0.0328)).toString()
}

fun convertUtc2Local(utcTime: String?): String {
    var time = ""
    if (utcTime != null) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local

fun convertUtc2EEEDDMMM(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local

fun convertUtc2DDMMM(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local

fun isTodayDate(utcTime: String?): Boolean {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
            //  0 comes when two date are same,
            //  1 comes when date1 is higher then date2
            // -1 comes when date1 is lower then date2
            if (utcFormatter.parse(utcFormatter.format(Date())).compareTo(gpsUTCDate) == 0) {
                Log.d("isCancelledAllowed", "true")
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return false
} // convertUtc2Local

fun convertUtc2EEEEDDMMMMYYYY(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local

fun convertUtc2HHMM(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local


fun convertTo2hhmm(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local


fun convertToddMMyyyy(utcTime: String?): String {
    var time = ""
    if (!utcTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        localFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        time = localFormatter.format(gpsUTCDate!!.time)
    }
    return time
} // convertUtc2Local


fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val isKitKatorAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor =
            uri?.let {
                context.contentResolver.query(
                    it,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
            }
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun openGoogleMap(act: MainActivity, latitude: String, longitude: String) {
    if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty()) {
        val url =
            "http://maps.google.com/maps?daddr=$latitude,$longitude&mode=driving"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        act.startActivity(intent)
    }
}

fun isCancelledAllowed(startTime: String): Boolean {
    if (!startTime.isNullOrEmpty()) {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val cal = Calendar.getInstance()
        cal.time = utcFormatter.parse(startTime)
        cal.add(Calendar.HOUR, -cancelBeforeHours)

        val currentUTCTime: Date = utcFormatter.parse(utcFormatter.format(Date()))
        Log.d("isCancelledAllowed", "1 -> " + utcFormatter.format(Date()))
        Log.d("isCancelledAllowed", "2 -> " + utcFormatter.format(cal.time))
        Log.d("isCancelledAllowed", "3 -> $startTime")
        //  0 comes when two date are same,
        //  1 comes when date1 is higher then date2
        // -1 comes when date1 is lower then date2
        if (currentUTCTime.compareTo(cal.time) == -1) {
            Log.d("isCancelledAllowed", "true")
            return true
        }
    }
    Log.d("isCancelledAllowed", "false")
    return false
}

fun setupUIKeyboards(context: Activity, view: View) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { v, event ->
            hideKeyboard(context)
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupUIKeyboards(context, innerView)
        }
    }
}

fun compareDate(start: String?, end: String?): Long {

    val inputFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
    var dif: Long = 0
    if (start != null && start.isNotEmpty() && end != null && end.isNotEmpty()) {
        try {
//                if (inputFormatter.parse(start).before(Date())) {
            dif = inputFormatter.parse(end).time - inputFormatter.parse(start).time
            if (dif > 0) {

                return dif
            } else {
//                        MainActivity.videoCall.setVisibility(View.INVISIBLE)
            }
//                } else {
//                    MainActivity.videoCall.setVisibility(View.INVISIBLE)
//                }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return 0
}
