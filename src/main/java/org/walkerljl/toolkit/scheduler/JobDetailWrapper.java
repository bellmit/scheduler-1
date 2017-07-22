package org.walkerljl.toolkit.scheduler;

import java.io.Serializable;
import java.util.Date;

import org.walkerljl.toolkit.datetime.CronExpression;
import org.walkerljl.toolkit.exception.AppException;
import org.walkerljl.toolkit.scheduler.enums.JobType;
import org.walkerljl.toolkit.util.StringUtils;

/**
 * 任务明细包装
 *
 * @author lijunlin
 */
public class JobDetailWrapper implements Serializable {

    private static final long serialVersionUID = 8665591333960536992L;

    /**
     * 任务明细
     */
    private JobDetail jobDetail;
    /**
     * 最近执行时间
     */
    private transient long lastExecutionTime;
    /**
     * 下次执行时间
     */
    private transient long nextExecutionTime;
    /**
     * 执行次数
     */
    private transient long executionCount;
    /**
     * 执行失败次数
     */
    private transient long errorExecutionCount;
    /**
     * 正在运行
     */
    private volatile transient boolean running;

    /**
     * 构造函数
     *
     * @param jobDetail
     */
    public JobDetailWrapper(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    /**
     * 递增执行次数
     *
     * @param executionCount
     */
    public void incrExecutionCount(long executionCount) {
        this.executionCount += executionCount;
    }

    /**
     * 递增失败的执行次数
     *
     * @param count
     */
    public void incrErrorExecutionCount(long count) {
        errorExecutionCount += count;
    }

    /**
     * 测试是否到达执行时间
     *
     * @return
     */
    public boolean canExecutable() {
        long currentTime = System.currentTimeMillis();
        if (lastExecutionTime == 0) {
            lastExecutionTime = currentTime;
        }
        if (executionCount <= 0) {
            calculateNextExecutionTime();
        }
        boolean result = currentTime >= nextExecutionTime;
        if (result && JobType.SINGLE == jobDetail.getType()) {
            result = executionCount <= 0;
        }
        return result;
    }

    /**
     * 计算下次执行时间
     */
    public void calculateNextExecutionTime() {
        long executionTime = lastExecutionTime;
        long nextTime = 0;
        if (jobDetail.getType() == JobType.SINGLE) {
            nextTime = executionTime;
        } else if (jobDetail.getType() == JobType.CYCLE && jobDetail.getFixedDelay() > 0) {
            nextTime = executionTime + jobDetail.getFixedDelay() * 1000;
        } else if (jobDetail.getType() == JobType.CRON && StringUtils.isNotBlank(jobDetail.getCronExpression())) {
            try {
                CronExpression c = new CronExpression(jobDetail.getCronExpression());
                nextTime = c.getNextValidTimeAfter(new Date(executionTime)).getTime();
            } catch (Exception e) {
                throw new AppException(e);
            }
        }
        if (executionCount == 0) {
            nextTime = executionTime + jobDetail.getInitialDelay() * 1000;
        }
        if (nextTime > nextExecutionTime) {
            nextExecutionTime = nextTime;
        }
    }

    //getters and setters
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(long lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public long getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(long nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public long getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(long executionCount) {
        this.executionCount = executionCount;
    }

    public long getErrorExecutionCount() {
        return errorExecutionCount;
    }

    public void setErrorExecutionCount(long errorExecutionCount) {
        this.errorExecutionCount = errorExecutionCount;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}