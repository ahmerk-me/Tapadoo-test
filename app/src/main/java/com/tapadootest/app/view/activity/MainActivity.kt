package com.tapadootest.app.view.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.*
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.tapadootest.app.R
import com.tapadootest.app.classes.*
import com.tapadootest.app.databinding.ActivityMainBinding
import com.tapadootest.app.view.fragment.BookListFragment
import com.tapadootest.app.viewmodel.UserAuthViewModel
import com.google.android.material.snackbar.Snackbar

open class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var act: MainActivity

    companion object {
        lateinit var listener: BaseActivityListener
    }

    lateinit var context: Context

    lateinit var tf: Typeface

    lateinit var tfBold: Typeface

    lateinit var viewModel: UserAuthViewModel

    var tabNumber = 0

    lateinit var drawer: DrawerLayout

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    fun setTextFonts(root: ViewGroup) {

        for (i in 0 until root.childCount) {

            val v = root.getChildAt(i)

            when (v) {

                is TextView -> {

                    v.typeface = tf

                }
                is Button -> {

                    v.typeface = tf

                }
                is EditText -> {

                    v.typeface = tf

                }
                is ViewGroup -> {

                    setTextFonts(v)

                }

            }

        }

    }


    fun setupDefaultSettings() {

        Log.d("setupDefaultSettings", "1")

        with(binding.appBarHome.appBarNormal) {

            act.binding.appBarHome.tvNoData.visibility = GONE

            act.binding.appBarHome.appbar.visibility = VISIBLE

            title.setTypeface(tf, Typeface.BOLD)

            title.visibility = VISIBLE

            if (act.supportFragmentManager.backStackEntryCount > 0) {

                back.visibility = VISIBLE

                back.setImageResource(R.drawable.back)

            } else {
                back.visibility = INVISIBLE
            }

        }

    }


    fun hideTopBar() {

        act.binding.appBarHome.appbar.visibility = GONE

    }


    override fun attachBaseContext(newBase: Context?) {

        super.attachBaseContext(newBase?.let { LocaleHelper.onAttach(it) })

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            refreshUI()
                if (it) {
//                showDummyNotification()
                } else {
                    Snackbar.make(
                        findViewById<View>(android.R.id.content).rootView,
                        "Please grant Notification permission from App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        act = this@MainActivity

        context = this@MainActivity

        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[UserAuthViewModel::class.java]

        setContentView(binding.root)

        languageSessionManager = LanguageSessionManager(this)

        sessionManager = SessionManager(this)

        val newbuilder = StrictMode.VmPolicy.Builder()

        StrictMode.setVmPolicy(newbuilder.build())

        if (Build.VERSION.SDK_INT > 9) {

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

        }

        if (LanguageSessionManager.language != null && LanguageSessionManager.language.isNotEmpty()) {

            updateViews(LanguageSessionManager.language.toString(), false)

        } else updateViews("en", false)

        with(binding.appBarHome) {

            setSupportActionBar(appBarNormal.toolbar)

            setTextFonts(binding.relativeSideMenu)

            setTextFonts(appBarNormal.linearTopBar)

        }

//        tabNumber = 1
//        setTabs()

        with(binding) {

            drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

            val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                act, drawer, appBarHome.appBarNormal.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)

                    val moveFactor = navView.width * slideOffset

                    appBarHome.contentHome.translationX = if (isEnglish) -moveFactor else moveFactor

                    appBarHome.appBarNormal.toolbar.translationX =
                        if (isEnglish) -moveFactor else moveFactor

                    Log.d("onDrawerSlide", "onDrawerSlide")

                }

                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)

                    Log.d("onDrawerOpened", "onDrawerOpened")

//                    relativeSideMenu?.let { setTextFonts(it) }

                }

            }

            drawer.addDrawerListener(toggle)

            toggle.syncState()

            appBarHome.appBarNormal.toolbar.navigationIcon = null

        }

        onClicks()

        if (intent.hasExtra("fragType")) {

            val fragType = intent.getIntExtra("fragType", 0)

            Log.e("22222 ", "MainAct fragtype ===>>> " + fragType)
            if (sessionManager!!.isFirstTime) {

                sessionManager!!.isFirstTime = false

//                sessionManager?.token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjdiMDFhZjI4LWVkNWUtNDY4OS05OGM3LWYwY2EwZTAyY2ZjNyIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlZlbmRvciIsImV4cCI6MjEzNDE5MzkwMiwiaXNzIjoiaHR0cHM6Ly9WZW5kdWUuY29tIiwiYXVkIjoiaHR0cHM6Ly9WZW5kdWUuY29tIn0.ZYwvAym1x9GdBrf4YxSHF1xPDGDU3HiWboymwOs_v5Ml7ECS2Q4yjtk1C7mi1UUk0XxiVP-8uyb3JcWyQ3rQlQ"
//                sessionManager?.LoginSession()

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
//                    showDummyNotification()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                Navigator.loadFragment(
                    this, BookListFragment(act),
                    R.id.content_home, false, "home"
                )

            } else {
                Navigator.loadFragment(
                    this, BookListFragment(act),
                    R.id.content_home, false, "home"
                )
            }

        } else {

            Navigator.loadFragment(
                this, BookListFragment(act),
                R.id.content_home, false, "home"
            )

        }

    }


    private fun backBtnClick() {
        onBackPressedDispatcher.onBackPressed()
        hideKeyboard(act)
    }


    private fun updateViews(languageCode: String, isReloadApp: Boolean) {

        if (languageCode == "en" && !isReloadApp)
            setEnglishView()
        else if (languageCode == "en" && isReloadApp)
            setArabicView()
        else if (languageCode == "ar" && !isReloadApp)
            setArabicView()
        else if (languageCode == "ar" && isReloadApp)
            setEnglishView()

        if (isReloadApp) {
            listener.onRestartApp()
        }

    }


    private fun setEnglishView() {
        LocaleHelper.setLocale(this, "en")
        isEnglish = true
        tfBold = Typeface.createFromAsset(context.assets, English_Bold_Font)
        tf = Typeface.createFromAsset(context.assets, English_Regular_Font)
        LanguageSessionManager.language = "en"
    }


    private fun setArabicView() {
        LocaleHelper.setLocale(this, "ar")
        isEnglish = false
        tfBold = Typeface.createFromAsset(this.assets, Arabic_Bold_Font)
        tf = Typeface.createFromAsset(this.assets, Arabic_Regular_Font)
        LanguageSessionManager.language = "ar"
    }


    private fun onClicks() {

        Log.d("onClicks", "1")

        //SideMenu Clicks
        with(binding) {

            tvEditProfile.setOnClickListener {

                drawer.closeDrawers()

                if (sessionManager!!.isLoggedin) {

//                    Navigator.loadFragment(
//                        act,
//                        if (isCustomerApp) MyProfileFragment(act) else MyProfileDoctorFragment(act),
//                        R.id.content_home, true, "home"
//                    )

                } else {

//                    Navigator.loadFragment(act, LoginFragment(act), R.id.content_home, true, "home")

                }

            }

        }

        //TopBar Clicks
        with(binding.appBarHome.appBarNormal) {

            back.setOnClickListener {

                Log.d("onClicks", "2")

                backBtnClick()

            }

            title.setOnClickListener { }

        }

    }


    fun setBaseActivityListener(listener: BaseActivityListener) {
        Companion.listener = listener
    }


    interface BaseActivityListener {
        fun onRestartApp()
    }


    override fun onBackPressed() {

//        if(ChatListFragment.fragment?.isVisible == true){
//            Log.d("ChatListFragment","isVisible")
//            ChatListFragment.fragment?.onEncCallClicked()
//            backBtnClick()
//        }
//        else{
        Log.d("backBtnClick", "isVisible")
        backBtnClick()
//        }

    }

}