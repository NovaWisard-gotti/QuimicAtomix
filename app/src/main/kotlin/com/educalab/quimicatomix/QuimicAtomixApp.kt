package com.educalab.quimicatomix

import android.app.Application

class QuimicAtomixApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
