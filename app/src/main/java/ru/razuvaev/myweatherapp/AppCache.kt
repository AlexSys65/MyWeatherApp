package ru.razuvaev.myweatherapp

import android.app.Activity
import android.content.SharedPreferences

class AppCache(activity: Activity) {
    private val CITY_KEY = "city"
    private val DEFAULT_TOWN_ID = "1496747"
    private var userPreferences: SharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE)

    fun getSavedCity(): String? { return userPreferences.getString(CITY_KEY, DEFAULT_TOWN_ID)}

    fun saveCity(cityID: String) { userPreferences.edit().putString(CITY_KEY, cityID).apply() }
}