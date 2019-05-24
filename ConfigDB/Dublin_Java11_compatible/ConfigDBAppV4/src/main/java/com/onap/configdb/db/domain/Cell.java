package com.onap.configdb.db.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the ran_config_database Cell table.
 * 
 * @author Devendra Chauhan
 *
 */
// param
@Entity
@Table(name = "Cell")
@NamedQuery(name = "Cell.findAll", query = "SELECT c FROM Cell c")
public class Cell implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 45)
	private String cellId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)

	private Date lastModifiedTS;

	/*
	 * @Column(nullable = false, length = 200) private String nbrList;
	 */

	@Column(nullable = true, length = 200)
	private String location;

	@Column(nullable = false, length = 45)
	private String networkId;

	@Column(nullable = false)
	private long pciValue;

	@Column(nullable = false)
	private String pnfId;

	@Column(nullable = true)
	private String notes;

	public Cell() {
	}

	/**
	 * This method returns CellId
	 * 
	 * @return cellId
	 */
	public String getCellId() {
		return this.cellId;
	}

	/**
	 * sets the cellId
	 * 
	 * @param cellId
	 */
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	/**
	 * This method returns lastModifiedTS
	 * 
	 * @return lastModifiedTS
	 */
	public Date getLastModifiedTS() {
		return this.lastModifiedTS;
	}

	/**
	 * Sets lastModifiedTS
	 * 
	 * @param lastModifiedTS
	 */
	public void setLastModifiedTS(Date lastModifiedTS) {
		this.lastModifiedTS = lastModifiedTS;
	}

	/**
	 * This method returns nbrList
	 * 
	 * @return nbrList
	 */
	/*
	 * public String getNbrList() { return this.nbrList; }
	 * 
	 *//**
		 * sets nbrList
		 * 
		 * @param nbrList
		 *//*
			 * public void setNbrList(String nbrList) { this.nbrList = nbrList; }
			 */

	/**
	 * This method returns networkId
	 * 
	 * @return networkId
	 */
	public String getNetworkId() {
		return this.networkId;
	}

	/**
	 * sets networkId
	 * 
	 * @param networkId
	 */
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	/**
	 * This method returns pnfName
	 * 
	 * @return pnfName
	 */
	public String getPnfId() {
		return pnfId;
	}

	/**
	 * sets pnfName
	 * 
	 * @param pnfName
	 */
	public void setPnfId(String pnfId) {
		this.pnfId = pnfId;
	}

	/**
	 * This method returns notes
	 * 
	 * @return notes
	 */

	public String getNotes() {
		return notes;
	}

	/**
	 * sets notes
	 * 
	 * @param notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * This method returns location
	 * 
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * sets location
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * This method returns pciValue
	 * 
	 * @return pciValue
	 */
	public long getPciValue() {
		return pciValue;
	}

	/**
	 * sets pciValue
	 * 
	 * @param pciValue
	 */
	public void setPciValue(long pciValue) {
		this.pciValue = pciValue;
	}

}