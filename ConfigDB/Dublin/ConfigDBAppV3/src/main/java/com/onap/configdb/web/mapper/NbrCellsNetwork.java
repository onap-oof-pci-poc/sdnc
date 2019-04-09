package com.onap.configdb.web.mapper;

import java.util.List;
/**
 * Request mapper Class for NbrCellNetwork
 * @author Devendra Chauhan
 *
 */
public class NbrCellsNetwork {
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
