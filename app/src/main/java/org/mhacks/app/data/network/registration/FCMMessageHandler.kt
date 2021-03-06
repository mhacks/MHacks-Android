package org.mhacks.app.data.network.registration

import android.app.Notification
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.mhacks.app.R
import timber.log.Timber

/**
 * Service for handling push notifications.
 */

class FCMMessageHandler : FirebaseMessagingService() {

    private val mhacksGroup = "MHacks Group"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        createNotification(notification!!)
    }

    private fun createNotification(notification: RemoteMessage.Notification) {
        val builder = NotificationCompat.Builder(baseContext, mhacksGroup)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

        val notificationManager = NotificationManagerCompat.from(baseContext)
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build())
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.d("New Token is $newToken")

        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }

    companion object {

        private const val MESSAGE_NOTIFICATION_ID = 435345

    }
}