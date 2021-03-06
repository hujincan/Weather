package org.bubbble.weather.utils

import android.util.Log
import org.bubbble.weather.bean.DayWeatherBean
import org.bubbble.weather.bean.DayWeatherBeans
import org.bubbble.weather.bean.FutureWeatherBean
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class WeatherUtil(code: String) {

    /**
     * 每天天气集合
     */
    val dayWeatherData = ArrayList<FutureWeatherBean>()

    /**
     * 当天小时天气数据
     */
    val hoursWeatherData = ArrayList<DayWeatherBean>()

    /**
     * 当天风速
     */
    var todayWindSpeed = ""

    /**
     * 当天湿度
     */
    var todayHumidity = ""

    /**
     * 最高温度
     */
    var todayHighest = ""

    /**
     * 天气类型
     */
    var todayStatus = "none"

    /**
     * 当下气温
     */
    var nowTemps = ""

    companion object {
        fun with(code: String): WeatherUtil {
            return WeatherUtil(code)
        }
    }

    // 抓取当天详细数据http://www.weather.com.cn/weather1d/101200801.shtml
    // 抓取未来详细数据http://www.weather.com.cn/weather15d/101200801.shtml

    init {
        doAsyncTask {
            try {
                val dayHtml = Jsoup.connect("http://www.weather.com.cn/weather1d/$code.shtml").userAgent("Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0").timeout(80000).get()
                val weekHtml = Jsoup.connect("http://www.weather.com.cn/weather15d/$code.shtml").userAgent("Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0").timeout(80000).get()

                val elements = weekHtml.select("div.con").select(".today").select(".clearfix").select("div.c15d")
                val day = elements.select("ul.t").select(".clearfix")

                // 抓取未来7-15天的天气情况
                for ((index,li) in day.select("li").withIndex()){

//                    val dateMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault())
//                    val dateDay = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    val month = dateMonth.format(Date())+"-"+li.select("h1").text().substring(0,li.select("h1").text().indexOf("日"))

                    // 抓取：周一（1日）
                    val time = li.select("span.time").text()

                    // 天气：小雨转阴天
                    val weather = li.select("span.wea").text()

                    // 温度格式：00℃/00℃
                    val temp = li.select("span.tem").text()
                    val maxTemperature = temp.substring(0, temp.indexOf("℃"))
                    val minTemperature = temp.substring(temp.indexOf("/") + 1, temp.indexOf("℃"))

                    val windDescribe = li.select("span.wind").text()
                    val windSpeed = li.select("span.wind1").text()

                    dayWeatherData.add(FutureWeatherBean(time, weather,"$maxTemperature°", "$minTemperature°", windDescribe, windSpeed))
                }

                val nowHours = Integer.parseInt(SimpleDateFormat("HH", Locale.getDefault()).format(Date()))
                val nowDay = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
                var nowTime = 10
                var nowTemp = ""
                var highTemp = -100

                //获取未来每三小时的天气情况
                for (hours in  elements.select("script")){

                    /*取得JS变量数组*/
                    val data = hours.data().toString().split("var")
                    for (d in data){
                        if (d.contains("hour3data")){
                            val s = d.substring(d.indexOf("[")+2,d.indexOf("\"]"))
                            for (weather in s.split("\",\"")){

                                val message = weather.split("[,，]".toRegex())

                                val time = message[0]
                                val weatherIcon = message[2]
                                val temp = message[3].substring(0,message[3].indexOf("℃"))

                                Log.e("time",time)
                                Log.e("message[2]",message[2])
                                Log.e("temp",temp)

                                hoursWeatherData.add(DayWeatherBean(time, weatherIcon,"$temp°"))

                                if (message[0].substring(0,2) == nowDay){
                                    val difference = abs(nowHours - Integer.parseInt(message[0].substring(3,message[0].indexOf("时"))))
                                    if (difference < nowTime){
                                        nowTime = difference
                                        nowTemp = temp
                                    }
                                    if (Integer.parseInt(temp) > highTemp){
                                        highTemp = Integer.parseInt(temp)
                                    }
                                }
                            }
                        }
                    }
                }

                nowTemps = "$nowTemp℃"
                todayHighest = "$highTemp℃"

                //获取湿度
                val humiditys = dayHtml.select("div.con").select(".today").select(".clearfix").select("script")
                var humidityData = 0
                for (ds in humiditys){
                    val data = ds.data().toString().split("var")
                    for (d in data){
                        if (d.contains("observe24h_data")){
                            val s = d.substring(d.indexOf("[{\"")+1,d.indexOf("]}};"))
                            for ((index,value) in s.split("od27\":\"").withIndex()){
                                if (index != 0){
                                    val humidity = value.substring(0,value.indexOf("\""))
                                    if (humidity != "null" && humidity.isNotEmpty()){

                                        if (Integer.parseInt(humidity.substring(0,2)) > humidityData){
                                            humidityData = Integer.parseInt(humidity.substring(0,2))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                todayHumidity = "$humidityData%"

            }catch(e: Exception) {
                Log.e("mytag", e.message)
            }
        }
    }

    fun getWeatherEnum(weather: String,isNight: Boolean): WeatherIcon{
        if (isNight){

            return when {
                weather.contains("晴转多云") -> WeatherIcon.FEW_CLOUDS_NIGHT
                weather.contains("多云转晴") -> WeatherIcon.FEW_CLOUDS_NIGHT
                weather.contains("雨夹雪") -> WeatherIcon.SNOW_RAIN
                weather.contains("雷阵雨") -> WeatherIcon.STORM_NIGHT
                weather.contains("小雨") -> WeatherIcon.DRIZZLE_NIGHT
                weather.contains("中雨") -> WeatherIcon.RAIN_NIGHT
                weather.contains("大雨") -> WeatherIcon.SHOWERS_NIGHT
                weather.contains("暴雨") -> WeatherIcon.SHOWERS_NIGHT
                weather.contains("小雪") -> WeatherIcon.SNOW_SCATTERED_NIGHT
                weather.contains("中雪") -> WeatherIcon.SNOW_SCATTERED_NIGHT
                weather.contains("大雪") -> WeatherIcon.SNOW
                weather.contains("冰雹") -> WeatherIcon.HAIL
                weather.contains("多云") -> WeatherIcon.HAZE
                weather.contains("冻雨") -> WeatherIcon.HAIL
                weather.contains("雨") -> WeatherIcon.SHOWERS_NIGHT
                weather.contains("风") -> WeatherIcon.WIND
                weather.contains("霾") -> WeatherIcon.HAZE
                weather.contains("阴") -> WeatherIcon.FEW_CLOUDS_NIGHT
                weather.contains("晴") -> WeatherIcon.CLEAR_NIGHT
                weather.contains("雾") -> WeatherIcon.FOG

                weather.contains("雷") -> WeatherIcon.STORM
                weather.contains("阵雨") -> WeatherIcon.RAIN_NIGHT
                else -> WeatherIcon.NONE_AVAILABLE
            }

        }else{

            return when {
                weather.contains("晴转多云") -> WeatherIcon.FEW_CLOUDS
                weather.contains("多云转晴") -> WeatherIcon.FEW_CLOUDS
                weather.contains("雨夹雪") -> WeatherIcon.SNOW_RAIN
                weather.contains("雷阵雨") -> WeatherIcon.STORM_DAY
                weather.contains("小雨") -> WeatherIcon.DRIZZLE_DAY
                weather.contains("中雨") -> WeatherIcon.RAIN_DAY
                weather.contains("大雨") -> WeatherIcon.SHOWERS_DAY
                weather.contains("暴雨") -> WeatherIcon.SHOWERS_DAY
                weather.contains("小雪") -> WeatherIcon.SNOW_SCATTERED_DAY
                weather.contains("中雪") -> WeatherIcon.SNOW_SCATTERED_DAY
                weather.contains("大雪") -> WeatherIcon.SNOW
                weather.contains("冰雹") -> WeatherIcon.HAIL
                weather.contains("多云") -> WeatherIcon.CLOUD
                weather.contains("冻雨") -> WeatherIcon.HAIL
                weather.contains("雨") -> WeatherIcon.SHOWERS_DAY
                weather.contains("阴") -> WeatherIcon.FEW_CLOUDS_NIGHT
                weather.contains("晴") -> WeatherIcon.CLEAR
                weather.contains("雾") -> WeatherIcon.FOG
                weather.contains("风") -> WeatherIcon.WIND
                weather.contains("霾") -> WeatherIcon.HAZE
                weather.contains("雷") -> WeatherIcon.STORM_DAY
                weather.contains("阵雨") -> WeatherIcon.RAIN_DAY
                else -> WeatherIcon.NONE_AVAILABLE
            }
        }
    }

    private fun getWeek(date: Date): String {
        val weeks = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        cal.time = date
        var weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1
        if (weekIndex < 0) {
            weekIndex = 0
        }
        return weeks[weekIndex]
    }

    fun getTimeDifference(startTime: String) : Boolean{
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
        val now = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(Date())

        val from = simpleFormat.parse(startTime).time
        val to = simpleFormat.parse(now).time
        val hours = Math.abs(((to - from) / (1000 * 60 * 60)).toInt())

        return hours >= 2
    }
}