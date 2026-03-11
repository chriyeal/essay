package com.ruoyi.web.controller.system;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 登录验证
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions))
        {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<Map<String, Object>> menus = buildSimpleMenus(user);
        return AjaxResult.success(menus);
    }

    /**
     * 构建简单的菜单路由
     */
    private List<Map<String, Object>> buildSimpleMenus(SysUser user)
    {
        List<Map<String, Object>> menus = new ArrayList<>();
        
        // 首页
        Map<String, Object> homeMenu = new HashMap<>();
        homeMenu.put("path", "/index");
        homeMenu.put("component", "Layout");
        homeMenu.put("redirect", "/index");
        List<Map<String, Object>> homeChildren = new ArrayList<>();
        Map<String, Object> homeChild = new HashMap<>();
        homeChild.put("path", "index");
        homeChild.put("component", "index");
        homeChild.put("name", "Index");
        homeChild.put("meta", createMeta("首页", "dashboard", true));
        homeChildren.add(homeChild);
        homeMenu.put("children", homeChildren);
        menus.add(homeMenu);

        // 学习计划
        menus.add(createMenu("/study-plan", "study/plan/index", "学习计划", "education", "study:plan:list"));

        // 番茄钟
        menus.add(createMenu("/tomato-clock", "study/tomato/index", "番茄钟", "clock", "study:tomato:list"));

        // 学习统计
        menus.add(createMenu("/statistics", "study/statistics/index", "学习统计", "chart", "study:statistics:list"));

        // 用户管理（仅管理员可见）
        if ("0".equals(user.getUserType()))
        {
            menus.add(createMenu("/user-management", "study/user/index", "用户管理", "user", "system:user:list"));
        }

        return menus;
    }

    /**
     * 创建菜单项
     */
    private Map<String, Object> createMenu(String path, String component, String title, String icon, String permission)
    {
        Map<String, Object> menu = new HashMap<>();
        menu.put("path", path);
        menu.put("component", "Layout");
        menu.put("redirect", path);
        
        List<Map<String, Object>> children = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        child.put("path", path.substring(1));
        child.put("component", component);
        child.put("name", title);
        child.put("meta", createMeta(title, icon, false));
        children.add(child);
        
        menu.put("children", children);
        return menu;
    }

    /**
     * 创建meta信息
     */
    private Map<String, Object> createMeta(String title, String icon, boolean affix)
    {
        Map<String, Object> meta = new HashMap<>();
        meta.put("title", title);
        meta.put("icon", icon);
        meta.put("affix", affix);
        return meta;
    }
}
