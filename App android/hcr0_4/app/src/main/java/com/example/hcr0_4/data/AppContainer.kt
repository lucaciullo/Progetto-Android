package com.example.hcr0_4.data


import android.content.Context

interface AppContainer {
    val cartellaRepository: CartelleRepository
    val refertoRepository: RefertiRepository
    val terapiaRepository: TerapieRepository
    val posologiaRepository: PosologieRepository
    val somministrazioneRepository: SomministrazioniRepository
    val parametroVitaleRepository: ParametriVitaliRepository
    val misurazioneRepository: MisurazioniRepository

}

class AppDataContainer(private val context: Context) : AppContainer {
    override val cartellaRepository: CartelleRepository by lazy {
        CartelleRepositoryOffline(DatabaseHCR.getDatabase(context).cartellaDao())
    }
    override val refertoRepository: RefertiRepository by lazy {
        RefertiRepositoryOffline(DatabaseHCR.getDatabase(context).refertoDao())
    }
    override val terapiaRepository: TerapieRepository by lazy {
        TerapieRepositoryOffline(DatabaseHCR.getDatabase(context).terapiaDao())
    }
    override val posologiaRepository: PosologieRepository by lazy {
        PosologieRepositoryOffline(DatabaseHCR.getDatabase(context).posologiaDao())
    }
    override val somministrazioneRepository: SomministrazioniRepository by lazy {
        SomministrazioniRepositoryOffline(DatabaseHCR.getDatabase(context).somministrazioneDao())
    }
    override val parametroVitaleRepository: ParametriVitaliRepository by lazy {
        ParametriVitaliRepositoryOffline(DatabaseHCR.getDatabase(context).parametroVitaleDao())
    }
    override val misurazioneRepository: MisurazioniRepository by lazy {
        MisurazioniRepositoryOffline(DatabaseHCR.getDatabase(context).misurazioneDao())
    }



}