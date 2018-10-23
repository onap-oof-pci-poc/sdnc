package com.onap.configdb.web.mapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Request mapper class
 * @author Devendra
 *
 */

public class CellObj {
	
	@JsonProperty("Cell")
	private CellData cell;
	private List<String> neighbor;
	
	public CellData getCell() {
		return cell;
	}
	public void setCell(CellData cell) {
		this.cell = cell;
	}
	public List<String> getNeighbor() {
		return neighbor;
	}
	public void setNeighbor(List<String> neighbor) {
		this.neighbor = neighbor;
	}
	
}
