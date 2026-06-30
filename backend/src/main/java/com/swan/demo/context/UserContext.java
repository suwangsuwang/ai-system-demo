package com.swan.demo.context;

import java.util.List;
import java.util.Set;

public class UserContext {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> roleHolder = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> permHolder = new ThreadLocal<>();

    public static void setUser(Long userId, Set<String> perms) {
        userIdHolder.set(userId);
//        roleHolder.set(roles);
        permHolder.set(perms);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static Set<String> getRole() {
        return roleHolder.get();
    }

    public static Set<String> getPermissions() {
        return permHolder.get();
    }

    public static boolean hasPerm(String perm) {
        Set<String> perms = permHolder.get();
        return perms != null && perms.contains(perm);
    }

    public static void clear() {
        userIdHolder.remove();
        roleHolder.remove();
        permHolder.remove();
    }
}