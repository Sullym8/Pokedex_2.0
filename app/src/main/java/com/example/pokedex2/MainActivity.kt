package com.example.pokedex2

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonUrlList: MutableList<String>
    private lateinit var pokemonObjectList: MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonUrlList = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)

        getPokemonUrl()
    }


    private fun getPokemonUrl() {
        val client = AsyncHttpClient()
        client["https://pokeapi.co/api/v2/pokemon/?limit=1000", object : JsonHttpResponseHandler() {
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
                    val pokemonUrlArray = pokemon.getJSONArray("results")
                    for (i in 0 until pokemonUrlArray.length()) {
                        pokemonUrlList.add(pokemonUrlArray.getJSONObject(i).getString("url"))
                    }
                    val adapter = PokemonAdapter(pokemonUrlList)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }]

    }
}

class SpacesItemDecoration(val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space;
        outRect.left = space
        outRect.right = space


    }

}
data class Pokemon(val name: String,
                   val type1: String,
                   val type2:String?,
                   val imageUrl:String) {
}