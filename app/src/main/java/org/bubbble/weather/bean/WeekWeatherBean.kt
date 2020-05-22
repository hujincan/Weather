package org.bubbble.weather.bean

/**
 * @author  Andrew
 * @date  2020/5/14 21:20
 */
data class WeekWeatherBean (val week: String,
                            val iconWeather: Int,
                            val maxTemperature: String,
                            val mixTemperature: String)