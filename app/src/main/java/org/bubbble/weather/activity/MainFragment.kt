package org.bubbble.weather.activity

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_main.*

import org.bubbble.weather.R
import org.bubbble.weather.`interface`.WeatherData
import org.bubbble.weather.adapter.WeekWeatherAdapter
import org.bubbble.weather.bean.DayWeatherBean
import org.bubbble.weather.bean.FutureWeatherBean
import org.bubbble.weather.bean.WeekWeatherBean

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), WeatherData {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initWeekWeatherList()
    }

    private fun init() {
        titleCity.text = "San Francisco, CA"
        titleWeather.text = "Party Cloudy"
        Glide.with(this).load(R.drawable.weather_few_clouds).into(iconWeather)
        temperature.typeface = Typeface.createFromAsset(context!!.assets, "fonts/RobotoLight.ttf")
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
        val adapter = WeekWeatherAdapter(context!!, weekWeatherDataList)
        weekWeather.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        weekWeather.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * 当天风速
     */
    override fun windSpeed(windSpeed: String) {

    }

    /**
     * 当天湿度
     */
    override fun humidity(humidity: String) {

    }

    /**
     * 当下天气类型
     */
    override fun weatherIcon(weatherIcon: String) {

    }

    /**
     * 当下温度
     */
    override fun temperature(temperature: String) {

    }

    /**
     * 当天最高温度
     */
    override fun maxTemperature(maxTemperature: String) {

    }

    /**
     * 当天小时天气
     */
    override fun hourWeather(hourWeather: ArrayList<DayWeatherBean>) {

    }

    /**
     * 未来6天天气
     */
    override fun futureWeather(futureWeather: ArrayList<FutureWeatherBean>) {

    }
}
