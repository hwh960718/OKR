package com.mobvista.okr.security;

import com.mobvista.okr.dto.mbacs.OperatorPerm;
import com.mobvista.okr.util.CookieUtil;
import com.mobvista.okr.util.TokenUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

//    /**
//     * Get the current user.
//     *
//     * @return the login of the current user
//     */
//    public static OKRUserDetails getCurrentUser() {
//
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                return (OKRUserDetails) authentication.getPrincipal();
//            }
//        }
//        throw new IllegalStateException("User not found!");
//    }


    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserLogin() {
        return TokenUtil.getUserNameByToken(getToken());
    }

    /**
     * Get the userId of the current user.
     *
     * @return the login of the current user
     */
    public static Long getCurrentUserId() {
        return TokenUtil.getUserIdByToken(getToken());
    }


//    /**
//     * Check if a user is authenticated.
//     *
//     * @return true if the user is authenticated, false otherwise
//     */
//    public static boolean isAuthenticated() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            return authentication.getAuthorities().stream()
//                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(OperatorConstants.ANONYMOUS));
//        }
//        return false;
//    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCanOperator(String authority) {
        List<String> operRoleList = TokenUtil.getUserOperByToken(getToken());
        return null != operRoleList && operRoleList.contains(authority);
    }

    public static OperatorPerm getUserOperatorPerm() {
        List<String> operRoleList = TokenUtil.getUserOperByToken(getToken());
        return new OperatorPerm(operRoleList);
    }

    public static String getToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //String token = CookieUtil.getCookieByName(servletRequestAttributes.getRequest(), CookieUtil.AUTH_NAME);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcGVyIjoiW1wib3Blcl9jYW5fc2VlX3NlYXNvbl9kZXRhaWxfcmFua19hbmRfc2NvcmVcIixcIm9wZXJfY2FuX3NlZV91c2VyX2RldGFpbF9mb3JfYXNzZXNzX3Njb3JlX2xpc3RcIixcIm9wZXJfY2FuX3NlZV9yYW5raW5nX2xpc3RcIixcIm9wZXJfY2FuX3NlZV91c2VyX3NlYXNvbl9kZXRhaWxfZm9yX3VzZXJfc2NvcmVfaXRlbVwiLFwib3Blcl9jYW5fbW9kaWZ5X3VzZXJfbWFuYWdlX3VzZXJfZGF0YVwiLFwib3Blcl9jYW5fc2VlX2FsbF9vcmRlcl9saXN0XCJdIiwiaWQiOiIxNDcxNiIsInVzZXJuYW1lIjoi6aG-54KcIn0.yR2UmjPnHhVeBjRC1q4HFqNhqBdWjPl1TifREqrCNM8";
        return token;
    }

}
