package me.jiangyb.netmonitor.mode

/**
 * @author jiangyb
 * @date 2020/12/11
 * @description 网络状态
 */
enum class NetworkMode {
    /**
     * WIFI断开连接状态
     */
    WIFI_DISCONNECT,

    /**
     * WIFI连接状态
     */
    WIFI_CONNECT,

    /**
     * 数据流量网络连接状态
     */
    MOBILE_CONNECT,

    /**
     * 数据流量网络断开连接状态
     */
    MOBILE_DISCONNECT,

    /**
     * 无网络连接状态
     */
    NONE
}