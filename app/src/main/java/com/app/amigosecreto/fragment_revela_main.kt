package com.app.amigosecreto

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.KeyEventDispatcher.dispatchKeyEvent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class fragment_revela_main : Fragment(R.layout.fragment_revela_main) {
    private lateinit var sharedViewModel: SharedViewModel
    private var codigo: String? = null
    private var tipoSorteio: String? = null
    private var participante: String? = null
    private var amigoSecreto: String? = null
    private lateinit var txt_nome_participante: TextView
    private lateinit var btn_nao: Button
    private lateinit var btn_sim: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        txt_nome_participante = view.findViewById(R.id.txt_nome_revela)
        btn_nao = view.findViewById(R.id.btn_nao_revela)
        btn_sim = view.findViewById(R.id.btn_sim_revela)
        val args = fragment_revela_mainArgs.fromBundle(requireArguments())
        codigo = args.codigoAmigoSecreto
        codigo = removerPrefixoEncriptado(codigo.toString())
        separaStrings(codigo.toString())
        tipoSorteio = args.tipoSorteio

        setListener()
    }

    private fun setListener() {
        btn_sim.setOnClickListener {
            exibirAmigoSecreto(amigoSecreto.toString())
            sharedViewModel.tocarSomCongrats(requireContext())
        }
        btn_nao.setOnClickListener {
            if(tipoSorteio == ""){
                findNavController().navigate(R.id.from_fragment_revela_main_to_fragment_main)
            }else{
                findNavController().navigateUp()
            }
        }
    }

    fun removerPrefixoEncriptado(textoCriptografado: String): String {
        return textoCriptografado.removePrefix("ENCRYPTED:")
    }

    fun separaStrings(codigo: String){
        val partes = codigo.split("/")

        // Verificar se há pelo menos duas partes antes de prosseguir
        if (partes.size >= 2) {
            participante = partes[0].trim()  // Remove espaços em branco extras
            amigoSecreto = partes[1].trim()  // Remove espaços em branco extras
            atualizaNomeParticipante(participante.toString())
        }
    }
    fun atualizaNomeParticipante(participante : String){
        txt_nome_participante.text = participante
    }

    fun exibirAmigoSecreto(mensagem: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.amigo_secreto_dialog, null)
        val txt_amigoSecreto = view.findViewById<TextView>(R.id.txt_amigo_secreto)
        txt_amigoSecreto.text = mensagem
        alertDialogBuilder.setView(view)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val btnMemorizei = view.findViewById<Button>(R.id.btn_memorizei_revela)
        btnMemorizei.setOnClickListener {
            if(tipoSorteio == ""){
                findNavController().navigate(R.id.from_fragment_revela_main_to_fragment_main)
                alertDialog.dismiss()
            }else{
                findNavController().navigateUp()
                alertDialog.dismiss()
            }

        }
    }
}