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
import com.ruoyi.system.domain.TomatoRecord;
import com.ruoyi.system.service.ITomatoRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 番茄钟记录Controller
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@RestController
@RequestMapping("/study/tomato")
public class TomatoRecordController extends BaseController
{
    @Autowired
    private ITomatoRecordService tomatoRecordService;

    /**
     * 查询番茄钟记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TomatoRecord tomatoRecord)
    {
        // 只能查看自己的番茄钟记录
        tomatoRecord.setUserId(SecurityUtils.getUserId());
        startPage();
        List<TomatoRecord> list = tomatoRecordService.selectTomatoRecordList(tomatoRecord);
        return getDataTable(list);
    }

    /**
     * 导出番茄钟记录列表
     */
    @PreAuthorize("@ss.hasPermi('study:tomato:export')")
    @Log(title = "番茄钟记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TomatoRecord tomatoRecord)
    {
        tomatoRecord.setUserId(SecurityUtils.getUserId());
        List<TomatoRecord> list = tomatoRecordService.selectTomatoRecordList(tomatoRecord);
        ExcelUtil<TomatoRecord> util = new ExcelUtil<TomatoRecord>(TomatoRecord.class);
        util.exportExcel(response, list, "番茄钟记录数据");
    }

    /**
     * 获取番茄钟记录详细信息
     */
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
        if (record == null) {
            return AjaxResult.error("记录不存在");
        }
        // 权限检查：只能查看自己的记录
        if (!record.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限访问该番茄钟记录");
        }
        return AjaxResult.success(record);
    }

    /**
     * 新增番茄钟记录
     */
    @Log(title = "番茄钟记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TomatoRecord tomatoRecord)
    {
        tomatoRecord.setUserId(SecurityUtils.getUserId());
        return toAjax(tomatoRecordService.insertTomatoRecord(tomatoRecord));
    }

    /**
     * 修改番茄钟记录
     */
    @PreAuthorize("@ss.hasPermi('study:tomato:edit')")
    @Log(title = "番茄钟记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TomatoRecord tomatoRecord)
    {
        // 权限检查：只能修改自己的记录
        TomatoRecord oldRecord = tomatoRecordService.selectTomatoRecordByRecordId(tomatoRecord.getRecordId());
        if (!oldRecord.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限修改该番茄钟记录");
        }
        return toAjax(tomatoRecordService.updateTomatoRecord(tomatoRecord));
    }

    /**
     * 删除番茄钟记录
     */
    @PreAuthorize("@ss.hasPermi('study:tomato:remove')")
    @Log(title = "番茄钟记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        // 权限检查：只能删除自己的记录
        for (Long recordId : recordIds) {
            TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
            if (!record.getUserId().equals(SecurityUtils.getUserId())) {
                return AjaxResult.error("无权限删除该番茄钟记录: " + recordId);
            }
        }
        return toAjax(tomatoRecordService.deleteTomatoRecordByRecordIds(recordIds));
    }

    /**
     * 启动番茄钟
     */
    @Log(title = "番茄钟", businessType = BusinessType.INSERT)
    @PostMapping("/start")
    public AjaxResult startTomato(@RequestBody TomatoStartRequest request)
    {
        try {
            System.out.println("=== 启动番茄钟请求 ===");
            System.out.println("planId: " + request.getPlanId());
            System.out.println("tomatoDuration: " + request.getTomatoDuration());
            System.out.println("restDuration: " + request.getRestDuration());
            
            TomatoRecord record = tomatoRecordService.startTomato(
                SecurityUtils.getUserId(),
                request.getPlanId(),
                request.getTomatoDuration(),
                request.getRestDuration()
            );
            
            System.out.println("=== 番茄钟创建成功 ===");
            System.out.println("recordId: " + record.getRecordId());
            System.out.println("userId: " + record.getUserId());
            System.out.println("tomatoDuration: " + record.getTomatoDuration());
            
            return AjaxResult.success(record);
        } catch (Exception e) {
            System.out.println("=== 启动番茄钟失败 ===");
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 完成番茄钟
     */
    @PutMapping("/complete/{recordId}")
    public AjaxResult completeTomato(@PathVariable Long recordId)
    {
        try {
            System.out.println("=== 接收完成番茄钟请求 ===");
            System.out.println("recordId: " + recordId);
            
            // 权限检查
            TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
            if (record == null) {
                System.out.println("记录不存在: " + recordId);
                return AjaxResult.error("记录不存在");
            }
            
            if (!record.getUserId().equals(SecurityUtils.getUserId())) {
                return AjaxResult.error("无权限操作该番茄钟");
            }
            
            int result = tomatoRecordService.completeTomato(recordId);
            System.out.println("=== 完成番茄钟结果 ===");
            System.out.println("recordId: " + recordId);
            System.out.println("result: " + result);
            
            return toAjax(result);
        } catch (Exception e) {
            System.out.println("=== 完成番茄钟异常 ===");
            e.printStackTrace();
            return AjaxResult.error("完成失败: " + e.getMessage());
        }
    }

    /**
     * 暂停番茄钟
     */
    @PutMapping("/pause/{recordId}")
    public AjaxResult pauseTomato(@PathVariable Long recordId)
    {
        // 权限检查
        TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
        if (record == null) {
            return AjaxResult.error("记录不存在");
        }
        if (!record.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限操作该番茄钟");
        }
        return toAjax(tomatoRecordService.pauseTomato(recordId));
    }

    /**
     * 恢复番茄钟
     */
    @PutMapping("/resume/{recordId}")
    public AjaxResult resumeTomato(@PathVariable Long recordId)
    {
        // 权限检查
        TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
        if (record == null) {
            return AjaxResult.error("记录不存在");
        }
        if (!record.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限操作该番茄钟");
        }
        return toAjax(tomatoRecordService.resumeTomato(recordId));
    }

    /**
     * 放弃番茄钟
     */
    @PutMapping("/abandon/{recordId}")
    public AjaxResult abandonTomato(@PathVariable Long recordId)
    {
        // 权限检查
        TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
        if (record == null) {
            return AjaxResult.error("记录不存在");
        }
        if (!record.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限操作该番茄钟");
        }
        return toAjax(tomatoRecordService.abandonTomato(recordId));
    }

    /**
     * 中断番茄钟
     */
    @Log(title = "番茄钟", businessType = BusinessType.UPDATE)
    @PutMapping("/interrupt/{recordId}")
    public AjaxResult interruptTomato(@PathVariable Long recordId, @RequestBody String reason)
    {
        // 权限检查
        TomatoRecord record = tomatoRecordService.selectTomatoRecordByRecordId(recordId);
        if (!record.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("无权限操作该番茄钟");
        }
        return toAjax(tomatoRecordService.interruptTomato(recordId, reason));
    }

    /**
     * 查询正在进行的番茄钟
     */
    @GetMapping("/active")
    public AjaxResult getActiveTomato()
    {
        TomatoRecord record = tomatoRecordService.getActiveTomato(SecurityUtils.getUserId());
        return AjaxResult.success(record);
    }

    /**
     * 强制中断所有进行中的番茄钟
     */
    @PutMapping("/force-stop")
    public AjaxResult forceStopActiveTomato()
    {
        try {
            TomatoRecord activeRecord = tomatoRecordService.getActiveTomato(SecurityUtils.getUserId());
            if (activeRecord != null) {
                System.out.println("=== 强制中断进行中的番茄钟 ===");
                System.out.println("recordId: " + activeRecord.getRecordId());
                tomatoRecordService.abandonTomato(activeRecord.getRecordId());
                return AjaxResult.success("已中断之前的番茄钟，可以开始新的了");
            }
            return AjaxResult.success("没有进行中的番茄钟");
        } catch (Exception e) {
            System.out.println("=== 强制中断失败 ===");
            e.printStackTrace();
            return AjaxResult.error("中断失败: " + e.getMessage());
        }
    }

    /**
     * 查询今日番茄钟记录
     */
    @GetMapping("/today")
    public AjaxResult getTodayTomatoRecords()
    {
        List<TomatoRecord> records = tomatoRecordService.selectTodayTomatoRecordByUserId(SecurityUtils.getUserId());
        return AjaxResult.success(records);
    }

    /**
     * 统计番茄钟完成情况
     */
    @GetMapping("/statistics")
    public AjaxResult getTomatoStatistics()
    {
        try {
            Long userId = SecurityUtils.getUserId();
            System.out.println("=== 接收到统计请求 ===");
            System.out.println("userId: " + userId);
            
            java.util.Map<String, Object> result = tomatoRecordService.getTomatoStatistics(userId);
            
            System.out.println("=== 统计数据查询结果 ===");
            System.out.println("userId: " + userId);
            System.out.println("todayCount: " + result.get("todayCount"));
            System.out.println("weekCount: " + result.get("weekCount"));
            System.out.println("monthCount: " + result.get("monthCount"));
            System.out.println("totalCount: " + result.get("totalCount"));
            System.out.println("========================");
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            System.out.println("=== 番茄钟统计接口异常 ===");
            e.printStackTrace();
            return AjaxResult.error("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 番茄钟启动请求参数类
     */
    public static class TomatoStartRequest {
        private Long planId;
        private Integer tomatoDuration;
        private Integer restDuration;

        public Long getPlanId() {
            return planId;
        }

        public void setPlanId(Long planId) {
            this.planId = planId;
        }

        public Integer getTomatoDuration() {
            return tomatoDuration;
        }

        public void setTomatoDuration(Integer tomatoDuration) {
            this.tomatoDuration = tomatoDuration;
        }

        public Integer getRestDuration() {
            return restDuration;
        }

        public void setRestDuration(Integer restDuration) {
            this.restDuration = restDuration;
        }
    }
}