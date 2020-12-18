package me.jiangyb.netmonitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import me.jiangyb.netmonitor.mode.NetworkType

/**
 * @author jiangyb
 * @date 2020/12/14
 * @description 全局变量
 */
val NETWORK_TRANSPORTS =
    intArrayOf(NetworkCapabilities.TRANSPORT_CELLULAR, NetworkCapabilities.TRANSPORT_WIFI)

internal fun isNetworkAvailable(network: Network): Boolean {
    // 获取网络连接管理器
    val cm =
        NetMonitor.mApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = cm?.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val networkInfo = cm?.activeNetworkInfo ?: return false
        return networkInfo.run {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                else -> false
            }
        }
    }
}

/**
 * 获取网络类型
 * @see me.jiangyb.netmonitor.mode.NetworkType
 *
 * @param capabilities
 * @return NetworkType网络类型
 */
@RequiresApi(Build.VERSION_CODES.M)
internal fun getNetType23(capabilities: NetworkCapabilities?): NetworkType = when {
    capabilities == null -> {
        NetworkType.NONE
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
        NetworkType.WIFI
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
        NetworkType.MOBILE
    }
    else -> {
        NetworkType.NONE
    }
}

/**
 * 获取网络类型
 * @see me.jiangyb.netmonitor.mode.NetworkType
 * @see Build.VERSION_CODES.LOLLIPOP_MR1 以下调用
 *
 * @param cm
 * @return NetType网络类型
 */
internal fun getNetType23Lower(cm: ConnectivityManager?): NetworkType {
    if (cm == null) {
        return NetworkType.NONE
    }
    val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    return if (wifi != null && wifi.isAvailable && wifi.isConnectedOrConnecting) {
        NetworkType.WIFI
    } else if (mobile != null && mobile.isAvailable && mobile.isConnectedOrConnecting) {
        NetworkType.MOBILE
    } else {
        NetworkType.NONE
    }
}
