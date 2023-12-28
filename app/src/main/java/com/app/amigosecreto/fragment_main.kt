package com.app.amigosecreto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlin.math.log

class fragment_main : Fragment(R.layout.fragment_main) {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var btn_realiza_sorteio: Button
    private lateinit var btn_revelar: Button
    private lateinit var btn_historico: Button
    private lateinit var txt_codigo: EditText
    private lateinit var codigo: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        btn_realiza_sorteio = view.findViewById(R.id.btn_realizar_sorteio)
        btn_revelar = view.findViewById(R.id.btn_revelar)
        btn_historico = view.findViewById(R.id.btn_historico)
        txt_codigo = view.findViewById(R.id.txt_codigo)

        setListeners()
    }

    fun setListeners(){
        btn_realiza_sorteio.setOnClickListener {
            findNavController().navigate(R.id.fragment_main_to_fragment_sorteio_main)
        }
        btn_revelar.setOnClickListener {
            try {
                codigo = sharedViewModel.descriptografar(txt_codigo.text.toString())
                if(sharedViewModel.isTextoCriptografado(codigo) == true) {
                    val action = fragment_mainDirections.fromFragmentMainToFragmentRevelaMain(codigo)
                    findNavController().navigate(action)
                } else {
                    sharedViewModel.exibirAlertDialog(requireContext(), getString(R.string.txt_codigo_valido))
                }
            } catch (e: Exception) {
                sharedViewModel.exibirAlertDialog(requireContext(), getString(R.string.txt_codigo_valido))
            }
        }

        btn_historico.setOnClickListener {
            findNavController().navigate(R.id.from_fragment_main_to_fragment_historico)
        }

        txt_codigo.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && txt_codigo.text.toString() == getString(R.string.txt_codigo) ) {
                txt_codigo.setText("")
            }
            if (!hasFocus && txt_codigo.text.isBlank()) {
                txt_codigo.setText(getString(R.string.txt_codigo))
            }
        }
    }
}