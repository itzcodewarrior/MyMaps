package com.example.mapsclone

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapsclone.databinding.ActivityCreateMapsBinding
import com.example.mapsclone.models.Place
import com.example.mapsclone.models.UserMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

private const val TAG = "CreateMapsActivity"
class CreateMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapsBinding
    private  var markers: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.view?.let {
            Snackbar.make(it, "Long Press to add a place", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", {})
                .setActionTextColor(ContextCompat.getColor(this,R.color.white))
                .show()
        }
        mapFragment.getMapAsync(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_button,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.btnSave){
            // Save markers
            // If no markers selected show toast
            if(markers.isEmpty()){
                Toast.makeText(this, "Add atleast 1 marker" , Toast.LENGTH_SHORT).show()
                return true
            }
            val places = markers.map { marker -> Place(marker.title.toString(),
                marker.snippet.toString(),marker.position.latitude,marker.position.longitude) }

            val userMap = UserMap(intent.getStringExtra(EXTRA_MAP_TITLE).toString(),places)
            Log.i(TAG,"user got ${userMap.title}")
            val result = Intent()
            result.putExtra(EXTRA_USER_MAP,userMap)
            setResult(Activity.RESULT_OK,result)
            finish()
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener {markerToDelete->
            Log.i(TAG,"setOnInfoWindowClickListener")
            markers.remove(markerToDelete)
            markerToDelete.remove()
        }
        mMap.setOnMapLongClickListener{ latLng->
            Log.i(TAG,"OnMapLongClickListener")
            showAlertDialog(latLng)
        }
        // add an example marker
        val Tokyo = LatLng(34.65,138.83)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Tokyo,6f))

    }

    private fun showAlertDialog(latLng: LatLng) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place,null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Place")
            .setView(placeFormView)
            .setPositiveButton("OK",null)
            .setNegativeButton("Cancel",null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
            val placeTitle = placeFormView.findViewById<EditText>(R.id.etTitle)?.text.toString()
            val placeDescription = dialog.findViewById<EditText>(R.id.etDescription)?.text.toString()
            if(placeTitle.trim().isEmpty() || placeDescription.trim().isEmpty() ){
                Toast.makeText(this,"Title and description should not be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(placeTitle).snippet(placeDescription))
            if (marker != null) {
                markers.add(marker)
            }
            dialog.dismiss()
        }

    }
}