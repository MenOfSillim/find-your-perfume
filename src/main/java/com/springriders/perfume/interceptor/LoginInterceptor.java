package com.springriders.perfume.interceptor;

import com.springriders.perfume.user.model.UserVO;
import com.springriders.perfume.utils.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        
        UserVO vo = SecurityUtils.getLoginUser(request);
		
        if(vo != null) {
			response.sendRedirect("/common/main");
			return false;
		}
        
		return true;
	}


}