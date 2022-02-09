package com.hk.sys.shiro;

import com.hk.sys.domain.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm {
    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = principalCollection.getPrimaryPrincipal().toString();
        Set<String> roleSet=new HashSet<String>();
        roleSet.add("admin");
        roleSet.add("manager");

        Set<String> permissionSet=new HashSet<String>();
        permissionSet.add("sys:user:list");
        permissionSet.add("sys:user:create");
        permissionSet.add("sys:user:update");
        permissionSet.add("sys:user:delete");
        permissionSet.add("sys:user:info");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleSet);
        simpleAuthorizationInfo.addStringPermissions(permissionSet);
        System.out.println("授权");
        return simpleAuthorizationInfo;
    }

    ///登陆认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());
        User user = new User("bhf", "123456",0);
        if(!user.getUsername().equals(username)){
            throw new UnknownAccountException("用户名不存在!~");
        }
        if(!user.getPassword().equals(password)){
            throw new CredentialsException("密码错误!");
        }
        if(user.getStatus()==1){
            throw new DisabledAccountException("账号禁用");
        }
        if(user.getStatus()==2){
            throw new LockedAccountException("账号锁定");
        }

        System.out.println("认证......成功:");
        return new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),getName());
    }
}
