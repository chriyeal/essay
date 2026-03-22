package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TomatoRecordMapper;
import com.ruoyi.system.domain.TomatoRecord;
import com.ruoyi.system.service.ITomatoRecordService;

/**
 * 番茄钟记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
@Service
public class TomatoRecordServiceImpl implements ITomatoRecordService
{
    @Autowired
    private TomatoRecordMapper tomatoRecordMapper;

    /**
     * 查询番茄钟记录
     * 
     * @param recordId 番茄钟记录主键
     * @return 番茄钟记录
     */
    @Override
    public TomatoRecord selectTomatoRecordByRecordId(Long recordId)
    {
        return tomatoRecordMapper.selectTomatoRecordByRecordId(recordId);
    }

    /**
     * 查询番茄钟记录列表
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 番茄钟记录
     */
    @Override
    public List<TomatoRecord> selectTomatoRecordList(TomatoRecord tomatoRecord)
    {
        return tomatoRecordMapper.selectTomatoRecordList(tomatoRecord);
    }

    /**
     * 新增番茄钟记录
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 结果
     */
    @Override
    public int insertTomatoRecord(TomatoRecord tomatoRecord)
    {
        tomatoRecord.setCreateTime(DateUtils.getNowDate());
        return tomatoRecordMapper.insertTomatoRecord(tomatoRecord);
    }

    /**
     * 修改番茄钟记录
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 结果
     */
    @Override
    public int updateTomatoRecord(TomatoRecord tomatoRecord)
    {
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
    }

    /**
     * 批量删除番茄钟记录
     * 
     * @param recordIds 需要删除的番茄钟记录主键
     * @return 结果
     */
    @Override
    public int deleteTomatoRecordByRecordIds(Long[] recordIds)
    {
        return tomatoRecordMapper.deleteTomatoRecordByRecordIds(recordIds);
    }

    /**
     * 删除番茄钟记录信息
     * 
     * @param recordId 番茄钟记录主键
     * @return 结果
     */
    @Override
    public int deleteTomatoRecordByRecordId(Long recordId)
    {
        return tomatoRecordMapper.deleteTomatoRecordByRecordId(recordId);
    }

    /**
     * 根据用户ID查询番茄钟记录列表
     * 
     * @param userId 用户ID
     * @return 番茄钟记录集合
     */
    @Override
    public List<TomatoRecord> selectTomatoRecordByUserId(Long userId)
    {
        return tomatoRecordMapper.selectTomatoRecordByUserId(userId);
    }

    /**
     * 查询用户今日番茄钟记录
     * 
     * @param userId 用户ID
     * @return 番茄钟记录集合
     */
    @Override
    public List<TomatoRecord> selectTodayTomatoRecordByUserId(Long userId)
    {
        return tomatoRecordMapper.selectTodayTomatoRecordByUserId(userId);
    }

    /**
     * 启动番茄钟
     * 
     * @param userId 用户ID
     * @param planId 学习计划ID
     * @param tomatoDuration 番茄钟时长(分钟)
     * @param restDuration 休息时长(分钟)
     * @return 番茄钟记录
     */
    @Override
    public TomatoRecord startTomato(Long userId, Long planId, Integer tomatoDuration, Integer restDuration)
    {
        // 检查是否已有正在进行的番茄钟
        TomatoRecord activeTomato = tomatoRecordMapper.selectActiveTomatoByUserId(userId);
        if (activeTomato != null) {
            throw new RuntimeException("已有正在进行的番茄钟，请先完成或中断当前番茄钟");
        }
        
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setUserId(userId);
        tomatoRecord.setPlanId(planId);
        tomatoRecord.setTomatoDuration(tomatoDuration != null ? tomatoDuration : 25);
        tomatoRecord.setRestDuration(restDuration != null ? restDuration : 5);
        tomatoRecord.setStartTime(new Date());
        tomatoRecord.setStatus(0); // 进行中
        
        tomatoRecordMapper.insertTomatoRecord(tomatoRecord);
        return tomatoRecord;
    }

    /**
     * 完成番茄钟
     * 
     * @param recordId 记录 ID
     * @return 结果
     */
    @Override
    public int completeTomato(Long recordId)
    {
        TomatoRecord tomatoRecord = tomatoRecordMapper.selectTomatoRecordByRecordId(recordId);
        if (tomatoRecord == null) {
            throw new RuntimeException("番茄钟记录不存在");
        }
            
        // 更新当前记录为已完成
        TomatoRecord updateRecord = new TomatoRecord();
        updateRecord.setRecordId(recordId);
        updateRecord.setEndTime(new Date());
        updateRecord.setStatus(1); // 已完成
        int result = tomatoRecordMapper.updateTomatoRecord(updateRecord);
            
        System.out.println("=== 完成番茄钟 ===");
        System.out.println("recordId: " + recordId);
        System.out.println("userId: " + tomatoRecord.getUserId());
        System.out.println("tomatoDuration: " + tomatoRecord.getTomatoDuration());
            
        // 更新今日统计数据
        try {
            updateTodayStatistics(tomatoRecord.getUserId(), tomatoRecord.getTomatoDuration());
            System.out.println("统计数据已更新");
        } catch (Exception e) {
            System.err.println("更新统计数据失败：" + e.getMessage());
        }
            
        return result;
    }
        
    /**
     * 更新今日统计数据
     * 
     * @param userId 用户 ID
     * @param duration 本次番茄钟时长（分钟）
     */
    private void updateTodayStatistics(Long userId, Integer duration)
    {
        // 这里不需要具体实现，因为首页统计是通过 SQL 直接查询 tomato_record 表的
        // 只要 tomato_record 表的状态更新了，统计数据就会自动更新
        System.out.println("准备更新用户 " + userId + " 的统计数据，时长：" + duration + "分钟");
    }

    /**
     * 中断番茄钟
     * 
     * @param recordId 记录ID
     * @param reason 中断原因
     * @return 结果
     */
    @Override
    public int interruptTomato(Long recordId, String reason)
    {
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setRecordId(recordId);
        tomatoRecord.setEndTime(new Date());
        tomatoRecord.setStatus(2); // 已中断
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
    }

    /**
     * 暂停番茄钟
     * 
     * @param recordId 记录ID
     * @return 结果
     */
    @Override
    public int pauseTomato(Long recordId)
    {
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setRecordId(recordId);
        tomatoRecord.setStatus(3); // 已暂停
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
    }

    /**
     * 恢复番茄钟
     * 
     * @param recordId 记录ID
     * @return 结果
     */
    @Override
    public int resumeTomato(Long recordId)
    {
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setRecordId(recordId);
        tomatoRecord.setStatus(0); // 恢复为进行中
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
    }

    /**
     * 放弃番茄钟
     * 
     * @param recordId 记录ID
     * @return 结果
     */
    @Override
    public int abandonTomato(Long recordId)
    {
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setRecordId(recordId);
        tomatoRecord.setEndTime(new Date());
        tomatoRecord.setStatus(4); // 已放弃
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
    }

    /**
     * 查询用户正在进行的番茄钟
     * 
     * @param userId 用户 ID
     * @return 番茄钟记录
     */
    @Override
    public TomatoRecord getActiveTomato(Long userId)
    {
        return tomatoRecordMapper.selectActiveTomatoByUserId(userId);
    }

    /**
     * 统计用户番茄钟完成情况
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    @Override
    public int countCompletedTomatoByUserId(Long userId)
    {
        return tomatoRecordMapper.countCompletedTomatoByUserId(userId);
    }
    
    /**
     * 获取番茄钟统计数据
     * 
     * @param userId 用户 ID
     * @return 统计结果 Map
     */
    public java.util.Map<String, Object> getTomatoStatistics(Long userId)
    {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        int todayCount = tomatoRecordMapper.countTodayCompletedTomatoByUserId(userId);
        int weekCount = tomatoRecordMapper.countWeekCompletedTomatoByUserId(userId);
        int monthCount = tomatoRecordMapper.countMonthCompletedTomatoByUserId(userId);
        int totalCount = tomatoRecordMapper.countTotalCompletedTomatoByUserId(userId);
        
        System.out.println("=== 番茄钟统计数据 ===");
        System.out.println("userId: " + userId);
        System.out.println("todayCount: " + todayCount);
        System.out.println("weekCount: " + weekCount);
        System.out.println("monthCount: " + monthCount);
        System.out.println("totalCount: " + totalCount);
        
        result.put("todayCount", todayCount);
        result.put("weekCount", weekCount);
        result.put("monthCount", monthCount);
        result.put("totalCount", totalCount);
        
        return result;
    }
}