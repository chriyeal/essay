package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudyStatistics;
import java.util.List;

/**
 * 学习统计数据Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public interface StudyStatisticsMapper 
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
     * 删除学习统计数据
     * 
     * @param statId 学习统计数据主键
     * @return 结果
     */
    public int deleteStudyStatisticsByStatId(Long statId);

    /**
     * 批量删除学习统计数据
     * 
     * @param statIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudyStatisticsByStatIds(Long[] statIds);

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
     * @return 学习统计数据（Map）
     */
    public java.util.Map<String, Object> selectStudySummaryByUserId(Long userId);
}