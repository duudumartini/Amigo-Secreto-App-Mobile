package com.app.amigosecreto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import java.time.format.DateTimeFormatter

class fragment_historico : Fragment(R.layout.fragment_historico) {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var containerHistorico : LinearLayout
    private lateinit var btn_limparHistorico: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        containerHistorico = view.findViewById(R.id.container_historico_sorteios)
        btn_limparHistorico = view.findViewById(R.id.btn_limpar_historico)
        atualizaHistorico()

        btn_limparHistorico.setOnClickListener {
            sharedViewModel.listaHistorico.clear()
            atualizaHistorico()
            containerHistorico.removeAllViews()
        }
    }

    fun atualizaHistorico(){
        for (historicoItem in sharedViewModel.listaHistorico) {
            val linhaSorteioHistorico: View = layoutInflater.inflate(R.layout.linha_sorteio_historico, null)
            val btn_verSorteio: LinearLayout = linhaSorteioHistorico.findViewById(R.id.btn_ver_sorteio)
            val txt_nomeSorteio: TextView = linhaSorteioHistorico.findViewById(R.id.txt_nome_sorteio_historico)
            val txt_data: TextView = linhaSorteioHistorico.findViewById(R.id.txt_data_historico)
            val txt_qtdParticipantes: TextView = linhaSorteioHistorico.findViewById(R.id.txt_qtd_participante)
            val txt_tipoSorteio: TextView = linhaSorteioHistorico.findViewById(R.id.txt_tipo_sorteio)
            val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss")
            val dataFormatada = historicoItem.data.format(formato)

            txt_nomeSorteio.text = historicoItem.nomeSorteio
            txt_qtdParticipantes.text = "${historicoItem.qtdParticipantes} Participantes"
            txt_data.text = dataFormatada
            txt_tipoSorteio.text = historicoItem.tipoSorteio

            btn_verSorteio.setOnClickListener{
                if(historicoItem.tipoSorteio == getString(R.string.btn_sorteio_online)){
                    sharedViewModel.listaParticipantes.clear()
                    sharedViewModel.listaParticipantes.addAll(historicoItem.participantes)
                    findNavController().navigate(R.id.from_fragment_historico_to_fragment_resultado_online)
                }
            }
            containerHistorico.addView(linhaSorteioHistorico)
        }
    }
}