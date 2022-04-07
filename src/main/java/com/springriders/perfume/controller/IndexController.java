package com.springriders.perfume.controller;

import javax.servlet.http.HttpServletRequest;

import com.springriders.perfume.constant.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(HttpServletRequest req) {		
		if(Const.realPath == null) {
			Const.realPath = req.getServletContext().getRealPath("");
		}
		log.info("root!!");
		return "redirect:/common/main";
	}
}
