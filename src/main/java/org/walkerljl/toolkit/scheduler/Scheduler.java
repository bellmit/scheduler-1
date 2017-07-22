package org.walkerljl.toolkit.scheduler;

import java.util.List;

import org.walkerljl.toolkit.scheduler.filter.JobFilter;
import org.walkerljl.toolkit.standard.Machine;

/**
 * 调度器
 *
 * @author lijunlin
 */
public interface Scheduler extends Machine {

    /**
     * 设置任务列表
     *
     * @param jobDetails
     */
    void setJobs(List<JobDetail> jobDetails);

    /**
     * 添加任务
     *
     * @param jobDetail
     */
    void addJob(JobDetail jobDetail);

    /**
     * 删除任务
     *
     * @param jobDetailId
     */
    void removeJob(String jobDetailId);

    /**
     * 设置任务过滤器
     *
     * @param filters
     */
    void setFilters(List<JobFilter> filters);

    /**
     * 设置任务加载器
     *
     * @param jobLoader
     */
    void setJobLoader(JobLoader jobLoader);
}
