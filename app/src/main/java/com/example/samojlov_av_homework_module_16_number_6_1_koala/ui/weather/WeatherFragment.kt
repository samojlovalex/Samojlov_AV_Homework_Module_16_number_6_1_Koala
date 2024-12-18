package com.example.samojlov_av_homework_module_16_number_6_1_koala.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.FragmentWeatherBinding
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.weather.CurrentWeather
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.weather.RetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    private lateinit var locationIconButtonIV: ImageView
    private lateinit var containerWeatherImageLL: LinearLayout
    private lateinit var weatherIV: ImageView
    private lateinit var cityTV: TextView
    private lateinit var containerTemperatureLL: LinearLayout
    private lateinit var temperatureGeneralTV: TextView
    private lateinit var temperatureFeelsTV: TextView
    private lateinit var temperatureMinTV: TextView
    private lateinit var temperatureMaxTV: TextView
    private lateinit var containerWindLL: LinearLayout
    private lateinit var windSpeedTV: TextView
    private lateinit var windGustTV: TextView
    private lateinit var containerVisibilityLL: LinearLayout
    private lateinit var visibilityTV: TextView
    private lateinit var containerCloudsLL: LinearLayout
    private lateinit var cloudsTV: TextView
    private lateinit var containerPressureLL: LinearLayout
    private lateinit var pressureTV: TextView
    private lateinit var containerHumidityLL: LinearLayout
    private lateinit var humidityTV: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[WeatherViewModel::class.java]

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        locationRequest()
    }

    private fun locationRequest() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }

                else -> {
                    // No location access granted.
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun init() {
        locationIconButtonIV = binding.locationIconButtonIV
        containerWeatherImageLL = binding.containerWeatherImageLL
        weatherIV = binding.weatherIV
        cityTV = binding.cityTV
        containerTemperatureLL = binding.containerTemperatureLL
        temperatureGeneralTV = binding.temperatureLayout.temperatureGeneralTV
        temperatureFeelsTV = binding.temperatureLayout.temperatureFeelsTV
        temperatureMinTV = binding.temperatureLayout.temperatureMinTV
        temperatureMaxTV = binding.temperatureLayout.temperatureMaxTV
        containerWindLL = binding.containerWindLL
        windSpeedTV = binding.windLayout.windSpeedTV
        windGustTV = binding.windLayout.windGustTV
        containerVisibilityLL = binding.containerVisibilityLL
        visibilityTV = binding.visibilityLayout.visibilityTV
        containerCloudsLL = binding.containerCloudsLL
        cloudsTV = binding.cloudsLayout.cloudsTV
        containerPressureLL = binding.containerPressureLL
        pressureTV = binding.pressureLayout.pressureTV
        containerHumidityLL = binding.containerHumidityLL
        humidityTV = binding.humidityLayout.humidityTV

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setLocation()

        locationIconButtonIV.setOnClickListener {
            setLocation()
        }
    }

    private fun setLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                getCurrentWeather(location?.latitude.toString(), location?.longitude.toString())
            }
    }

    private fun getCurrentWeather(lat: String, lon: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response =
                try {
                    RetrofitInstance.api.getCurrentWeatherLocation(
                        lat,
                        lon,
                        getString(R.string.metricUnits),
                        getString(R.string.lang_RU),
                        activity?.applicationContext!!.getString(R.string.apiKey)
                    )
                } catch (e: IOException) {
                    Toast.makeText(
                        activity?.applicationContext!!,
                        getString(R.string.app_error_Toast, e.message), Toast.LENGTH_LONG
                    ).show()
                    return@launch
                } catch (e: HttpException) {
                    Toast.makeText(
                        activity?.applicationContext!!,
                        getString(R.string.http_error_Toast, e.message), Toast.LENGTH_LONG
                    )
                        .show()
                    return@launch
                }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    currentWeather(response)
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun currentWeather(response: Response<CurrentWeather>) {
        val data = response.body()

        cityTV.text = data!!.name
        temperatureGeneralTV.text = "${data.main.temp} ℃"
        temperatureFeelsTV.text = "${data.main.feels_like} ℃"
        temperatureMinTV.text = "${data.main.temp_min} ℃"
        temperatureMaxTV.text = "${data.main.temp_max} ℃"
        windSpeedTV.text = "${data.wind.speed} м/с"
        windGustTV.text = "${data.wind.gust} м/с"
        visibilityTV.text = "${data.visibility} м"
        cloudsTV.text = "${data.clouds.all} %"
        pressureTV.text = "${data.main.pressure / 10} кПа"
        humidityTV.text = "${data.main.humidity} %"

        val iconId = data.weather[0].icon
        val imageUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
        Picasso.get().load(imageUrl).into(weatherIV)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}