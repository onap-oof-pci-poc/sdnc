package com.onap.configdb.web.mapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Response Mapper for NbrCellsNetwork
 * @author Devendra Chauhan
 *
 */
public class NbrCellsNetworkResponse {
	
	private String networkId;
	//private List<NbrCellsNetwork> NbrCellsNetworkObjList;
	@JsonProperty("cellsNbrList")
	private List<NbrListResponse> cellsNbrList;
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public List<NbrListResponse> getCellsNbrList() {
		return cellsNbrList;
	}
	public void setCellsNbrList(List<NbrListResponse> cellsNbrList) {
		this.cellsNbrList = cellsNbrList;
	}
	
	
	
	/*public List<NbrCellsNetwork> getNbrCellsNetworkObjList() {
		return NbrCellsNetworkObjList;
	}
	public void setNbrCellsNetworkObjList(List<NbrCellsNetwork> nbrCellsNetworkObjList) {
		NbrCellsNetworkObjList = nbrCellsNetworkObjList;
	}*/
	
	

}
