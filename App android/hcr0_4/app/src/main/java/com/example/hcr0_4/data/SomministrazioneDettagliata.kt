package com.example.hcr0_4.data

import com.example.hcr0_4.TipoFrequenza
import java.time.LocalDate
import java.time.LocalTime

data class SomministrazioneDettagliata(
    val idTerapia: Int,
    val idSomministrazione: Int,
    val idPosologia: Int,
    val nomeTerapia: String,
    val tipo: String,
    val forma: String,
    val dose: Double,
    val dataSomministrazione: LocalDate,
    val oraSomministrazione: LocalTime,
    val statoSomministrazione: String, // Add this line
    val statoTerapia: String, // Add this line
    val dataPresa: LocalDate?, // Add this line
    val oraPresa: LocalTime?, // Add this line
    val frequenza: String?,
    val durata: Int?,
    val dataFine: LocalDate?,
    val giorniSettimana: String?,
    val tipiDurata: String,
    val aderenza: Int,
    val nonAderenza: Int,
    val ogniTotMin: Int?,
    val ogniTotOre: Int?,
    val ogniTotGiorni: Int?,
    val tipoAvviso: String,
    val dosePresa: Double,
    val formaPresa: String,
)