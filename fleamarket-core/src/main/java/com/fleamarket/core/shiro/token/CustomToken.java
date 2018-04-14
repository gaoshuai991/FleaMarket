package com.fleamarket.core.shiro.token;

import com.fleamarket.core.shiro.Identity;
import org.apache.shiro.authc.UsernamePasswordToken;
/**
 * Create By Jackie Gao in 2018/03/22
 */
public class CustomToken extends UsernamePasswordToken {
	/**
	 * 登录身份：user, admin
 	 */
	private Identity type;

	public CustomToken(String username, String password,boolean rememberMe, Identity type) {
		super(username, password,rememberMe);
		this.type = type;
	}

	public Identity getType() {
		return type;
	}

	public void setType(Identity type) {
		this.type = type;
	}
}
