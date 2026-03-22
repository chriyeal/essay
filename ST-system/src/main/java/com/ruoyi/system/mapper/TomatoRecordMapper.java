package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TomatoRecord;
import java.util.List;

/**
 * 番茄钟记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-18
 */
public interface TomatoRecordMapper 
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
     * 删除番茄钟记录
     * 
     * @param recordId 番茄钟记录主键
     * @return 结果
     */
    public int deleteTomatoRecordByRecordId(Long recordId);

    /**
     * 批量删除番茄钟记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTomatoRecordByRecordIds(Long[] recordIds);

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
     * 统计用户番茄钟完成情况
     * 
     * @param userId 用户ID
     * @return 统计结果
     */
    public int countCompletedTomatoByUserId(Long userId);

    /**
     * 查询正在进行的番茄钟
     * 
     * @param userId 用户 ID
     * @return 番茄钟记录
     */
    public TomatoRecord selectActiveTomatoByUserId(Long userId);
    
    /**
     * 统计今日完成的番茄钟数量
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    public int countTodayCompletedTomatoByUserId(Long userId);
    
    /**
     * 统计本周完成的番茄钟数量
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    public int countWeekCompletedTomatoByUserId(Long userId);
    
    /**
     * 统计本月完成的番茄钟数量
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    public int countMonthCompletedTomatoByUserId(Long userId);
    
    /**
     * 统计总共完成的番茄钟数量
     * 
     * @param userId 用户 ID
     * @return 统计结果
     */
    public int countTotalCompletedTomatoByUserId(Long userId);
}