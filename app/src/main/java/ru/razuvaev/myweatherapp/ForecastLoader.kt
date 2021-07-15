package ru.razuvaev.myweatherapp

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ForecastLoader {
    private const val OPEN_WEATHER_MAP_API =
        "http://api.openweathermap.org/data/2.5/weather?id=%s&&appid=%s&units=metric"
    private const val API_KEY = "4d44baf3d9ac1c58f8b1b43df16f0069"
    private const val RESPONSE = "cod"
    private const val NEW_LINE = "\n"
    private const val RESPONSE_SUCCESS = 200

    fun getJsonData(cityID: String?): JSONObject? {
        return try {
            val url = URL(
                String.format(
                    OPEN_WEATHER_MAP_API,
                    cityID,
                    API_KEY
                )
            )
            val connection = url.openConnection() as HttpURLConnection
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val rawData = StringBuilder(1024)
            var tempVariable: String?
            while (reader.readLine().also { tempVariable = it } != null) {
                rawData.append(tempVariable).append(NEW_LINE)
            }
            reader.close()

            val jsonObject = JSONObject(rawData.toString())
            if (jsonObject.getInt(RESPONSE) == RESPONSE_SUCCESS) {
                jsonObject
            } else {
                null
            }
        } catch (e: Exception) {
            null // Обработка ошибки
        }
    }
}