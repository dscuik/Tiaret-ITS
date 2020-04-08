package com.example.myitsdriver.ui.main

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myitsdriver.R
import com.example.myitsdriver.models.BusInfo
import com.example.myitsdriver.ui.addBus.AddBusActivity
import com.example.myitsdriver.ui.driving.DrivingActivity
import com.example.myitsdriver.utils.Utils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val busesList = mutableListOf<BusInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        databaseInit()

        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        startButton.setOnClickListener {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Please Activate Location", Toast.LENGTH_LONG).show()
                turnOnGpsDialog()
                return@setOnClickListener
            }

            if (!Utils.isNetworkConnected(this)) {
                Toast.makeText(this, "Please Check out your internet connection.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (busesList.isEmpty()){
                databaseInit()
            }

            val busesName = busesList.map {
                it.name
            }

            AlertDialog.Builder(this).setTitle("Please choose the Bus")
                .setItems(
                    busesName.toTypedArray()
                ) { _, which ->
                    val bus = busesList[which]
                    val intent = Intent(this, DrivingActivity::class.java)
                    intent.putExtra("Bus", bus.name)
                    startActivity(intent)
                }
                .show()
        }
    }

    private fun turnOnGpsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You have to enable GPS ?")
        builder.setCancelable(false).setPositiveButton("Yes") { _, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
        }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_add_buss) {
            startActivity(Intent(this, AddBusActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun databaseInit() {
        val database = FirebaseDatabase.getInstance()
        val buses = database.getReference("current_buses")
        startButton.hide()
        buses.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val element = p0.getValue(BusInfo::class.java)
                busesList.add(element!!)
                startButton.show()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
