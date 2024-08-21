package com.example.mapsclone

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsclone.models.UserMap


private const val TAG = "MapsAdapter"
class MapsAdapter(val context: Context, var mapsList: List<UserMap>, val userMapInterface:UserMapInterface) : RecyclerView.Adapter<MapsAdapter.ViewHolder>(), Filterable {

    private var mapsListCopy = mapsList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(context).inflate(R.layout.dialog_create_map,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap = mapsList[position]
        val textViewTitle:TextView = holder.itemView.findViewById(R.id.tvTitle)
        val iconBookmark: AppCompatImageView = holder.itemView.findViewById(R.id.imageFavorite)

        textViewTitle.text = userMap.title
        val icon = if(userMap.isBookmark) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark
        iconBookmark.setImageResource(icon)

        textViewTitle.setOnClickListener{
            userMapInterface.onItemClick(position)
        }
        iconBookmark.setOnClickListener{
            userMapInterface.onBookmarkClicked(position)
        }
    }

    override fun getItemCount() = mapsList.size

    // for editText Search option
    fun filterMaps(filteredMaps: MutableList<UserMap>) {
        mapsList = filteredMaps
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }

    // implementing filterable class
    // for menu option search
    override fun getFilter(): Filter {
        return mapsFilter
    }

    private val mapsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList : MutableList<UserMap> = ArrayList<UserMap>()
            if(constraint == null || constraint.length == 0) {
                filteredList.addAll(mapsListCopy)
            }
            else {
                val filterPattern = constraint.toString().lowercase().trim()
                for(map in mapsListCopy){
                    if(map.title.lowercase().contains(filterPattern)){
                        filteredList.add(map)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mapsList = mutableListOf<UserMap>()
            (mapsList as MutableList<UserMap>).addAll(results.values as List<UserMap>)
            notifyDataSetChanged()
        }
    }
}
