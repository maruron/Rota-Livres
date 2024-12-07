package com.example.rotalivre

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Principal : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val cadastrar = findViewById<AppCompatButton>(R.id.usuario)
        cadastrar.setOnClickListener {
            val intent = Intent(this, user::class.java)
            startActivity(intent)
        }


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val feiraDeSantana = LatLng(-12.2761, -38.9313)
        map.addMarker(
            MarkerOptions()
                .position(feiraDeSantana)
                .title("Marcador em Feira de Santana")
        )
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(feiraDeSantana, 12.0f)
        )
    }

    private fun enableEdgeToEdge() {
    }
}