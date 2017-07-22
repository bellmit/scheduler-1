package org.walkerljl.toolkit.scheduler.defaults;

import java.util.Date;
import java.util.List;

import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;
import org.walkerljl.toolkit.scheduler.JobDetail;
import org.walkerljl.toolkit.scheduler.JobDetailWrapper;
import org.walkerljl.toolkit.scheduler.JobExecutionContext;
import org.walkerljl.toolkit.scheduler.exception.JobExecutionException;
import org.walkerljl.toolkit.scheduler.RunnableJob;
import org.walkerljl.toolkit.scheduler.filter.JobFilter;

/**
 * DefaultRunnableJob
 *
 * @author lijunlin
 */
public class DefaultRunnableJob implements RunnableJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRunnableJob.class);

    private JobExecutionContext jobContext;
    private JobDetailWrapper jobDetailWrapper;

    public DefaultRunnableJob(JobExecutionContext jobContext, JobDetailWrapper jobDetailWrapper) {
        this.jobContext = jobContext;
        this.jobDetailWrapper = jobDetailWrapper;
    }

    @Override
    public void run() {
        JobDetail jobDetail = jobDetailWrapper.getJobDetail();
        try {
            jobDetailWrapper.setRunning(true);
            execute(jobContext);
        } catch (Throwable e) {
            jobDetailWrapper.incrErrorExecutionCount(1);
            LOGGER.error(String.format("Occurs error when execute the job.[groupId=%s,id=%s,name=%s]",
                    jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()), e);
        } finally {
            jobDetailWrapper.incrExecutionCount(1);
            long lastTime = System.currentTimeMillis();
            jobDetailWrapper.setLastExecutionTime(lastTime);
            jobDetailWrapper.calculateNextExecutionTime();
            jobDetailWrapper.setRunning(false);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("LastExecutionTime=%s,NextExecutionTime=%s.[groupId=%s,id=%s,name=%s]",
                        DateUtils.dateFormatDateTime(new Date(jobDetailWrapper.getLastExecutionTime())),
                        DateUtils.dateFormatDateTime(new Date(jobDetailWrapper.getNextExecutionTime())),
                        jobDetail.getGroupId(), jobDetail.getId(), jobDetail.getName()));
            }
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<JobFilter> filters = context.getFilters();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (JobFilter filter : filters) {
                if (!filter.before(context)) {
                    return;
                }
            }
        }
        jobDetailWrapper.getJobDetail().getJob().execute(jobContext);
        if (CollectionUtils.isNotEmpty(filters)) {
            for (JobFilter filter : filters) {
                if (!filter.after(context)) {
                    return;
                }
            }
        }
    }
}
