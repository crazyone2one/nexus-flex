package cn.master.nexus.common.util;

import cn.master.nexus.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
public class SessionUtils {
    private static final ThreadLocal<String> projectId = new ThreadLocal<>();
    private static final ThreadLocal<String> organizationId = new ThreadLocal<>();

    public static void setCurrentOrganizationId(String organizationId) {
        SessionUtils.organizationId.set(organizationId);
    }

    public static void setCurrentProjectId(String projectId) {
        SessionUtils.projectId.set(projectId);
    }

    public static String getCurrentOrganizationId() {
        if (StringUtils.isNotEmpty(organizationId.get())) {
            return organizationId.get();
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            LogUtils.debug("ORGANIZATION: {}", request.getHeader("ORGANIZATION"));
            if (request.getHeader("ORGANIZATION") != null) {
                return request.getHeader("ORGANIZATION");
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage(), e);
        }
        return Objects.requireNonNull(getCurrentUser()).user().getLastOrganizationId();
    }

    public static String getCurrentProjectId() {
        if (StringUtils.isNotEmpty(projectId.get())) {
            return projectId.get();
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            LogUtils.debug("PROJECT: {}", request.getHeader("PROJECT"));
            if (request.getHeader("PROJECT") != null) {
                return request.getHeader("PROJECT");
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage(), e);
        }
        return Objects.requireNonNull(getCurrentUser()).user().getLastProjectId();
    }

    public static void clearCurrentOrganizationId() {
        organizationId.remove();
    }

    public static void clearCurrentProjectId() {
        projectId.remove();
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    public static String getUserId() {
        return Objects.requireNonNull(getCurrentUser()).user().getId();
    }

    public static String getUserName() {
        return Objects.requireNonNull(getCurrentUser()).user().getName();
    }
}
