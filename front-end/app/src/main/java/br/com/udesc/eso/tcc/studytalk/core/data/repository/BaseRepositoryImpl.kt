package br.com.udesc.eso.tcc.studytalk.core.data.repository


open class BaseRepositoryImpl {

//    @Inject
//    lateinit private var connectivityManager: ConnectivityManager

    protected fun isOnline(): Boolean {
//        if (connectivityManager.activeNetworkInfo?.isConnected == true) {
//            return true
//        }
//        return false
        return true
    }

}