package com.example.geoconnect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleMap: GoogleMap
    companion object{
        const val LOCATION_REQUEST_CODE=1
    }
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        auth = FirebaseAuth.getInstance()
        loadGoogleMap()
        val name:EditText=findViewById(R.id.name)
        val age:EditText=findViewById(R.id.age)
        val adder:Button=findViewById(R.id.save)
        val number = intent.getStringExtra("phoneNumber")
        adder.setOnClickListener {
            val user_name=name.text.toString().trim()
            val age=age.text.toString().trim()
            if(user_name.isEmpty()){
                Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show()
            }
            else{
                if(age.isEmpty()){
                    Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show()
                }
                else{
                    addUserToDatabase(user_name,age,currentLatitude.toString(),currentLongitude.toString(),auth.currentUser?.uid!!,number.toString())
                    val intent=Intent(this,UserList::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun addUserToDatabase(userName: String, age: String, currentLatitude: String, currentLongitude: String, uid: String,number: String) {
        mDbRef= FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(userName,age,currentLatitude,currentLongitude,uid,number))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            auth.signOut()
            val intent= Intent(this@Profile,MainActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }
    private fun loadGoogleMap() {
        val mapContainer = findViewById<FrameLayout>(R.id.mapView)
        if (mapContainer.childCount == 0) {
            val mapFragment = SupportMapFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.mapView, mapFragment)
                .commit()
            mapFragment.getMapAsync { map ->
                googleMap = map
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    googleMap.isMyLocationEnabled = true
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            currentLatitude = location.latitude
                            currentLongitude = location.longitude
                            val currentLocation = LatLng(location.latitude, location.longitude)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_REQUEST_CODE
                    )
                }
            }
        }
    }

}