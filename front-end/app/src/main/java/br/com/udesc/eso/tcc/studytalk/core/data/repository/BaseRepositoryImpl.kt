package br.com.udesc.eso.tcc.studytalk.core.data.repository

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject


open class BaseRepositoryImpl {

    protected fun isOnline(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =  connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null
    }

}