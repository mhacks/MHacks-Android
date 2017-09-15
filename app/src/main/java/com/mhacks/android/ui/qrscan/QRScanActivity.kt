package com.mhacks.android.ui.qrscan

import android.os.Bundle
import com.mhacks.android.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_qr_scan.*
import org.mhacks.android.R

class QRScanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)

        setContentView(R.layout.activity_qr_scan)
        setSupportActionBar(qr_scan_toolbar)
        setTitle("Scan QR")
    }

}
