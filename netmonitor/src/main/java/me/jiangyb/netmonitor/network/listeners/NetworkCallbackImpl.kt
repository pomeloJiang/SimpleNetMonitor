package me.jiangyb.netmonitor.network.listeners

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import me.jiangyb.netmonitor.NetMonitor
import me.jiangyb.netmonitor.poster.NetworkHandler

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 网络回调，配合{@see #ConnectivityManager}使用
 */
internal class NetworkCallbackImpl(private val handler: NetworkHandler) :
    ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.i("jiangyb", "NetworkCallbackImpl#onAvailable")
        handler.postAvailable(network)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Log.i("jiangyb", "NetworkCallbackImpl#onLinkPropertiesChanged")
        handler.postLinkChanged(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        handler.postLost(network)
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Log.i("jiangyb", "NetworkCallbackImpl#onLosing")
    }
}