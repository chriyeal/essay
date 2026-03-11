package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学习统计数据对象 study_statistics
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public class StudyStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    private Long statId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 学习日期 */
    @Excel(name = "学习日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date studyDate;

    /** 计划学习时长(分钟) */
    @Excel(name = "计划学习时长")
    private Integer planTimeSpent;

    /** 番茄钟学习时长(分钟) */
    @Excel(name = "番茄钟学习时长")
    private Integer tomatoTimeSpent;

    /** 总学习时长(分钟) */
    @Excel(name = "总学习时长")
    private Integer totalTimeSpent;

    /** 完成计划数 */
    @Excel(name = "完成计划数")
    private Integer completedPlans;

    /** 各科目学习时长分布 */
    @Excel(name = "科目分布")
    private String subjectDistribution;

    public void setStatId(Long statId) 
    {
        this.statId = statId;
    }

    public Long getStatId() 
    {
        return statId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setStudyDate(Date studyDate) 
    {
        this.studyDate = studyDate;
    }

    public Date getStudyDate() 
    {
        return studyDate;
    }
    public void setPlanTimeSpent(Integer planTimeSpent) 
    {
        this.planTimeSpent = planTimeSpent;
    }

    public Integer getPlanTimeSpent() 
    {
        return planTimeSpent;
    }
    public void setTomatoTimeSpent(Integer tomatoTimeSpent) 
    {
        this.tomatoTimeSpent = tomatoTimeSpent;
    }

    public Integer getTomatoTimeSpent() 
    {
        return tomatoTimeSpent;
    }
    public void setTotalTimeSpent(Integer totalTimeSpent) 
    {
        this.totalTimeSpent = totalTimeSpent;
    }

    public Integer getTotalTimeSpent() 
    {
        return totalTimeSpent;
    }
    public void setCompletedPlans(Integer completedPlans) 
    {
        this.completedPlans = completedPlans;
    }

    public Integer getCompletedPlans() 
    {
        return completedPlans;
    }
    public void setSubjectDistribution(String subjectDistribution) 
    {
        this.subjectDistribution = subjectDistribution;
    }

    public String getSubjectDistribution() 
    {
        return subjectDistribution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("statId", getStatId())
            .append("userId", getUserId())
            .append("studyDate", getStudyDate())
            .append("planTimeSpent", getPlanTimeSpent())
            .append("tomatoTimeSpent", getTomatoTimeSpent())
            .append("totalTimeSpent", getTotalTimeSpent())
            .append("completedPlans", getCompletedPlans())
            .append("subjectDistribution", getSubjectDistribution())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}