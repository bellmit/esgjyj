package com.eastsoft.esgjyj.util;

import com.eastsoft.esgjyj.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


public class ShiroUtils {
    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static User getUser() {
        return (User)getSubjct().getPrincipal();
    }
    public static String  getUserId() {
        return getUser().getLogid();
    }
    public static void logout() {
        getSubjct().logout();
    }
}

