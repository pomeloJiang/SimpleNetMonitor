package me.jiangyb.netmonitor.poster

import android.os.Handler
import android.os.Looper
import android.util.Log
import me.jiangyb.netmonitor.SubscriberMethod
import me.jiangyb.netmonitor.mode.NetworkMode
import me.jiangyb.netmonitor.mode.NetworkType
import java.lang.reflect.Method
import java.util.*

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 网络变化推送工具类
 */
object NetworkPoster {
    private val subscriptions = LinkedList<Subscription>()
    private var isPosting: Boolean = false
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    fun post(mode: NetworkMode) {
        if (!isPosting) {
            isPosting = true
            mainThreadHandler.post {
                try {
                    val iterator = subscriptions.iterator()
                    while (iterator.hasNext()) {
                        invokeSubscriber(iterator.next(), mode)
                    }
                } finally {
                    isPosting = false
                }
            }
        }
    }

    internal fun registerSubscription(subscription: Subscription) {
        if (!isPosting) {
            subscriptions.add(subscription)
        } else {
            mainThreadHandler.post {
                subscriptions.add(subscription)
            }
        }
    }

    fun unRegisterSubscription(subscriber: Any) {
        val subscription = subscriptions.first {
            it == subscriber
        }

        if (!isPosting) {
            subscriptions.remove(subscription)
        } else {
            mainThreadHandler.post {
                subscriptions.remove(subscription)
            }
        }
    }

    /**
     * 通过反射的方式唤醒订阅者
     */
    private fun invokeSubscriber(subscription: Subscription, mode: NetworkMode) {
        val subscriber = subscription.subscriber
        val subscriberMethods = subscription.subscriberMethods
        subscriberMethods.forEach { subscriberMethod ->
            if (subscriberMethod.modes.isEmpty() || subscriberMethod.modes.contains(mode)) {
                invokeSubscriberInner(subscriber, subscriberMethod, mode)
            }
        }
    }

    /**
     * 通过反射的方式唤醒订阅者
     */
    private fun invokeSubscriberInner(
        subscriber: Any,
        subscriberMethod: SubscriberMethod,
        mode: NetworkMode
    ) {
        val method = subscriberMethod.method
        when (subscriberMethod.parameterCount) {
            0 -> method.invoke(subscriber)
            1 -> method.invoke(subscriber, mode)
        }
    }
}

