package com.example.appespacoculturalkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide

class ObraActivity : AppCompatActivity() {

    private lateinit var uuid: String
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra)

        uuid = intent.getStringExtra("uuid") ?: ""
        db = FirebaseFirestore.getInstance()

        // Carrega as informações da obra
        carregarDetalhesDaObra()

        val btnVoltar: Button = findViewById(R.id.btn_voltar_obra)

        btnVoltar.setOnClickListener{
            onBackPressed()
        }
    }

    private fun carregarDetalhesDaObra() {

        val obraRef = db.collection("Obras").document(uuid)
        obraRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nome = document.getString("nome") ?: ""
                    val autor = document.getString("autor") ?: ""
                    val ano = document.getString("ano") ?: ""
                    val info = document.getString("info") ?: ""
                    val imageUrl = document.getString("url") ?: ""

                    // Atualiza a UI com os detalhes da obra
                    exibirDetalhesDaObra(nome, autor, ano, info, imageUrl)
                } else {
                    Log.d("ObraActivity", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ObraActivity", "get failed with ", exception)
            }
    }

    private fun exibirDetalhesDaObra(nome: String, autor: String, ano: String, info: String, imageUrl: String) {
        val textViewObra = findViewById<TextView>(R.id.obra_textview)
        val textViewInfo = findViewById<TextView>(R.id.obra_info)
        val imageViewImagem = findViewById<ImageView>(R.id.obra_imagem)

        textViewObra.text = "$autor - $nome, $ano"
        textViewInfo.text = "Mais Informações: ${info}"

        // Carrega a imagem da obra usando uma biblioteca de carregamento de imagens, como Glide ou Picasso
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewImagem)
    }
}