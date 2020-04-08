package com.example.myitsuser.ui.main

import com.example.myitsuser.models.BusInfo

interface BusClickListener {

    fun onBusClick(busInfo: BusInfo)

    fun onFinishLoading()
}