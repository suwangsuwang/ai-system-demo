package com.swan.demo.aspect;


import com.swan.demo.annotation.Require;
import com.swan.demo.annotation.RequirePermission;
import com.swan.demo.annotation.RequireRole;
import com.swan.demo.context.UserContext;
import com.swan.demo.evaluator.PermissionEvaluator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Aspect
@Component
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint,
                                  RequirePermission requirePermission) throws Throwable {

        // 1. 获取当前用户权限
        Set<String> permissions = UserContext.getPermissions();

        if (permissions == null) {
            throw  new RuntimeException("未登录");
        }

        // 2. 获取注解要求的权限
        String need = requirePermission.value();

        // 3. 判断是否拥有权限
        if (!permissions.contains(need)) {
            throw new RuntimeException("无权限访问: " + need);
        }
        return joinPoint.proceed();
    }

    @Around("@annotation(require)")
    public Object check(ProceedingJoinPoint joinPoint, Require require) throws Throwable {

        String perm = require.value();

        if (!UserContext.hasPerm(perm)) {
            throw new RuntimeException("403 无权限: " + perm);
        }

        return  joinPoint.proceed();
    }
}
