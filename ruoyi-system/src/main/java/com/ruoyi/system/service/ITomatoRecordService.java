package com.ruoyi.system.service;

import com.ruoyi.system.domain.TomatoRecord;
import java.util.List;

/**
 * 番茄钟记录Service接口
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public interface ITomatoRecordService 
{
    /**
     * 查询番茄钟记录
     * 
     * @param recordId 番茄钟记录主键
     * @return 番茄钟记录
     */
    public TomatoRecord selectTomatoRecordByRecordId(Long recordId);

    /**
     * 查询番茄钟记录列表
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 番茄钟记录集合
     */
    public List<TomatoRecord> selectTomatoRecordList(TomatoRecord tomatoRecord);

    /**
     * 新增番茄钟记录
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 结果
     */
    public int insertTomatoRecord(TomatoRecord tomatoRecord);

    /**
     * 修改番茄钟记录
     * 
     * @param tomatoRecord 番茄钟记录
     * @return 结果
     */
    public int updateTomatoRecord(TomatoRecord tomatoRecord);

    /**
     * 批量删除番茄钟记录
     * 
     * @param recordIds 需要删除的番茄钟记录主键集合
     * @return 结果
     */
    public int deleteTomatoRecordByRecordIds(Long[] recordIds);

    /**
     * 删除番茄钟记录信息
     * 
     * @param recordId 番茄钟记录主键
     * @return 结果
     */
    public int deleteTomatoRecordByRecordId(Long recordId);

    /**
     * 根据用户ID查询番茄钟记录列表
     * 
     * @param userId 用户ID
     * @return 番茄钟记录集合
     */
    public List<TomatoRecord> selectTomatoRecordByUserId(Long userId);

    /**
     * 查询用户今日番茄钟记录
     * 
     * @param userId 用户ID
     * @return 番茄钟记录集合
     */
    public List<TomatoRecord> selectTodayTomatoRecordByUserId(Long userId);

    /**
     * 启动番茄钟
     * 
     * @param userId 用户ID
     * @param planId 学习计划ID
     * @param tomatoDuration 番茄钟时长(分钟)
     * @param restDuration 休息时长(分钟)
     * @return 番茄钟记录
     */
    public TomatoRecord startTomato(Long userId, Long planId, Integer tomatoDuration, Integer restDuration);

    /**
     * 完成番茄钟
     * 
     * @param recordId 记录ID
     * @return 结果
     */
    public int completeTomato(Long recordId);

    /**
     * 中断番茄钟
     * 
     * @param recordId 记录ID
     * @param reason 中断原因
     * @return 结果
     */
    public int interruptTomato(Long recordId, String reason);

    /**
     * 查询用户正在进行的番茄钟
     * 
     * @param userId 用户ID
     * @return 番茄钟记录
     */
    public TomatoRecord getActiveTomato(Long userId);

    /**
     * 统计用户番茄钟完成情况
     * 
     * @param userId 用户ID
     * @return 统计结果
     */
    public int countCompletedTomatoByUserId(Long userId);
}