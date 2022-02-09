package com.hk;

import com.hk.sys.shiro.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class ShiroTest {

    @Test
    public void test(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
//        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
        ShiroRealm shiroRealm = new ShiroRealm();
        defaultSecurityManager.setRealm(shiroRealm);

        //设置缓存管理器
        /*CacheManager cacheManager=new MemoryConstrainedCacheManager();
        defaultSecurityManager.setCacheManager(cacheManager);*/

        //需要第三方依赖
        EhCacheManager cacheManager=new EhCacheManager();
        defaultSecurityManager.setCacheManager(cacheManager);

        SecurityUtils.setSecurityManager(defaultSecurityManager);


        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("bhf", "123456");
        subject.login(token);
        //用户的认证状态
        System.out.println("status:"+subject.isAuthenticated());

       /* boolean admin = subject.hasRole("admin");
        System.out.println(admin);*/

        //权限
        boolean permitted = subject.isPermitted("sys:user:list");
        boolean create = subject.isPermitted("sys:user:create");
//        System.out.println(subject.getPrincipal());
        System.out.println(permitted);
        System.out.println(create);

        subject.logout();

    }

    @Test
    public void hashTest(){
        Md5Hash noSalt = new Md5Hash("123456");
        System.out.println(noSalt);

        Md5Hash withSalt = new Md5Hash("123456","wxq");
        System.out.println(withSalt);
    }
}
