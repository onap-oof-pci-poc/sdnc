package com.onap.configdb.web.mapper;

import java.util.List;
/**
 * Mapper Class for NbrList
 * @author Devendra Chauhan
 *
 */
public class NbrList {
	//param
	
	private String targetCellId;
	private long pciValue;
	private boolean ho;
	
	
	public String getTargetCellId() {
		return targetCellId;
	}
	public void setTargetCellId(String targetCellId) {
		this.targetCellId = targetCellId;
	}
	public long getPciValue() {
		return pciValue;
	}
	public void setPciValue(long pciValue) {
		this.pciValue = pciValue;
	}
	public boolean isHo() {
		return ho;
	}
	public void setHo(boolean ho) {
		this.ho = ho;
	}
	
	

}
