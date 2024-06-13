package com.example.appespacoculturalkotlin.telas_adm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appespacoculturalkotlin.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ObraEditarAdmActivity : AppCompatActivity() {

    private lateinit var uuid: String
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUri: Uri

    private lateinit var editTextNome: EditText
    private lateinit var editTextAutor: EditText
    private lateinit var editTextAno: EditText
    private lateinit var editTextInfo: EditText
    private lateinit var imageViewImagem: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                // Atualiza o ImageView com a imagem selecionada da galeria
                imageViewImagem.setImageURI(it)
                imageUri = it
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_activity_editar_obra)

        uuid = intent.getStringExtra("uuid") ?: ""
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        editTextNome = findViewById(R.id.editText_Obra)
        editTextAutor = findViewById(R.id.editText_Autor)
        editTextAno = findViewById(R.id.editText_Ano)
        editTextInfo = findViewById(R.id.editText_Info)
        imageViewImagem = findViewById(R.id.adm_obra_imagem)

        val btnVoltar: Button = findViewById(R.id.adm_btn_voltar_obra)
        val btnSalvar: Button = findViewById(R.id.btn_salvar)
        val editarFoto: ImageView = findViewById(R.id.editar_foto)

        btnVoltar.setOnClickListener {
            onBackPressed()
        }

        btnSalvar.setOnClickListener {
            salvarDadosNoFirestore()
        }

        editarFoto.setOnClickListener {
            abrirGaleria()
        }

        carregarDetalhesDaObra()
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
        val textViewObra = findViewById<TextView>(R.id.adm_obra_textview)
        val imageViewImagem = findViewById<ImageView>(R.id.adm_obra_imagem)

        // Log para depuração
        Log.d("ObraActivity", "textViewObra: $textViewObra, imageViewImagem: $imageViewImagem")

        editTextNome.setText(nome)
        editTextAutor.setText(autor)
        editTextAno.setText(ano)
        editTextInfo.setText(info)

        textViewObra.text = "$autor - $nome, $ano"

        // Carrega a imagem da obra usando Glide
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewImagem)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch(intent)
    }

    private fun salvarDadosNoFirestore() {
        val nome = editTextNome.text.toString()
        val autor = editTextAutor.text.toString()
        val ano = editTextAno.text.toString()
        val info = editTextInfo.text.toString()

        // Atualiza os dados no Firestore
        val obraRef = db.collection("Obras").document(uuid)
        val obraData = hashMapOf(
            "nome" to nome,
            "autor" to autor,
            "ano" to ano,
            "info" to info,
            // Se a imagem foi alterada, salva a nova URL
            "url" to imageUri.toString()
        )
        obraRef.set(obraData)
            .addOnSuccessListener {
                Log.d("ObraActivity", "Document successfully updated!")
                // Recarrega os detalhes da obra após salvar
                carregarDetalhesDaObra()
            }
            .addOnFailureListener { e ->
                Log.w("ObraActivity", "Error updating document", e)
            }
    }
}