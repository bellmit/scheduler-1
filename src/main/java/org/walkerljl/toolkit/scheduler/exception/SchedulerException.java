package org.walkerljl.toolkit.scheduler.exception;

import org.walkerljl.toolkit.standard.exception.AppException;
import org.walkerljl.toolkit.standard.exception.ErrorCode;

/**
 * SchedulerException
 *
 * @author lijunlin
 */
public class SchedulerException extends AppException {

    /**
     * 默认构造函数
     */
    public SchedulerException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public SchedulerException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param e 异常对象
     */
    public SchedulerException(Throwable e) {
        super(e);
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     */
    public SchedulerException(ErrorCode code) {
        super(code.getDescription());
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param message 异常消息
     */
    public SchedulerException(ErrorCode code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param e 异常对象
     */
    public SchedulerException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param message 异常消息
     * @param e 异常对象
     */
    public SchedulerException(ErrorCode code, String message, Throwable e) {
        super(code, message, e);
    }
}
