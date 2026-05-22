package com.learning.agentic_coding

import android.app.Application

class SaaApplication : Application() {
    lateinit var services: ServiceLocator
        private set

    override fun onCreate() {
        super.onCreate()
        services = ServiceLocator(applicationContext = this)
    }
}
