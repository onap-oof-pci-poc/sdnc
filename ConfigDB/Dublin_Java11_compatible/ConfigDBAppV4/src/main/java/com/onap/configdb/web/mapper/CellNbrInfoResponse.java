package com.onap.configdb.web.mapper;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CellNbrInfoResponse {
	
	
	
	@JsonProperty("cellId")
	private String cellId;



	@JsonProperty("targetCellId")
	private String targetCellId;
	
	private boolean ho;
	
	
	public CellNbrInfoResponse(String cellId,String targetCellId,boolean ho) {
		this.cellId =  cellId;
		this.targetCellId = targetCellId;
		this.ho =  ho;
		
		
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

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
