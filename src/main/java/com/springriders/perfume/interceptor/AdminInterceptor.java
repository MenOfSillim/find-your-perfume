package com.springriders.perfume.interceptor;

import com.springriders.perfume.constant.Const;
import com.springriders.perfume.utils.SecurityUtils;
import com.springriders.perfume.user.model.UserVO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

        UserVO vo = SecurityUtils.getLoginUser(request);
		if(vo == null) {
			response.sendRedirect("/user/login");
			return false;
		}
		int user_type = vo.getUser_type();
		if(user_type != Const.ADMIN) {
			SecurityUtils.isLogout(request);
			response.sendRedirect("/user/login");
			return false;
		}
		return true;
	}


}
