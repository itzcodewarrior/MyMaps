package com.example.mapsclone

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsclone.models.Place
import com.example.mapsclone.models.UserMap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import androidx.fragment.app.commit

private const val TAG = "MainActivity"
const val FILENAME = "UserMaps.data"

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"

class MainActivity : AppCompatActivity() {

    lateinit var userMaps : MutableList<UserMap>

//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        if(it.resultCode == Activity.RESULT_OK){
//            Log.i(TAG,"User tapped on save button ")
//            val userMap = it.data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
//            userMaps.add(userMap)
//            adapter.notifyDataSetChanged()
//            serializeUserMaps(this,userMaps)
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fbtnAddMap: FloatingActionButton = findViewById(R.id.fbtnAddMap)
//        val etSearch: EditText = findViewById(R.id.etSearch)

        val mapsfromFile = generateSampleData()
        userMaps = mapsfromFile.toMutableList()


        //Loading your fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view,MapsListFragment())
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bundle = Bundle().apply{
                putString("mapsId", "hello")
            }
            replace(R.id.fragment_container_view,MapsListFragment().apply {
                arguments = bundle
            })
        }

        //Creating your own map
        fbtnAddMap.setOnClickListener{
//            showAlertDialog()
        }

        //Filtering maps based on search
//        etSearch.addTextChangedListener(object : TextWatcher{

//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun afterTextChanged(p0: Editable?) {
//                    filter(p0.toString())
//            }
//
//        })

    }

//    private fun filter(searchTxt: String) {
//        var filteredMaps = emptyList<UserMap>().toMutableList()
//        for(map in userMaps){
//            if(map.title.lowercase().contains(searchTxt.lowercase())){
//                filteredMaps.add(map)
//                Log.i(TAG,"added ${map.title} to filtered maps")
//            }
//        }
//        adapter.filterMaps(filteredMaps)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_menu,menu)
//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView : SearchView = searchItem?.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
//                return false
//            }
//
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    private fun serializeUserMaps(context: Context,userMaps: List<UserMap>){
        Log.i(TAG, "serializeUserMaps")
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use { it.writeObject(userMaps) }
    }

    private fun deserializeUserMaps(context: Context): List<UserMap> {
        Log.i(TAG, "DeserializeUserMaps")
        val datafile = getDataFile(context)
        if(!datafile.exists()){
            Log.i(TAG,"data file does not exist")
            return emptyList()
        }
        return ObjectInputStream(FileInputStream(getDataFile(context))).use { it.readObject() } as List<UserMap>
    }

    private fun getDataFile(context:Context): File {
        Log.i(TAG, "Getting file from ${context.filesDir}")
        return File(context.filesDir,FILENAME)
    }

//    private fun showAlertDialog() {
//        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map,null)
//        val dialog = AlertDialog.Builder(this)
//            .setTitle("Add New map")
//            .setView(mapFormView)
//            .setPositiveButton("OK",null)
//            .setNegativeButton("Cancel",null)
//            .show()
//
//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
//            val placeTitle = mapFormView.findViewById<EditText>(R.id.etTitle).text.toString()
//            if(placeTitle.trim().isEmpty() ){
//                Toast.makeText(this,"Title should not be empty", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            launcher.launch(Intent(this,CreateMapsActivity::class.java).putExtra(EXTRA_MAP_TITLE,placeTitle))
//            dialog.dismiss()
//        }
//    }

    private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap(
                "Memories from University",
                listOf(
                    Place("Branner Hall", "Best dorm at Stanford", 37.426, -122.163),
                    Place(
                        "Gates CS building",
                        "Many long nights in this basement",
                        37.430,
                        -122.173
                    ),
                    Place("Pinkberry", "First date with my wife", 37.444, -122.170)
                )
            ),
            UserMap(
                "January vacation planning!",
                listOf(
                    Place("Tokyo", "Overnight layover", 35.67, 139.65),
                    Place("Ranchi", "Family visit + wedding!", 23.34, 85.31),
                    Place("Singapore", "Inspired by \"Crazy Rich Asians\"", 1.35, 103.82)
                )
            ),
            UserMap(
                "Singapore travel itinerary",
                listOf(
                    Place("Gardens by the Bay", "Amazing urban nature park", 1.282, 103.864),
                    Place(
                        "Jurong Bird Park",
                        "Family-friendly park with many varieties of birds",
                        1.319,
                        103.706
                    ),
                    Place("Sentosa", "Island resort with panoramic views", 1.249, 103.830),
                    Place(
                        "Botanic Gardens",
                        "One of the world's greatest tropical gardens",
                        1.3138,
                        103.8159
                    )
                )
            ),
            UserMap(
                "My favorite places in the Midwest",
                listOf(
                    Place(
                        "Chicago",
                        "Urban center of the midwest, the \"Windy City\"",
                        41.878,
                        -87.630
                    ),
                    Place("Rochester, Michigan", "The best of Detroit suburbia", 42.681, -83.134),
                    Place(
                        "Mackinaw City",
                        "The entrance into the Upper Peninsula",
                        45.777,
                        -84.727
                    ),
                    Place("Michigan State University", "Home to the Spartans", 42.701, -84.482),
                    Place("University of Michigan", "Home to the Wolverines", 42.278, -83.738)
                )
            ),
            UserMap(
                "Restaurants to try",
                listOf(
                    Place("Champ's Diner", "Retro diner in Brooklyn", 40.709, -73.941),
                    Place("Althea", "Chicago upscale dining with an amazing view", 41.895, -87.625),
                    Place("Shizen", "Elegant sushi in San Francisco", 37.768, -122.422),
                    Place(
                        "Citizen Eatery",
                        "Bright cafe in Austin with a pink rabbit",
                        30.322,
                        -97.739
                    ),
                    Place(
                        "Kati Thai",
                        "Authentic Portland Thai food, served with love",
                        45.505,
                        -122.635
                    )
                )
            )
        )
    }
}