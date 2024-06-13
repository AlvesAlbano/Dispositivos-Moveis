package com.example.appespacoculturalkotlin

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appespacoculturalkotlin.adapter.AdapterObras
import java.util.Timer
import java.util.TimerTask

import com.google.firebase.firestore.FirebaseFirestore

class DeviceScanActivity : AppCompatActivity() {

    private val detectedBeacons = mutableListOf<String>()
    private lateinit var textoAguarde: TextView
    private lateinit var startScanButton: Button
    private val db = FirebaseFirestore.getInstance()

    companion object {
        lateinit var nomeBeacon:String
    }

    private val targetBeacons = setOf(
        "b4323cca-b7e3-0088-a64d-b3e7e41e2d37",
        "644f76f7-6a52-42bc-e911-fd902c9bb987"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_device_scan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textoAguarde = findViewById(R.id.texto_aguarde)
        startScanButton = findViewById(R.id.startScanButton)

        val backButton = findViewById<Button>(R.id.btnVoltarView)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    fun onScanButtonClick(view: View) {
        val textoAguarde = findViewById<TextView>(R.id.texto_aguarde)

        textoAguarde.visibility = View.VISIBLE
        startScanButton.visibility = Button.INVISIBLE

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        BeaconManager.getInstanceForApplication(applicationContext).beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )
        val region = Region("all-beacons-region", null, null, null)

        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
        beaconManager.startRangingBeacons(region)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                beaconManager.stopRangingBeacons(region)
                Handler(Looper.getMainLooper()).post {
                    if (detectedBeacons.isEmpty()) {
                        naoDetectou()
                    } else {
                        fetchObraNames()
                    }
                    startScanButton.visibility = Button.VISIBLE
                }
            }
        }, 15000) // 15000 milissegundos = 15 segundos
    }

    val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        beacons.forEach { beacon ->
            val uuid = beacon.id1.toString()
            nomeBeacon = uuid

            if (targetBeacons.contains(uuid) && uuid !in detectedBeacons) {
                detectedBeacons.add(uuid)
            }
        }
        Log.d("DeviceScanActivity", "Detected beacons: $detectedBeacons")
    }

    private fun fetchObraNames() {
        val detectedObras = mutableListOf<String>()
        val db = FirebaseFirestore.getInstance()

        detectedBeacons.forEach { uuid ->
            db.collection("Obras").document(uuid).get()
                .addOnSuccessListener { document ->
                    val obraName = document.getString("nome") ?: "Nome Desconhecido"
                    detectedObras.add(obraName)

                    if (detectedObras.size == detectedBeacons.size) {
                        listarObras(detectedObras)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("DeviceScanActivity", "Error getting documents.", exception)
                }
        }
    }

    private fun listarObras(obras: List<String>) {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.popup_obras)

        val titleTextView = dialog.findViewById<TextView>(R.id.titulo)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.listaObras)
        val okButton = dialog.findViewById<Button>(R.id.buttonVoltarObras)

        titleTextView.text = "Visualizar Obras"

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterObras(obras, object : AdapterObras.OnItemClickListener {
            override fun onItemClick(nome: String) {
                Log.d("Tag", nome)
            }
        })

        okButton.setOnClickListener {
            textoAguarde.visibility = View.INVISIBLE
            detectedBeacons.clear()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun naoDetectou() {
        if (!isFinishing && !isDestroyed) {
            val dialog = Dialog(this)

            dialog.setContentView(R.layout.popup_nao_detectou)

            val voltarButton = dialog.findViewById<Button>(R.id.buttonVoltarFalha)

            voltarButton.setOnClickListener {
                textoAguarde.visibility = View.INVISIBLE
                detectedBeacons.clear()
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}