package me.jiangyb.netmonitor.network.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 网络变化广播，Android N以下的设备使用
 */
internal class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.getSystemService(Context.CONNECTIVITY_SERVICE)?.also {
            val cm = it as ConnectivityManager
            cm.activeNetworkInfo?.also {
                it.type
                it.isConnected
            }
        }
    }
}