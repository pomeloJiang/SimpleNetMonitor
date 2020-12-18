package me.jiangyb.netmonitor

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import me.jiangyb.netmonitor.exceptions.NetMonitorException
import me.jiangyb.netmonitor.network.listeners.NetworkCallbackImpl
import me.jiangyb.netmonitor.network.listeners.NetworkReceiver
import me.jiangyb.netmonitor.poster.NetworkHandler
import me.jiangyb.netmonitor.poster.NetworkPoster
import me.jiangyb.netmonitor.poster.Subscription

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description NetMonitor可以帮助简化网络监听，类似于EventBus的使用方式
 */
object NetMonitor {
    private var isInit: Boolean = false
    lateinit var mApplication: Application
        private set
    private var mConnectivityManager: ConnectivityManager? = null

    fun init(application: Application) {
        this.mApplication = application
        val handler = NetworkHandler()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                this.mConnectivityManager =
                    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkBuilder = NetworkRequest.Builder();
                NETWORK_TRANSPORTS.forEach {
                    networkBuilder.addTransportType(it)
                }
                val networkRequest = networkBuilder.build()
                mConnectivityManager?.registerNetworkCallback(
                    networkRequest,
                    NetworkCallbackImpl(handler)
                )
            }
            else -> {
                val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                mApplication.registerReceiver(NetworkReceiver(), filter)
            }
        }
        this.isInit = true
    }

    /**
     * 网络注册
     * @param subscriber 订阅者
     */
    fun register(subscriber: Any) {
        if (!isInit) {
            throw NetMonitorException("register method must be called after init!")
        }

        val subscriberClass = subscriber.javaClass
        val subscriberMethods =
            SubscriberMethodCollector.get().findSubscribeMethods(subscriberClass)
        NetworkPoster.registerSubscription(Subscription(subscriber, subscriberMethods))
    }

    fun unregister(subscriber: Any) {
        if (!isInit) {
            throw NetMonitorException("unregister method must be called after init!")
        }
        NetworkPoster.unRegisterSubscription(subscriber)
    }
}