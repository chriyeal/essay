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
     * @param planId 学习计划 ID
     * @param actualHours 实际学习时长
     * @return 结果
     */
    @Override
    public int completeStudyPlan(Long planId, Double actualHours)
    {
        StudyPlan plan = new StudyPlan();
        plan.setPlanId(planId);
        plan.setIsCompleted(1);
        plan.setStatus("1"); // 设置状态为已完成
        plan.setProgress(100); // 设置进度为 100%
        plan.setCompletedTime(new Date());
        if (actualHours != null) {
            plan.setActualHours(new java.math.BigDecimal(actualHours));
        }
        return updateStudyPlan(plan);
    }

    /**
     * 根据优先级和截止日期自动生成每日学习计划
     * 
     * @param userId 用户 ID
     * @return 生成的学习计划列表
     */
    @Override
    public List<StudyPlan> generateDailyStudyPlan(Long userId)
    {
        // 查询用户未完成的学习计划
        StudyPlan queryPlan = new StudyPlan();
        queryPlan.setUserId(userId);
        queryPlan.setIsCompleted(0);
        List<StudyPlan> allPlans = studyPlanMapper.selectStudyPlanList(queryPlan);
            
        // 按优先级降序、截止日期升序排列（优先级高的在前，截止日期近的在前）
        List<StudyPlan> sortedPlans = allPlans.stream()
            .sorted((p1, p2) -> {
                // 首先按优先级排序
                int priorityCompare = p2.getPriority().compareTo(p1.getPriority());
                if (priorityCompare != 0) {
                    return priorityCompare;
                }
                // 优先级相同则按截止日期排序
                if (p1.getDeadline() == null && p2.getDeadline() == null) {
                    return 0;
                }
                if (p1.getDeadline() == null) {
                    return 1;
                }
                if (p2.getDeadline() == null) {
                    return -1;
                }
                return p1.getDeadline().compareTo(p2.getDeadline());
            })
            .collect(Collectors.toList());
            
        // 返回前 3 个最高优先级的计划作为今日计划
        return sortedPlans.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * 统计用户学习计划完成情况
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    @Override
    public int countCompletedPlansByUserId(Long userId)
    {
        return studyPlanMapper.countCompletedPlansByUserId(userId);
    }

    /**
     * 获取学习计划统计汇总
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    @Override
    public java.util.Map<String, Object> getPlanSummary(Long userId)
    {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        // 查询所有计划
        StudyPlan queryAll = new StudyPlan();
        queryAll.setUserId(userId);
        List<StudyPlan> allPlans = studyPlanMapper.selectStudyPlanList(queryAll);
        
        // 查询未完成计划
        StudyPlan queryOngoing = new StudyPlan();
        queryOngoing.setUserId(userId);
        queryOngoing.setIsCompleted(0);
        List<StudyPlan> ongoingPlans = studyPlanMapper.selectStudyPlanList(queryOngoing);
        
        // 查询已完成计划
        StudyPlan queryCompleted = new StudyPlan();
        queryCompleted.setUserId(userId);
        queryCompleted.setIsCompleted(1);
        List<StudyPlan> completedPlans = studyPlanMapper.selectStudyPlanList(queryCompleted);
        
        // 查询模板计划
        StudyPlan queryTemplate = new StudyPlan();
        queryTemplate.setUserId(userId);
        queryTemplate.setIsTemplate(1);
        List<StudyPlan> templatePlans = studyPlanMapper.selectStudyPlanList(queryTemplate);
        
        result.put("totalPlans", allPlans != null ? allPlans.size() : 0);
        result.put("ongoingPlans", ongoingPlans != null ? ongoingPlans.size() : 0);
        result.put("completedPlans", completedPlans != null ? completedPlans.size() : 0);
        result.put("templatePlans", templatePlans != null ? templatePlans.size() : 0);
        
        return result;
    }
}