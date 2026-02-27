package com.example.hcr0_4

import android.app.Application
import com.example.hcr0_4.data.AppContainer
import com.example.hcr0_4.data.AppDataContainer
import com.example.hcr0_4.data.DatabaseHCR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HcrApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        initializeDatabase()
    }

    private fun initializeDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseHCR.getDatabase(this@HcrApplication).initializeDatabase(this@HcrApplication)
        }
    }
}