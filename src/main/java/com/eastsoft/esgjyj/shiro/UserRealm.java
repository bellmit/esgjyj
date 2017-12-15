package com.eastsoft.esgjyj.shiro;


import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.util.PassWd;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	UserMapper userMapper;
//	@Autowired
//	MenuService menuService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
//		Long userId = ShiroUtils.getUserId();
//		Set<String> perms = menuService.listPerms(userId);
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.setStringPermissions(perms);
//		return info;
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("username", username);
		map.put("courtNo","0F");

		String password = new String((char[]) token.getCredentials());

		// 查询用户信息
		User user = userMapper.listByUserName(map).get(0);

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		// 密码错误
		PassWd passWd = new PassWd();
		if (!password.equals(passWd.f_decrypt(user.getLogpass()))) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		// 账号锁定
//		if (user.getStatus() == 0) {
//			throw new LockedAccountException("账号已被锁定,请联系管理员");
//		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}
