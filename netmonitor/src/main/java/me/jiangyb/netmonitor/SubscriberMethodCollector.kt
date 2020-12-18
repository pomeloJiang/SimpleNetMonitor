package me.jiangyb.netmonitor

import android.util.Log
import me.jiangyb.netmonitor.annotations.NetworkSubscribe
import me.jiangyb.netmonitor.exceptions.NetMonitorException
import me.jiangyb.netmonitor.mode.NetworkMode
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 订阅者收集
 */
internal class SubscriberMethodCollector private constructor() {
    companion object {
        /**
         * 如果一个类继承了泛型类或者实现了泛型接口，编译器在编译这个类的时候
         * 会生成一个桥接的混合方法
         */
        private const val BRIDGE = 0x40
        private const val SYNTHETIC = 0x1000
        private const val MODIFIERS_IGNORE =
            Modifier.ABSTRACT or Modifier.STATIC or BRIDGE or SYNTHETIC
        private val METHOD_CACHE = mutableMapOf<Class<*>, MutableList<SubscriberMethod>?>()

        private var instance: SubscriberMethodCollector? = null
            get() {
                if (field == null) {
                    field = SubscriberMethodCollector()
                }
                return field
            }

        @Synchronized
        fun get(): SubscriberMethodCollector {
            return instance!!
        }
    }

    fun findSubscribeMethods(subscribeClass: Class<*>): List<SubscriberMethod> {
        val subscriberMethods = METHOD_CACHE[subscribeClass]
        return subscriberMethods ?: findSubscribeMethodsInternal(subscribeClass)
    }

    /**
     * 找到{@see NetworkSubscribe}注解的方法
     */
    private fun findSubscribeMethodsInternal(subscribeClass: Class<*>): List<SubscriberMethod> {
        var subscribeMethods: MutableList<SubscriberMethod>? = null
        subscribeClass.declaredMethods.apply {
            forEach {
                if (it.isAnnotationPresent(NetworkSubscribe::class.java)) {
                    val networkModes = it.getAnnotation(NetworkSubscribe::class.java)!!.modes
                    checkSubscriberMethod(it, networkModes)
                    val subscriberMethod =
                        SubscriberMethod(it, networkModes, it.parameterTypes.size)
                    //found target method,add it to cache
                    if (subscribeMethods == null) {
                        subscribeMethods = mutableListOf()
                    }
                    subscribeMethods?.add(subscriberMethod)
                }
            }

            METHOD_CACHE[subscribeClass] = subscribeMethods
        }
        return subscribeMethods?.toList() ?: emptyList<SubscriberMethod>()
    }

    /**
     * example
     *
     * @NetworkSubscribe(NetworkMode.WIFI_CONNECT)
     * public void onNetworkChanged(){
     * }
     *
     * @NetworkSubscribe(NetworkMode.WIFI_CONNECT)
     * public void onNetworkChanged(NetworkMode mode){
     * }
     *
     * @NetworkSubscribe()
     * public void onNetworkChanged(NetworkMode mode){
     * }
     *
     * @NetworkSubscribe(NetworkMode.WIFI_CONNECT,NetworkMode.WIFI_DISCONNECT)
     * public void onNetworkChanged(NetworkMode mode){
     * }
     */
    private fun checkSubscriberMethod(method: Method, networkModes: Array<out NetworkMode>) {
        val modifiers = method.modifiers
        when {
            modifiers and Modifier.PUBLIC == 0 ->
                throw NetMonitorException("Subscriber ${method.declaringClass} has no public methods with the @NetworkSubscribe annotation")
            modifiers and MODIFIERS_IGNORE != 0 ->
                throw NetMonitorException("@NetworkSubscribe method must be public, non-static, and non-abstract")
            else -> {
                val parameterTypes = method.parameterTypes
                when {
                    networkModes.isEmpty() && parameterTypes.size != 1 ->
                        throw NetMonitorException("@NetworkSubscribe method must have exactly 1 parameter when @NetworkSubscribe value is empty")
                    networkModes.size > 1 && parameterTypes.size != 1 ->
                        throw NetMonitorException("@NetworkSubscribe method must have exactly 1 parameter when @NetworkSubscribe value.size is more than 1")
                    else -> {
                        parameterTypes.forEach {
                            if (it != NetworkMode::class.java) {
                                throw NetMonitorException("@NetworkSubscribe method's parameter must be ${NetworkMode::class.java} or Empty")
                            }
                        }
                    }
                }
            }
        }
    }
}