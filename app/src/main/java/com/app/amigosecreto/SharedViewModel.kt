package com.app.amigosecreto

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun isTextoCriptografado(texto: CharSequence): Boolean {
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

    fun exibirAlertDialogSimNao(context: Context, mensagem: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.alert_dialog_sim_nao, null)
        val textViewMessage = view.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = mensagem
        alertDialogBuilder.setView(view)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        val btnSim = view.findViewById<Button>(R.id.btn_sim)
        val btnNao = view.findViewById<Button>(R.id.btn_nao)

        btnNao.setOnClickListener {
            alertDialog.dismiss()
            callback(false)
        }

        btnSim.setOnClickListener {
            alertDialog.dismiss()
            callback(true)
        }
    }

    fun salvarListaHistoricoNoArquivo(context: Context) {
        val mPrefs: SharedPreferences = context.getSharedPreferences("NOME_DO_ARQUIVO", MODE_PRIVATE)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(listaHistorico)
        prefsEditor.putString("HISTORICO", json)
        prefsEditor.apply()
    }

    fun carregarListaHistoricoDoArquivo(context: Context) {
        val mPrefs: SharedPreferences = context.getSharedPreferences("NOME_DO_ARQUIVO", MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString("HISTORICO", "")
        val type = object : TypeToken<List<sorteio>?>() {}.type
        listaHistorico = (gson.fromJson<List<sorteio>>(json, type) ?: mutableListOf()).toMutableList()
    }

}