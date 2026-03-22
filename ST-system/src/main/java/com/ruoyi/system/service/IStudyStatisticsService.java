package com.ruoyi.system.service;

import com.ruoyi.system.domain.StudyStatistics;
import java.util.List;

/**
 * 学习统计数据Service接口
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public interface IStudyStatisticsService 
{
    /**
     * 查询学习统计数据
     * 
     * @param statId 学习统计数据主键
     * @return 学习统计数据
     */
    public StudyStatistics selectStudyStatisticsByStatId(Long statId);

    /**
     * 查询学习统计数据列表
     * 
     * @param studyStatistics 学习统计数据
     * @return 学习统计数据集合
     */
    public List<StudyStatistics> selectStudyStatisticsList(StudyStatistics studyStatistics);

    /**
     * 新增学习统计数据
     * 
     * @param studyStatistics 学习统计数据
     * @return 结果
     */
    public int insertStudyStatistics(StudyStatistics studyStatistics);

    /**
     * 修改学习统计数据
     * 
     * @param studyStatistics 学习统计数据
     * @return 结果
     */
    public int updateStudyStatistics(StudyStatistics studyStatistics);

    /**
     * 批量删除学习统计数据
     * 
     * @param statIds 需要删除的学习统计数据主键集合
     * @return 结果
     */
    public int deleteStudyStatisticsByStatIds(Long[] statIds);

    /**
     * 删除学习统计数据信息
     * 
     * @param statId 学习统计数据主键
     * @return 结果
     */
    public int deleteStudyStatisticsByStatId(Long statId);

    /**
     * 根据用户ID和日期查询学习统计数据
     * 
     * @param userId 用户ID
     * @param studyDate 学习日期
     * @return 学习统计数据
     */
    public StudyStatistics selectStudyStatisticsByUserIdAndDate(Long userId, String studyDate);

    /**
     * 查询用户最近7天学习统计数据
     * 
     * @param userId 用户ID
     * @return 学习统计数据集合
     */
    public List<StudyStatistics> selectRecentStudyStatisticsByUserId(Long userId);

    /**
     * 查询用户学习统计汇总
     * 
     * @param userId 用户 ID
     * @return 学习统计数据
     */
    public java.util.Map<String, Object> selectStudySummaryByUserId(Long userId);

    /**
     * 更新用户当日学习统计数据
     * 
     * @param userId 用户ID
     * @param planTimeSpent 计划学习时长(分钟)
     * @param tomatoTimeSpent 番茄钟学习时长(分钟)
     * @param completedPlans 完成计划数
     * @return 结果
     */
    public int updateDailyStudyStatistics(Long userId, Integer planTimeSpent, Integer tomatoTimeSpent, Integer completedPlans);

    /**
     * 生成学习趋势数据
     * 
     * @param userId 用户ID
     * @param days 天数
     * @return 趋势数据
     */
    public List<StudyStatistics> generateStudyTrendData(Long userId, Integer days);

    /**
     * 生成学习时间分布数据
     * 
     * @param userId 用户ID
     * @param date 日期
     * @return 分布数据
     */
    public String generateTimeDistributionData(Long userId, String date);

    /**
     * 计算学习效率评分
     * 
     * @param userId 用户ID
     * @param studyDate 学习日期
     * @return 效率评分
     */
    public double calculateProductivityScore(Long userId, String studyDate);
}