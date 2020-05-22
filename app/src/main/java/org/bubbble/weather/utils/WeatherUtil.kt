package org.bubbble.weather.utils

import android.util.Log
import org.bubbble.weather.bean.DayWeatherBean
import org.bubbble.weather.bean.DayWeatherBeans
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class WeatherUtil(code: String) {

    val dayWeatherData = ArrayList<DayWeatherBean>()
    val hoursWeatherData = ArrayList<DayWeatherBean>()
    val dayWeatherData2 = ArrayList<DayWeatherBeans>()

    var todayWindSpeed = ""
    var todayHumidity = ""
    var todayHighest = ""
    var todayStatus = "none"
    var nowTemps = ""

    init {
        try {
            //获取未来7天的天气情况
            val doc = Jsoup.connect("http://www.weather.com.cn/weather/$code.shtml").userAgent("Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0").timeout(80000).get()
            val elements = doc.select("div.con").select(".today").select(".clearfix").select("div.c7d")
            val day = elements.select("ul.t").select(".clearfix")
            for ((index,li) in day.select("li").withIndex()){


                val dateMonth = SimpleDateFormat("yyyy-MM")
                val dateDay = SimpleDateFormat("yyyy-MM-dd")
                val month = dateMonth.format(Date())+"-"+li.select("h1").text().substring(0,li.select("h1").text().indexOf("日"))

                val time = dateDay.parse(month)
                val weather = li.select("p.wea").text()
                val temp = li.select("p.tem").text().substring(0,li.select("p.tem").text().indexOf("℃"))
                var windSpeeds = ""

                val windSpeed = li.select("p.win").text()

                if (index == 0){

                    todayWindSpeed = if (windSpeed.indexOf("级")+1 > 0){
                        windSpeed.substring(0,windSpeed.indexOf("级")+1)
                    }else{
                        windSpeed
                    }
                    todayStatus = weather
                }


                if (windSpeed.indexOf("级")+1 > 0){
                    todayWindSpeed = windSpeed.substring(0,windSpeed.indexOf("级")+1)
                    windSpeeds = todayWindSpeed
                }else{
                    todayWindSpeed = windSpeed
                    windSpeeds = windSpeed
                }


                dayWeatherData.add(DayWeatherBean(getWeek(time),weather,"$temp°"))
                dayWeatherData2.add(DayWeatherBeans(getWeek(time),weather,"$temp°",windSpeeds))
            }

            val nowHours = Integer.parseInt(SimpleDateFormat("HH").format(Date()))
            val nowDay = SimpleDateFormat("dd").format(Date())
            var nowTime = 10
            var nowTemp = ""
            var highTemp = -100

            //获取未来几小时的天气情况
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

                            hoursWeatherData.add(DayWeatherBean(time,weatherIcon,"$temp°"))

                            if (message[0].substring(0,2) == nowDay){
                                val difference = Math.abs(nowHours - Integer.parseInt(message[0].substring(3,message[0].indexOf("时"))))
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
            todayHighest = highTemp.toString()+"℃"

            //获取湿度
            val humiditys = doc.select("div.con").select(".today").select(".clearfix").select("script")
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

            todayHumidity = humidityData.toString()+"%"

        }catch(e: Exception) {
            Log.e("mytag", e.message)
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
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val now = SimpleDateFormat("yyyy-MM-dd hh:mm").format(Date())

        val from = simpleFormat.parse(startTime).time
        val to = simpleFormat.parse(now).time
        val hours = Math.abs(((to - from) / (1000 * 60 * 60)).toInt())

        return hours >= 2
    }

}