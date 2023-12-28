package com.app.amigosecreto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class fragment_resultado_presencial : Fragment(R.layout.fragment_resultado_presencial) {
    private lateinit var sharedViewModel: SharedViewModel
    private var participantesList = mutableListOf<participante>()
    private lateinit var containerParticipantes : LinearLayout
    private lateinit var btn_home: ImageView
    private var jaEmbaralhou: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        participantesList.clear()
        participantesList.addAll(sharedViewModel.listaParticipantes)
        containerParticipantes = view.findViewById(R.id.container_resultado_presencial)
        btn_home = view.findViewById(R.id.btn_home)

        inflarLinhasParticipantes()
    }
    private fun inflarLinhasParticipantes() {
        participantesList.forEach { participante ->
            val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_resultado_presencial, null)
            val ic_olho: ImageView = linhaParticipanteView.findViewById(R.id.ic_olho)
            val txtParticipante: TextView = linhaParticipanteView.findViewById(R.id.txt_participante)
            val blocoParticipante: LinearLayout = linhaParticipanteView.findViewById(R.id.bloco_participante)

            txtParticipante.text = participante.nomeParticipante

            ic_olho.setOnClickListener {
                participante.revelado = true
                val action = fragment_resultado_presencialDirections.fromFragmentResultadoPresencialToFragmentRevela("ENCRYPTED:${participante.nomeParticipante}/${participante.amigoSecreto}", getString(R.string.btn_sorteio_prencial))
                findNavController().navigate(action)
            }

            if (participante.revelado == true) {
                ic_olho.setImageResource(R.drawable.ic_olho_fechado)
                blocoParticipante.setBackgroundResource(R.drawable.bk5_round)
                txtParticipante.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            if (participante.embaralhado == false){
                var listaDeAmigosSecretos = mutableListOf<participante>()
                listaDeAmigosSecretos.addAll(participantesList.shuffled())
                for(amigo in listaDeAmigosSecretos){
                    if(amigo.nomeParticipante != participante.nomeParticipante && amigo.escolhido != true){
                        participante.amigoSecreto = amigo.nomeParticipante
                        amigo.escolhido = true
                        participante.embaralhado = true
                        break
                    }
                }
            }
            containerParticipantes.addView(linhaParticipanteView)
        }
    }
}