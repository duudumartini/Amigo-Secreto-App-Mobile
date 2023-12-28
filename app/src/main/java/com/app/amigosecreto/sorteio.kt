package com.app.amigosecreto
import java.time.LocalDateTime

class sorteio(
    var nomeSorteio: String,
    var qtdParticipantes: Int,
    var data: LocalDateTime,
    var tipoSorteio: String
) {
    var participantes: MutableList<participante> = mutableListOf()

}