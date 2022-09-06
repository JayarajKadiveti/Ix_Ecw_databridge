package com.ix.ecw.databridge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Connector {

	@SerializedName("dataSourceId")
	@Expose
	private Integer dataSourceId;
	@SerializedName("dataSourceName")
	@Expose
	private String dataSourceName;
	@SerializedName("endPointUrl")
	@Expose
	private String endPointUrl;
	@SerializedName("isSecure")
	@Expose
	private Boolean isSecure;
	@SerializedName("connectorId")
	@Expose
	private Integer connectorId;
	@SerializedName("credentials")
	@Expose
	private String credentials;
	@SerializedName("lastUpdated")
	@Expose
	private String lastUpdated;
	@SerializedName("isLinked")
	@Expose
	private Boolean isLinked;
	@SerializedName("filePath")
	@Expose
	private String filePath;
	@SerializedName("startCommand")
	@Expose
	private String startCommand;
	@SerializedName("stopCommand")
	@Expose
	private String stopCommand;
	@SerializedName("isRemoved")
	@Expose
	private Boolean isRemoved;
	@SerializedName("datasourceConfigValues")
	@Expose
	private String datasourceConfigValues;
	@SerializedName("dataSourceStatus")
	@Expose
	private String dataSourceStatus;
	@SerializedName("isResourceMapped")
	@Expose
	private Boolean isResourceMapped;
	@SerializedName("fhirversion")
	@Expose
	private String fhirversion;
	public Integer getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getEndPointUrl() {
		return endPointUrl;
	}
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}
	public Boolean getIsSecure() {
		return isSecure;
	}
	public void setIsSecure(Boolean isSecure) {
		this.isSecure = isSecure;
	}
	public Integer getConnectorId() {
		return connectorId;
	}
	public void setConnectorId(Integer connectorId) {
		this.connectorId = connectorId;
	}
	public String getCredentials() {
		return credentials;
	}
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Boolean getIsLinked() {
		return isLinked;
	}
	public void setIsLinked(Boolean isLinked) {
		this.isLinked = isLinked;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getStartCommand() {
		return startCommand;
	}
	public void setStartCommand(String startCommand) {
		this.startCommand = startCommand;
	}
	public String getStopCommand() {
		return stopCommand;
	}
	public void setStopCommand(String stopCommand) {
		this.stopCommand = stopCommand;
	}
	public Boolean getIsRemoved() {
		return isRemoved;
	}
	public void setIsRemoved(Boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	public String getDatasourceConfigValues() {
		return datasourceConfigValues;
	}
	public void setDatasourceConfigValues(String datasourceConfigValues) {
		this.datasourceConfigValues = datasourceConfigValues;
	}
	public String getDataSourceStatus() {
		return dataSourceStatus;
	}
	public void setDataSourceStatus(String dataSourceStatus) {
		this.dataSourceStatus = dataSourceStatus;
	}
	public Boolean getIsResourceMapped() {
		return isResourceMapped;
	}
	public void setIsResourceMapped(Boolean isResourceMapped) {
		this.isResourceMapped = isResourceMapped;
	}
	public String getFhirversion() {
		return fhirversion;
	}
	public void setFhirversion(String fhirversion) {
		this.fhirversion = fhirversion;
	}
	
	
	

}
