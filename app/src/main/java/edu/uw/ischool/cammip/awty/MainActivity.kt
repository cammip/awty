package edu.uw.ischool.cammip.awty

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.text.TextUtils
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var message : EditText
    private lateinit var num: EditText
    private lateinit var nag: EditText

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        var alarmReceiver: BroadcastReceiver? = null

        Log.d("Test", "started")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        message = findViewById(R.id.edit1)
        num = findViewById(R.id.edit2)
        nag = findViewById(R.id.edit3)
        val ALARM_ACTION = "edu.uw.ischool.cammip.ALARM"

        button.setOnClickListener {
            //end alarm
            if(button.text.toString() == "Stop") {
                unregisterReceiver(alarmReceiver)
                alarmReceiver = null
                Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show()
                button.text = "Start"
            }

            //if the data is invalid, button does nothing
            else if(button.text.toString() == "Start" &&
                TextUtils.isEmpty(message.text.toString())
                || TextUtils.isEmpty(num.text.toString())
                || TextUtils.isEmpty(nag.text.toString())) {
                val toastError = Toast.makeText(this@MainActivity, "Incorrect values inputted",
                    Toast.LENGTH_LONG)
                toastError.show()

            //if data is valid, start alarm
            } else if (button.text.toString() != "Stop"){
                button.text = "Stop"
                val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(ALARM_ACTION)
                val pendingIntent = PendingIntent.getBroadcast(applicationContext,
                    0, intent, PendingIntent.FLAG_IMMUTABLE)

                if(alarmReceiver == null) {
                    alarmReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            Log.d("Test", "onReceive called")
                            val msg = "${num.text.toString()}: ${message.text.toString()}"
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            Log.d("Test", "toast was toasted")

                        }
                    }
                    val filter = IntentFilter(ALARM_ACTION)
                    registerReceiver(alarmReceiver, filter)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + num.text.toString().toInt(),
                    num.text.toString().toLong() * 60000,
                    pendingIntent)
            }
        }
    }
}