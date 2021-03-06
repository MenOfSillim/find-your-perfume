package com.springriders.perfume.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.springriders.perfume.mapper.UserMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.springriders.perfume.utils.CommonUtils;
import com.springriders.perfume.constant.Const;
import com.springriders.perfume.utils.FileUtils;
import com.springriders.perfume.utils.SecurityUtils;
import com.springriders.perfume.mapper.CommonMapper;
import com.springriders.perfume.common.model.BrandCodeVO;
import com.springriders.perfume.common.model.NoteCodeVO;
import com.springriders.perfume.common.model.PerfumeDMI;
import com.springriders.perfume.common.model.PerfumeVO;
import com.springriders.perfume.user.model.UserDMI;
import com.springriders.perfume.common.model.PerfumePARAM;
import com.springriders.perfume.user.model.UserPARAM;
import com.springriders.perfume.user.model.UserVO;

@Service
public class UserService {

	@Autowired
	private CommonMapper cMapper;

	@Autowired
	private UserMapper mapper;

	public int join(MultipartHttpServletRequest mReq, UserPARAM param) throws IOException {

		MultipartFile mf = mReq.getFile("file");

		String path = "/img/profileImg/";
		String realPath = new ClassPathResource("/static").getFile().getAbsolutePath() + path;
		String saveFileNm = FileUtils.saveFile(realPath, mf);

		System.out.println("saveFileNm :" + saveFileNm);

		if (saveFileNm == null) {
			saveFileNm = "default_img.jpg";
			param.setProfile_img(saveFileNm);
		} else {
			param.setProfile_img(saveFileNm);
		}

		System.out.println("user_pw : " + param.getUser_pw());
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);

		param.setSalt(salt);
		param.setUser_pw(cryptPw);

		String[] strUserNote = mReq.getParameterValues("nt_m_c");

		mapper.insUser(param);

		param = mapper.selUserPk(param);
		param.setI_user(param.getI_user());
		System.out.println("i_user : " + param.getI_user());

		if (mReq.getParameter("nt_m_c") != null) {

			for (String strUserNotes : strUserNote) {
				int nt_m_c = CommonUtils.parseStringToInt(strUserNotes);
				param.setNt_m_c(nt_m_c);

				mapper.insUserNote(param);
			}

		}
		return Const.SUCCESS;
	}

	public int login(UserPARAM param) {
		String id = param.getUser_id();

		System.out.println("?????????:" + id.contains(" "));

		if (param.getUser_id().equals("")) {
			return Const.EMPTY_ID;
		}
		if (param.getUser_id().contains(" ")) {
			return Const.BLANK_ID;
		}
		if (param.getUser_id().equals("")) {
			return Const.NO_ID;
		}
		if (param.getUser_id().length() < 5) {
			return Const.SHORT_ID;
		}

		// ????????? ???????????????
		String regExp = "^[a-z0-9]{5,12}$";

		if (param.getUser_id().matches(regExp) == false) {
			System.out.println(param.getUser_id().matches(regExp));
			return Const.NOT_ALLOW_ID;
		}

		UserDMI dbUser = mapper.selUser(param);

		if (dbUser == null) {
			return Const.NO_ID;
		}

		String cryptPw = SecurityUtils.getEncrypt(param.getUser_pw(), dbUser.getSalt());

		if (!cryptPw.equals(dbUser.getUser_pw())) {
			return Const.NO_PW;
		}

		// by - ?????? , ????????? ???????????? ????????? ????????? ????????? ageGroup??? ?????? , 2020-11-02
		// ??????
		String bd = dbUser.getBd();
		String strYear = bd.substring(0, 4);
		int userYear = CommonUtils.parseStringToInt(strYear);

		// ?????? ?????? ??????
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String strNowYear = format.format(date);
		int nowYear = CommonUtils.parseStringToInt(strNowYear);

		// ?????? ?????? ??????
		int age = nowYear - userYear + 1;

		int ageGroup = 0;

		if (10 <= age && age < 100) {
			// ?????? ?????? ??????
			String strAgeGroup = String.valueOf(age);
			strAgeGroup = strAgeGroup.substring(0, 1);
			strAgeGroup += 0;
			ageGroup = CommonUtils.parseStringToInt(strAgeGroup);
		} else if (age < 10) {
			// 10??? ???????????? 1 ????????????
			ageGroup = 1;
		} else if (age >= 100) {
			// 100??? ???????????? 100 ????????????
			ageGroup = 100;
		}
		// ???

		param.setAgeGroup(ageGroup);
		param.setStrGender(dbUser.getStrGender());
		param.setUser_type(dbUser.getUser_type());
		param.setI_user(dbUser.getI_user());
		param.setUser_pw(null);
		param.setNm(dbUser.getNm());
		param.setProfile_img(dbUser.getProfile_img());
		param.setBd(dbUser.getBd());
		param.setR_dt(dbUser.getR_dt());

		return Const.SUCCESS;
	}

	public int changeAuth(UserPARAM param) {
		mapper.changeAuth(param);

		return Const.SUCCESS;
	}

	public List<UserVO> selUserList() {
		UserVO p = new UserVO();
		return mapper.selUserList(p);
	}

	public List<UserVO> selAdminList() {
		UserVO p = new UserVO();
		return mapper.selAdminList(p);
	}

	public List<BrandCodeVO> selBrandList() {
		BrandCodeVO p = new BrandCodeVO();
		return cMapper.selBrandCodeList(p);
	}

	public List<NoteCodeVO> selNoteList() {
		NoteCodeVO p = new NoteCodeVO();
		return cMapper.selNoteCodeList(p);
	}

	public int insPerfume(MultipartHttpServletRequest mReq) {
		int p_price = Integer.parseInt(mReq.getParameter("p_price"));
		int p_size = Integer.parseInt(mReq.getParameter("p_size"));
		int p_brand = CommonUtils.parseStringToInt(mReq.getParameter("p_brand"));
		String p_nm = mReq.getParameter("p_nm");

		// ?????? ????????????
		MultipartFile mf = mReq.getFile("p_pic");

		String path = "/resources/img/perfume/";
		String realPath = mReq.getServletContext().getRealPath(path);
		String saveFileNm = FileUtils.saveFile(realPath, mf);

		PerfumeVO vo = new PerfumeVO();
		vo.setP_price(p_price);
		vo.setP_size(p_size);
		vo.setP_brand(p_brand);
		vo.setP_nm(p_nm);
		vo.setP_pic(saveFileNm);

		cMapper.insPerfume(vo);
		vo = cMapper.selPerfumePk(vo);
		int i_p = vo.getI_p();

		String[] strP_notes = mReq.getParameterValues("p_note");

		for (String strP_note : strP_notes) {
			NoteCodeVO noteVo = new NoteCodeVO();
			int p_note = CommonUtils.parseStringToInt(strP_note);
			System.out.println("???????????? : " + p_note);

			noteVo.setI_p(i_p);
			noteVo.setNt_d_c(p_note);

			cMapper.insPerfumeNote(noteVo);
		}
		return Const.SUCCESS;
	}

	public int uptUser(MultipartHttpServletRequest mReq, HttpSession hs) {
		int i_user = SecurityUtils.getLoginUserPk(hs);
		String user_pw = mReq.getParameter("user_pw");
		System.out.println("user_pw : " + user_pw);
		String salt = SecurityUtils.generateSalt();
		String nm = mReq.getParameter("nm");
		System.out.println("nm : " + nm);
		String cryptPw = SecurityUtils.getEncrypt(user_pw, salt);

		UserPARAM param = new UserPARAM();
		param.setI_user(i_user);
		UserDMI dmi = mapper.selUser(param);
		String profileImg = dmi.getProfile_img();
		
		String path = "/resources/img/profileImg/";
		String realPath = mReq.getServletContext().getRealPath(path);

		System.out.println("proIMG : " + profileImg);
		
		if(profileImg != null && !"".equals(profileImg)) {
			FileUtils.delFile(realPath + profileImg);
		}
		
		MultipartFile mf = mReq.getFile("profile_pic");
			  
		String saveFileNm = FileUtils.saveFile(realPath, mf);
		System.out.println("saveFileNm : " + saveFileNm);
	  
		if(!user_pw.equals("")){
			param.setUser_pw(cryptPw); 
			param.setSalt(salt); 
		} 
		if(!nm.equals("")) { param.setNm(nm); }
		if(saveFileNm != null) {
			param.setProfile_img(saveFileNm); 
		}
	
		mapper.uptUser(param);
		UserDMI vo2 = mapper.selUser(param);
		hs.setAttribute(Const.LOGIN_USER, vo2);
		
		return Const.SUCCESS; 
	}

	public List<PerfumeDMI> selFavoriteList(UserPARAM param) {
		List<PerfumeDMI> list = mapper.selFavoriteList(param);

		return list;
	}

	public int delUserFavorite(UserPARAM param) {
		return mapper.delUserFavorite(param);
	}

	public int insUserFavorite(UserPARAM param) {
		return mapper.insUserFavorite(param);
	}

	public List<PerfumeDMI> selFavNotes(UserPARAM p) {
		return mapper.selFavNotes(p);
	}

	public int ajaxAddFavNotes(PerfumePARAM param) {
		return mapper.ajaxAddFavNotes(param);
	}

	public int ajaxDelFavNotes(PerfumePARAM param) {
		return mapper.ajaxDelFavNotes(param);
	}

//	????????? auth?????? ???????????????

//	private boolean _authFail(int i_rest, int i_user) {
//		RestPARAM param = new RestPARAM();
//		param.setI_rest(i_rest);		
//		int dbI_user = mapper.selRestChkUser(i_rest);
//		if(i_user != dbI_user) {
//			return true;
//		}		
//		return false;
//	}

}