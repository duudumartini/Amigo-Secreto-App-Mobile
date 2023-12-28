package com.app.amigosecreto

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class fragment_participantes : Fragment(R.layout.fragment_participantes) {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var containerParticipantes : LinearLayout
    private lateinit var btn_adicionarParticipante: Button
    private lateinit var btn_realizarSorteio: Button
    private lateinit var txt_nome_sorteio: EditText
    private lateinit var txt_titulo: TextView
    private var participantesList = mutableListOf<participante>()
    private val args: fragment_participantesArgs by navArgs()
    private var tipoSorteio: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        containerParticipantes = view.findViewById(R.id.container_participantes)
        btn_adicionarParticipante = view.findViewById(R.id.btn_adicionar_participante)
        btn_realizarSorteio = view.findViewById(R.id.btn_realizar_sorteio)
        txt_nome_sorteio = view.findViewById(R.id.txt_nome_sorteio)
        txt_titulo = view.findViewById(R.id.txt_titulo_participantes_tipo_sorteio)
        val _tiposorteio = args.tipoSorteio
        verificaTipoSorteio(_tiposorteio)
        repeat(4) {
            adicionarParticipante()
        }
        setListener()
    }

    private fun verificaTipoSorteio(_tiposorteio: Boolean) {
        if(_tiposorteio == false){
            tipoSorteio = getString(R.string.btn_sorteio_prencial)
        }
        else{
            tipoSorteio = getString(R.string.btn_sorteio_online)
        }
        txt_titulo.text = tipoSorteio
    }

    private fun setListener() {
        btn_adicionarParticipante.setOnClickListener {
            adicionarParticipante()
        }

        btn_realizarSorteio.setOnClickListener {
            participantesList.clear()
            participantesList.addAll(criarListaDeParticipantes())

            if(txt_nome_sorteio.text.toString() != getString(R.string.txt_nome_sorteio)){
                if (participantesList.size >= 2) {

                    if (participantesList.size % 2 == 0) {
                        sharedViewModel.listaParticipantes.clear()
                        sharedViewModel.listaParticipantes.addAll(participantesList)
                        adicionaSorteioHistorico(participantesList)
                        if(tipoSorteio == getString(R.string.btn_sorteio_online)){
                            findNavController().navigate(R.id.from_fragment_participantes_online_to_fragment_resultado_online)
                        }else if(tipoSorteio == getString(R.string.btn_sorteio_prencial)){
                            findNavController().navigate(R.id.from_fragment_participantes_online_to_fragment_resultado_presencial)
                        }

                    } else {
                        sharedViewModel.exibirAlertDialog(requireContext(),"É necessário que haja um número par de participantes para que todos possam participar.")
                    }
                } else {
                    sharedViewModel.exibirAlertDialog(requireContext(),"É necessário contar com dois ou mais participantes para realizar o sorteio.")
                }
            }
            else{
                sharedViewModel.exibirAlertDialog(requireContext(),"Insira o nome do sorteio.")
            }
        }

        txt_nome_sorteio.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && txt_nome_sorteio.text.toString() == getString(R.string.txt_nome_sorteio)) {
                txt_nome_sorteio.setText("")
            }
            if (!hasFocus && txt_nome_sorteio.text.isBlank()) {
                txt_nome_sorteio.setText(getString(R.string.txt_nome_sorteio))
            }
        }

    }


    private fun adicionarParticipante() {
        val linhaParticipanteView: View = layoutInflater.inflate(R.layout.linha_participante, null)

        val btnExcluirLinha: ImageButton = linhaParticipanteView.findViewById(R.id.btn_excluir_linha)
        btnExcluirLinha.setOnClickListener {
            if(containerParticipantes.childCount >2){
                excluirLinha(it)
            }
            else{
                sharedViewModel.exibirAlertDialog(requireContext(),"É necessário contar com dois ou mais participantes.")
            }
        }

        val txtParticipante: EditText = linhaParticipanteView.findViewById(R.id.txt_participante)
        txtParticipante.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && txtParticipante.text.toString() == getString(R.string.txt_nome_participante) ) {
                txtParticipante.setText("")
            }
            if (!hasFocus && txtParticipante.text.isBlank()) {
                txtParticipante.setText(getString(R.string.txt_nome_participante))
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

    private fun adicionaSorteioHistorico(participantes: MutableList<participante>){
        val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val dataEHoraAtualString = LocalDateTime.now().format(formato)
        val sorteioAtual = sorteio(
            txt_nome_sorteio.text.toString(),
            sharedViewModel.listaParticipantes.size,
            LocalDateTime.parse(dataEHoraAtualString, formato),
            tipoSorteio
        )
        sorteioAtual.participantes.addAll(participantes)
        sharedViewModel.listaHistorico.add(sorteioAtual)
    }
}