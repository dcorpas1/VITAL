package com.vital.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class requerida por Hilt para inicializar el grafo de dependencias.
 * Debe declararse en AndroidManifest.xml con android:name=".VitalApplication"
 */
@HiltAndroidApp
class VitalApplication : Application()
