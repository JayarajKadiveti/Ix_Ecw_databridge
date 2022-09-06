package com.ix.ecw.databridge.model;

import java.util.Date;

/**
 * The Class ExtractionDetails.
 */
public class ExtractionDetails {
	
	/** The extraction id. */
	private Integer extractionId;
	
	/** The data set id. */
	private Integer dataSetId;
	
	/** The data set name. */
	private String dataSetName;
	
	/** The data source id. */
	private Integer dataSourceId;
	
	/** The data source name. */
	private String dataSourceName;
	
	/** The files location. */
	private String filesLocation;
	
	/** The status. */
	private String status;
	
	/** The data processing status. */
	private String dataProcessingStatus;
	
	/** The extraction start time. */
	private Date extractionStartTime;
	
	/** The extraction end time. */
	private Date extractionEndTime;
	
	/**
	 * Gets the extraction id.
	 *
	 * @return the extractionId value
	 */
	public Integer getExtractionId() {
		return extractionId;
	}
	
	/**
	 * Sets the extraction id.
	 *
	 * @param extractionId the extractionId to set
	 */
	public void setExtractionId(Integer extractionId) {
		this.extractionId = extractionId;
	}
	
	/**
	 * Gets the data set id.
	 *
	 * @return the dataSetId value
	 */
	public Integer getDataSetId() {
		return dataSetId;
	}
	
	/**
	 * Sets the data set id.
	 *
	 * @param dataSetId the dataSetId to set
	 */
	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}
	
	/**
	 * Gets the data set name.
	 *
	 * @return the dataSetName value
	 */
	public String getDataSetName() {
		return dataSetName;
	}
	
	/**
	 * Sets the data set name.
	 *
	 * @param dataSetName the dataSetName to set
	 */
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	
	/**
	 * Gets the data source id.
	 *
	 * @return the dataSourceId value
	 */
	public Integer getDataSourceId() {
		return dataSourceId;
	}
	
	/**
	 * Sets the data source id.
	 *
	 * @param dataSourceId the dataSourceId to set
	 */
	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	/**
	 * Gets the data source name.
	 *
	 * @return the dataSourceName value
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}
	
	/**
	 * Sets the data source name.
	 *
	 * @param dataSourceName the dataSourceName to set
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	
	/**
	 * Gets the files location.
	 *
	 * @return the filesLocation value
	 */
	public String getFilesLocation() {
		return filesLocation;
	}
	
	/**
	 * Sets the files location.
	 *
	 * @param filesLocation the filesLocation to set
	 */
	public void setFilesLocation(String filesLocation) {
		this.filesLocation = filesLocation;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status value
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Gets the data processing status.
	 *
	 * @return the dataProcessingStatus value
	 */
	public String getDataProcessingStatus() {
		return dataProcessingStatus;
	}
	
	/**
	 * Sets the data processing status.
	 *
	 * @param dataProcessingStatus the dataProcessingStatus to set
	 */
	public void setDataProcessingStatus(String dataProcessingStatus) {
		this.dataProcessingStatus = dataProcessingStatus;
	}
	
	/**
	 * Gets the extraction start time.
	 *
	 * @return the extractionStartTime value
	 */
	public Date getExtractionStartTime() {
		return extractionStartTime;
	}
	
	/**
	 * Sets the extraction start time.
	 *
	 * @param extractionStartTime the extractionStartTime to set
	 */
	public void setExtractionStartTime(Date extractionStartTime) {
		this.extractionStartTime = extractionStartTime;
	}
	
	/**
	 * Gets the extraction end time.
	 *
	 * @return the extractionEndTime value
	 */
	public Date getExtractionEndTime() {
		return extractionEndTime;
	}
	
	/**
	 * Sets the extraction end time.
	 *
	 * @param extractionEndTime the extractionEndTime to set
	 */
	public void setExtractionEndTime(Date extractionEndTime) {
		this.extractionEndTime = extractionEndTime;
	}
}
