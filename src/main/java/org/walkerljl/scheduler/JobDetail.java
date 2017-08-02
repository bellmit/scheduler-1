package org.walkerljl.scheduler;

import java.io.Serializable;
import java.util.Set;

import org.walkerljl.scheduler.enums.JobStatus;
import org.walkerljl.scheduler.enums.JobType;

/**
 * 任务明细
 *
 * @author lijunlin
 */
public class JobDetail implements Serializable {

    private static final long serialVersionUID = 8665591333960536992L;

    /**
     * ID
     */
    private String  id;
    /**
     * 名称
     */
    private String  name;
    /**
     * 所属组ID
     */
    private String  groupId;
    /**
     * 可执行的任务
     */
    private Job     job;
    /**
     * 任务类型
     */
    private JobType type;
    /**
     * 固定延迟时间(单位：秒)
     */
    private long    fixedDelay;
    /**
     * 初始延迟时间(单位：秒)
     **/
    private long    initialDelay;
    /**
     * Cron表达式
     */
    private String  cronExpression;
    /**
     * 任务状态
     */
    private JobStatus status = JobStatus.EXECUTABLE;
    /**
     * 是否单实例运行，true：单实例
     **/
    private boolean singleInstanceExecution;
    /**
     * 可运行实例ID列表
     **/
    private Set<String> executableInstanceIds;

    /**
     * 构造函数
     */
    public JobDetail() {}

    /**
     * 获取任务ID
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置任务ID
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取任务名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置任务名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取任务所属分组ID
     *
     * @return
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 设置任务所属分组ID
     *
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取可执行的任务
     *
     * @return
     */
    public Job getJob() {
        return job;
    }

    /**
     * 设置可执行的任务
     *
     * @param job
     */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * 获取任务类型
     *
     * @return
     */
    public JobType getType() {
        return type;
    }

    /**
     * 设置任务类型
     *
     * @param type
     */
    public void setType(JobType type) {
        this.type = type;
    }

    /**
     * 获取任务固定延迟时间
     *
     * @return
     */
    public long getFixedDelay() {
        return fixedDelay;
    }

    /**
     * 设置任务固定延迟时间
     *
     * @param fixedDelay
     */
    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    /**
     * 获取任务初始延迟时间
     *
     * @return
     */
    public long getInitialDelay() {
        return initialDelay;
    }

    /**
     * 设置任务初始延迟时间
     *
     * @param initialDelay
     */
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * 获取执行计划CRON表达式
     *
     * @return
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置执行计划CRON表达式
     *
     * @param cronExpression
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 获取任务状态
     *
     * @return
     */
    public JobStatus getStatus() {
        return status;
    }

    /**
     * 设置任务状态
     *
     * @param status
     */
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    /**
     * 任务是否单实例执行
     *
     * @return
     */
    public boolean isSingleInstanceExecution() {
        return singleInstanceExecution;
    }

    /**
     * 设置任务是否单实例执行
     *
     * @param singleInstanceExecution
     */
    public void setSingleInstanceExecution(boolean singleInstanceExecution) {
        this.singleInstanceExecution = singleInstanceExecution;
    }

    /**
     * 获取可执行实例ID列表
     *
     * @return
     */
    public Set<String> getExecutableInstanceIds() {
        return executableInstanceIds;
    }

    /**
     * 设置可执行实例ID列表
     *
     * @param executableInstanceIds
     */
    public void setExecutableInstanceIds(Set<String> executableInstanceIds) {
        this.executableInstanceIds = executableInstanceIds;
    }

    @Override
    public String toString() {
        return "JobDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groupId='" + groupId + '\'' +
                ", job=" + job +
                ", type=" + type +
                ", fixedDelay=" + fixedDelay +
                ", initialDelay=" + initialDelay +
                ", cronExpression='" + cronExpression + '\'' +
                ", status=" + status +
                ", singleInstanceExecution=" + singleInstanceExecution +
                ", executableInstanceIds=" + executableInstanceIds +
                '}';
    }
}