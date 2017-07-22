package org.walkerljl.toolkit.scheduler;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.walkerljl.toolkit.scheduler.filter.JobFilter;

/**
 * 任务执行上下文
 *
 * @author lijunlin
 */
public class JobExecutionContext implements Serializable {

    private static final long serialVersionUID = -2098603974229084011L;

    private List<JobFilter> filters;
    private ConcurrentMap<String, Object> PROPERTIES_MAP = new ConcurrentHashMap<String, Object>();

    public List<JobFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<JobFilter> filters) {
        this.filters = filters;
    }

    public void addProperty(String key, Object value) {
        PROPERTIES_MAP.put(key, value);
    }

    public Object getProperty(String key) {
        return PROPERTIES_MAP.get(key);
    }
}
