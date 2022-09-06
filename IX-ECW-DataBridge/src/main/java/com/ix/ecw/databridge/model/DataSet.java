package com.ix.ecw.databridge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * The Class DataSet.
 */
public class DataSet {
	
	/** The data set id. */
	private Integer dataSetId;
	
	/** The data set name. */
	private String dataSetName;
	
	/** The datasources. */
	private Set<DataSource> datasources = new HashSet<DataSource>(0);
	
	/** The schedule frequency. */
	private String scheduleFrequency;
	
	/** The schedule time. */
	private String scheduleTime;
	
	/** The last updated. */
	private Date lastUpdated;
	
	/** The fromdate. */
	private Date fromdate;
	
	/** The todate. */
	private Date todate;
	
	/** The weekday. */
	private String weekday;
	
	/** The monthdate. */
	private Date monthdate;
	
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
	 * Gets the datasources.
	 *
	 * @return the datasources value
	 */
	public Set<DataSource> getDatasources() {
		return datasources;
	}
	
	/**
	 * Sets the datasources.
	 *
	 * @param datasources the datasources to set
	 */
	public void setDatasources(Set<DataSource> datasources) {
		this.datasources = datasources;
	}
	
	/**
	 * Gets the schedule frequency.
	 *
	 * @return the scheduleFrequency value
	 */
	public String getScheduleFrequency() {
		return scheduleFrequency;
	}
	
	/**
	 * Sets the schedule frequency.
	 *
	 * @param scheduleFrequency the scheduleFrequency to set
	 */
	public void setScheduleFrequency(String scheduleFrequency) {
		this.scheduleFrequency = scheduleFrequency;
	}
	
	/**
	 * Gets the schedule time.
	 *
	 * @return the scheduleTime value
	 */
	public String getScheduleTime() {
		return scheduleTime;
	}
	
	/**
	 * Sets the schedule time.
	 *
	 * @param scheduleTime the scheduleTime to set
	 */
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
	/**
	 * Gets the last updated.
	 *
	 * @return the lastUpdated value
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	/**
	 * Sets the last updated.
	 *
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	/**
	 * Gets the fromdate.
	 *
	 * @return the fromdate value
	 */
	public Date getFromdate() {
		return fromdate;
	}
	
	/**
	 * Sets the fromdate.
	 *
	 * @param fromdate the fromdate to set
	 */
	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}
	
	/**
	 * Gets the todate.
	 *
	 * @return the todate value
	 */
	public Date getTodate() {
		return todate;
	}
	
	/**
	 * Sets the todate.
	 *
	 * @param todate the todate to set
	 */
	public void setTodate(Date todate) {
		this.todate = todate;
	}
	
	/**
	 * Gets the weekday.
	 *
	 * @return the weekday value
	 */
	public String getWeekday() {
		return weekday;
	}
	
	/**
	 * Sets the weekday.
	 *
	 * @param weekday the weekday to set
	 */
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
	/**
	 * Gets the monthdate.
	 *
	 * @return the monthdate value
	 */
	public Date getMonthdate() {
		return monthdate;
	}
	
	/**
	 * Sets the monthdate.
	 *
	 * @param monthdate the monthdate to set
	 */
	public void setMonthdate(Date monthdate) {
		this.monthdate = monthdate;
	}
}
