package com.fleamarket.core.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

public class ShiroHelper {
	public static void flushSession() {
		Session session = SecurityUtils.getSubject().getSession();
		session.removeAttribute("user");
		session.removeAttribute("admin");
	}
}
