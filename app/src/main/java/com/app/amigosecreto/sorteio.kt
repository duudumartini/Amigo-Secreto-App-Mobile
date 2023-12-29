package com.app.amigosecreto

class sorteio(
    var nomeSorteio: String,
    var qtdParticipantes: Int,
    var dataHora: String,
    var tipoSorteio: String
) {
    var participantes: MutableList<participante> = mutableListOf()
}