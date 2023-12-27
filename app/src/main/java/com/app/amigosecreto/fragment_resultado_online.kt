package com.app.amigosecreto

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider


class fragment_resultado_online : Fragment(R.layout.fragment_resultado_online) {
    private lateinit var sharedViewModel: SharedViewModel
    private var participantesList = mutableListOf<participante>()
    private lateinit var containerParticipantes : LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        participantesList.addAll(sharedViewModel.listaParticipantes)
        containerParticipantes = view.findViewById(R.id.container_resultado_online)

        criaTabelaParticipantes()
    }

    private fun criaTabelaParticipantes() {
        // Criar uma lista embaralhada dos índices dos participantes
        val indicesEmbaralhados = participantesList.indices.shuffled()

        participantesList.forEachIndexed { index, participante ->
            val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_resultado_online, null)
            val btnCopiarCodigo: ImageButton = linhaParticipanteView.findViewById(R.id.btn_copiar_codigo)
            val btnCompartilharCodigo: ImageButton = linhaParticipanteView.findViewById(R.id.btn_compartilhar_codigo)
            val txtParticipante: TextView = linhaParticipanteView.findViewById(R.id.txt_participante)
            txtParticipante.text = participante.nomeParticipante

            // Obter o índice embaralhado correspondente
            val amigoSecretoIndex = indicesEmbaralhados[index]

            // Encontrar um amigo secreto não escolhido para o participante
            val amigoSecreto = participantesList[amigoSecretoIndex]

            // Atribuir e marcar como escolhido
            participante.amigoSecreto = amigoSecreto.nomeParticipante
            amigoSecreto.escolhido = true

            btnCopiarCodigo.setOnClickListener{
                val stringAmigoSecreto = "${participante.nomeParticipante}/${participante.amigoSecreto}"
                val codigoAmigoSecreto = sharedViewModel.criptografar(stringAmigoSecreto, sharedViewModel.gerarChave())
                val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Código do Amigo Secreto", codigoAmigoSecreto)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(requireContext(), "Amigo Secreto do participante ${participante.nomeParticipante} copiado!", Toast.LENGTH_SHORT).show()
            }
            btnCompartilharCodigo.setOnClickListener {
                val stringAmigoSecreto = "${participante.nomeParticipante}/${participante.amigoSecreto}"
                val codigoAmigoSecreto = sharedViewModel.criptografar(stringAmigoSecreto, sharedViewModel.gerarChave())

                // Criar um Intent para compartilhamento
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, codigoAmigoSecreto)

                // Iniciar a atividade de compartilhamento
                startActivity(Intent.createChooser(intent, "Compartilhar código do Amigo Secreto"))
            }


            containerParticipantes.addView(linhaParticipanteView)
        }


    }
    
}