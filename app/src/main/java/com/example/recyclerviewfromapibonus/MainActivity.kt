package com.example.recyclerviewfromapibonus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var rv:RecyclerView
    lateinit var namesList:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv=findViewById(R.id.rv)
        namesList= arrayListOf()

        CallApi()
        
    }

    private fun CallApi() {
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val api = URL("https://dojo-recipes.herokuapp.com/people/")
                    .readText(Charsets.UTF_8)
                if (api.isNotEmpty()){
                    getData(api)
                }else {
                    Toast.makeText(this@MainActivity,"NULL VALUE", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                println("Error $e")
            }
        }
    }

    private suspend fun getData(api: String) {
        withContext(Dispatchers.Main){
            val names = JSONArray(api)
            for (i in 0 until names.length()){
                namesList.add(names.getJSONObject(i).getString("name"))
            }
            rv.adapter=myAdapter(namesList)
            rv.layoutManager= LinearLayoutManager(this@MainActivity)
        }
    }
}