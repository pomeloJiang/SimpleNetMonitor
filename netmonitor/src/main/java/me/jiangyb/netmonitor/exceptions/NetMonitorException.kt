package me.jiangyb.netmonitor.exceptions

/**
 * @author jiangyb
 * @date 2020/12/14
 * @description 自定义异常
 */
class NetMonitorException : Exception {

    /**
     * Constructs an `NetMonitorException` without a
     * detail message.
     */
    constructor() : super()

    /**
     * Constructs an `NetMonitorException` with a detail message.
     *
     * @param   message   the detail message.
     */
    constructor(message: String?) : super(message)
}
