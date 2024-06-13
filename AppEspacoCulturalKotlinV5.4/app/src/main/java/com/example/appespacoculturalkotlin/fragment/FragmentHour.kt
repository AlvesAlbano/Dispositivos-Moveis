package com.example.appespacoculturalkotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appespacoculturalkotlin.R
import com.example.appespacoculturalkotlin.HorarioAdapter
import com.example.appespacoculturalkotlin.HorarioItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log

class FragmentHour : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorarioAdapter
    private val horarios = mutableListOf<HorarioItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hour, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HorarioAdapter(horarios)
        recyclerView.adapter = adapter

        fetchHorarios()

        return view
    }

    private fun fetchHorarios() {
        firestore.collection("Horarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val dia = document.id
                    val data = document.data
                    val horarioInicio = document.getString("Horario Inicio") ?: ""
                    val horarioTermino = document.getString("Horario Termino") ?: ""
                    val contadorKey = "contador$dia" // Monta a chave dinamicamente
                    val pessoasInteressadas = data[contadorKey] ?: 0 // Obtém o valor do contador

                    horarios.add(HorarioItem(dia, horarioInicio, horarioTermino, pessoasInteressadas as Long? ?: 0))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("FragmentHour", "Error getting documents: ", exception)
            }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHour().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}