package me.jiangyb.netmonitor.poster

import me.jiangyb.netmonitor.SubscriberMethod


/**
 * @author jiangyb
 * @date 2020/12/11
 * @description
 */
internal class Subscription(val subscriber: Any, val subscriberMethods: List<SubscriberMethod>)