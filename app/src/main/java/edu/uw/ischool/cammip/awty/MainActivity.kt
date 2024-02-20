package edu.uw.ischool.cammip.awty

import android.app.Activity
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
import android.os.SystemClock
import android.util.Log
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var message : EditText
    private lateinit var num: EditText
    private lateinit var nag: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        message = findViewById(R.id.edit1)
        num = findViewById(R.id.edit2)
        nag = findViewById(R.id.edit3)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        button.setOnClickListener() {
            //end alarm
            if(button.text.toString() == "Stop") {
                button.text = "Start"

                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show()
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

                sendBroadcast(intent)

                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    nag.text.toString().toLong() * 60000,
                    pendingIntent
                )
            }
        }
    }

    class MyAlarm : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val num = context.applicationContext.findViewById<EditText>(R.id.edit2)
            //val msg = (context as Activity).findViewById<EditText>(R.id.edit1)
            //val message = "${num.text.toString()}: ${msg.text.toString()}"

            val message = num.toString()
            Log.d("msg", message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }
    }
}