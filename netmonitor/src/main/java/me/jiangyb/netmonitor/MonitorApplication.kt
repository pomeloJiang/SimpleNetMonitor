package me.jiangyb.netmonitor

import android.annotation.SuppressLint
import android.app.Application
import java.lang.Exception
import java.lang.RuntimeException

internal object MonitorApplication {

    private var mApplication: Application? = null

    fun get(): Application {
        return mApplication ?: initApplication()
    }

    /**
     * 反射获取ActivityThread.currentApplication
     */
    @SuppressLint("PrivateApi")
    private fun initApplication(): Application {
        try {
            Class.forName("android.app.ActivityThread").let {
                mApplication = it.getMethod("currentApplication").invoke(null) as Application
            }
            return mApplication!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw RuntimeException("Application has not been initialized!!!")
    }
}