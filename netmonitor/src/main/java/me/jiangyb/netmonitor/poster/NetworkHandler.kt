package me.jiangyb.netmonitor.poster

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import me.jiangyb.netmonitor.NetMonitor
import me.jiangyb.netmonitor.getNetType23
import me.jiangyb.netmonitor.getNetType23Lower
import me.jiangyb.netmonitor.isNetworkAvailable
import me.jiangyb.netmonitor.mode.NetworkMode
import me.jiangyb.netmonitor.mode.NetworkType


/**
 * @author jiangyb
 * @date 2020/12/14
 * @description 处理网络连接回调时的网络类型
 */
class NetworkHandler {
    private var cm: ConnectivityManager? = null

    init {
        cm =
            NetMonitor.mApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    }

    fun postAvailable(network: Network) {
        when (getNetType(network)) {
            NetworkType.MOBILE -> {
                Log.i("jiangyb", "NetworkHandler#NetworkMode.MOBILE_CONNECT")
                NetworkPoster.post(NetworkMode.MOBILE_CONNECT)
            }
            NetworkType.WIFI -> {
                Log.i("jiangyb", "NetworkHandler#NetworkMode.WIFI_CONNECT")
                NetworkPoster.post(NetworkMode.WIFI_CONNECT)
            }
            NetworkType.NONE -> {
            }
        }
    }

    fun postLinkChanged(network: Network) {

    }

    fun postLost(network: Network) {
        val networkType = getNetType(network)
        when {
            !isNetworkAvailable(network) -> {
                NetworkPoster.post(NetworkMode.NONE)
                Log.i("jiangyb", "网络断连");
            }
            networkType == NetworkType.MOBILE -> {
                NetworkPoster.post(NetworkMode.MOBILE_DISCONNECT)
                Log.i("jiangyb", "postLost#MOBILE_DISCONNECT");
            }
            networkType == NetworkType.WIFI -> {
                NetworkPoster.post(NetworkMode.WIFI_DISCONNECT)
                Log.i("jiangyb", "postLost#WIFI_DISCONNECT");
            }
        }
    }

    private fun getNetType(network: Network): NetworkType {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm?.getNetworkCapabilities(network)
            getNetType23(networkCapabilities)
        } else {
            getNetType23Lower(cm)
        }
    }
}