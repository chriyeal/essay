package com.ruoyi.web.controller.study;

import java.util.List;
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
        // 只能查看自己的统计数据
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
        // 权限检查：只能查看自己的统计数据
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
        // 返回空列表，成就功能暂未实现
        return AjaxResult.success(new java.util.ArrayList<>());
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
        // 权限检查：只能修改自己的统计数据
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
        // 权限检查：只能删除自己的统计数据
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
    @PreAuthorize("@ss.hasPermi('study:statistics:query')")
    @GetMapping("/recent")
    public AjaxResult getRecentStatistics()
    {
        List<StudyStatistics> statistics = studyStatisticsService.selectRecentStudyStatisticsByUserId(SecurityUtils.getUserId());
        return AjaxResult.success(statistics);
    }

    /**
     * 查询用户学习统计汇总
     */
    @GetMapping("/summary")
    public AjaxResult getStudySummary()
    {
        try {
            Long userId = SecurityUtils.getUserId();
            System.out.println("=== 接收到首页统计请求 ===");
            System.out.println("userId: " + userId);
            
            java.util.Map<String, Object> summary = studyStatisticsService.selectStudySummaryByUserId(userId);
            
            System.out.println("=== 首页统计数据查询结果 ===");
            System.out.println("userId: " + userId);
            System.out.println("plan_count: " + summary.get("plan_count"));
            System.out.println("study_hours: " + summary.get("study_hours"));
            System.out.println("completed_tasks: " + summary.get("completed_tasks"));
            System.out.println("============================");
            
            return AjaxResult.success(summary);
        } catch (Exception e) {
            System.out.println("=== 首页统计接口异常 ===");
            e.printStackTrace();
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
     * 获取学习趋势数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:query')")
    @GetMapping("/trend")
    public AjaxResult getStudyTrend(@RequestParam(defaultValue = "7") Integer days)
    {
        List<StudyStatistics> trendData = studyStatisticsService.generateStudyTrendData(SecurityUtils.getUserId(), days);
        return AjaxResult.success(trendData);
    }

    /**
     * 获取学习时间分布数据
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:query')")
    @GetMapping("/distribution")
    public AjaxResult getTimeDistribution(@RequestParam(required = false) String date)
    {
        String distributionData = studyStatisticsService.generateTimeDistributionData(SecurityUtils.getUserId(), date);
        return AjaxResult.success(distributionData);
    }

    /**
     * 获取学习效率评分
     */
    @PreAuthorize("@ss.hasPermi('study:statistics:query')")
    @GetMapping("/productivity")
    public AjaxResult getProductivityScore(@RequestParam String studyDate)
    {
        double score = studyStatisticsService.calculateProductivityScore(SecurityUtils.getUserId(), studyDate);
        return AjaxResult.success(score);
    }
}