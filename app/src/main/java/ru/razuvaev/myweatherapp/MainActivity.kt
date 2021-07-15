package ru.razuvaev.myweatherapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), FragmentSendDataListener {

    private val myFragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            myFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, ListOfCities.newInstance("Moscow", "night"))
                .commitNow()
        }

    }

    override fun onSendData(data: City, action: String) {
        when (action) {
            "add" -> { } //TODO добавить город в список
            "delete" -> { } //TODO удалить город из списка
            "show" -> {
                myFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, CityFragment.newInstance(data.id, "night"))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}