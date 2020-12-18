package me.jiangyb.netmonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.jiangyb.netmonitor.annotations.NetworkSubscribe
import me.jiangyb.netmonitor.mode.NetworkMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetMonitor.register(this)
    }

    @NetworkSubscribe(NetworkMode.MOBILE_CONNECT)
    fun onNetworkChanged() {
        Log.i("MainActivity", "onNetworkChanged");
    }

    @NetworkSubscribe(NetworkMode.WIFI_CONNECT, NetworkMode.WIFI_DISCONNECT)
    fun onNetworkChanged1(mode: NetworkMode) {
        Log.i("MainActivity", "onNetworkChanged1 mode=${mode}");
    }

    @NetworkSubscribe(NetworkMode.MOBILE_CONNECT, NetworkMode.MOBILE_DISCONNECT)
    fun onNetworkChanged2(mode: NetworkMode) {
        Log.i("MainActivity", "onNetworkChanged2 mode=${mode}");
    }

    @NetworkSubscribe(NetworkMode.NONE)
    fun onNetworkChanged3(mode: NetworkMode) {
        Log.i("MainActivity", "onNetworkChanged3 mode=${mode}")
    }

    override fun onDestroy() {
        super.onDestroy()
        NetMonitor.unregister(this)
    }
}