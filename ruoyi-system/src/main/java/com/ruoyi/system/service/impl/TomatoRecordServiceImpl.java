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
     * @param recordId 记录ID
     * @return 结果
     */
    @Override
    public int completeTomato(Long recordId)
    {
        TomatoRecord tomatoRecord = new TomatoRecord();
        tomatoRecord.setRecordId(recordId);
        tomatoRecord.setEndTime(new Date());
        tomatoRecord.setStatus(1); // 已完成
        return tomatoRecordMapper.updateTomatoRecord(tomatoRecord);
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
     * 查询用户正在进行的番茄钟
     * 
     * @param userId 用户ID
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
     * @param userId 用户ID
     * @return 统计结果
     */
    @Override
    public int countCompletedTomatoByUserId(Long userId)
    {
        return tomatoRecordMapper.countCompletedTomatoByUserId(userId);
    }
}