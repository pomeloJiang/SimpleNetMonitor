package me.jiangyb.netmonitor

import android.app.Application
import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * @author jiangyb
 * @date 2020/12/14
 * @description TODO
 */
class MonitorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetMonitor.init(this)
    }
}