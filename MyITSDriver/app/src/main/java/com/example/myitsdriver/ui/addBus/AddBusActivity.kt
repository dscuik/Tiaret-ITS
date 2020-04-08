package com.example.myitsdriver.ui.addBus

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myitsdriver.R
import com.example.myitsdriver.models.BusInfo
import com.example.myitsdriver.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_bus.*

class AddBusActivity : AppCompatActivity() {

    private lateinit var currentBus: BusInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bus)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val database = FirebaseDatabase.getInstance()
        val currentBuses = database.getReference("current_buses")

        addButton.setOnClickListener {
            if (validate()) {
                if (Utils.isNetworkConnected(this)) {
                    currentBuses.push().setValue(
                        currentBus
                    ) { p0, p1 ->
                        Toast.makeText(
                            this,
                            "Bus was added!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection and retry!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        val busName = busNameEdit.text.toString()
        if (busName.isEmpty()) {
            busNameInput.isErrorEnabled = true
            busNameInput.error = "Please Provide a name."
            valid = false
        } else {
            busNameInput.isErrorEnabled = false
            busNameInput.error = null
        }

        val busPrice = busPriceEdit.text.toString()
        if (busPrice.isEmpty()) {
            busPriceInput.isErrorEnabled = true
            busPriceInput.error = "Please Provide a Price."
            valid = false
        } else {
            busPriceInput.isErrorEnabled = false
            busPriceInput.error = null
        }

        val busFrom = busFromEdit.text.toString()
        if (busFrom.isEmpty()) {
            busFromInput.isErrorEnabled = true
            busFromInput.error = "Please Provide a Depart location."
            valid = false
        } else {
            busFromInput.isErrorEnabled = false
            busFromInput.error = null
        }

        val busTo = busToEdit.text.toString()
        if (busTo.isEmpty()) {
            busToInput.isErrorEnabled = true
            busToInput.error = "Please Provide a Destination."
            valid = false
        } else {
            busToInput.isErrorEnabled = false
            busToInput.error = null
        }

        currentBus = BusInfo(busName, busFrom, busTo, busPrice)
        return valid
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
