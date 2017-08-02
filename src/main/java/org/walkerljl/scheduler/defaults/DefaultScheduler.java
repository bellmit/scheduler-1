package org.walkerljl.scheduler.defaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.walkerljl.scheduler.Scheduler;
import org.walkerljl.toolkit.lang.MapUtils;
import org.walkerljl.toolkit.lang.SetUtils;
import org.walkerljl.toolkit.lang.StringUtils;
import org.walkerljl.toolkit.lang.thread.NamedThreadFactory;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;
import org.walkerljl.scheduler.JobDetail;
import org.walkerljl.scheduler.JobDetailWrapper;
import org.walkerljl.scheduler.JobExecutionContext;
import org.walkerljl.scheduler.JobLoader;
import org.walkerljl.scheduler.SchedulingContext;
import org.walkerljl.scheduler.enums.JobStatus;
import org.walkerljl.scheduler.enums.JobType;
import org.walkerljl.scheduler.exception.SchedulerException;
import org.walkerljl.scheduler.filter.JobFilter;
import org.walkerljl.toolkit.standard.abstracts.AbstractMachine;
import org.walkerljl.toolkit.standard.exception.machine.CannotStartMachineException;
import org.walkerljl.toolkit.standard.exception.machine.CannotStopMachineException;

/**
 * 默认的调度器
 *
 * @author: lijunlin
 */
public class DefaultScheduler extends AbstractMachine implements Scheduler {

    private static final Logger LOGGER           = LoggerFactory.getLogger(DefaultScheduler.class);
    /**
     * 默认的分组ID
     */
    private static final String DEFAULT_GROUP_ID = "0";
    /**
     * 初始延迟时间(单位：秒)
     **/
    private long initialDelay;
    /**
     * 固定延迟时间(单位：秒)
     */
    private long fixedDelay;
    /**
     * 任务执行线程池
     */
    private ThreadPoolExecutor jobExecutionThreadPool;
    /**
     * 任务字典，key:任务id，value：任务对象
     */
    private Map<String, JobDetailWrapper> jobMap = new HashMap<String, JobDetailWrapper>();
    /**
     * 任务加载器，支持动态加载外部任务
     */
    private JobLoader       jobLoader;
    /**
     * 过滤器列表
     */
    private List<JobFilter> filters;
    /**
     * 运行标志
     */
    private volatile boolean running = false;
    /**
     * 调度器上下文
     */
    private SchedulingContext context;
    /**
     * 调度器名称
     */
    private String            schedulerName;

    /**
     * 构造函数
     *
     * @param id                     调度器ID
     * @param jobExecutionThreadPool 任务执行线程池
     */
    public DefaultScheduler(String id, ThreadPoolExecutor jobExecutionThreadPool) {
        this(id, 0, 0, jobExecutionThreadPool);
    }

    /**
     * 构造函数
     *
     * @param id                     调度器ID
     * @param initialDelay           任务加载定时器初始延迟时间
     * @param fixedDelay             任务加载定时器固定延迟时间
     * @param jobExecutionThreadPool 任务执行线程池
     */
    public DefaultScheduler(String id, long initialDelay, long fixedDelay, ThreadPoolExecutor jobExecutionThreadPool) {
        this.initialDelay = initialDelay > 0 ? initialDelay : 1;
        this.fixedDelay = fixedDelay > 0 ? fixedDelay : 5;
        this.jobExecutionThreadPool = jobExecutionThreadPool;
        context = new SchedulingContext();
        context.setInstanceId(id);
        schedulerName = getClass().getSimpleName() + "_" + context.getInstanceId();
    }

    @Override
    public void processStart() throws CannotStartMachineException {

    }

    @Override
    public void processStop() throws CannotStopMachineException {

    }

    @Override
    public void start() throws CannotStartMachineException {
        if (StringUtils.isBlank(context.getInstanceId())) {
            throw new SchedulerException("Scheduler id can't blank.");
        }
        if (jobExecutionThreadPool == null) {
            throw new SchedulerException("jobExecutionThreadPool can't null.");
        }

        synchronized (this) {
            if (running) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("Scheduler[%s] has started.", context.getInstanceId()));
                }
                return;
            }
            running = true;
        }
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory(schedulerName);
        Executors.newSingleThreadScheduledExecutor(namedThreadFactory).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    schedule(context);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Has scheduled one time.");
                    }
                } catch (Throwable e) {
                    LOGGER.warn("Occurs one error when scheduling,waiting next scheduling.", e);
                } finally {
                    if (jobLoader != null) {
                        try {
                            List<JobDetail> jobDetails = jobLoader.load();
                            setJobs(jobDetails);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Has loaded some tasks,count " + jobDetails.size() + ".");
                            }
                        } catch (Throwable e) {
                            LOGGER.error("Occurs one error when load other jobs,wait next scheduling.", e);
                        }
                    }
                }
            }
        }, initialDelay, fixedDelay, TimeUnit.SECONDS);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Scheduler[%s] has started.", context.getInstanceId()));
        }
    }

    @Override
    public void stop() throws CannotStopMachineException {
        synchronized (this) {
            if (!running) {
                return;
            }
            running = false;
        }
        jobMap = null;
        context = null;
        if (jobExecutionThreadPool.isShutdown()) {
            jobExecutionThreadPool.shutdown();
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(schedulerName + " has stopped.");
        }
    }

    @Override
    public void restart() throws CannotStartMachineException, CannotStopMachineException {
        synchronized (this) {
            stop();
            start();
        }
    }

    @Override
    public boolean isRunning() {
        synchronized (this) {
            return running;
        }
    }

    @Override
    public void setFilters(List<JobFilter> filters) {
        this.filters = filters;
    }

    @Override
    public void setJobLoader(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @Override
    public void addJob(JobDetail jobDetail) {
        if (jobDetail == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,job is null.");
            }
            return;
        }
        if (StringUtils.isBlank(jobDetail.getId())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,id is null.");
            }
            return;
        } else if (jobDetail.getJob() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,executable job is null.");
            }
            return;
        } else if (jobDetail.getType() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,type is null.");
            }
            return;
        } else if (jobDetail.getType() == JobType.CRON
                && StringUtils.isBlank(jobDetail.getCronExpression())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,type is cron expression,but the expression string is blank.");
            }
            return;
        } else if (jobDetail.getStatus() == JobStatus.NON_EXECUTABLE) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fail to add one job,job is nonexecutable.");
            }
            return;
        }
        if (StringUtils.isBlank(jobDetail.getGroupId())) {
            jobDetail.setGroupId(DEFAULT_GROUP_ID);
        }
        if (StringUtils.isBlank(jobDetail.getName())) {
            jobDetail.setName(jobDetail.getId());
        }
        jobMap.put(jobDetail.getId(), new JobDetailWrapper(jobDetail));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Success to add one job.[groupId=%s,id=%s,name=%s]",
                    jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
        }
    }

    @Override
    public void setJobs(List<JobDetail> jobDetails) {
        if (jobDetails == null || jobDetails.isEmpty()) {
            return;
        }
        for (JobDetail jobDetail : jobDetails) {
            addJob(jobDetail);
        }
    }

    @Override
    public void removeJob(String jobDetailId) {
        if (StringUtils.isBlank(jobDetailId)) {
            LOGGER.debug("Job id is null.");
            return;
        }
        jobMap.remove(jobDetailId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Success to remove one job.[id=%s]",
                    jobDetailId));
        }
    }

    /**
     * 调度任务
     *
     * @param schedulingContext
     */
    private void schedule(SchedulingContext schedulingContext) {
        if (MapUtils.isEmpty(jobMap)) {
            return;
        }
        for (JobDetailWrapper jobDetailWrapper : jobMap.values()) {
            JobDetail jobDetail = jobDetailWrapper.getJobDetail();
            if (jobDetail.getStatus() == JobStatus.NON_EXECUTABLE) {
                removeJob(jobDetail.getId());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Job can't executable,remove it and schedule next job.[groupId=%s,id=%s,name=%s]",
                            jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
                }
                continue;
            }
            if (jobDetailWrapper.isRunning()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Job is running,schedule next job.[groupId=%s,id=%s,name=%s]",
                            jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
                }
                continue;
            }
            if (!jobDetailWrapper.canExecutable()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Job is not time to execute,schedule next job.[groupId=%s,id=%s,name=%s]",
                            jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
                }
                continue;
            }
            if (jobDetail.isSingleInstanceExecution()
                    && !SetUtils.contains(jobDetail.getExecutableInstanceIds(), schedulingContext.getInstanceId())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Job can't execute with the scheduler.[instanceId=%s,groupId=%s,id=%s,name=%s]",
                            schedulingContext.getInstanceId(), jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
                }
                return;
            }
            JobExecutionContext jobExecutionContext = new JobExecutionContext();
            jobExecutionContext.setFilters(filters);
            jobExecutionThreadPool.submit(new DefaultRunnableJob(jobExecutionContext, jobDetailWrapper));
        }
    }

    @Override
    public String getId() {
        return "scheduler";
    }

    @Override
    public String getName() {
        return getId();
    }

    @Override
    public String getGroup() {
        return "orgwalkerljl-commons";
    }
}
