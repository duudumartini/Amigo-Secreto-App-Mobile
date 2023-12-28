package com.app.amigosecreto

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class fragment_resultado_presencial : Fragment(R.layout.fragment_resultado_presencial) {
    private lateinit var sharedViewModel: SharedViewModel
    private var participantesList = mutableListOf<participante>()
    private lateinit var containerParticipantes : LinearLayout
    private lateinit var btn_home: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        participantesList.clear()
        participantesList.addAll(sharedViewModel.listaParticipantes)
        containerParticipantes = view.findViewById(R.id.container_resultado_presencial)
        btn_home = view.findViewById(R.id.btn_home)
        criaTabelaParticipantes()
    }
    private fun criaTabelaParticipantes() {
        // Criar uma lista embaralhada dos índices dos participantes
        val indicesEmbaralhados = participantesList.indices.shuffled()

        participantesList.forEachIndexed { index, participante ->
            val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_resultado_presencial, null)
            val ic_olho: ImageView = linhaParticipanteView.findViewById(R.id.ic_olho)
            val txtParticipante: TextView = linhaParticipanteView.findViewById(R.id.txt_participante)
            val blocoParticipante: LinearLayout = linhaParticipanteView.findViewById(R.id.bloco_participante)
            txtParticipante.text = participante.nomeParticipante

            ic_olho.setOnClickListener{
                participante.revelado = true
                val action = fragment_resultado_presencialDirections.fromFragmentResultadoPresencialToFragmentRevela("ENCRYPTED:${participante.nomeParticipante}/${participante.amigoSecreto}",getString(R.string.btn_sorteio_prencial))
                findNavController().navigate(action)
            }

            if(participante.revelado == true){
                ic_olho.setImageResource(R.drawable.ic_olho_fechado)
                blocoParticipante.setBackgroundResource(R.drawable.bk5_round)
                txtParticipante.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            // Obter o índice embaralhado correspondente
            val amigoSecretoIndex = indicesEmbaralhados[index]

            // Encontrar um amigo secreto não escolhido para o participante
            val amigoSecreto = participantesList[amigoSecretoIndex]

            // Atribuir e marcar como escolhido
            participante.amigoSecreto = amigoSecreto.nomeParticipante
            amigoSecreto.escolhido = true
            containerParticipantes.addView(linhaParticipanteView)
        }


    }
}