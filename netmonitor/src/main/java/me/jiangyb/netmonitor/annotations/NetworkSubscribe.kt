package me.jiangyb.netmonitor.annotations

import me.jiangyb.netmonitor.mode.NetworkMode
import me.jiangyb.netmonitor.mode.NetworkType

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 需要网络监听的方法注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkSubscribe(vararg val modes: NetworkMode)