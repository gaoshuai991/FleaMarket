package com.fleamarket.core.shiro.filter;

import com.fleamarket.core.shiro.Identity;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By Gss in 2018/3/22
 */
@Log4j2
public class IdentityFilter extends UserFilter {
    private Identity identity;

    public IdentityFilter(Identity identity) {
        this.identity = identity;
        if(Identity.USER.equals(identity)){
            setLoginUrl("/login");
        } else if (Identity.ADMIN.equals(identity)) {
            setLoginUrl("/admin/login");
        }
        if (getLoginUrl().equals(DEFAULT_LOGIN_URL)) {
            throw new AuthenticationException("未知的身份：" + identity.getValue());
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response)) {
            if(getSubject(request, response).getSession().getAttribute(identity.getValue()) != null){
                try {
                    ((HttpServletResponse) response).sendRedirect("index");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }else {
            return getSubject(request, response).getSession().getAttribute(identity.getValue()) != null;
        }
    }
}

