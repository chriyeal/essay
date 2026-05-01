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
     */
    @Override
    public StudyStatistics selectStudyStatisticsByStatId(Long statId)
    {
        return studyStatisticsMapper.selectStudyStatisticsByStatId(statId);
    }

    /**
     * 查询学习统计数据列表
     */
    @Override
    public List<StudyStatistics> selectStudyStatisticsList(StudyStatistics studyStatistics)
    {
        return studyStatisticsMapper.selectStudyStatisticsList(studyStatistics);
    }

    /**
     * 新增学习统计数据
     */
    @Override
    public int insertStudyStatistics(StudyStatistics studyStatistics)
    {
        studyStatistics.setCreateTime(DateUtils.getNowDate());
        return studyStatisticsMapper.insertStudyStatistics(studyStatistics);
    }

    /**
     * 修改学习统计数据
     */
    @Override
    public int updateStudyStatistics(StudyStatistics studyStatistics)
    {
        studyStatistics.setUpdateTime(DateUtils.getNowDate());
        return studyStatisticsMapper.updateStudyStatistics(studyStatistics);
    }

    /**
     * 批量删除学习统计数据
     */
    @Override
    public int deleteStudyStatisticsByStatIds(Long[] statIds)
    {
        return studyStatisticsMapper.deleteStudyStatisticsByStatIds(statIds);
    }

    /**
     * 删除学习统计数据信息
     */
    @Override
    public int deleteStudyStatisticsByStatId(Long statId)
    {
        return studyStatisticsMapper.deleteStudyStatisticsByStatId(statId);
    }

    /**
     * 根据用户ID和日期查询学习统计数据
     */
    @Override
    public StudyStatistics selectStudyStatisticsByUserIdAndDate(Long userId, String studyDate)
    {
        return studyStatisticsMapper.selectStudyStatisticsByUserIdAndDate(userId, studyDate);
    }

    /**
     * 查询用户最近7天学习统计数据
     */
    @Override
    public List<StudyStatistics> selectRecentStudyStatisticsByUserId(Long userId)
    {
        return studyStatisticsMapper.selectRecentStudyStatisticsByUserId(userId);
    }

    /**
     * 查询用户学习统计汇总（今日数据）
     */
    @Override
    public Map<String, Object> selectStudySummaryByUserId(Long userId)
    {
        return studyStatisticsMapper.selectStudySummaryByUserId(userId);
    }

    /**
     * 更新用户当日学习统计数据
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
            statistics.setTotalTimeSpent((planTimeSpent != null ? planTimeSpent : 0) + (tomatoTimeSpent != null ? tomatoTimeSpent : 0));
            statistics.setTomatoTimeSpent(tomatoTimeSpent != null ? tomatoTimeSpent : 0);
            statistics.setCompletedPlans(completedPlans != null ? completedPlans : 0);
            return insertStudyStatistics(statistics);
        } else {
            // 更新当日统计数据
            int newTotal = statistics.getTotalTimeSpent() + (planTimeSpent != null ? planTimeSpent : 0) + (tomatoTimeSpent != null ? tomatoTimeSpent : 0);
            statistics.setTotalTimeSpent(newTotal);
            if (tomatoTimeSpent != null) {
                statistics.setTomatoTimeSpent(statistics.getTomatoTimeSpent() + tomatoTimeSpent);
            }
            if (completedPlans != null) {
                statistics.setCompletedPlans(statistics.getCompletedPlans() + completedPlans);
            }
            return updateStudyStatistics(statistics);
        }
    }

    /**
     * 查询学习趋势数据（从study_statistics表获取真实数据）
     */
    @Override
    public List<Map<String, Object>> getStudyTrendData(Long userId, Integer days)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String endDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -(days - 1));
        String startDate = sdf.format(calendar.getTime());
        
        // 从数据库获取真实趋势数据
        List<Map<String, Object>> dbData = studyStatisticsMapper.selectStudyTrendByDateRange(userId, startDate, endDate);
        
        // 构建日期到数据的映射
        Map<String, Map<String, Object>> dateMap = new LinkedHashMap<>();
        for (Map<String, Object> row : dbData) {
            String dateKey = String.valueOf(row.get("studyDate"));
            dateMap.put(dateKey, row);
        }
        
        // 填充所有日期（即使没有数据也显示0）
        List<Map<String, Object>> result = new ArrayList<>();
        calendar.setTime(new Date());
        for (int i = days - 1; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            String dateStr = sdf.format(calendar.getTime());
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);
            
            if (dateMap.containsKey(dateStr)) {
                Map<String, Object> dbRow = dateMap.get(dateStr);
                dayData.put("studyTime", dbRow.get("totalTimeSpent") != null ? ((Number) dbRow.get("totalTimeSpent")).intValue() : 0);
                dayData.put("tomatoCount", dbRow.get("tomatoTimeSpent") != null ? ((Number) dbRow.get("tomatoTimeSpent")).intValue() : 0);
                dayData.put("completedTasks", dbRow.get("completedPlans") != null ? ((Number) dbRow.get("completedPlans")).intValue() : 0);
            } else {
                dayData.put("studyTime", 0);
                dayData.put("tomatoCount", 0);
                dayData.put("completedTasks", 0);
            }
            result.add(dayData);
        }
        
        return result;
    }

    /**
     * 查询学科学习时长分布（从tomato_record关联study_plan获取真实数据）
     */
    @Override
    public List<Map<String, Object>> getSubjectDistributionData(Long userId, String startDate, String endDate)
    {
        // 如果没有传入日期范围，默认查询最近30天
        if (startDate == null || startDate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            startDate = sdf.format(calendar.getTime());
        }
        
        return studyStatisticsMapper.selectSubjectDistributionByUserId(userId, startDate, endDate);
    }

    /**
     * 查询学习时间按小时分布（从tomato_record获取真实数据）
     */
    @Override
    public List<Map<String, Object>> getTimeDistributionData(Long userId, String startDate, String endDate)
    {
        // 如果没有传入日期范围，默认查询最近7天
        if (startDate == null || startDate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            startDate = sdf.format(calendar.getTime());
        }
        
        List<Map<String, Object>> rawData = studyStatisticsMapper.selectTimeDistributionByUserId(userId, startDate, endDate);
        
        // 构建完整的时间段（0-24小时，每2小时一个区间）
        Map<Integer, Integer> hourMap = new LinkedHashMap<>();
        for (int h = 0; h < 24; h += 2) {
            hourMap.put(h, 0);
        }
        
        // 填充实际数据
        for (Map<String, Object> row : rawData) {
            int hourVal = row.get("hourVal") != null ? ((Number) row.get("hourVal")).intValue() : -1;
            int minutes = row.get("totalMinutes") != null ? ((Number) row.get("totalMinutes")).intValue() : 0;
            // 映射到2小时区间（0->0, 1->0, 2->2, 3->2, ...）
            int slotHour = (hourVal / 2) * 2;
            if (hourMap.containsKey(slotHour)) {
                hourMap.put(slotHour, hourMap.get(slotHour) + minutes);
            }
        }
        
        // 转为列表返回
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : hourMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("timeSlot", entry.getKey() + "-" + (entry.getKey() + 2));
            item.put("totalMinutes", entry.getValue());
            result.add(item);
        }
        
        return result;
    }

    /**
     * 计算学习效率评分
     */
    @Override
    public double calculateProductivityScore(Long userId, String studyDate)
    {
        StudyStatistics statistics = studyStatisticsMapper.selectStudyStatisticsByUserIdAndDate(userId, studyDate);
        if (statistics == null) {
            return 0.0;
        }
        
        // 效率评分算法：完成计划数 * 2 + 学习时长/60
        double score = (statistics.getCompletedPlans() != null ? statistics.getCompletedPlans() : 0) * 2.0 
            + (statistics.getTotalTimeSpent() != null ? statistics.getTotalTimeSpent() : 0) / 60.0;
        return Math.min(score, 10.0); // 最高10分
    }
}
