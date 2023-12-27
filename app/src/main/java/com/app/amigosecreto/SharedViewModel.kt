package com.app.amigosecreto

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
class SharedViewModel: ViewModel() {
    public val listaParticipantes: MutableList<participante> = mutableListOf()

    public fun gerarChave(): Key {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128) // Tamanho da chave em bits
        return keyGenerator.generateKey()
    }

    public fun criptografar(texto: String, chave: Key): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, chave)
        val textoCriptografado = cipher.doFinal(texto.toByteArray())
        return Base64.getEncoder().encodeToString(textoCriptografado)
    }
    public fun descriptografar(textoCriptografado: String, chave: Key): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, chave)
        val textoDescriptografado = cipher.doFinal(Base64.getDecoder().decode(textoCriptografado))
        return String(textoDescriptografado)
    }

    public fun exibirAlertDialog(context: Context, mensagem: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.alert_dialog, null)

        val textViewMessage = view.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = mensagem

        alertDialogBuilder.setView(view)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        // Defina qualquer lógica adicional para o botão OK, se necessário
        val buttonOK = view.findViewById<Button>(R.id.buttonOK)
        buttonOK.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}