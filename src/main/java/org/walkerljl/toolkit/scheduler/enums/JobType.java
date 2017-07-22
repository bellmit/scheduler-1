package org.walkerljl.toolkit.scheduler.enums;

/**
 * 任务类型
 *
 * @author: lijunlin
 */
public enum JobType {

    /**
     * 单次执行
     */
    SINGLE(1),

    /**
     * 循环执行
     */
    CYCLE(2),

    /**
     * CRON表达式
     */
    CRON(3);

    /**
     * 值
     */
    private int value;

    /**
     * 构造函数
     *
     * @param value
     */
    JobType(int value) {
        this.value = value;
    }

    /**
     * 获取类型
     *
     * @param value
     * @return
     */
    public static JobType getType(int value) {
        if (value <= 0) {
            return null;
        }
        for (JobType ele : JobType.values()) {
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