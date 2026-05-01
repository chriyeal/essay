package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudyStatistics;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
    public StudyStatistics selectStudyStatisticsByUserIdAndDate(@Param("userId") Long userId, @Param("studyDate") String studyDate);

    /**
     * 查询用户最近7天学习统计数据
     * 
     * @param userId 用户ID
     * @return 学习统计数据集合
     */
    public List<StudyStatistics> selectRecentStudyStatisticsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户学习统计汇总
     * 
     * @param userId 用户 ID
     * @return 学习统计数据（Map）
     */
    public Map<String, Object> selectStudySummaryByUserId(@Param("userId") Long userId);

    /**
     * 查询指定日期范围内的学习趋势数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据列表
     */
    public List<Map<String, Object>> selectStudyTrendByDateRange(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询学科学习时长分布
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 学科分布数据列表
     */
    public List<Map<String, Object>> selectSubjectDistributionByUserId(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询学习时间按小时分布
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时间分布数据列表
     */
    public List<Map<String, Object>> selectTimeDistributionByUserId(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
