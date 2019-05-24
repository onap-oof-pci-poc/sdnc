package com.onap.configdb.web.mapper;

import java.util.List;
/**
 * 
 * Response Mapper class for NbrList
 * @author Devendra Chauhan
 *
 */
public class NbrListResponse {
	
	private String cellId;
	private List<NbrList> nbrList;
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public List<NbrList> getNbrList() {
		return nbrList;
	}
	public void setNbrList(List<NbrList> nbrList) {
		this.nbrList = nbrList;
	}
	
	

}
