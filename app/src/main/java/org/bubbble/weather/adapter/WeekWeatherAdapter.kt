package org.bubbble.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bubbble.weather.R
import org.bubbble.weather.bean.WeekWeatherBean

/**
 * @author  Andrew
 * @date  2020/5/14 21:06
 */
class WeekWeatherAdapter(private val context: Context,
                         private val dataList: ArrayList<WeekWeatherBean>) : RecyclerView.Adapter<WeekWeatherAdapter.WeekWeatherHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWeatherHolder {
        return WeekWeatherHolder(LayoutInflater.from(context).inflate(R.layout.item_daily_weather, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: WeekWeatherHolder, position: Int) {
        val bean = dataList[position]
        holder.week.text = bean.week
        Glide.with(context).load(bean.iconWeather).into(holder.iconWeather)
        holder.maxTemperature.text = bean.maxTemperature
        holder.minTemperature.text = bean.mixTemperature
    }

    class WeekWeatherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var week: TextView = itemView.findViewById(R.id.week)
        var iconWeather: ImageView = itemView.findViewById(R.id.iconWeather)
        var maxTemperature: TextView = itemView.findViewById(R.id.maxTemperature)
        var minTemperature: TextView = itemView.findViewById(R.id.minTemperature)
        var line: View = itemView.findViewById(R.id.line)
    }
}