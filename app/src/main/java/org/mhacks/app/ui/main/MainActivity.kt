package org.mhacks.app.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mhacks.app.BuildConfig
import org.mhacks.app.R
import org.mhacks.app.core.Activities
import org.mhacks.app.core.intentTo
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.databinding.ActivityMainBinding
import org.mhacks.app.ui.NavigationActivity
import org.mhacks.app.ui.NavigationColor
import javax.inject.Inject

/**
 * Main Activity that handles most of the interactions. Sets up the Auth Activity and loads
 * feature fragments with a bottom navigation bar.
 */

class MainActivity : NavigationActivity()
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
                R.id.main_activity_fragment_host
        )
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        subscribeNonUi()
        checkIfInstantApp()
    }

    private fun checkIfInstantApp() {
        val appLinkIntent = intent
        val appLinkData = appLinkIntent?.data

        if (appLinkData?.path == BuildConfig.INSTANT_APP_URL) {
            initActivity()
            return
        }

        mainViewModel.checkIfLoggedIn()
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
        )
        .apply {
            subscribeUi(this)
            mainActivityNavigation.menu.getItem(0).setTitle(R.string.title_home)
            setSupportActionBar(mainActivityToolbar)
            setupBottomNavBar(mainActivityNavigation)

        }
        // Must be set after binding is set. The navigation fragment accesses abstract values that
        // require binding to be set.
        navController.setGraph(R.navigation.nav_main)
        navController.navigate(R.id.welcome_fragment)
        setBottomNavigationColor(NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))
    }

    private fun subscribeUi(binding: ActivityMainBinding) {
        mainViewModel.isAdmin.observe(this, Observer {
            it?.let { isAdmin ->
                val listener = if (isAdmin) {
                    View.OnClickListener {
                        showAdminOptions()
                    }
                } else {
                    View.OnClickListener {
                        showTicketDialogFragment()
                    }
                }
                binding.mainActivityQrTicketFab.setOnClickListener(listener)
            }
        })
    }

    private fun subscribeNonUi() {
        mainViewModel.auth.observe(this, Observer {
            it?.let { initActivity() } ?: run { startSignInActivity() }
        })
        mainViewModel.text.observe(this, Observer {
            binding.root.showSnackBar(it)
        })
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.main_activity_fragment_host).navigateUp()

    private fun startSignInActivity() {
        val intent = intentTo(Activities.SignIn)
        startActivity(intent)
        finish()
    }

    private fun startQRScanActivity() {
//        startActivity(Intent(this, QRScanActivity::class.java))
//        finish()
    }

    private fun showCreateAnnouncementDialogFragment() {
//        val fragment = CreateAnnouncementDialogFragment.instance
//        fragment.show(supportFragmentManager, null)
    }

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

    private fun showAdminOptions() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.admin))
                .setItems(R.array.admin_options) { _, which ->
                    when (which) {

                        0 -> startQRScanActivity()

                        1 -> showCreateAnnouncementDialogFragment()

                        2 -> showTicketDialogFragment()
                    }
                }.show()
    }
}
