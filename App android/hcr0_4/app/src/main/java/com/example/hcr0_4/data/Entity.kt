package com.example.hcr0_4.data


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Entity(tableName = "Cartelle")
data class Cartella(
    @PrimaryKey(autoGenerate = true)
    val idCartella: Int = 1,
    val nome: String
)

@Entity(tableName = "Referti",
    foreignKeys = [
        ForeignKey(entity = Cartella::class,
            parentColumns = arrayOf("idCartella"),
            childColumns = arrayOf("idCartella"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Referto (
    @PrimaryKey(autoGenerate = true)
    val idReferto: Int = 1,
    val idCartella: Int? = null, //foreign key
    val percorsoFfile: String,
    val nomeFile: String
)

@Entity(tableName = "Terapie",
    foreignKeys = [ForeignKey(entity = Referto::class,
    parentColumns = arrayOf("idReferto"),
    childColumns = arrayOf("idReferto"))]
)
data class Terapia(
    @PrimaryKey(autoGenerate = true)
    val idTerapia: Int = 1,
    val idReferto: Int? = null, //foreign key
    val tipo: String, //enum (farmacologica, esercizio fisico, dieta, ecc)
    val nomeTerapia: String,
    val statoTerapia: String, //enum (attiva, disattiva)
    val dataInizio: LocalDate,
    val dataFine: LocalDate,
    val tipoAvviso: String, //enum (sveglia, notifica, off)
    val forma: String, //enum (compresse, sciroppo, iniezioni, ecc)
    val frequenza: String, //enum (giornaliera, settimanale, personalizzata)
    val durata: Int, //enum numero della variabile sotto
    val tipiDurata: String, //enum (data fine, numero somministrazioni, numero giorni)
    val aderenza: Int,
    val nonAderenza: Int,
    val ogniTotMin: Int,
    val ogniTotOre: Int,
    val ogniTotGiorni: Int,
    val personalizzata: Boolean

    )


@Entity(tableName = "Posologie",
    foreignKeys = [
        ForeignKey(
            entity = Terapia::class,
            parentColumns = arrayOf("idTerapia"),
            childColumns = arrayOf("idTerapia"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Posologia(
    @PrimaryKey(autoGenerate = true)
    val idPosologia: Int = 1,
    var idTerapia: Int, //foreign key
    val dose: Double,
    val giorniSettimana: String,
    val oraPosologia: LocalTime
)

@Entity(tableName = "Somministrazioni",
    foreignKeys = [
        ForeignKey(
            entity = Posologia::class,
            parentColumns = arrayOf("idPosologia"),
            childColumns = arrayOf("idPosologia"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Somministrazione(
    @PrimaryKey(autoGenerate = true)
    val idSomministrazione: Int = 0,
    val idPosologia: Int, //foreign key
    val dataSomministrazione: LocalDate,
    val oraSomministrazione: LocalTime,
    val statoSomministrazione: String, //enum (completata, programmata, in corso, saltata, disattiva)
    val dataPresa: LocalDate?,
    val oraPresa: LocalTime?,
    val formaPresa: String,
    val dosePresa: Double
)


@Entity(tableName = "ParametriVitali")
data class ParametroVitale(
    @PrimaryKey(autoGenerate = true)
    val idParametro: Int = 1,
    val nomeParametro: String,
    val unitaMisura: String,
    val valoriAccumulati: Boolean
)

@Entity(tableName = "Misurazioni",
    foreignKeys = [
        ForeignKey(
            entity = ParametroVitale::class,
            parentColumns = arrayOf("idParametro"),
            childColumns = arrayOf("idParametro"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Misurazione(
    @PrimaryKey(autoGenerate = true)
    val idMisurazione: Int = 1,
    val idParametro: Int, //foreign key
    val valore: Double,
    val data: LocalDate,
    val ora: LocalTime
)