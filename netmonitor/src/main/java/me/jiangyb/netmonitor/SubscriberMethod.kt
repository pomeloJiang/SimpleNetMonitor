package me.jiangyb.netmonitor

import me.jiangyb.netmonitor.annotations.NetworkSubscribe
import me.jiangyb.netmonitor.mode.NetworkMode
import java.lang.reflect.Method

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 订阅方法和网络类型的封装
 */
internal class SubscriberMethod(
    val method: Method,
    val modes: Array<out NetworkMode>,
    val parameterCount: Int
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            this -> true
            is SubscriberMethod -> getMethodString() == other.getMethodString()
            else -> false
        }
    }

    private fun getMethodString(): String {
        val sb = StringBuilder()
        modes.forEach { mode ->
            sb.append("${mode.name},")
        }
        sb.deleteCharAt(sb.lastIndexOf(","))
        return """
            ${NetworkSubscribe::javaClass}[]{$sb};
            ${method.name}($parameterCount)
        """.trimIndent()
    }

    override fun hashCode(): Int {
        return method.hashCode()
    }
}