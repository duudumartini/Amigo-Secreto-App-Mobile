package com.app.amigosecreto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController

class fragment_tipo_sorteio : Fragment(R.layout.fragment_tipo_sorteio) {
    private lateinit var btnPresencial: Button
    private lateinit var btnOnline: Button
    private var tipoSorteio: Boolean = false //false para presencial, true para online
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPresencial = view.findViewById(R.id.btn_sorteio_presencial)
        btnOnline = view.findViewById(R.id.btn_sorteio_online)
        setListeners()
    }
    private fun setListeners() {
        btnPresencial.setOnClickListener {
            tipoSorteio = false
            val action = fragment_tipo_sorteioDirections.fromFragmentSorteioMainToFragmentParticipantes(tipoSorteio)
            findNavController().navigate(action)
        }
        btnOnline.setOnClickListener {
            tipoSorteio = true
            val action = fragment_tipo_sorteioDirections.fromFragmentSorteioMainToFragmentParticipantes(tipoSorteio)
            findNavController().navigate(action)
        }
    }
}