package cn.sfturing.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sfturing.dao.CommonUserDao;
import cn.sfturing.entity.CommonUser;
import cn.sfturing.service.CommonUserService;
import cn.sfturing.utils.MD5;

@Service
public class CommonUserServiceImpl implements CommonUserService {

	@Autowired
	private CommonUserDao commonUserDao;

	// 登录业务/
	/**
	 * 0:用户不存在 1:密码不正确 2:密码正确
	 */
	@Transactional
	@Override
	public int login(String userIdenf, String userPassword,HttpServletRequest request) {
		CommonUser commonUser = commonUserDao.findCommonUserByUserIdenf(userIdenf);
		if (commonUser == null) {
			return 0; // 用户不存在就返回0
		} else {
			if (MD5.getMD5(userPassword).equals(commonUser.getUserPassword())) {
				return 2; // 用户密码正确返回2
			} else {
				return 1; // 用户密码错误返回1
			}
		}
	}

	/**
	 * 
	 */
	@Transactional
	public int sign(CommonUser commonUser,HttpServletRequest request) {
		String userIdenf = commonUser.getUserIdenf();
		if (commonUserDao.findCommonUserByUserIdenf(userIdenf) != null) {
			return 0;// 用户身份证号已注册
		}
		String userEmail = commonUser.getUserEmail();
		if (commonUserDao.findCommonUserByEmail(userEmail) != null) {
			return 1;// 用户邮箱已注册
		}
		String userMobile = commonUser.getUserMobile();
		if (commonUserDao.findCommonUserByMobile(userMobile) != null) {
			return 2;// 用户手机号已注册
		} else {
			String userpwd = commonUser.getUserPassword();
			commonUser.setUserPassword(MD5.getMD5(userpwd));
		}

		return 3;// 用户注册成功
	}

}