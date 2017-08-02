package org.walkerljl.scheduler.exception;

import org.walkerljl.toolkit.standard.exception.AppException;
import org.walkerljl.toolkit.standard.exception.ErrorCode;

/**
 * 任务执行异常
 *
 * @author lijunlin
 */
public class JobExecutionException extends AppException {

    /**
     * 默认构造函数
     */
    public JobExecutionException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public JobExecutionException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param e 异常对象
     */
    public JobExecutionException(Throwable e) {
        super(e);
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     */
    public JobExecutionException(ErrorCode code) {
        super(code.getDescription());
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param message 异常消息
     */
    public JobExecutionException(ErrorCode code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param e 异常对象
     */
    public JobExecutionException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param message 异常消息
     * @param e 异常对象
     */
    public JobExecutionException(ErrorCode code, String message, Throwable e) {
        super(code, message, e);
    }
}
