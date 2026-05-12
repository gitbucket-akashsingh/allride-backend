package com.example.allride.auth.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceUtil {

    private DeviceUtil() {}

    public static String extractDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) return "Unknown Device";

        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iPhone")) return "iPhone";
        if (userAgent.contains("Windows")) return "Windows PC";
        if (userAgent.contains("Mac")) return "Mac";
        if (userAgent.contains("Linux")) return "Linux";

        return "Unknown Device";
    }
}
