package com.fleamarket.core.shiro.realm;

import com.fleamarket.core.model.User;
import com.fleamarket.core.service.UserService;
import com.fleamarket.core.util.Constant;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By Gss in 2018/3/22
 */
@Log4j2
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户的输入的账号.
        String principal = String.valueOf(token.getPrincipal());
        // 通过username从数据库中查找 User对象，如果找到，没找到.
        // 这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.selectByPrincipal(principal);
        if (user == null) {
            log.error("凭证\"{}\"不存在，登录失败！",principal);
            throw new UnknownAccountException();
        } else if (user.getStatus() == Constant.USER_LOCKED_STATUS) {
            log.error("凭证为\"{}\"的用户已被锁定，登录失败！",principal);
            throw new LockedAccountException();
        }
        log.debug("Principal: " + principal);
        // 加密交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，盐值为当前用户用户名
        return new SimpleAuthenticationInfo(principal, user.getPassword(),ByteSource.Util.bytes(user.getUsername()), getName());
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }
}
