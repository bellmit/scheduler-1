package org.walkerljl.scheduler.defaults;

import org.walkerljl.scheduler.JobExecutionContext;
import org.walkerljl.scheduler.filter.JobFilter;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 * SimpleJobFilter
 *
 * @author lijunlin
 */
public class SimpleJobFilter implements JobFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJobFilter.class);

    @Override
    public boolean before(JobExecutionContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Do filter before job execution.");
        }
        return true;
    }

    @Override
    public boolean after(JobExecutionContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Do filter after job execution.");
        }
        return true;
    }
}
