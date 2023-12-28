package com.app.amigosecreto

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Base64

class SharedViewModel: ViewModel() {
    val listaParticipantes: MutableList<participante> = mutableListOf()
    var listaHistorico: MutableList<sorteio> = mutableListOf()

    fun criptografar(texto: String): String {
        val chave = "00101010"
        val chaveInt = Integer.parseInt(chave, 2)
        val textoParaCriptografar = "ENCRYPTED:$texto"
        val bytes = textoParaCriptografar.toByteArray()
        val bytesCriptografados = bytes.map { (it + chaveInt).toByte() }.toByteArray()

        return Base64.getEncoder().encodeToString(bytesCriptografados)
    }

    fun descriptografar(textoCriptografado: String): String {
        val chave = "00101010"
        val chaveInt = Integer.parseInt(chave, 2)

        val bytesCriptografados = Base64.getDecoder().decode(textoCriptografado)
        val bytesDescriptografados = bytesCriptografados.map { (it - chaveInt).toByte() }.toByteArray()
        return String(bytesDescriptografados)
    }

    fun isTextoCriptografado(texto: String): Boolean {
        return texto.startsWith("ENCRYPTED:")
    }
    fun exibirAlertDialog(context: Context, mensagem: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.alert_dialog, null)
        val textViewMessage = view.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = mensagem
        alertDialogBuilder.setView(view)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val buttonOK = view.findViewById<Button>(R.id.buttonOK)
        buttonOK.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}