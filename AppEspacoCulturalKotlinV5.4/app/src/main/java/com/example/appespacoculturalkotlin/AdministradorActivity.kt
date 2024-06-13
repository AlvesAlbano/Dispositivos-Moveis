package com.example.appespacoculturalkotlin

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import android.content.ContentValues.TAG
import android.database.DatabaseUtils
import android.util.Base64
import android.util.Log
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class AdministradorActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        firestore = FirebaseFirestore.getInstance()

        val botaoEntrar:Button = findViewById(R.id.button)
        val buttonVoltarAdmin = findViewById<Button>(R.id.buttonVoltarAdmin)
        val usuarioCampo:EditText = findViewById(R.id.Usuario_Campo)
        val senhaCampo:EditText = findViewById(R.id.Senha_Campo)

        botaoEntrar.setOnClickListener {
            val usuarioValor:String = usuarioCampo.text.toString()
            val senhaValor:String = senhaCampo.text.toString()
            autenticacao(usuarioValor, senhaValor)
        }

        buttonVoltarAdmin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun autenticacao(usuario: String, senha: String) {
        firestore.collection("Login").document("wGpfbEZjlfv2DpPJmQtC").get().addOnSuccessListener { document ->
            if (document.exists()) {
                val usuarioCorreto = document.getString("Usuário")
                val senhaCorreta = document.getString("Senha")

                if (usuario == usuarioCorreto && senha == senhaCorreta) {
                    // Autenticação bem-sucedida, navegue para a próxima atividade
                    Adm.admValor = true
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Senha incorreta
                    Log.d(TAG, "Senha incorreta")
                }
            } else {
                // Documento não encontrado
                Log.d(TAG, "Documento não encontrado")
            }
        }
            .addOnFailureListener { exception ->
                // Lidar com falhas na leitura do Firestore
                Log.e(TAG, "Erro ao acessar Firestore", exception)
            }

    }
}