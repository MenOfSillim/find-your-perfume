package com.springriders.perfume.utils;

import com.springriders.perfume.constant.Const;
import com.springriders.perfume.user.model.UserVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

	public static int getLoginUserPk(HttpServletRequest request) {
		return getLoginUserPk(request.getSession());
	}
	
	public static int getLoginUserPk(HttpSession hs) {
		UserVO loginUser = (UserVO)hs.getAttribute(Const.LOGIN_USER);
		return loginUser == null ? 0 : loginUser.getI_user();
	}
	
	public static UserVO getLoginUser(HttpSession hs) {
		return (UserVO)hs.getAttribute(Const.LOGIN_USER);
	}
	
	public static UserVO getLoginUser(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		return (UserVO)hs.getAttribute(Const.LOGIN_USER);
	}
	
	public static boolean isLogout(HttpServletRequest request) {				
		return getLoginUser(request) == null;
	}
	
	public static boolean loginChk(HttpServletRequest request) {		
		if(getLoginUser(request) != null) {
			return true;			
		}
		return false;
	}
	
	public static String generateSalt() {
		return BCrypt.gensalt();
	}

	public static String getEncrypt(String pw, String salt) {
		return BCrypt.hashpw(pw, salt); 
	}
}