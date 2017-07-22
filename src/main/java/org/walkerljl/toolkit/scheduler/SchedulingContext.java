package org.walkerljl.toolkit.scheduler;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.walkerljl.toolkit.scheduler.filter.JobFilter;

/**
 * 调度器上下文
 *
 * @author lijunlin
 */
public class SchedulingContext implements Serializable {

    private List<JobFilter> filters;
    private String instanceId;

    private ConcurrentMap<String, Object> PROPERTIES_MAP = new ConcurrentHashMap<String, Object>();

    public SchedulingContext() {
    }

    public List<JobFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<JobFilter> filters) {
        this.filters = filters;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void addProperty(String key, Object value) {
        PROPERTIES_MAP.put(key, value);
    }

    public Object getProperty(String key) {
        return PROPERTIES_MAP.get(key);
    }


}
