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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class fragment_resultado_online : Fragment(R.layout.fragment_resultado_online) {
    private lateinit var sharedViewModel: SharedViewModel
    private var participantesList = mutableListOf<participante>()
    private lateinit var containerParticipantes : LinearLayout
    private lateinit var btn_home: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        participantesList.addAll(sharedViewModel.listaParticipantes)
        containerParticipantes = view.findViewById(R.id.container_resultado_online)
        btn_home = view.findViewById(R.id.btn_home)

        criaTabelaParticipantes()
        btn_home.setOnClickListener{
            findNavController().navigate(R.id.from_fragment_resultado_online_to_fragment_main)
        }
    }


    private fun criaTabelaParticipantes() {
        participantesList.forEachIndexed { index, participante ->
            val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_resultado_online, null)
            val btnCopiarCodigo: ImageButton = linhaParticipanteView.findViewById(R.id.btn_copiar_codigo)
            val btnCompartilharCodigo: ImageButton = linhaParticipanteView.findViewById(R.id.btn_compartilhar_codigo)
            val txtParticipante: TextView = linhaParticipanteView.findViewById(R.id.txt_participante)
            txtParticipante.text = participante.nomeParticipante

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

            btnCopiarCodigo.setOnClickListener{
                val stringAmigoSecreto = "${participante.nomeParticipante}/${participante.amigoSecreto}"
                val codigoAmigoSecreto = sharedViewModel.criptografar(stringAmigoSecreto)
                val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Código do Amigo Secreto", codigoAmigoSecreto)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(requireContext(), "Amigo Secreto do participante ${participante.nomeParticipante} copiado!", Toast.LENGTH_SHORT).show()
            }
            btnCompartilharCodigo.setOnClickListener {
                val stringAmigoSecreto = "${participante.nomeParticipante}/${participante.amigoSecreto}"
                val codigoAmigoSecreto = sharedViewModel.criptografar(stringAmigoSecreto)

                // Criar um Intent para compartilhamento
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, codigoAmigoSecreto)

                // Iniciar a atividade de compartilhamento
                startActivity(Intent.createChooser(intent, "Compartilhar código do Amigo Secreto"))
            }
            containerParticipantes.addView(linhaParticipanteView)
        }
        sharedViewModel.salvarListaHistoricoNoArquivo(requireContext())
    }
}