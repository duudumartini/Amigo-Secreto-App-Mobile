package com.app.amigosecreto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController

class fragment_main : Fragment(R.layout.fragment_main) {

    private lateinit var btn_realiza_sorteio: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_realiza_sorteio = view.findViewById(R.id.btn_realizar_sorteio)

        setListeners()
    }

    fun setListeners(){
        btn_realiza_sorteio.setOnClickListener {
            findNavController().navigate(R.id.fragment_main_to_fragment_sorteio_main)
        }
    }
}