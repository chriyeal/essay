package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudyPlan;
import java.util.List;

/**
 * 学习计划Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public interface StudyPlanMapper 
{
    /**
     * 查询学习计划
     * 
     * @param planId 学习计划主键
     * @return 学习计划
     */
    public StudyPlan selectStudyPlanByPlanId(Long planId);

    /**
     * 查询学习计划列表
     * 
     * @param studyPlan 学习计划
     * @return 学习计划集合
     */
    public List<StudyPlan> selectStudyPlanList(StudyPlan studyPlan);

    /**
     * 新增学习计划
     * 
     * @param studyPlan 学习计划
     * @return 结果
     */
    public int insertStudyPlan(StudyPlan studyPlan);

    /**
     * 修改学习计划
     * 
     * @param studyPlan 学习计划
     * @return 结果
     */
    public int updateStudyPlan(StudyPlan studyPlan);

    /**
     * 删除学习计划
     * 
     * @param planId 学习计划主键
     * @return 结果
     */
    public int deleteStudyPlanByPlanId(Long planId);

    /**
     * 批量删除学习计划
     * 
     * @param planIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudyPlanByPlanIds(Long[] planIds);

    /**
     * 根据用户ID查询学习计划列表
     * 
     * @param userId 用户ID
     * @return 学习计划集合
     */
    public List<StudyPlan> selectStudyPlanByUserId(Long userId);

    /**
     * 查询用户今日学习计划
     * 
     * @param userId 用户ID
     * @return 学习计划集合
     */
    public List<StudyPlan> selectTodayStudyPlanByUserId(Long userId);

    /**
     * 统计用户学习计划完成情况
     * 
     * @param userId 用户ID
     * @return 统计结果
     */
    public int countCompletedPlansByUserId(Long userId);
}