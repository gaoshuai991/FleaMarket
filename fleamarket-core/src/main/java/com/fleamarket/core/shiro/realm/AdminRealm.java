package com.fleamarket.core.shiro.realm;

import com.fleamarket.core.model.Admin;
import com.fleamarket.core.service.AdminService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By Gss in 2018/3/22
 */
@Log4j2
public class AdminRealm extends AuthorizingRealm {
    @Autowired
    private AdminService adminService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户的输入的凭证.
        String principal = String.valueOf(token.getPrincipal());
        // 通过凭证从数据库中查找User对象.
        // 这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        Admin admin = new Admin();
        admin.setUsername(principal);
        admin = adminService.selectOne(admin);
        if (admin == null) {
            throw new UnknownAccountException();
        }
        // 加密交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，盐值为当前用户用户名
        return new SimpleAuthenticationInfo(
                principal,
                admin.getPassword(),
                ByteSource.Util.bytes(admin.getUsername()),
                getName());
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
