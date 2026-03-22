package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 番茄钟记录对象 tomato_record
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public class TomatoRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 关联学习计划ID */
    @Excel(name = "关联学习计划ID")
    private Long planId;

    /** 番茄钟时长(分钟) */
    @Excel(name = "番茄钟时长")
    private Integer tomatoDuration;

    /** 休息时长(分钟) */
    @Excel(name = "休息时长")
    private Integer restDuration;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态(0进行中1已完成2中断) */
    @Excel(name = "状态")
    private Integer status;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setPlanId(Long planId) 
    {
        this.planId = planId;
    }

    public Long getPlanId() 
    {
        return planId;
    }
    public void setTomatoDuration(Integer tomatoDuration) 
    {
        this.tomatoDuration = tomatoDuration;
    }

    public Integer getTomatoDuration() 
    {
        return tomatoDuration;
    }
    public void setRestDuration(Integer restDuration) 
    {
        this.restDuration = restDuration;
    }

    public Integer getRestDuration() 
    {
        return restDuration;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("userId", getUserId())
            .append("planId", getPlanId())
            .append("tomatoDuration", getTomatoDuration())
            .append("restDuration", getRestDuration())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}