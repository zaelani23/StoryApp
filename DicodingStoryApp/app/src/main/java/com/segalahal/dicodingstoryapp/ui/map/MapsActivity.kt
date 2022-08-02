package com.segalahal.dicodingstoryapp.ui.map

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem
import com.segalahal.dicodingstoryapp.databinding.ActivityMapsBinding
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: StoryMapViewModel
    private var bearer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.title_activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[StoryMapViewModel::class.java]

        val preferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        bearer = "Bearer " + preferences.getString("token", "").toString()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.getStoryMaps(bearer).observe(this){
            it.listStory?.let { it1 -> addManyMarker(it1) }
        }
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun addManyMarker(stories : List<StoryItem>){
        stories.forEach {storyMap ->
            val latLng = LatLng(storyMap.lat, storyMap.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(storyMap.name))
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}