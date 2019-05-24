package com.onap.configdb.web.mapper;

/**
 * Mapper class for nbrObj
 * @author Devendra Chauhan
 *
 */
public class NbrObj {
	private String targetCellId;
	private boolean ho;
	
	public String getTargetCellId() {
		return targetCellId;
	}
	public void setTargetCellId(String targetCellId) {
		this.targetCellId = targetCellId;
	}
	public boolean isHo() {
		return ho;
	}
	public void setHo(boolean ho) {
		this.ho = ho;
	}
	
	

}
