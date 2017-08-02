package org.walkerljl.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.walkerljl.scheduler.defaults.DefaultScheduler;
import org.walkerljl.toolkit.lang.PropertiesUtils;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;
import org.walkerljl.scheduler.filter.JobFilter;

/**
 * 调度器引导程序
 *
 * @author lijunlin
 */
public class SchedulerBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerBootstrap.class);

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Load scheduler config property file:%s.", args[0]));
        }
        try {
            Properties properties = PropertiesUtils.createFromInputStream(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream(args[0]));
            /**
             * 调度器名称
             */
            String schedulerName = PropertiesUtils.getPropertyAsString(properties, "scheduler.name", "orgwalkerljl-scheduler");
            /**
             * 任务执行器线程池参数
             */
            int corePoolSize = PropertiesUtils.getPropertyAsInt(properties,
                    "scheduler.job.execution.threadpool.corePoolSize", 10);
            int maximumPoolSize = PropertiesUtils.getPropertyAsInt(properties,
                    "scheduler.job.execution.threadpool.maximumPoolSize", 500);
            long keepAliveTime = PropertiesUtils.getPropertyAsLong(properties,
                    "scheduler.job.execution.threadpool.keepAliveTime", 60);
            int workQueueSize = PropertiesUtils.getPropertyAsInt(properties,
                    "scheduler.job.execution.threadpool.workQueueSize", 1000);
            /**
             * 调度器定时器配置
             */
            long schedulerTimerInitialDelay = PropertiesUtils.getPropertyAsLong(properties,
                    "scheduler.timer.initialdelay", 1);
            long schedulerTimerFixdDelay = PropertiesUtils.getPropertyAsLong(properties,
                    "scheduler.timer.fixeddelay", 5);
            /**
             * 任务加载器Class名称
             */
            String jobLoderClassName = PropertiesUtils.getPropertyAsString(properties, "scheduler.job.loader",
                    "org.walkerljl.commons.schedule.defaults.SimpleJobLoader");
            //实例化任务加载器
            JobLoader jobLoader = (JobLoader) Class.forName(jobLoderClassName)
                    .newInstance();
            /**
             * 任务过滤器Class名称
             */
            String jobFilterClassName = PropertiesUtils.getPropertyAsString(properties, "scheduler.job.filter",
                    "org.walkerljl.commons.schedule.defaults.SimpleJobFilter");
            /**
             * 打印配置信息
             */
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("\n..........scheduler config..........\n" +
                                "scheduler.name=%s\n" +
                                "scheduler.timer.initialdelay=%s\n" +
                                "scheduler.timer.fixeddelay=%s\n" +
                                "scheduler.job.execution.threadpool.corePoolSize=%s\n" +
                                "scheduler.job.execution.threadpool.maximumPoolSize=%s\n" +
                                "scheduler.job.execution.threadpool.keepAliveTime=%s\n" +
                                "scheduler.job.execution.threadpool.workQueueSize=%s\n" +
                                "scheduler.job.loader=%s\n" +
                                "scheduler.job.filter=%s\n",
                        schedulerName,
                        schedulerTimerInitialDelay,
                        schedulerTimerFixdDelay,
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        workQueueSize,
                        jobLoderClassName,
                        jobFilterClassName));
            }
            //实例化任务过滤器
            JobFilter jobFilter = (JobFilter) Class.forName(jobFilterClassName)
                    .newInstance();
            //创建任务执行线程池
            ThreadPoolExecutor jobExecutionThreadPool = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTime,
                    TimeUnit.SECONDS, new LinkedBlockingQueue(workQueueSize));
            //创建调度器
            Scheduler scheduler = new DefaultScheduler(schedulerName, schedulerTimerInitialDelay, schedulerTimerFixdDelay,
                    jobExecutionThreadPool);
            //设置任务加载器
            scheduler.setJobLoader(jobLoader);
            //设置任务过滤器
            List<JobFilter> jobFilters = new ArrayList<JobFilter>();
            jobFilters.add(jobFilter);
            scheduler.setFilters(jobFilters);
            //启动
            scheduler.start();
        } catch (Throwable e) {
            LOGGER.error("Scheduler starts error.", e);
        }
    }
}
