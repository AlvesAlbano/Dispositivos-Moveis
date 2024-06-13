package com.example.appespacoculturalkotlin

data class HorarioItem(
    val diaSemana: String,
    val horaInicio: String,
    val horaTermino: String,
    var pessoasInteressadas: Long,
    var interesseConfirmado: Boolean = false // Adicionamos a variável interesseConfirmado
)
