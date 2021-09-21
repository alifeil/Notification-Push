package com.alifeil.notificationpushfirebase


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        btn_send.setOnClickListener {

            sendNotification()
        }
        btn_url.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registrastion token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "Token" + msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d(TAG,"Successfully send to all")
                } else{
                    Log.d(TAG,"Notification failed")
                }
            }


    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = descriptionText

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val judul = judultxt.text.toString()
        val deskripsi = deskripsitxt.text.toString()

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        val bitmap = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.japan)
        val bitmapLargeIcon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.sakura)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(judul)
            .setContentText("Spring in Japan")
            .setLargeIcon(bitmapLargeIcon)
//            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))


            .setStyle(NotificationCompat.BigTextStyle().bigText(deskripsi))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}