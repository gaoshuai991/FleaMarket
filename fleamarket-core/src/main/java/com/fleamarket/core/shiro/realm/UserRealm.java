package com.fleamarket.core.shiro.realm;

import com.fleamarket.core.model.User;
import com.fleamarket.core.service.UserService;
import com.fleamarket.core.util.Constant;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created By Gss in 2018/3/22
 */
@Log4j2
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户的输入的凭证.
        String principal = String.valueOf(token.getPrincipal());
        // 通过凭证从数据库中查找User对象.
        // 这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.selectByPrincipal(principal);
        if (user == null) {
            throw new UnknownAccountException();
        } else if (user.getStatus() == Constant.USER_LOCKED_STATUS) {
            throw new LockedAccountException();
        }
        // 加密交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，盐值为当前用户用户名
        return new SimpleAuthenticationInfo(
                new SimplePrincipalCollection(Arrays.asList(principal,user.getNickname()),getName()),
                user.getPassword(),
                ByteSource.Util.bytes(user.getUsername()),
                getName());
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }
}
