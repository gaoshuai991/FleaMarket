package com.fleamarket.core.util;

import com.fleamarket.core.model.User;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpSession;

public class Utils {
    public static User getUserSession(HttpSession session){
        return (User) session.getAttribute("user");
    }
}
