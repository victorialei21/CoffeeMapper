package com.example.coffeemapper

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

// Victoria Lei and Michelle Yun

var names = ArrayList<String>()
var places = ArrayList<String>()
var locations = ArrayList<LatLng>()
var arrayAdapter: ArrayAdapter<*>? = null
var latitudes = ArrayList<String>()
var longitudes = ArrayList<String>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listView = findViewById<ListView>(R.id.listView)
        val sharedPreferences = getSharedPreferences("com.example.coffeemapper", Context.MODE_PRIVATE)

        places.clear()
        latitudes.clear()
        longitudes.clear()
        locations.clear()
        names.clear()

        places = ObjectSerializer.deserialize(
            sharedPreferences
                .getString("places", ObjectSerializer.serialize(ArrayList<String>()))
        ) as ArrayList<String>
        latitudes = ObjectSerializer.deserialize(
            sharedPreferences
                .getString("lats", ObjectSerializer.serialize(ArrayList<String>()))
        ) as ArrayList<String>
        longitudes = ObjectSerializer.deserialize(
            sharedPreferences
                .getString("lons", ObjectSerializer.serialize(ArrayList<String>()))
        ) as ArrayList<String>
        names = ObjectSerializer.deserialize(
            sharedPreferences
                .getString("names", ObjectSerializer.serialize(ArrayList<String>()))
        ) as ArrayList<String>

        if (places.size > 0 && latitudes.size > 0 && longitudes.size > 0 && names.size > 0) {
            for (i in latitudes.indices) {
                locations.add(LatLng(latitudes[i].toDouble(), longitudes[i].toDouble()))
            }
        } else {
            names.add("+  Add a new cafe")
            locations.add(LatLng(0.0, 0.0))
            places.add("X")
            latitudes.add("0")
            longitudes.add("0")
        }

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        listView.adapter = arrayAdapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->
                if (position == 0) {
                    val intent = Intent(applicationContext, MapActivity::class.java)
                    intent.putExtra("placeNumber", position)
                    startActivity(intent)
                } else {
                    val intent = Intent(applicationContext, CoffeeDetailActivity::class.java)
                    intent.putExtra("placeNumber", position)
                }
            }
        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { adapterView, view, i, l ->

                val itemDelete = i

                AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this cafe?")
                    .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->

                        places.removeAt(itemDelete)
                        latitudes.removeAt(itemDelete)
                        longitudes.removeAt(itemDelete)
                        names.removeAt(itemDelete)

                        arrayAdapter!!.notifyDataSetChanged()
                        sharedPreferences.edit()
                            .putString("places", ObjectSerializer.serialize(places))
                            .apply()
                        sharedPreferences.edit()
                            .putString("lats", ObjectSerializer.serialize(latitudes))
                            .apply()
                        sharedPreferences.edit()
                            .putString("lons", ObjectSerializer.serialize(longitudes))
                            .apply()
                        sharedPreferences.edit()
                            .putString("names", ObjectSerializer.serialize(names))
                            .apply()
                    }//setPositiveButton
                    .setNegativeButton("No", null)
                    .show()
                return@OnItemLongClickListener true
            }
    }//onCreate
}//MainActivity