package org.walkerljl.scheduler;

import org.walkerljl.scheduler.exception.JobExecutionException;

/**
 * 可执行任务
 *
 * @author lijunlin
 */
public interface Job {

    /**
     * 执行业务代码
     *
     * @param context 任务执行上下文
     * @throws JobExecutionException 任务执行异常
     */
    void execute(JobExecutionContext context) throws JobExecutionException;
}
