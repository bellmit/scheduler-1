package org.walkerljl.scheduler;

import java.util.List;

/**
 * 任务加载器
 *
 * @author: lijunlin
 */
public interface JobLoader {

    /**
     * 加载任务
     *
     * @return
     */
    List<JobDetail> load();
}
