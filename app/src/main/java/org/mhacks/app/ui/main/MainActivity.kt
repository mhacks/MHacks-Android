package org.mhacks.app.ui.main

import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mhacks.app.BuildConfig
import org.mhacks.app.R
import org.mhacks.app.databinding.ActivityMainBinding
import org.mhacks.app.ui.BaseActivity
import org.mhacks.app.ui.NavigationColor

/**
 * Main Activity that handles most of the interactions. Sets up the Login Activity and loads
 * feature fragments with a bottom navigation bar.
 */

class MainActivity : BaseActivity()
//        , TicketDialogFragment.Callback
{
    private lateinit var binding: ActivityMainBinding

    override val bottomNavigationView: BottomNavigationView
        get() = binding.mainActivityNavigation

    override val containerView: FrameLayout
        get() = binding.mainActivityFragmentContainer

    // Default value for the first fragment id reference.
    private var itemId = R.id.welcome_fragment

    private val navController by lazy {
        Navigation.findNavController(
                this,
                R.id.main_activity_fragment_host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)

        subscribeNonUi()

        initActivity()

        checkIfInstantApp()
    }

    private fun checkIfInstantApp() {
        val appLinkIntent = intent
        val appLinkData = appLinkIntent?.data

        if (appLinkData?.path == BuildConfig.INSTANT_APP_URL) {
            initActivity()
            return
        }

//        mainViewModel.checkIfLoggedIn()
    }

    private fun showTicketDialogFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        val prev = supportFragmentManager.findFragmentByTag("ticket_dialog")
//        if (prev != null) ft.remove(prev)
//        ft.addToBackStack(null)
//        val ticket: TicketDialogFragment = TicketDialogFragment.newInstance()
//        ticket.show(ft, "ticket_dialog")
    }

    private fun initActivity() {
        setSystemFullScreenUI()
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
        ).apply {
            subscribeUi(this)
            mainActivityNavigation.menu.getItem(0).setTitle(R.string.title_home)

            setSupportActionBar(mainActivityToolbar)


            navController.navigate(R.id.welcome_fragment)
            setupBottomNavBar(mainActivityNavigation)

        }
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))
    }

    private fun subscribeUi(binding: ActivityMainBinding) {
//        mainViewModel.isAdmin.observe(this, Observer {
//            it?.let { isAdmin ->
//                val listener = if (isAdmin) {
//                    View.OnClickListener { _ ->
//                        showAdminOptions()
//                    }
//                } else {
//                    View.OnClickListener { _ ->
//                        showTicketDialogFragment()
//                    }
//                }
//                binding.mainActivityQrTicketFab.setOnClickListener(listener)
//            }
//        })
    }

    private fun subscribeNonUi() {
//        mainViewModel.login.observe(this, Observer {
//            it?.let { _ ->
//                initActivity()
//            } ?: run {
//                startLoginActivity()
//            }
//
//        })
//
//        mainViewModel.textMessage.observe(this, Observer {
//            it?.let { textMessage ->
//                showSnackBar(textMessage)
//            }
//        })
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.main_activity_fragment_host).navigateUp()
//
//    override fun startLoginActivity() {
//        startActivity(Intent(this, SignInActivity::class.java))
//        finish()
//    }
//
//    private fun startQRScanActivity() =
//            startActivity(Intent(this, QRScanActivity::class.java))
//
//
//    private fun showCreateAnnouncementDialogFragment() {
//        val fragment = CreateAnnouncementDialogFragment.instance
//        fragment.show(supportFragmentManager, null)
//    }

    //    // Handles the click events for bottom navigation menu
    private fun setupBottomNavBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationView.isEnabled = false
            if (itemId != item.itemId) {
                val fragmentId = when (item.itemId) {

                    R.id.welcome_fragment -> R.id.welcome_fragment

                    R.id.announcement_fragment -> R.id.announcement_fragment

                    R.id.events_fragment -> R.id.events_fragment

                    R.id.map_view_fragment -> R.id.map_view_fragment

                    R.id.info_fragment -> R.id.info_fragment

                    else -> 0
                }

                navController.navigate(fragmentId)
                itemId = item.itemId
            }
            bottomNavigationView.isEnabled = true
            true
        }
    }
//
//    private fun showAdminOptions() {
//        AlertDialog.Builder(this)
//                .setTitle(getString(R.string.admin))
//                .setItems(R.array.admin_options) { _, which ->
//                    when (which) {
//
//                        0 -> startQRScanActivity()
//
//                        1 -> showCreateAnnouncementDialogFragment()
//
//                        2 -> showTicketDialogFragment()
//                    }
//                }.show()
}
