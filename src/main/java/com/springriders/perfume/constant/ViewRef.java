package com.springriders.perfume.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewRef {	
	public static final String TEMP_DEFAULT = "template/default";

	//상하위, 사이드바
	public static final String TEMP_MENU = "template/menuTemp"; 
	
	//상하위
	public static final String TEMP_MENU_NO_SIDEBAR = "template/menuTempNoSidebar";
	
}