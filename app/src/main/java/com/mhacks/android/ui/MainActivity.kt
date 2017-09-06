package com.mhacks.android.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.FragmentTransaction
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.network.NetworkSingleton
import com.mhacks.android.data.network.NetworkSingleton.Callback
import com.mhacks.android.data.network.services.HackathonApiService
import com.mhacks.android.ui.announcements.AnnouncementFragment
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import com.mhacks.android.ui.countdown.WelcomeFragment
import com.mhacks.android.ui.info.InfoFragment
import com.mhacks.android.ui.map.MapViewFragment
import com.mhacks.android.ui.schedule.EventFragment
import com.mhacks.android.ui.ticket.TicketDialogFragment
import com.mhacks.android.util.ResourceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.mhacks.android.R
import org.mhacks.mhacks.login.LoginActivity
import javax.inject.Inject

/**
 * Activity defines primarily the initial network calls to GCM as well as handle Fragment transactions.
 */
class MainActivity : AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        BaseFragment.OnNavigationChangeListener {

    lateinit var regid: String

    var notif: String? = null

    val PROJECT_NUMBER: String by lazy { getString(R.string.gcm_server_id) }

    private var canUsePlayServices: Boolean = false
    private var menuItem: MenuItem? = null

    val networkSingleton by lazy {
        NetworkSingleton.newInstance(application = application as MHacksApplication)
    }

    private val gcm: GoogleCloudMessaging by lazy { GoogleCloudMessaging.getInstance(applicationContext) }

    @Inject lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkSingleton.getConfiguration(object: Callback<Config> {
            override fun success(response: Config) {
                Log.d(TAG, response.status.toString())
            }

            override fun failure(error: Throwable) {
                Log.d(TAG, error.message)
            }

        })

        setTheme(R.style.MHacksTheme)
        setSystemFullScreenUI()
        setContentView(R.layout.activity_main)
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))

        // Add the toolbar
        qr_ticket_fab.setOnClickListener({
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val ticket: TicketDialogFragment = TicketDialogFragment
                    .newInstance("Jeffrey Chang")
            ticket.show(ft, "dialog")
        })

        menuItem = navigation?.menu?.getItem(0)
        menuItem?.setTitle(R.string.title_home)
        setSupportActionBar(toolbar)
        updateFragment(WelcomeFragment.instance)


        // If Activity opened from push notification, value will reflect fragment that will initially open
        notif = intent.getStringExtra("notif_link")

        if (true) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        navigation?.setOnNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    updateFragment(WelcomeFragment.instance)
                }
                R.id.navigation_announcements -> {
                    updateFragment(AnnouncementFragment.instance)
                }
                R.id.navigation_events -> {
                    updateFragment(EventFragment.instance)
                }
                R.id.navigation_map -> {
                    updateFragment(MapViewFragment.instance)
                }
                R.id.navigation_info -> {
                    updateFragment(InfoFragment.instance)
                }
            }
            menuItem = item
            true
        })

        //TODO: Adam Commenting this out because it's not even used anyways, but you don't need
        //TODO: Adam explicit null checks when you can use the safe operator.
//        if (notif != null) {
//            // Opens Announcements
//            if (notif == "Announcements") {
//                // updateFragment(announcementsFragment);
//            }
//        }
    }


    @TargetApi(21)
    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    @SuppressLint("InlinedApi")
    fun setSystemFullScreenUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @SuppressLint("InlinedApi")
    override fun setTransparentStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(21)
    override fun clearTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setActionBarColor(@ColorRes color: Int) {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, color))
    }

    override fun setFragmentTitle(title: Int) {
        setTitle(title)
    }

    private fun setBottomNavigationColor(color: NavigationColor) {
        val colorStateList = NavigationColor.getColorStateList(
                ContextCompat.getColor(this, color.primaryColor),
                ContextCompat.getColor(this, color.secondaryColor)
        )

        navigation?.itemIconTintList = colorStateList
        navigation?.itemTextColor = colorStateList
    }

    //    public void login() {
    //        // Log in if possible.
    //        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    //        final String username = sharedPref.getString(SettingsFragment.USERNAME_KEY, "");
    //        String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");
    //
    //        final NetworkManager networkManager = NetworkManager.getInstance();
    //        if (username.length() != 0 && password.length() != 0) {
    //            networkManager.login(username, password, new HackathonCallback<User>() {
    //                @Override
    //                public void success(User response) {
    //                    userProfile.withName(response.getName());
    //                    mDrawer.updateItem(userProfile);
    //                }
    //
    //                @Override
    //                public void failure(Throwable error) {
    //                }
    //            });
    //        }
    //    }


//    fun updateGcm() {
//        if (!checkPlayServices()) {
//            Log.e(TAG, "No valid Google Play Services APK found.")
//            return
//        }
//        // Grabs the Google Cloud Messaging REG ID and sends it to the backend
//
//        object : AsyncTask<Void, Void, String>() {
//            override fun doInBackground(vararg params: Void): String {
//                var msg = ""
//                try {
//                    // reg_id
//                    val data = Bundle()
//
//                    regid = gcm.register(PROJECT_NUMBER)
//
//                    data.putString("regid", regid)
//                    /*InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/
//
//                    val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                    val gcmPush = sharedPref.getString("gcm", "")
//                    val channels = sharedPref.getStringSet(SettingsFragment.PUSH_NOTIFICATION_CHANNELS, null)
//
//                    var pref = 1
//                    if (channels != null) {
//                        val channelPrefs = channels.toTypedArray()
//                        for (i in channelPrefs.indices) {
//                            pref += Integer.parseInt(channelPrefs[i])
//                        }
//                    } else
//                        pref = 63
//
//                    val token = Token(regid)
//                    token.setName(pref.toString())
//                    // active=true by default
//
//                    val networkManager = NetworkManager.getInstance()
//                    networkManager.sendToken(token, object : HackathonCallback<Token> {
//                        override fun success(response: Token) {
//                            Log.d(TAG, "gcm sent successfully: " + token.getRegistrationId())
//                            sharedPref.edit().putString("gcm", regid).apply()
//                        }
//
//                        override fun failure(error: Throwable) {
//                            Log.e(TAG, "gcm didnt work", error)
//                        }
//                    })
//
//                    msg = "Device registered, reg id =" + regid
//                    Log.i("GCM", msg)
//
//                } catch (ex: IOException) {
//                    msg = "Error :" + ex.message
//                    Log.e(TAG, "IOException when registering the device", ex)
//
//                }
//
//                return msg
//            }
//
//            override fun onPostExecute(msg: String) {
//
//            }
//        }.execute(null, null, null)
//    }


//    // Checks if the user can obtain the correct Google Play Services number
//    private fun checkPlayServices(): Boolean {
//        canUsePlayServices = true
//        object : AsyncTask<Void, Void, String>() {
//            override fun doInBackground(vararg params: Void): String {
//                val msg = ""
//                val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
//                if (resultCode != ConnectionResult.SUCCESS) {
//                    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                        //GooglePlayServicesUtil.getErrorDialog(resultCode, this ,GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
//                    } else {
//                        Log.e(TAG, "This device is not supported.")
//                        finish()
//                    }
//
//                    return "false"
//                }
//
//                return "true"
//            }
//
//            override fun onPostExecute(msg: String) {
//                canUsePlayServices = (msg == "true")
//            }
//        }.execute(null, null, null)
//        return canUsePlayServices
//    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param fragment fragment to replace the main container with
     */
    private fun updateFragment(fragment: Fragment?) {
        if (fragment == null) return  // only used for pre-release while fragments are not finalized

        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    //    private void requestAndEnableLocation() {
    //        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
    //            PackageManager.PERMISSION_GRANTED) {
    //
    //            new AlertDialog.Builder(this)
    //                    .setTitle("Location Permission")
    //                    .setMessage("The MHacksApplication app uses your location to show you where you are on the map and help you find rooms. Would you like to enable location services?")
    //                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    //                        public void onClick(DialogInterface dialog, int which) {
    //                            // Request permission
    //                            ActivityCompat.requestPermissions(SecondaryActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
    //                                                                                          Manifest.permission.ACCESS_COARSE_LOCATION},
    //                                                              LOCATION_REQUEST_CODE);
    //                        }
    //                    })
    //                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
    //                        public void onClick(DialogInterface dialog, int which) {
    //                            updateFragment(mapViewFragment, true);
    //                        }
    //                    })
    //                    .show();
    //        } else {
    //            updateFragment(mapViewFragment, true);
    //        }
    //    }


//    /**
//     * Handles all the clicks for the EventFragment and it's fragments.
//     * @param v clicked View
//     */
//    fun scheduleFragmentClick(v: View) {
//        //        if (v.getId() == R.id.event_close_button) scheduleFragment.scheduleFragmentClick(v);
//    }


    override fun addPadding() {
        val height: Int = ResourceUtil.convertDpResToPixel(context = this,
                res = R.dimen.toolbar_height)
        fragment_container.setPadding(0, height, 0, 0)
    }

    override fun removePadding() {
        fragment_container.setPadding(0, 0, 0, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (permissions.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "Location permissions granted")
            }
        }// updateFragment(mapViewFragment);
    }


    companion object {

        val TAG = "org.mhacks/MainActivity"

        // Permissions
        val LOCATION_REQUEST_CODE = 7
    }
}
