package com.onap.configdb.web.mapper;
/**
 * Request mapper Class 
 * @author Devendra Chauhan
 *
 */

public class CellPciValueObj {
	
	private String cellId;
	private long pciValue;
	
	
	public CellPciValueObj(String cellId, long pciValue) {
		super();
		this.cellId = cellId;
		this.pciValue = pciValue;
	}
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public long getPciValue() {
		return pciValue;
	}
	public void setPciValue(long pciValue) {
		this.pciValue = pciValue;
	}
	

}
