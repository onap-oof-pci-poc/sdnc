package com.onap.configdb.web.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mapper Class for Result
 * 
 * @author Devendra Chauhan
 *
 */
public class Result {
	// param
	@JsonProperty("attribute-name")
	private String attributeName;
	@JsonProperty("value")
	private String value;

	public Result(String atributeName, String value) {
		super();
		this.attributeName = atributeName;
		this.value = value;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
