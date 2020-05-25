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
import org.bubbble.weather.`interface`.WeatherData
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
    }

    private fun init() {

    }

}