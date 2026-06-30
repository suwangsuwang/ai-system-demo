package com.swan.demo.evaluator;

public class PermissionEvaluator {

    public static boolean evaluate(String expr, String role) {

        if (expr == null || expr.isEmpty()) {
            return  true;
        }

        // 1. 直接匹配
        if (!expr.contains("&") && !expr.contains("|")) {
            return expr.equals(role);
        }

        // 2. OR 逻辑
        if (expr.contains("||")) {
            String[] parts = expr.split("\\|\\|");
            for (String p: parts) {
                if (p.trim().equals(role)) {
                    return  true;
                }
            }
            return false;
        }

        // 3. AND 逻辑
        if (expr.contains("&&")) {
            String[] parts = expr.split("&&");
            for (String p: parts) {
                if (!p.trim().equals(role)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
