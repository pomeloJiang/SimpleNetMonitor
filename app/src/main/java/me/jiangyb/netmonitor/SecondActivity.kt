package me.jiangyb.netmonitor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*
import me.jiangyb.netmonitor.poster.NetworkPoster
import me.jiangyb.netmonitor.mode.NetworkMode

/**
 * @author jiangyb
 * @date 2020/12/14
 * @description TODO
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        NetworkPoster.post(NetworkMode.MOBILE_CONNECT);
        main_activity.setOnClickListener {
            this@SecondActivity.finish()
        }
    }
}