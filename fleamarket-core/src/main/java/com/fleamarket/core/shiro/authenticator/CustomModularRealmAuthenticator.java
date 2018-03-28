package com.fleamarket.core.shiro.authenticator;

import com.fleamarket.core.shiro.token.CustomToken;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * 自定义验证器
 * 分别定义Realm时，对应Realm的name必须包含对应token中的type，如UserRealm必须包含user
 * Create By Jackie Gao in 2018/03/22
 */
@Log4j2
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
		log.debug("Token ClassName = "+ authenticationToken.getClass().getName());
		CustomToken customToken = (CustomToken) authenticationToken;
		for (Realm realm : getRealms()) {
			log.debug("RealmName = " + realm.getName() + ", Type = " + customToken.getType().getValue());
			if (realm.getName().contains(customToken.getType().getValue())) {
				return doSingleRealmAuthentication(realm, customToken);
			}
		}
		log.error("没有找到对应Realm，请检查Realm名称是否正确！");
		throw new AuthenticationException("没有找到对应Realm，请检查Realm名称是否正确！");
	}
}
