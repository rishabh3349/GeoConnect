package com.example.geoconnect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserInfo : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    companion object{
        private const val LOCATION_REQUEST_CODE=1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info)
        val name = intent.getStringExtra("name")
        val nameTextView: TextView = findViewById(R.id.name_o)
        nameTextView.text = "Name: ${name}"
        val age = intent.getStringExtra("age")
        val ageTextView: TextView = findViewById(R.id.age_o)
        ageTextView.text = "Age: ${age}"
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapView_o) as? SupportMapFragment
            ?: SupportMapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().replace(R.id.mapView_o, it).commit()
            }
        mapFragment.getMapAsync(this)
        val uid = intent.getStringExtra("uid")
        val call:Button=findViewById(R.id.call)
        call.setOnClickListener {
            val intent=Intent(this,CallActivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }

    }
    override fun onMapReady(googleMap: GoogleMap) {
        val currentLatitude = intent.getStringExtra("currentLatitude")
        val currentLongitude = intent.getStringExtra("currentLongitude")
        mMap = googleMap

        mMap.setOnMapLoadedCallback {
            if (currentLatitude != null && currentLongitude != null) {
                val location = LatLng(currentLatitude.toDouble(), currentLongitude.toDouble())
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
                googleMap.addMarker(MarkerOptions().position(location).title("User's Location"))
            }
        }
    }
}