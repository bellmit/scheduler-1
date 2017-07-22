package org.walkerljl.toolkit.scheduler.defaults;

import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;
import org.walkerljl.toolkit.scheduler.JobExecutionContext;
import org.walkerljl.toolkit.scheduler.filter.JobFilter;

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
