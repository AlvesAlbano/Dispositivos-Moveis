package com.example.appespacoculturalkotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.appespacoculturalkotlin.R

class FragmentHome : Fragment() {

    // Array de imagens de exemplo, incluindo apenas até a foto 4
    private val fotos = intArrayOf(
        R.drawable.quadros,
        R.drawable.quadros2,
        R.drawable.quadros3,
        R.drawable.quadros4
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate o layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val gridView: GridView = view.findViewById(R.id.fotos_grid)
        val adapter = FotosAdapter(requireActivity(), fotos)
        gridView.adapter = adapter

        return view
    }
}