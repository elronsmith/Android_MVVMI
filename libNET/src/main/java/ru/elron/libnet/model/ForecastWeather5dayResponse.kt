package ru.elron.libnet.model

class ForecastWeather5dayResponse {
    var list: List<WeatherItem>? = null
    var city: City? = null

    fun hasCityName(): Boolean {
        if (city == null) return false
        if (city!!.name == null) return false
        return city!!.name!!.isNotEmpty()
    }

    class WeatherItem {
        /** timestamp */
        var dt: Long = 0
        var main: Main? = null
        var weather: List<Weather2>? = null
        var wind : Wind? = null
        var visibility: Int = 0
        var dt_txt: String? = null
    }

    class Main {
        var temp: Double = .0
        var pressure: Int = 0
        var humidity: Int = 0
    }

    class Weather2 {
        var id: Int = 0
        var main: String? = null
        var description: String? = null
        var icon: String? = null
    }

    class Wind {
        var speed: Double = .0
        var deg: Int = 0
    }

    class City {
        var id: Long = 0
        var name: String? = null
        var country: String? = null
    }
}
