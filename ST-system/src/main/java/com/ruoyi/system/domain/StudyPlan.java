package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学习计划对象 study_plan
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public class StudyPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学习计划ID */
    private Long planId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 学习计划名称 */
    @Excel(name = "学习计划名称")
    private String planName;

    /** 计划类型(overall总体计划, today今日计划) */
    @Excel(name = "计划类型")
    private String planType;

    /** 父计划ID(用于关联总体计划和今日计划) */
    @Excel(name = "父计划ID")
    private Long parentPlanId;

    /** 开始日期 */
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 截止日期 */
    @Excel(name = "截止日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    /** 优先级(0低1中2高) */
    @Excel(name = "优先级")
    private Integer priority;

    /** 难度等级(1简单2中等3困难) */
    @Excel(name = "难度等级")
    private Integer difficulty;

    /** 标签分类(逗号分隔) */
    @Excel(name = "标签分类")
    private String tags;

    /** 学科/类别 */
    @Excel(name = "学科")
    private String subject;

    /** 计划描述 */
    @Excel(name = "计划描述")
    private String description;

    /** 学习目标 */
    @Excel(name = "学习目标")
    private String learningGoals;

    /** 预计学习时长(小时) */
    @Excel(name = "预计学习时长")
    private BigDecimal estimatedHours;

    /** 实际学习时长 (小时) */
    @Excel(name = "实际学习时长")
    private BigDecimal actualHours;
    
    /** 总天数（多日计划的总天数） */
    @Excel(name = "总天数")
    private Integer totalDays;
    
    /** 进度百分比 (0-100) */
    @Excel(name = "进度")
    private Integer progress;

    /** 总任务数 */
    @Excel(name = "总任务数")
    private Integer totalTasks;

    /** 已完成任务数 */
    @Excel(name = "已完成任务数")
    private Integer completedTasks;

    /** 状态(0进行中1已完成2已取消) */
    @Excel(name = "状态")
    private String status;

    /** 是否为模板(0否1是) */
    @Excel(name = "是否为模板")
    private Integer isTemplate;

    /** 是否完成(0未完成1已完成) */
    @Excel(name = "是否完成")
    private Integer isCompleted;

    /** 完成时间 */
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completedTime;

    public void setPlanId(Long planId) 
    {
        this.planId = planId;
    }

    public Long getPlanId() 
    {
        return planId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setPlanName(String planName) 
    {
        this.planName = planName;
    }

    public String getPlanName() 
    {
        return planName;
    }
    public void setPlanType(String planType) 
    {
        this.planType = planType;
    }

    public String getPlanType() 
    {
        return planType;
    }
    public void setParentPlanId(Long parentPlanId) 
    {
        this.parentPlanId = parentPlanId;
    }

    public Long getParentPlanId() 
    {
        return parentPlanId;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }
    public void setDeadline(Date deadline) 
    {
        this.deadline = deadline;
    }

    public Date getDeadline() 
    {
        return deadline;
    }
    public void setPriority(Integer priority) 
    {
        this.priority = priority;
    }

    public Integer getPriority() 
    {
        return priority;
    }
    public void setDifficulty(Integer difficulty) 
    {
        this.difficulty = difficulty;
    }

    public Integer getDifficulty() 
    {
        return difficulty;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setIsCompleted(Integer isCompleted) 
    {
        this.isCompleted = isCompleted;
    }

    public Integer getIsCompleted() 
    {
        return isCompleted;
    }
    public void setCompletedTime(Date completedTime) 
    {
        this.completedTime = completedTime;
    }

    public Date getCompletedTime() 
    {
        return completedTime;
    }
    public void setEstimatedHours(BigDecimal estimatedHours) 
    {
        this.estimatedHours = estimatedHours;
    }

    public BigDecimal getEstimatedHours() 
    {
        return estimatedHours;
    }
    public void setActualHours(BigDecimal actualHours) 
    {
        this.actualHours = actualHours;
    }

    public BigDecimal getActualHours() 
    {
        return actualHours;
    }
    public void setTotalDays(Integer totalDays) 
    {
        this.totalDays = totalDays;
    }

    public Integer getTotalDays() 
    {
        return totalDays;
    }
    public void setSubject(String subject) 
    {
        this.subject = subject;
    }

    public String getSubject() 
    {
        return subject;
    }
    public void setLearningGoals(String learningGoals) 
    {
        this.learningGoals = learningGoals;
    }

    public String getLearningGoals() 
    {
        return learningGoals;
    }
    public void setProgress(Integer progress) 
    {
        this.progress = progress;
    }

    public Integer getProgress() 
    {
        return progress;
    }
    public void setTotalTasks(Integer totalTasks) 
    {
        this.totalTasks = totalTasks;
    }

    public Integer getTotalTasks() 
    {
        return totalTasks;
    }
    public void setCompletedTasks(Integer completedTasks) 
    {
        this.completedTasks = completedTasks;
    }

    public Integer getCompletedTasks() 
    {
        return completedTasks;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setIsTemplate(Integer isTemplate) 
    {
        this.isTemplate = isTemplate;
    }

    public Integer getIsTemplate() 
    {
        return isTemplate;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("planId", getPlanId())
            .append("userId", getUserId())
            .append("planName", getPlanName())
            .append("planType", getPlanType())
            .append("parentPlanId", getParentPlanId())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("deadline", getDeadline())
            .append("priority", getPriority())
            .append("difficulty", getDifficulty())
            .append("tags", getTags())
            .append("subject", getSubject())
            .append("description", getDescription())
            .append("learningGoals", getLearningGoals())
            .append("estimatedHours", getEstimatedHours())
            .append("actualHours", getActualHours())
            .append("totalDays", getTotalDays())
            .append("progress", getProgress())
            .append("totalTasks", getTotalTasks())
            .append("completedTasks", getCompletedTasks())
            .append("status", getStatus())
            .append("isTemplate", getIsTemplate())
            .append("isCompleted", getIsCompleted())
            .append("completedTime", getCompletedTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}