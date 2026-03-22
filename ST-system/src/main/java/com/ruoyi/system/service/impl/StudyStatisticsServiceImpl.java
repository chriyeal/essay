package com.ruoyi.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StudyStatisticsMapper;
import com.ruoyi.system.domain.StudyStatistics;
import com.ruoyi.system.service.IStudyStatisticsService;

/**
 * 学习统计数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@Service
public class StudyStatisticsServiceImpl implements IStudyStatisticsService
{
    @Autowired
    private StudyStatisticsMapper studyStatisticsMapper;

    /**
     * 查询学习统计数据
     * 
     * @param statId 学习统计数据主键
     * @return 学习统计数据
     */
    @Override
    public StudyStatistics selectStudyStatisticsByStatId(Long statId)
    {
        return studyStatisticsMapper.selectStudyStatisticsByStatId(statId);
    }

    /**
     * 查询学习统计数据列表
     * 
     * @param studyStatistics 学习统计数据
     * @return 学习统计数据
     */
    @Override
    public List<StudyStatistics> selectStudyStatisticsList(StudyStatistics studyStatistics)
    {
        return studyStatisticsMapper.selectStudyStatisticsList(studyStatistics);
    }

    /**
     * 新增学习统计数据
     * 
     * @param studyStatistics 学习统计数据
     * @return 结果
     */
    @Override
    public int insertStudyStatistics(StudyStatistics studyStatistics)
    {
        studyStatistics.setCreateTime(DateUtils.getNowDate());
        return studyStatisticsMapper.insertStudyStatistics(studyStatistics);
    }

    /**
     * 修改学习统计数据
     * 
     * @param studyStatistics 学习统计数据
     * @return 结果
     */
    @Override
    public int updateStudyStatistics(StudyStatistics studyStatistics)
    {
        studyStatistics.setUpdateTime(DateUtils.getNowDate());
        return studyStatisticsMapper.updateStudyStatistics(studyStatistics);
    }

    /**
     * 批量删除学习统计数据
     * 
     * @param statIds 需要删除的学习统计数据主键
     * @return 结果
     */
    @Override
    public int deleteStudyStatisticsByStatIds(Long[] statIds)
    {
        return studyStatisticsMapper.deleteStudyStatisticsByStatIds(statIds);
    }

    /**
     * 删除学习统计数据信息
     * 
     * @param statId 学习统计数据主键
     * @return 结果
     */
    @Override
    public int deleteStudyStatisticsByStatId(Long statId)
    {
        return studyStatisticsMapper.deleteStudyStatisticsByStatId(statId);
    }

    /**
     * 根据用户ID和日期查询学习统计数据
     * 
     * @param userId 用户ID
     * @param studyDate 学习日期
     * @return 学习统计数据
     */
    @Override
    public StudyStatistics selectStudyStatisticsByUserIdAndDate(Long userId, String studyDate)
    {
        return studyStatisticsMapper.selectStudyStatisticsByUserIdAndDate(userId, studyDate);
    }

    /**
     * 查询用户最近7天学习统计数据
     * 
     * @param userId 用户ID
     * @return 学习统计数据集合
     */
    @Override
    public List<StudyStatistics> selectRecentStudyStatisticsByUserId(Long userId)
    {
        return studyStatisticsMapper.selectRecentStudyStatisticsByUserId(userId);
    }

    /**
     * 查询用户学习统计汇总
     * 
     * @param userId 用户 ID
     * @return 学习统计数据
     */
    @Override
    public java.util.Map<String, Object> selectStudySummaryByUserId(Long userId)
    {
        return studyStatisticsMapper.selectStudySummaryByUserId(userId);
    }

    /**
     * 更新用户当日学习统计数据
     * 
     * @param userId 用户ID
     * @param planTimeSpent 计划学习时长(分钟)
     * @param tomatoTimeSpent 番茄钟学习时长(分钟)
     * @param completedPlans 完成计划数
     * @return 结果
     */
    @Override
    public int updateDailyStudyStatistics(Long userId, Integer planTimeSpent, Integer tomatoTimeSpent, Integer completedPlans)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        StudyStatistics statistics = studyStatisticsMapper.selectStudyStatisticsByUserIdAndDate(userId, today);
        
        if (statistics == null) {
            // 新增当日统计数据
            statistics = new StudyStatistics();
            statistics.setUserId(userId);
            statistics.setStudyDate(new Date());
            statistics.setPlanTimeSpent(planTimeSpent != null ? planTimeSpent : 0);
            statistics.setTomatoTimeSpent(tomatoTimeSpent != null ? tomatoTimeSpent : 0);
            statistics.setTotalTimeSpent((planTimeSpent != null ? planTimeSpent : 0) + (tomatoTimeSpent != null ? tomatoTimeSpent : 0));
            statistics.setCompletedPlans(completedPlans != null ? completedPlans : 0);
            return insertStudyStatistics(statistics);
        } else {
            // 更新当日统计数据
            if (planTimeSpent != null) {
                statistics.setPlanTimeSpent(statistics.getPlanTimeSpent() + planTimeSpent);
            }
            if (tomatoTimeSpent != null) {
                statistics.setTomatoTimeSpent(statistics.getTomatoTimeSpent() + tomatoTimeSpent);
            }
            statistics.setTotalTimeSpent(statistics.getPlanTimeSpent() + statistics.getTomatoTimeSpent());
            if (completedPlans != null) {
                statistics.setCompletedPlans(statistics.getCompletedPlans() + completedPlans);
            }
            return updateStudyStatistics(statistics);
        }
    }

    /**
     * 生成学习趋势数据
     * 
     * @param userId 用户ID
     * @param days 天数
     * @return 趋势数据
     */
    @Override
    public List<StudyStatistics> generateStudyTrendData(Long userId, Integer days)
    {
        // 这里可以根据需要从数据库查询或计算趋势数据
        List<StudyStatistics> trendData = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        
        for (int i = days - 1; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            
            StudyStatistics stat = new StudyStatistics();
            stat.setStudyDate(calendar.getTime());
            stat.setTotalTimeSpent(new Random().nextInt(300) + 60); // 模拟数据
            stat.setCompletedPlans(new Random().nextInt(5) + 1); // 模拟数据
            trendData.add(stat);
        }
        
        return trendData;
    }

    /**
     * 生成学习时间分布数据
     * 
     * @param userId 用户ID
     * @param date 日期
     * @return 分布数据
     */
    @Override
    public String generateTimeDistributionData(Long userId, String date)
    {
        // 模拟科目时间分布数据
        Map<String, Object> distribution = new HashMap<>();
        distribution.put("数学", new Random().nextInt(120) + 30);
        distribution.put("英语", new Random().nextInt(120) + 30);
        distribution.put("语文", new Random().nextInt(120) + 30);
        distribution.put("物理", new Random().nextInt(90) + 20);
        distribution.put("化学", new Random().nextInt(90) + 20);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(distribution);
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * 计算学习效率评分
     * 
     * @param userId 用户ID
     * @param studyDate 学习日期
     * @return 效率评分
     */
    @Override
    public double calculateProductivityScore(Long userId, String studyDate)
    {
        // 简单的效率评分算法：完成计划数 * 2 + 学习时长/60
        StudyStatistics statistics = studyStatisticsMapper.selectStudyStatisticsByUserIdAndDate(userId, studyDate);
        if (statistics == null) {
            return 0.0;
        }
        
        double score = statistics.getCompletedPlans() * 2.0 + statistics.getTotalTimeSpent() / 60.0;
        return Math.min(score, 10.0); // 最高10分
    }
}