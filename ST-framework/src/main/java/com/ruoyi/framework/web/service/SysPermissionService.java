package com.ruoyi.framework.web.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 用户权限处理 - 简化版（基于userType）
 */
@Component
public class SysPermissionService
{
    /**
     * 获取角色数据权限
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add(Constants.SUPER_ADMIN);
        }
        else
        {
            // 基于userType判断角色
            if ("0".equals(user.getUserType()))
            {
                roles.add("admin");
            }
            roles.add("user");
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add(Constants.ALL_PERMISSION);
        }
        else
        {
            // 基于userType给予基本权限
            if ("0".equals(user.getUserType()))
            {
                perms.add("*:*:*"); // 管理员所有权限
            }
            else
            {
                perms.add("study:*"); // 普通用户学习模块权限
            }
        }
        return perms;
    }
}
