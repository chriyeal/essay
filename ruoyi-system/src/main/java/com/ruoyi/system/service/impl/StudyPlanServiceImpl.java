package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StudyPlanMapper;
import com.ruoyi.system.domain.StudyPlan;
import com.ruoyi.system.service.IStudyPlanService;

/**
 * 学习计划Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@Service
public class StudyPlanServiceImpl implements IStudyPlanService
{
    @Autowired
    private StudyPlanMapper studyPlanMapper;

    /**
     * 查询学习计划
     * 
     * @param planId 学习计划主键
     * @return 学习计划
     */
    @Override
    public StudyPlan selectStudyPlanByPlanId(Long planId)
    {
        return studyPlanMapper.selectStudyPlanByPlanId(planId);
    }

    /**
     * 查询学习计划列表
     * 
     * @param studyPlan 学习计划
     * @return 学习计划
     */
    @Override
    public List<StudyPlan> selectStudyPlanList(StudyPlan studyPlan)
    {
        return studyPlanMapper.selectStudyPlanList(studyPlan);
    }

    /**
     * 新增学习计划
     * 
     * @param studyPlan 学习计划
     * @return 结果
     */
    @Override
    public int insertStudyPlan(StudyPlan studyPlan)
    {
        studyPlan.setCreateTime(DateUtils.getNowDate());
        return studyPlanMapper.insertStudyPlan(studyPlan);
    }

    /**
     * 修改学习计划
     * 
     * @param studyPlan 学习计划
     * @return 结果
     */
    @Override
    public int updateStudyPlan(StudyPlan studyPlan)
    {
        studyPlan.setUpdateTime(DateUtils.getNowDate());
        return studyPlanMapper.updateStudyPlan(studyPlan);
    }

    /**
     * 批量删除学习计划
     * 
     * @param planIds 需要删除的学习计划主键
     * @return 结果
     */
    @Override
    public int deleteStudyPlanByPlanIds(Long[] planIds)
    {
        return studyPlanMapper.deleteStudyPlanByPlanIds(planIds);
    }

    /**
     * 删除学习计划信息
     * 
     * @param planId 学习计划主键
     * @return 结果
     */
    @Override
    public int deleteStudyPlanByPlanId(Long planId)
    {
        return studyPlanMapper.deleteStudyPlanByPlanId(planId);
    }

    /**
     * 根据用户ID查询学习计划列表
     * 
     * @param userId 用户ID
     * @return 学习计划集合
     */
    @Override
    public List<StudyPlan> selectStudyPlanByUserId(Long userId)
    {
        return studyPlanMapper.selectStudyPlanByUserId(userId);
    }

    /**
     * 查询用户今日学习计划
     * 
     * @param userId 用户ID
     * @return 学习计划集合
     */
    @Override
    public List<StudyPlan> selectTodayStudyPlanByUserId(Long userId)
    {
        return studyPlanMapper.selectTodayStudyPlanByUserId(userId);
    }

    /**
     * 标记学习计划为已完成
     * 
     * @param planId 学习计划ID
     * @param actualHours 实际学习时长
     * @return 结果
     */
    @Override
    public int completeStudyPlan(Long planId, Double actualHours)
    {
        StudyPlan plan = new StudyPlan();
        plan.setPlanId(planId);
        plan.setIsCompleted(1);
        plan.setCompletedTime(new Date());
        if (actualHours != null) {
            plan.setActualHours(new java.math.BigDecimal(actualHours));
        }
        return updateStudyPlan(plan);
    }

    /**
     * 根据优先级自动生成每日学习计划
     * 
     * @param userId 用户ID
     * @return 生成的学习计划列表
     */
    @Override
    public List<StudyPlan> generateDailyStudyPlan(Long userId)
    {
        // 查询用户未完成的学习计划，按优先级排序
        StudyPlan queryPlan = new StudyPlan();
        queryPlan.setUserId(userId);
        queryPlan.setIsCompleted(0);
        List<StudyPlan> allPlans = studyPlanMapper.selectStudyPlanList(queryPlan);
        
        // 按优先级降序排列
        List<StudyPlan> sortedPlans = allPlans.stream()
            .sorted((p1, p2) -> p2.getPriority().compareTo(p1.getPriority()))
            .collect(Collectors.toList());
        
        // 返回前3个最高优先级的计划作为今日计划
        return sortedPlans.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * 统计用户学习计划完成情况
     * 
     * @param userId 用户ID
     * @return 统计结果
     */
    @Override
    public int countCompletedPlansByUserId(Long userId)
    {
        return studyPlanMapper.countCompletedPlansByUserId(userId);
    }
}