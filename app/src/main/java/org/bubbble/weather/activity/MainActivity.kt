package org.bubbble.weather.activity

import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.bubbble.weather.R
import org.bubbble.weather.adapter.WeekWeatherAdapter
import org.bubbble.weather.bean.WeekWeatherBean
import org.bubbble.weather.utils.DisplayUtil
import org.bubbble.weather.utils.TypefaceUtil

/**
 * @author Andrew
 * @date 2020/05/12 20:40
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TypefaceUtil.replaceFont(this, "fonts/ProductSans.ttf")

        // 获取屏幕宽高
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        if (displayMetrics.widthPixels > DisplayUtil.dip2px(this, 340F)) {
            // 设置Dialog最大宽度
            val windowParams = window.attributes
            window.attributes = windowParams
            windowParams.width = DisplayUtil.dip2px(this, 340F)
        }


        init()
        initWeekWeatherList()
    }

    private fun init() {
        titleCity.text = "San Francisco, CA"
        titleWeather.text = "Party Cloudy"
        Glide.with(this).load(R.drawable.weather_few_clouds).into(iconWeather)
        temperature.typeface = Typeface.createFromAsset(assets, "fonts/RobotoLight.ttf")
        temperature.text = "68°"
        windSpeed.text = "SE 7 mph\nHumidity 70%"
    }

    private fun initWeekWeatherList() {
        val weekWeatherDataList = ArrayList<WeekWeatherBean>()
            weekWeatherDataList.add(WeekWeatherBean("FRI", R.drawable.weather_haze, "32", "27"))
        weekWeatherDataList.add(WeekWeatherBean("SAT", R.drawable.weather_mist, "27", "14"))
        weekWeatherDataList.add(WeekWeatherBean("SUM", R.drawable.weather_clouds, "26", "10"))
        weekWeatherDataList.add(WeekWeatherBean("MON", R.drawable.weather_few_clouds, "28", "11"))
        weekWeatherDataList.add(WeekWeatherBean("AND", R.drawable.weather_mist, "27", "12"))
        val adapter = WeekWeatherAdapter(this, weekWeatherDataList)
        weekWeather.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        weekWeather.adapter = adapter
    }
}