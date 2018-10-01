package com.mhacks.app.ui.info

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mhacks.app.data.Constants
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.common.SpacingItemDecoration
import com.mhacks.app.ui.info.model.Info
import com.mhacks.app.ui.info.widget.InfoCardRecyclerViewAdapter
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentInfoBinding
import timber.log.Timber
import javax.inject.Inject
import android.content.Intent.ACTION_VIEW
import android.net.Uri

class InfoFragment: NavigationFragment() {

    @Inject lateinit var wiFiInstaller: WifiInstaller

    override var setTransparent = false

    override var appBarTitle: Int = R.string.title_info

    override var rootView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoBinding.inflate(
                inflater, container, false).apply {
            context?.let {
                infoFragmentInfoRecyclerView.layoutManager = LinearLayoutManager(
                        it,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
                infoFragmentInfoRecyclerView.addItemDecoration(
                        SpacingItemDecoration(it, 6)
                )

                val adapter = InfoCardRecyclerViewAdapter()
                adapter.infoCallback = ::onItemSelected
                infoFragmentInfoRecyclerView.adapter = adapter
            }
        }
        return binding.root
    }

    private fun onItemSelected(info: Info) {
        Timber.d("%s Clicked!", info.type.toString())

        context?.let {
            when (info.type) {
                Info.TYPE.WIFI -> {
                    val isWifiInstall = wiFiInstaller.installConferenceWifi(
                        Constants.WIFI_CONFIGUATION
                    )
                    val wifiMessage = if (isWifiInstall) {
                        R.string.wifi_install_success
                    } else {
                        R.string.wifi_install_failure
                    }
                    Toast.makeText(it, wifiMessage, Toast.LENGTH_SHORT).show()
                    Timber.d("Installed network: %s", isWifiInstall.toString())
                }
                Info.TYPE.ADDRESS -> {
                    // Replace the spaces with addition for URI parsing.
                    val formattedAddress =
                            it.getString(R.string.info_address)
                                    .replace(" ", "+" )

                    val googleMapsUrl = Constants.GOOGLE_MAPS_URL + formattedAddress

                    Timber.d("Opening $formattedAddress in Google Maps")

                    val i = Intent(ACTION_VIEW, Uri.parse(googleMapsUrl))
                    i.component = ComponentName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity")
                    try {
                        startActivity(i)
                    } catch (e: ActivityNotFoundException){
                        Toast.makeText(
                                it,
                                getString(R.string.google_maps_error),
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                Info.TYPE.SLACK -> {
                    val i = Intent(ACTION_VIEW, Uri.parse(Constants.SLACK_INVITE_URL))
                    i.action = ACTION_VIEW
                    startActivity(i)
                }
                Info.TYPE.EMAIL -> {
                    val i = Intent(
                            Intent.ACTION_SENDTO,
                            Uri.fromParts(
                                    "mailto",
                                    Constants.MHACKS_EMAIL,
                                    null))
                    i.putExtra(Intent.EXTRA_TEXT, "Sent from Android app.")
                    startActivity(Intent.createChooser(i, "Send Email"))
                }
            }
        }
    }
}