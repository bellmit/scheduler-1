package org.walkerljl.toolkit.scheduler.filter;

import org.walkerljl.toolkit.scheduler.JobExecutionContext;

/**
 * 任务拦截器
 *
 * @author: lijunlin
 */
public interface JobFilter {

    /**
     * 执行任务之前执行
     *
     * @param context 上下文
     * @return
     */
    boolean before(JobExecutionContext context);

    /**
     * 执行任务之后执行
     *
     * @param context 上下文
     * @return
     */
    boolean after(JobExecutionContext context);
}