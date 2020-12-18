package me.jiangyb.netmonitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import me.jiangyb.netmonitor.annotations.NetworkSubscribe
import me.jiangyb.netmonitor.mode.NetworkMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetMonitor.register(this)
        second_activity.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        }
    }

    @NetworkSubscribe(NetworkMode.MOBILE_CONNECT)
    fun onNetworkChanged() {
        Log.i("MainActivity", "onNetworkChanged");
    }

    @NetworkSubscribe(NetworkMode.WIFI_CONNECT, NetworkMode.WIFI_DISCONNECT)
    fun onNetworkChanged1(mode: NetworkMode) {
        Log.i("MainActivity", "onNetworkChanged1 mode=${mode}");
    }

    @NetworkSubscribe(NetworkMode.WIFI_CONNECT, NetworkMode.WIFI_DISCONNECT)
    fun onNetworkChanged2(mode: NetworkMode) {
        Log.i("MainActivity", "onNetworkChanged2 mode=${mode}");
    }

    @NetworkSubscribe(NetworkMode.NONE)
    fun onNetworkChanged3(mode: NetworkMode) {
        Log.i("jiangyb","onNetworkChanged3 mode=${mode}")
    }

    override fun onDestroy() {
        super.onDestroy()
        NetMonitor.unregister(this)
    }
}