package ru.razuvaev.myweatherapp

interface FragmentSendDataListener {
    fun onSendData(data: City, action: String)
}