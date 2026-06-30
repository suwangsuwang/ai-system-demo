package com.swan.demo.interceptor;

import com.swan.demo.annotation.RequireRole;
import com.swan.demo.context.UserContext;
import com.swan.demo.service.AuthService;
import com.swan.demo.util.JwtUtil;
import com.swan.demo.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Set;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisUtil redisUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return  true;
        }
        String auth = request.getHeader("Authorization");


        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "未登录");
            return false;
        }

        try {
            String token = auth.substring(7);

            Claims claims = JwtUtil.parseToken(token);

            Long userId = Long.valueOf(claims.getSubject());
            Set<String> perms =  redisUtil.get("perm:" + userId, Set.class);

            UserContext.setUser(userId, perms);

            return true;
        } catch (Exception e) {

            unauthorized(response, "token无效");
            return false;
        }

    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        UserContext.clear();
    }

    private  void unauthorized(HttpServletResponse response, String msg) throws Exception {
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");

      response.getWriter().write("""
              {
                "code": 401,
                "message":"%s"
              }
              """.formatted(msg));
    }

}
