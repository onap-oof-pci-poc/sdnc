package com.onap.configdb.web.mapper;
/**
 * 
 * Request mapper class
 * @author Devendra
 *
 */
public class CellData {
	private String networkId;
	private String nodeId;
	private Long physicalCellId;
	private String pnfName;
	private String sectorNumber;
	private String latitude;
	private String longitude;
	
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public Long getPhysicalCellId() {
		return physicalCellId;
	}
	public void setPhysicalCellId(Long physicalCellId) {
		this.physicalCellId = physicalCellId;
	}
	public String getPnfName() {
		return pnfName;
	}
	public void setPnfName(String pnfName) {
		this.pnfName = pnfName;
	}
	public String getSectorNumber() {
		return sectorNumber;
	}
	public void setSectorNumber(String sectorNumber) {
		this.sectorNumber = sectorNumber;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
