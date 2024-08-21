package com.example.mapsclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsclone.models.UserMap

class MapsListFragment: Fragment(R.layout.fragment_maps_list), UserMapInterface {
    private lateinit var adapter: MapsAdapter
    private val userMaps: MutableList<UserMap>
        get() = (activity as MainActivity).userMaps
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = MapsAdapter(activity as MainActivity, userMaps, this)
        val rvMaps: RecyclerView = view.findViewById(R.id.rvMaps)

        rvMaps.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        // When user taps on item in RV, navigate to new activity
        val intent = Intent(activity as MainActivity, DisplayMapsActivity::class.java)
        intent.putExtra("userMapTitle", userMaps[position])
        startActivity(intent)
    }

    override fun onBookmarkClicked(position: Int) {
        val userMap = userMaps[position]
        userMap.isBookmark = !userMap.isBookmark
        adapter.notifyDataSetChanged()
    }
}