package com.onap.configdb.db.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Cell_Nbr_Info")
/**
 *The persistent class for the ran_config_database Cell_Nbr_Info table.
 * @author Devendra Chauhan
 *
 */
public class CellNbrInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	// @JsonProperty("CellNbrInfo")
	private CellNbrInfoCompositePrimaryKey cellNbrInfoCompositePrimaryKey;

	private boolean ho;

	public CellNbrInfoCompositePrimaryKey getCellNbrInfoCompositePrimaryKey() {
		return cellNbrInfoCompositePrimaryKey;
	}

	public void setCellNbrInfoCompositePrimaryKey(CellNbrInfoCompositePrimaryKey cellNbrInfoCompositePrimaryKey) {
		this.cellNbrInfoCompositePrimaryKey = cellNbrInfoCompositePrimaryKey;
	}

	public boolean isHo() {
		return ho;
	}

	public void setHo(boolean ho) {
		this.ho = ho;
	}

}
