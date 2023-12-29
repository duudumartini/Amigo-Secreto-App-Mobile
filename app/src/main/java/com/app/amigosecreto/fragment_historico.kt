package com.app.amigosecreto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
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
            sharedViewModel.exibirAlertDialogSimNao(requireContext(),"Deseja apagar o histórico de sorteios?\n\nEsta ação é irreversível!"){resposta ->
                if(resposta){
                    sharedViewModel.listaHistorico.clear()
                    sharedViewModel.salvarListaHistoricoNoArquivo(requireContext())
                    atualizaHistorico()
                    containerHistorico.removeAllViews()
                }
            }
        }
    }

    fun atualizaHistorico() {
        sharedViewModel.carregarListaHistoricoDoArquivo(requireContext())
        for (historicoItem in sharedViewModel.listaHistorico) {
            val linhaSorteioHistorico: View = layoutInflater.inflate(R.layout.linha_sorteio_historico, null)
            val btn_verSorteio: LinearLayout = linhaSorteioHistorico.findViewById(R.id.linha_participante)
            val txt_nomeSorteio: TextView = linhaSorteioHistorico.findViewById(R.id.txt_nome_sorteio_historico)
            val txt_data: TextView = linhaSorteioHistorico.findViewById(R.id.txt_data_historico)
            val txt_qtdParticipantes: TextView = linhaSorteioHistorico.findViewById(R.id.txt_qtd_participante)
            val txt_tipoSorteio: TextView = linhaSorteioHistorico.findViewById(R.id.txt_tipo_sorteio)

            txt_nomeSorteio.text = historicoItem.nomeSorteio
            txt_qtdParticipantes.text = "${historicoItem.qtdParticipantes} Participantes"
            txt_data.text = historicoItem.dataHora
            txt_tipoSorteio.text = historicoItem.tipoSorteio

            val navigateToResultadoOnline = { _: View ->
                sharedViewModel.listaParticipantes.clear()
                sharedViewModel.listaParticipantes.addAll(historicoItem.participantes)
                if(historicoItem.tipoSorteio == getString(R.string.btn_sorteio_online)){
                    findNavController().navigate(R.id.from_fragment_historico_to_fragment_resultado_online)
                }
                else{
                    findNavController().navigate(R.id.from_fragment_historico_to_fragment_resultado_presencial)
                }
            }

            btn_verSorteio.setOnClickListener(navigateToResultadoOnline)
            txt_data.setOnClickListener(navigateToResultadoOnline)
            txt_nomeSorteio.setOnClickListener(navigateToResultadoOnline)
            txt_tipoSorteio.setOnClickListener(navigateToResultadoOnline)
            txt_qtdParticipantes.setOnClickListener(navigateToResultadoOnline)

            containerHistorico.addView(linhaSorteioHistorico)
        }
    }


}