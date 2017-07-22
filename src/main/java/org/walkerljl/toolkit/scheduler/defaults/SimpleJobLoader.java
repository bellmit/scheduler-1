package org.walkerljl.toolkit.scheduler.defaults;

import java.util.ArrayList;
import java.util.List;

import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;
import org.walkerljl.toolkit.scheduler.Job;
import org.walkerljl.toolkit.scheduler.JobDetail;
import org.walkerljl.toolkit.scheduler.JobExecutionContext;
import org.walkerljl.toolkit.scheduler.exception.JobExecutionException;
import org.walkerljl.toolkit.scheduler.JobLoader;
import org.walkerljl.toolkit.scheduler.enums.JobType;

/**
 * Simple job loader
 *
 * @author: lijunlin
 */
public class SimpleJobLoader implements JobLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJobLoader.class);

    @Override
    public List<JobDetail> load() {
        List<JobDetail> jobDetails = new ArrayList<JobDetail>();
        JobDetail jobDetail = new JobDetail();
        jobDetail.setId("1");
        jobDetail.setName("SimpleJob");
        jobDetail.setType(JobType.CYCLE);
        jobDetail.setFixedDelay(5);
        jobDetail.setJob(new Job() {
            @Override
            public void execute(JobExecutionContext context)
                    throws JobExecutionException {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Simple job run one time.");
                }
            }
        });
        jobDetails.add(jobDetail);
        return jobDetails;
    }
}
