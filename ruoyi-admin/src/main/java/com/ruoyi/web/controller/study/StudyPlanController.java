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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.StudyPlan;
import com.ruoyi.system.service.IStudyPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学习计划Controller
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@RestController
@RequestMapping("/study/plan")
public class StudyPlanController extends BaseController
{
    @Autowired
    private IStudyPlanService studyPlanService;

    /**
     * 查询学习计划列表
     */
    @PreAuthorize("@ss.hasPermi('study:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudyPlan studyPlan)
    {
        // 只能查看自己的学习计划
        studyPlan.setUserId(SecurityUtils.getUserId());
        startPage();
        List<StudyPlan> list = studyPlanService.selectStudyPlanList(studyPlan);
        return getDataTable(list);
    }

    /**
     * 导出学习计划列表
     */
    @PreAuthorize("@ss.hasPermi('study:plan:export')")
    @Log(title = "学习计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudyPlan studyPlan)
    {
        studyPlan.setUserId(SecurityUtils.getUserId());
        List<StudyPlan> list = studyPlanService.selectStudyPlanList(studyPlan);
        ExcelUtil<StudyPlan> util = new ExcelUtil<StudyPlan>(StudyPlan.class);
        util.exportExcel(response, list, "学习计划数据");
    }

    /**
     * 获取学习计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('study:plan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId)
    {
        StudyPlan plan = studyPlanService.selectStudyPlanByPlanId(planId);
        // 权限检查：只能查看自己的计划
        if (!plan.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限访问该学习计划");
        }
        return AjaxResult.success(plan);
    }

    /**
     * 新增学习计划
     */
    @PreAuthorize("@ss.hasPermi('study:plan:add')")
    @Log(title = "学习计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudyPlan studyPlan)
    {
        studyPlan.setUserId(SecurityUtils.getUserId());
        return toAjax(studyPlanService.insertStudyPlan(studyPlan));
    }

    /**
     * 修改学习计划
     */
    @PreAuthorize("@ss.hasPermi('study:plan:edit')")
    @Log(title = "学习计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudyPlan studyPlan)
    {
        // 权限检查：只能修改自己的计划
        StudyPlan oldPlan = studyPlanService.selectStudyPlanByPlanId(studyPlan.getPlanId());
        if (!oldPlan.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限修改该学习计划");
        }
        return toAjax(studyPlanService.updateStudyPlan(studyPlan));
    }

    /**
     * 删除学习计划
     */
    @PreAuthorize("@ss.hasPermi('study:plan:remove')")
    @Log(title = "学习计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds)
    {
        // 权限检查：只能删除自己的计划
        for (Long planId : planIds) {
            StudyPlan plan = studyPlanService.selectStudyPlanByPlanId(planId);
            if (!plan.getUserId().equals(SecurityUtils.getUserId())) {
                return AjaxResult.error("无权限删除该学习计划: " + planId);
            }
        }
        return toAjax(studyPlanService.deleteStudyPlanByPlanIds(planIds));
    }

    /**
     * 标记学习计划为已完成
     */
    @PreAuthorize("@ss.hasPermi('study:plan:complete')")
    @Log(title = "学习计划", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{planId}")
    public AjaxResult complete(@PathVariable Long planId, @RequestBody(required = false) Double actualHours)
    {
        // 权限检查
        StudyPlan plan = studyPlanService.selectStudyPlanByPlanId(planId);
        if (!plan.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限操作该学习计划");
        }
        return toAjax(studyPlanService.completeStudyPlan(planId, actualHours));
    }

    /**
     * 生成每日学习计划
     */
    @PreAuthorize("@ss.hasPermi('study:plan:generate')")
    @Log(title = "学习计划", businessType = BusinessType.OTHER)
    @PostMapping("/generate")
    public AjaxResult generateDailyPlan()
    {
        List<StudyPlan> plans = studyPlanService.generateDailyStudyPlan(SecurityUtils.getUserId());
        return AjaxResult.success(plans);
    }

    /**
     * 查询今日学习计划
     */
    @PreAuthorize("@ss.hasPermi('study:plan:list')")
    @GetMapping("/today")
    public AjaxResult getTodayPlans()
    {
        List<StudyPlan> plans = studyPlanService.selectTodayStudyPlanByUserId(SecurityUtils.getUserId());
        return AjaxResult.success(plans);
    }

    /**
     * 统计学习计划完成情况
     */
    @PreAuthorize("@ss.hasPermi('study:plan:query')")
    @GetMapping("/statistics")
    public AjaxResult getPlanStatistics()
    {
        int completedCount = studyPlanService.countCompletedPlansByUserId(SecurityUtils.getUserId());
        return AjaxResult.success(completedCount);
    }
}