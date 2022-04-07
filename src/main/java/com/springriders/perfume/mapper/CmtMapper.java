package com.springriders.perfume.mapper;

import com.springriders.perfume.cmt.model.CmtDMI;
import com.springriders.perfume.cmt.model.CmtPARAM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CmtMapper {

//	List<CmtDMI> selCmtList(PerfumePARAM param);
	
	List<CmtDMI> selPageCmtList(CmtPARAM param);
	
	CmtDMI selPageCnt(CmtPARAM param);

	int InsCmt(CmtPARAM param);

	int DelCmt(CmtPARAM param);

	int UpdCmt(CmtPARAM param);

}
