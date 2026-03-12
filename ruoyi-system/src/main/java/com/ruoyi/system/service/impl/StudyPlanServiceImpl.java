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
        
        // 更新当前计划
        int result = updateStudyPlan(plan);
        
        // 如果是今日计划，需要更新关联的总体计划进度
        StudyPlan currentPlan = studyPlanMapper.selectStudyPlanByPlanId(planId);
        if ("today".equals(currentPlan.getPlanType()) && currentPlan.getParentPlanId() != null) {
            StudyPlan overallPlan = studyPlanMapper.selectStudyPlanByPlanId(currentPlan.getParentPlanId());
            if (overallPlan != null && "overall".equals(overallPlan.getPlanType())) {
                // 增加总体计划的已完成任务数
                Integer completedTasks = overallPlan.getCompletedTasks() != null ? overallPlan.getCompletedTasks() : 0;
                overallPlan.setCompletedTasks(completedTasks + 1);
                
                // 计算总体计划进度
                Integer totalTasks = overallPlan.getTotalTasks() != null ? overallPlan.getTotalTasks() : 1;
                int progress = (int) Math.round((double) overallPlan.getCompletedTasks() / totalTasks * 100);
                overallPlan.setProgress(progress);
                
                // 如果总体计划的所有任务都完成了，标记总体计划为完成
                if (progress >= 100) {
                    overallPlan.setIsCompleted(1);
                    overallPlan.setStatus("1");
                    overallPlan.setProgress(100);
                    overallPlan.setCompletedTime(new Date());
                }
                
                updateStudyPlan(overallPlan);
            }
        }
        
        return result;
    }

    /**
     * 根据优先级、难度和截止日期智能生成每日学习计划
     * 将总体计划拆分为多个今日计划
     * 
     * @param userId 用户 ID
     * @return 生成的今日计划列表
     */
    @Override
    public List<StudyPlan> generateDailyStudyPlan(Long userId)
    {
        // 查询用户未完成的多日计划（总体计划）
        StudyPlan queryOverall = new StudyPlan();
        queryOverall.setUserId(userId);
        queryOverall.setPlanType("overall");
        queryOverall.setIsCompleted(0);
        List<StudyPlan> overallPlans = studyPlanMapper.selectStudyPlanList(queryOverall);
            
        if (overallPlans.isEmpty()) {
            return new java.util.ArrayList<>();
        }
            
        // 按优先级降序、难度降序、截止日期升序排列
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlToday = new java.sql.Date(today.getTime());
            
        List<StudyPlan> sortedPlans = overallPlans.stream()
            .sorted((p1, p2) -> {
                // 1. 首先按优先级排序（高的在前）
                int priorityCompare = p2.getPriority().compareTo(p1.getPriority());
                if (priorityCompare != 0) {
                    return priorityCompare;
                }
                // 2. 优先级相同按难度排序（难的在前）
                int difficultyCompare = p2.getDifficulty().compareTo(p1.getDifficulty());
                if (difficultyCompare != 0) {
                    return difficultyCompare;
                }
                // 3. 难度相同按截止日期排序（近的在前）
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
            
        // 为前 3 个总体计划生成今日计划（根据总天数生成对应数量的今日计划）
        List<StudyPlan> todayPlans = new java.util.ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedPlans.size()); i++) {
            StudyPlan overall = sortedPlans.get(i);
                
            // 获取总体计划的天数
            Integer totalDays = overall.getTotalDays() != null ? overall.getTotalDays() : 1;
                
            // 检查该总体计划已经生成了多少个今日计划
            StudyPlan queryToday = new StudyPlan();
            queryToday.setUserId(userId);
            queryToday.setPlanType("today");
            queryToday.setParentPlanId(overall.getPlanId());
            List<StudyPlan> existingTodayPlans = studyPlanMapper.selectStudyPlanList(queryToday);
            int generatedCount = existingTodayPlans.size();
                
            // 如果已经生成够了，跳过这个总体计划
            if (generatedCount >= totalDays) {
                continue;
            }
                
            // 生成剩余的今日计划
            for (int day = generatedCount + 1; day <= totalDays; day++) {
                // 计算这一天的日期（从今天开始往后推）
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(sqlToday);
                calendar.add(java.util.Calendar.DAY_OF_MONTH, day - 1);
                java.sql.Date planDate = new java.sql.Date(calendar.getTimeInMillis());
                    
                // 创建今日计划
                StudyPlan todayPlan = new StudyPlan();
                todayPlan.setUserId(userId);
                todayPlan.setPlanName(overall.getPlanName() + " - 第" + day + "天");
                todayPlan.setPlanType("today");
                todayPlan.setParentPlanId(overall.getPlanId()); // 关联到总体计划
                todayPlan.setStartDate(planDate);
                todayPlan.setEndDate(planDate);
                todayPlan.setSubject(overall.getSubject());
                todayPlan.setDescription(overall.getDescription());
                todayPlan.setLearningGoals(overall.getLearningGoals());
                todayPlan.setDifficulty(overall.getDifficulty());
                todayPlan.setPriority(overall.getPriority());
                todayPlan.setEstimatedHours(overall.getEstimatedHours() != null ? 
                    overall.getEstimatedHours().divide(new java.math.BigDecimal(totalDays), 2, java.math.BigDecimal.ROUND_HALF_UP) : 
                    new java.math.BigDecimal(2));
                todayPlan.setStatus("0");
                todayPlan.setIsCompleted(0);
                todayPlan.setProgress(0);
                todayPlan.setTotalTasks(1);
                todayPlan.setCompletedTasks(0);
                todayPlan.setIsTemplate(0);
                todayPlan.setCreateTime(new Date());
                todayPlan.setUpdateTime(new Date());
                    
                todayPlans.add(todayPlan);
            }
        }
            
        // 批量插入今日计划
        for (StudyPlan plan : todayPlans) {
            studyPlanMapper.insertStudyPlan(plan);
        }
            
        return todayPlans;
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
        
        // 查询所有计划（不包括模板）
        StudyPlan queryAll = new StudyPlan();
        queryAll.setUserId(userId);
        queryAll.setIsTemplate(0);
        List<StudyPlan> allPlans = studyPlanMapper.selectStudyPlanList(queryAll);
        
        // 总计划数 = 所有非模板计划
        int totalPlans = allPlans.size();
        
        // 已完成计划数（只根据 status 判断）
        long completedPlans = allPlans.stream()
            .filter(p -> "1".equals(p.getStatus()))
            .count();
        
        // 进行中计划数 = 总数 - 已完成
        int ongoingPlans = (int) Math.max(0, totalPlans - completedPlans);
        
        result.put("totalPlans", totalPlans);
        result.put("ongoingPlans", ongoingPlans);
        result.put("completedPlans", (int) completedPlans);
        
        System.out.println("=== 统计数据 ===");
        System.out.println("userId: " + userId);
        System.out.println("总计划数：" + totalPlans);
        System.out.println("已完成：" + completedPlans);
        System.out.println("进行中：" + ongoingPlans);
        
        return result;
    }
}