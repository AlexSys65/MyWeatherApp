package ru.razuvaev.myweatherapp

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.json.JSONObject
import ru.razuvaev.myweatherapp.databinding.FragmentCityBinding
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class CityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

   // private val FONT_FILENAME = "weather.ttf"
    private val URL_ICON = "http://openweathermap.org/img/w/%s.png"

    private val handler = Handler(Looper.getMainLooper())

    private var _binding: FragmentCityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val appCache = this.activity?.let { AppCache(it) }
        //       val mWeatherFont = Typeface.createFromAsset(activity?.assets, FONT_FILENAME)
//        _binding?.weatherIcon?.setTypeface(mWeatherFont)

        if (appCache != null) {
            updateWeatherData(appCache.getSavedCity())
        }
    }

    private fun updateWeatherData(city: String?) {
        object : Thread() {
            //Отдельный поток для запроса на сервер
            override fun run() {
                val json: JSONObject? = ForecastLoader.getJsonData(city)
                // вызов методов напрямую может вызвать runtime error
                if (json == null) {
                    handler.post(Runnable {
                        Toast.makeText(
                            requireContext(), getString(R.string.place_not_found),
                            Toast.LENGTH_LONG
                        ).show()
                    })
                } else {
                    handler.post({ renderWeather(json) })
                }
            }
        }.start()
    }

    private fun renderWeather(json: JSONObject) {
        Log.d("Log", "json $json")
        try {
            _binding?.cityField?.text =
                json.getString("name").uppercase(Locale.getDefault()) + ", " +
                        json.getJSONObject("sys").getString("country")
            val details = json.getJSONArray("weather").getJSONObject(0)
            val main = json.getJSONObject("main")
            _binding?.detailField?.text =
                (details.getString("description").uppercase(Locale.getDefault()) + "\n" +
                        "Humidity: " + main.getString("humidity") + "%" + "\n" +
                        "Pressure: " + main.getString("pressure") + " hPa")
            _binding?.currentTemperatureField?.text = String.format(
                "%.2f",
                main.getDouble("temp")
            ) + " °C"
            val df = DateFormat.getDateTimeInstance()
            val updatedOn = df.format(Date(json.getLong("dt") * 1000))
            _binding?.updateField?.text = "Last update: $updatedOn"
            //val mURL_ICON = String.format(URL_ICON,details.getString("icon"))
            setWeatherIcon(String.format(URL_ICON, details.getString("icon")))
        } catch (e: Exception) {
            Log.d("Log", "One or more fields not found in the JSON data") // Обработка ошибки
        }
    }

    // Подстановка нужной иконки
    private fun setWeatherIcon(urlIcon: String) {
        try {
            Glide
                .with(this)
                .load(urlIcon)
                .into(_binding!!.weatherIcon)
        } catch (e: Exception) {
            Log.d("Log", "Icon image not found") // Обработка ошибки
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}