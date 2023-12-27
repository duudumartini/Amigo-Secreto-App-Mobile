package com.app.amigosecreto

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
class frament_participantes_online : Fragment(R.layout.fragment_frament_participantes_online) {

    private lateinit var containerParticipantes : LinearLayout
    private lateinit var btn_adicionarParticipante: Button
    private lateinit var btn_realizarSorteio: Button
    private var participantesList = mutableListOf<participante>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerParticipantes = view.findViewById(R.id.container_participantes)
        btn_adicionarParticipante = view.findViewById(R.id.btn_adicionar_participante)
        btn_realizarSorteio = view.findViewById(R.id.btn_realizar_sorteio)

        repeat(4) {
            adicionarParticipante()
        }


        setListener()
    }

    private fun setListener() {
        btn_adicionarParticipante.setOnClickListener {
            adicionarParticipante()
        }
        btn_realizarSorteio.setOnClickListener {
            participantesList.clear()
            participantesList.addAll(criarListaDeParticipantes())
        }

    }


    private fun adicionarParticipante() {
        val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_participante, null)

        val btnExcluirLinha: ImageButton = linhaParticipanteView.findViewById(R.id.btn_excluir_linha)
        btnExcluirLinha.setOnClickListener {
            if(containerParticipantes.childCount >1){
                excluirLinha(it)
            }
            else{
                Toast.makeText(requireContext(), "Não é possível excluir todos os participantes!", Toast.LENGTH_SHORT).show()
            }
        }

        val txtParticipante: EditText = linhaParticipanteView.findViewById(R.id.txt_participante)
        txtParticipante.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && txtParticipante.text.toString() == getString(R.string.txt_nome_participante) ) {
                txtParticipante.setText("")
            }
            if (!hasFocus && txtParticipante.text.isBlank()) {
                txtParticipante.setText(getString(R.string.txt_nome_participante))  // Definir o texto padrão ao perder o foco se estiver vazio
            }
        }
        containerParticipantes.addView(linhaParticipanteView)
    }

    private fun excluirLinha(view: View){
        val parentView = view.parent as View
        containerParticipantes.removeView(parentView)
    }

    private fun criarListaDeParticipantes(): List<participante> {
        val listaParticipantes = mutableListOf<participante>()

        for (i in 0 until containerParticipantes.childCount) {
            val linhaParticipanteView = containerParticipantes.getChildAt(i)

            if (linhaParticipanteView is LinearLayout) {
                val txtParticipante = linhaParticipanteView.findViewById<EditText>(R.id.txt_participante)
                val nomeParticipante = txtParticipante.text.toString()

                // Criar uma instância de Participante e adicionar à lista
                if (nomeParticipante != getString(R.string.txt_nome_participante)) {
                    val participante = participante()
                    participante.nomeParticipante = nomeParticipante
                    listaParticipantes.add(participante)
                }
            }
        }
        return listaParticipantes
    }

}