package com.example.appespacoculturalkotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RecentActivity : AppCompatActivity() {
    private lateinit var adapter: RecentesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.lista_recente)
        adapter = RecentesAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load data from Firestore
        loadDataFromFirestore()
    }

    private fun loadDataFromFirestore() {
        val nomeBeacon: String = DeviceScanActivity.nomeBeacon
        val db = FirebaseFirestore.getInstance()

        db.collection("Obras")
            .whereEqualTo("nomeBeacon", nomeBeacon)
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<RecentesData>()
                for (document in result) {
                    val nome = document.getString("nome") ?: ""
                    val autor = document.getString("autor") ?: ""
                    val imagem = document.getString("url") ?: ""
                    items.add(RecentesData(nome, autor, imagem))
                }
                adapter.updateData(items)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.w("RecentActivity", "Error getting documents: ", exception)
            }
    }
}