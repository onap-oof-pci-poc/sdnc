package com.onap.configdb.web.mapper;

import java.util.List;

/**
 * Request Mapper Class for BulkUpoad
 * 
 * @author Devendra Chauhan
 *
 */

public class CellInputPayload {

	List<CellObj> cellList;

	public List<CellObj> getCellList() {
		return cellList;
	}

	public void setCellList(List<CellObj> cellList) {
		this.cellList = cellList;
	}

}
