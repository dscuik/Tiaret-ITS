package com.example.myitsuser.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myitsuser.R
import com.example.myitsuser.models.BusInfo
import com.example.myitsuser.ui.findbus.MapsActivity
import com.example.myitsuser.utils.Utils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), BusClickListener {

    private val listOfBuses = mutableListOf<BusInfo>()
    val adapter = BusesAdapter(this)
    private lateinit var manager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        databaseInit()
        uiInit()
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
        }
    }

    private fun uiInit() {
        busesRecycler.adapter = adapter
        busesRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        uiInternetCheck()

        emptyLayout.setOnClickListener {
            uiInternetCheck()
        }
    }

    private fun databaseInit() {
        val database = FirebaseDatabase.getInstance()
        val buses = database.getReference("current_buses")

        buses.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val element = p0.getValue(BusInfo::class.java)
                listOfBuses.add(element!!)
                adapter.setData(listOfBuses)
                mainProgress.visibility = View.GONE
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private fun uiInternetCheck() {

        if (Utils.isNetworkConnected(this)) {
            emptyLayout.visibility = View.GONE
            busesRecycler.visibility = View.VISIBLE
            mainProgress.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.VISIBLE
            busesRecycler.visibility = View.GONE
            mainProgress.visibility = View.GONE
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

    override fun onBusClick(busInfo: BusInfo) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please Activate Location.", Toast.LENGTH_LONG).show()
            turnOnGpsDialog()
            return
        }

        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, "Please Check out your internet connection.", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("Bus", busInfo.name)
        startActivity(intent)
    }

    override fun onFinishLoading() {
        mainProgress.visibility = View.GONE
    }
}
