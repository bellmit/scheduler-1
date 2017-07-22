package org.walkerljl.toolkit.scheduler.enums;

/**
 * 任务状态
 *
 * @author: lijunlin
 */
public enum JobStatus {

    /**
     * 不可执行
     */
    NON_EXECUTABLE(0),

    /**
     * 可执行
     */
    EXECUTABLE(1);

    /**
     * 值
     */
    private int value;

    /**
     * 构造函数
     *
     * @param value
     */
    JobStatus(int value) {
        this.value = value;
    }

    /**
     * 获取类型
     *
     * @param value
     * @return
     */
    public static JobStatus getType(int value) {
        if (value <= 0) {
            return null;
        }
        for (JobStatus ele : JobStatus.values()) {
            if (value == ele.getValue()) {
                return ele;
            }
        }
        return null;
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getValue() {
        return value;
    }
}