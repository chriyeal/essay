package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 用户 业务层
 */
public interface ISysUserService
{
    /**
     * 根据条件分页查询用户列表
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 通过用户名查询用户
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     */
    public SysUser selectUserById(Long userId);

    /**
     * 根据用户ID查询用户所属角色组
     */
    public String selectUserRoleGroup(String userName);

    /**
     * 根据用户ID查询用户所属岗位组
     */
    public String selectUserPostGroup(String userName);

    /**
     * 校验用户名称是否唯一
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 校验用户是否有数据权限
     */
    public void checkUserDataScope(Long userId);

    /**
     * 新增用户信息
     */
    public int insertUser(SysUser user);

    /**
     * 注册用户信息
     */
    public boolean registerUser(SysUser user);

    /**
     * 修改用户信息
     */
    public int updateUser(SysUser user);

    /**
     * 用户授权角色
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * 修改用户状态
     */
    public int updateUserStatus(SysUser user);

    /**
     * 修改用户基本信息
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     */
    public boolean updateUserAvatar(Long userId, String avatar);

    /**
     * 更新用户登录信息（IP和登录时间）
     */
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate);

    /**
     * 重置用户密码
     */
    public int resetPwd(SysUser user);

    /**
     * 重置用户密码
     */
    public int resetUserPwd(Long userId, String password);

    /**
     * 通过用户ID删除用户
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 导入用户数据
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);
}
