package com.ix.ecw.databridge.model;

import java.util.Date;
import java.util.List;

/**
 * The Class DataSource.
 */
public class DataSource {

	/** The data source id. */
	private Integer dataSourceId;

	/** The data source name. */
	private String dataSourceName;

	/** The end point url. */
	private String endPointUrl;

	/** The is secure. */
	private Boolean isSecure;

	/** The security method. */
	private String securityMethod;

	/** The connector id. */
	private Integer connectorId;

	/** The credentials. */
	private String credentials;

	/** The ehr admin email. */
	private String ehrAdminEmail;

	/** The database server. */
	private String databaseServer;

	/** The ehr admin contact. */
	private String ehrAdminContact;

	/** The last updated. */
	private Date lastUpdated;

	/** The is provider. */
	private Boolean isProvider;

	/** The is linked. */
	private Boolean isLinked;

	/** The file path. */
	private String filePath;

	/** The patient id. */
	private List<String> patientId;

	/** The ccda File Path. */
	private String connectorFilePath;

	private String ccdaExtractionType;

	private String awsAccessKey;

	private String awsSecretKey;

	private String awsBucketName;

	private String awsRegion;

	private String ccdaPushedType;

	private String azureConnectionString;

	private String azureContainerName;

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
	 * Gets the end point url.
	 *
	 * @return the endPointUrl value
	 */
	public String getEndPointUrl() {
		return endPointUrl;
	}

	/**
	 * Sets the end point url.
	 *
	 * @param endPointUrl the endPointUrl to set
	 */
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}

	/**
	 * Gets the checks if is secure.
	 *
	 * @return the isSecure value
	 */
	public Boolean getIsSecure() {
		return isSecure;
	}

	/**
	 * Sets the checks if is secure.
	 *
	 * @param isSecure the isSecure to set
	 */
	public void setIsSecure(Boolean isSecure) {
		this.isSecure = isSecure;
	}

	/**
	 * Gets the security method.
	 *
	 * @return the securityMethod value
	 */
	public String getSecurityMethod() {
		return securityMethod;
	}

	/**
	 * Sets the security method.
	 *
	 * @param securityMethod the securityMethod to set
	 */
	public void setSecurityMethod(String securityMethod) {
		this.securityMethod = securityMethod;
	}

	/**
	 * Gets the connector id.
	 *
	 * @return the connectorId value
	 */
	public Integer getConnectorId() {
		return connectorId;
	}

	/**
	 * Sets the connector id.
	 *
	 * @param connectorId the connectorId to set
	 */
	public void setConnectorId(Integer connectorId) {
		this.connectorId = connectorId;
	}

	/**
	 * Gets the credentials.
	 *
	 * @return the credentials value
	 */
	public String getCredentials() {
		return credentials;
	}

	/**
	 * Sets the credentials.
	 *
	 * @param credentials the credentials to set
	 */
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	/**
	 * Gets the ehr admin email.
	 *
	 * @return the ehrAdminEmail value
	 */
	public String getEhrAdminEmail() {
		return ehrAdminEmail;
	}

	/**
	 * Sets the ehr admin email.
	 *
	 * @param ehrAdminEmail the ehrAdminEmail to set
	 */
	public void setEhrAdminEmail(String ehrAdminEmail) {
		this.ehrAdminEmail = ehrAdminEmail;
	}

	/**
	 * Gets the database server.
	 *
	 * @return the databaseServer value
	 */
	public String getDatabaseServer() {
		return databaseServer;
	}

	/**
	 * Sets the database server.
	 *
	 * @param databaseServer the databaseServer to set
	 */
	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}

	/**
	 * Gets the ehr admin contact.
	 *
	 * @return the ehrAdminContact value
	 */
	public String getEhrAdminContact() {
		return ehrAdminContact;
	}

	/**
	 * Sets the ehr admin contact.
	 *
	 * @param ehrAdminContact the ehrAdminContact to set
	 */
	public void setEhrAdminContact(String ehrAdminContact) {
		this.ehrAdminContact = ehrAdminContact;
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
	 * Gets the checks if is provider.
	 *
	 * @return the isProvider value
	 */
	public Boolean getIsProvider() {
		return isProvider;
	}

	/**
	 * Sets the checks if is provider.
	 *
	 * @param isProvider the isProvider to set
	 */
	public void setIsProvider(Boolean isProvider) {
		this.isProvider = isProvider;
	}

	/**
	 * Gets the checks if is linked.
	 *
	 * @return the isLinked value
	 */
	public Boolean getIsLinked() {
		return isLinked;
	}

	/**
	 * Sets the checks if is linked.
	 *
	 * @param isLinked the isLinked to set
	 */
	public void setIsLinked(Boolean isLinked) {
		this.isLinked = isLinked;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the filePath value
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the patient id.
	 *
	 * @return the patient id
	 */
	public List<String> getPatientId() {
		return patientId;
	}

	/**
	 * Sets the patient id.
	 *
	 * @param patientId the new patient id
	 */
	public void setPatientId(List<String> patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the connectorFilePath
	 */
	public String getConnectorFilePath() {
		return connectorFilePath;
	}

	/**
	 * @param connectorFilePath the connectorFilePath to set
	 */
	public void setConnectorFilePath(String connectorFilePath) {
		this.connectorFilePath = connectorFilePath;
	}

	public String getCcdaExtractionType() {
		return ccdaExtractionType;
	}

	public void setCcdaExtractionType(String ccdaExtractionType) {
		this.ccdaExtractionType = ccdaExtractionType;
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	public void setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}

	public String getAwsBucketName() {
		return awsBucketName;
	}

	public void setAwsBucketName(String awsBucketName) {
		this.awsBucketName = awsBucketName;
	}

	public String getAwsRegion() {
		return awsRegion;
	}

	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	public String getCcdaPushedType() {
		return ccdaPushedType;
	}

	public void setCcdaPushedType(String ccdaPushedType) {
		this.ccdaPushedType = ccdaPushedType;
	}

	public String getAzureConnectionString() {
		return azureConnectionString;
	}

	public void setAzureConnectionString(String azureConnectionString) {
		this.azureConnectionString = azureConnectionString;
	}

	public String getAzureContainerName() {
		return azureContainerName;
	}

	public void setAzureContainerName(String azureContainerName) {
		this.azureContainerName = azureContainerName;
	}
	
	

}
