package com.example.coffeemapper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

class AddActivity : AppCompatActivity() {
    var rating = 5
    var cafeName = ""
    lateinit var address : String
    lateinit var latLng : LatLng
    lateinit var ratingText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cafe)
        address = intent.getStringExtra("address").toString()
        val bundle = intent.getParcelableExtra<Bundle>("bundle")
        if (bundle != null) {
            latLng = bundle.getParcelable("latLng")!!
        }
        ratingText = findViewById(R.id.ratingId)


    }//onCreate

    fun addItem(view: View) {
        if (cafeName=="") { // disallow add
            Toast.makeText(applicationContext, "You must enter cafe name!", Toast.LENGTH_SHORT).show()
        }
        else {
            places.add(address)
            locations.add(latLng)

            val sharedPreferences =
                getSharedPreferences("com.example.coffeemapper", MODE_PRIVATE)

            for (coord in locations) {
                latitudes.add(coord.latitude.toString())
                longitudes.add(coord.longitude.toString())
            }
            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(places)).apply()
            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply()
            sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply()
            arrayAdapter!!.notifyDataSetChanged()
            Toast.makeText(this, "$latLng saved", Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    } // addItem

    fun incrementRating(view: View) {
        if(rating == 5) return
        rating++
        ratingText.text = "$rating/5"
    } // incrementRating

    fun decrementRating(view: View) {
        if(rating == 1) return
        rating--
        ratingText.text = "$rating/5"
    } // decrementRating

}