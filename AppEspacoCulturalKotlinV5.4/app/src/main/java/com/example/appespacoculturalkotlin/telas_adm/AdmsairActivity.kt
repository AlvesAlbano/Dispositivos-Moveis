package com.example.appespacoculturalkotlin.telas_adm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appespacoculturalkotlin.Adm
import com.example.appespacoculturalkotlin.MainActivity
import com.example.appespacoculturalkotlin.R

class AdmsairActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_activity_sair)

        val btnSair:Button = findViewById(R.id.btn_sair)
        val btnVoltar:Button = findViewById(R.id.btn_voltar)

        btnSair.setOnClickListener{
            Adm.admValor = false
            finish()
        }

        btnVoltar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}