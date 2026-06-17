package com.swan.demo.interceptor;

import com.swan.demo.context.UserContext;
import com.swan.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        System.out.println("🔥 interceptor path = " + request.getRequestURI());
        String auth =
                request.getHeader("Authorization");

        System.out.println("🔥 auth = " + auth);

        if (auth == null || !auth.startsWith("Bearer ")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write("""
        {
          "code":401,
          "message":"未登录"
        }
        """);

            return false;
        }

        System.out.println("✅ TOKEN EXISTS -> PASS");
//        return true;

        try {
            String token = auth.substring(7);

            Long userId = JwtUtil.parseToken(token);

            UserContext.set(userId);
            return true;
        } catch (Exception e) {

            response.setStatus(401);
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

}
