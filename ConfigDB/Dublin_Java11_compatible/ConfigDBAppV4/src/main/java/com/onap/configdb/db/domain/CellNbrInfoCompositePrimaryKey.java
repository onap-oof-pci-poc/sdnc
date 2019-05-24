package com.onap.configdb.db.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Composite Primary key for Cell_Nbr_Info table
 * @author Devendra Chauhan
 *
 */

@Embeddable
public class CellNbrInfoCompositePrimaryKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "cell_id" , length = 45)
	@JsonProperty("cellId")
	private String cellId;

	@Column(name = "target_cell_id",length = 45)
	@JsonProperty("targetCellId")
	private String targetCellId;
	
	public CellNbrInfoCompositePrimaryKey(){
		
	}
	
	public CellNbrInfoCompositePrimaryKey(String cellId,String targetCellId){
	
	this.cellId= cellId;
	this.targetCellId = targetCellId;
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
	

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CellNbrInfoCompositePrimaryKey))
			return false;
		CellNbrInfoCompositePrimaryKey that = (CellNbrInfoCompositePrimaryKey) o;
		return Objects.equals(getCellId(), that.getCellId())
				&& Objects.equals(getTargetCellId(), that.getTargetCellId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCellId(), getTargetCellId());
	}

}
