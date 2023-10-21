package com.example.pokedex2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class PokemonAdapter(private val pokemonList: List<String>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pokemonName : TextView
        val pokemonType1: TextView
        val pokemonType2: TextView
        val pokemonImage: ImageView
        init {
            pokemonName = view.findViewById(R.id.pokemonName)
            pokemonType1 = view.findViewById(R.id.type1)
            pokemonType2 = view.findViewById(R.id.type2)
            pokemonImage = view.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonAdapter.ViewHolder, position: Int) {
//        holder.pokemonName.text = pokemonList[position]
        getPokemonData(holder, pokemonList[position])
        holder.pokemonImage.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Link to pokemon: ${pokemonList[position]}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getPokemonData(holder: ViewHolder, url:String){
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d("PokeError","Error")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                if (json != null) {
                    val pokemon = json.jsonObject
                    val name = pokemon.getString("name").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    val type1 = pokemon.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    val type2 = if (pokemon.getJSONArray("types").length() == 2) {
                        pokemon.getJSONArray("types").getJSONObject(1).getJSONObject("type")
                            .getString("name")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    } else {
                        null
                    }
                    val imageUrl = pokemon.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default")

                    holder.pokemonName.text = name
                    holder.pokemonType1.text = type1
                    if (type2 == null) {
                        holder.pokemonType2.visibility= View.GONE
                    } else {
                        holder.pokemonType2.visibility= View.VISIBLE
                        holder.pokemonType2.text = type2
                    }
                    Glide.with(holder.itemView).load(imageUrl).fitCenter().into(holder.pokemonImage)
                }
            }
        }]
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}