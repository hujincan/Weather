package org.bubbble.weather.bean

/**
 * @author  Andrew
 * @date  2020/5/23 21:41
 * 未来每天天气数据结构
 */
data class FutureWeatherBean(
    val week: String,
    val weatherType: String,
    val maxTemperature: String,
    val minTemperature: String,
    val windDescribe: String,
    val windSpeed: String
)