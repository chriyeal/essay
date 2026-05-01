package com.ruoyi.web.controller.study;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.StudyStatistics;
import com.ruoyi.system.service.IStudyStatisticsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学习统计数据Controller
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@RestController
@RequestMapping("/study/statistics")
public class StudyStatisticsController extends BaseController
{
    @Autowired
    private IStudyStatisticsService studyStatisticsService;

    /**
     * 查询学习统计数据列表
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudyStatistics studyStatistics)
    {
        studyStatistics.setUserId(SecurityUtils.getUserId());
        startPage();
        List<StudyStatistics> list = studyStatisticsService.selectStudyStatisticsList(studyStatistics);
        return getDataTable(list);
    }

    /**
     * 导出学习统计数据列表
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:export')")
    @Log(title = "学习统计数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudyStatistics studyStatistics)
    {
        studyStatistics.setUserId(SecurityUtils.getUserId());
        List<StudyStatistics> list = studyStatisticsService.selectStudyStatisticsList(studyStatistics);
        ExcelUtil<StudyStatistics> util = new ExcelUtil<StudyStatistics>(StudyStatistics.class);
        util.exportExcel(response, list, "学习统计数据");
    }

    /**
     * 获取学习统计数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:query')")
    @GetMapping(value = "/detail/{statId}")
    public AjaxResult getInfo(@PathVariable("statId") Long statId)
    {
        StudyStatistics statistics = studyStatisticsService.selectStudyStatisticsByStatId(statId);
        if (!statistics.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限访问该统计数据");
        }
        return AjaxResult.success(statistics);
    }

    /**
     * 获取成就数据
     */
    @GetMapping("/achievements")
    public AjaxResult getAchievements()
    {
        return AjaxResult.success(new ArrayList<>());
    }

    /**
     * 新增学习统计数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:add')")
    @Log(title = "学习统计数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudyStatistics studyStatistics)
    {
        studyStatistics.setUserId(SecurityUtils.getUserId());
        return toAjax(studyStatisticsService.insertStudyStatistics(studyStatistics));
    }

    /**
     * 修改学习统计数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:edit')")
    @Log(title = "学习统计数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudyStatistics studyStatistics)
    {
        StudyStatistics oldStats = studyStatisticsService.selectStudyStatisticsByStatId(studyStatistics.getStatId());
        if (!oldStats.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限修改该统计数据");
        }
        return toAjax(studyStatisticsService.updateStudyStatistics(studyStatistics));
    }

    /**
     * 删除学习统计数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:remove')")
    @Log(title = "学习统计数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{statIds}")
    public AjaxResult remove(@PathVariable Long[] statIds)
    {
        for (Long statId : statIds) {
            StudyStatistics stats = studyStatisticsService.selectStudyStatisticsByStatId(statId);
            if (!stats.getUserId().equals(SecurityUtils.getUserId())) {
                return AjaxResult.error("无权限删除该统计数据: " + statId);
            }
        }
        return toAjax(studyStatisticsService.deleteStudyStatisticsByStatIds(statIds));
    }

    /**
     * 查询用户最近7天学习统计数据
     */
    @GetMapping("/recent")
    public AjaxResult getRecentStatistics()
    {
        List<StudyStatistics> statistics = studyStatisticsService.selectRecentStudyStatisticsByUserId(SecurityUtils.getUserId());
        return AjaxResult.success(statistics);
    }

    /**
     * 查询用户学习统计汇总（今日数据） - 首页使用，无需权限
     */
    @GetMapping("/summary")
    public AjaxResult getStudySummary()
    {
        try {
            Long userId = SecurityUtils.getUserId();
            Map<String, Object> summary = studyStatisticsService.selectStudySummaryByUserId(userId);
            return AjaxResult.success(summary);
        } catch (Exception e) {
            logger.error("获取统计数据失败", e);
            return AjaxResult.error("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 更新当日学习统计数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:update')")
    @Log(title = "学习统计数据", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDaily")
    public AjaxResult updateDailyStatistics(
            @RequestParam(required = false) Integer planTimeSpent,
            @RequestParam(required = false) Integer tomatoTimeSpent,
            @RequestParam(required = false) Integer completedPlans)
    {
        int result = studyStatisticsService.updateDailyStudyStatistics(
            SecurityUtils.getUserId(),
            planTimeSpent,
            tomatoTimeSpent,
            completedPlans
        );
        return toAjax(result);
    }

    /**
     * 获取学习趋势数据（真实数据）
     */
    @GetMapping("/trend")
    public AjaxResult getStudyTrend(@RequestParam(defaultValue = "7") Integer days)
    {
        List<Map<String, Object>> trendData = studyStatisticsService.getStudyTrendData(SecurityUtils.getUserId(), days);
        return AjaxResult.success(trendData);
    }

    /**
     * 获取学科分布数据（真实数据）
     */
    @GetMapping("/distribution")
    public AjaxResult getSubjectDistribution(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate)
    {
        List<Map<String, Object>> distributionData = studyStatisticsService.getSubjectDistributionData(
            SecurityUtils.getUserId(), startDate, endDate);
        return AjaxResult.success(distributionData);
    }

    /**
     * 获取学习时间分布数据（按小时，真实数据）
     */
    @GetMapping("/time-distribution")
    public AjaxResult getTimeDistribution(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate)
    {
        List<Map<String, Object>> timeData = studyStatisticsService.getTimeDistributionData(
            SecurityUtils.getUserId(), startDate, endDate);
        return AjaxResult.success(timeData);
    }

    /**
     * 获取学习效率评分
     */
    @GetMapping("/productivity")
    public AjaxResult getProductivityScore(@RequestParam(required = false) String studyDate)
    {
        if (studyDate == null || studyDate.isEmpty()) {
            studyDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        double score = studyStatisticsService.calculateProductivityScore(SecurityUtils.getUserId(), studyDate);
        return AjaxResult.success(score);
    }
}
