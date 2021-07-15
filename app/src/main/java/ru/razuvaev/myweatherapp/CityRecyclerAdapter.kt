package ru.razuvaev.myweatherapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class CityRecyclerAdapter(private val names: List<String>) :
    RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder>() {



    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameCity: TextView? = null
        var coordinates: TextView? = null

        init {
            nameCity = itemView.findViewById(R.id.name_city_and_country)
            coordinates = itemView.findViewById(R.id.coordinates_city)

            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                Toast.makeText(itemView.context , "$bindingAdapterPosition open weather " + (nameCity?.text
                    ?: String), Toast.LENGTH_SHORT).show()
                // TODO сохранить город в SharedPreferences. Взвать фрагмент CityFragment
                


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_list_item, parent, false)
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.nameCity?.text = names[position]
        holder.coordinates?.text = "lon: 143.1667, lat: 51.8333"
    }

    override fun getItemCount() = names.size
}