package com.app.amigosecreto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController

class fragment_sorteio_main : Fragment(R.layout.fragment_sorteio_main) {

    private var tiposorteio: Boolean = true //True para Presencial, False para Online
    private lateinit var btnPresencial: Button
    private lateinit var btnOnline: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPresencial = view.findViewById(R.id.btn_sorteio_presencial)
        btnOnline = view.findViewById(R.id.btn_sorteio_online)
        setListeners()
    }
    private fun setListeners() {
        btnPresencial.setOnClickListener {
            tiposorteio = true;
            findNavController().navigate(R.id.fragment_sorteio_main_to_fragment_participantes_online)
        }
        btnOnline.setOnClickListener {
            tiposorteio = false
            findNavController().navigate(R.id.fragment_main_to_fragment_sorteio_main)
        }
    }
}