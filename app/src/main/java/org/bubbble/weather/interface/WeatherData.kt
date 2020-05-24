package org.bubbble.weather.`interface`

import org.bubbble.weather.bean.DayWeatherBean
import org.bubbble.weather.bean.FutureWeatherBean

/**
 * @author  Andrew
 * @date  2020/5/23 13:36
 * 天气数据接口
 */
interface WeatherData {

    /**
     * 当天风速
     */
    fun windSpeed(windSpeed: String)

    /**
     * 当天湿度
     */
    fun humidity(humidity: String)

    /**
     * 当下天气类型
     */
    fun weatherIcon(weatherIcon: String)

    /**
     * 当下温度
     */
    fun temperature(temperature: String)

    /**
     * 当天最高温度
     */
    fun maxTemperature(maxTemperature: String)

    /**
     * 当天小时天气
     */
    fun hourWeather(hourWeather: ArrayList<DayWeatherBean>)

    /**
     * 未来6天天气
     */
    fun futureWeather(futureWeather: ArrayList<FutureWeatherBean>)

}