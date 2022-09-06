package com.ix.ecw.databridge.model;

public class AgentConfiguration {

	String ixPlatformBaseUrl;
	String datasourceId;
	String heartBeatTimePeriod;
	String extractionTimePeriod;
	String agentSftpUserName;
	String agentSftpHost;
	String agentSftpPort;
	String agentSftpPassWord;
	String agentSftpKeyPath;
	String instanceId;
	String fileMonitorTimePeriod;
	String keycloakAuthServerUrl;
	String keycloakRealm;
	String keycloakClientId;
	String keycloakClientSecret;
	String keycloakUser;
	String keycloakPassword;
	
	public AgentConfiguration(){
		
	}
	
	public String getIxPlatformBaseUrl() {
		return ixPlatformBaseUrl;
	}

	public void setIxPlatformBaseUrl(String ixPlatformBaseUrl) {
		this.ixPlatformBaseUrl = ixPlatformBaseUrl;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getHeartBeatTimePeriod() {
		return heartBeatTimePeriod;
	}

	public void setHeartBeatTimePeriod(String heartBeatTimePeriod) {
		this.heartBeatTimePeriod = heartBeatTimePeriod;
	}

	public String getExtractionTimePeriod() {
		return extractionTimePeriod;
	}

	public void setExtractionTimePeriod(String extractionTimePeriod) {
		this.extractionTimePeriod = extractionTimePeriod;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getFileMonitorTimePeriod() {
		return fileMonitorTimePeriod;
	}

	public void setFileMonitorTimePeriod(String fileMonitorTimePeriod) {
		this.fileMonitorTimePeriod = fileMonitorTimePeriod;
	}

	public String getAgentSftpUserName() {
		return agentSftpUserName;
	}

	public void setAgentSftpUserName(String agentSftpUserName) {
		this.agentSftpUserName = agentSftpUserName;
	}

	public String getAgentSftpHost() {
		return agentSftpHost;
	}

	public void setAgentSftpHost(String agentSftpHost) {
		this.agentSftpHost = agentSftpHost;
	}

	public String getAgentSftpPort() {
		return agentSftpPort;
	}

	public void setAgentSftpPort(String agentSftpPort) {
		this.agentSftpPort = agentSftpPort;
	}

	public String getAgentSftpPassWord() {
		return agentSftpPassWord;
	}

	public void setAgentSftpPassWord(String agentSftpPassWord) {
		this.agentSftpPassWord = agentSftpPassWord;
	}

	public String getAgentSftpKeyPath() {
		return agentSftpKeyPath;
	}

	public void setAgentSftpKeyPath(String agentSftpKeyPath) {
		this.agentSftpKeyPath = agentSftpKeyPath;
	}

	public String getKeycloakAuthServerUrl() {
		return keycloakAuthServerUrl;
	}

	public void setKeycloakAuthServerUrl(String keycloakAuthServerUrl) {
		this.keycloakAuthServerUrl = keycloakAuthServerUrl;
	}

	public String getKeycloakRealm() {
		return keycloakRealm;
	}

	public void setKeycloakRealm(String keycloakRealm) {
		this.keycloakRealm = keycloakRealm;
	}

	public String getKeycloakClientId() {
		return keycloakClientId;
	}

	public void setKeycloakClientId(String keycloakClientId) {
		this.keycloakClientId = keycloakClientId;
	}

	public String getKeycloakClientSecret() {
		return keycloakClientSecret;
	}

	public void setKeycloakClientSecret(String keycloakClientSecret) {
		this.keycloakClientSecret = keycloakClientSecret;
	}

	public String getKeycloakUser() {
		return keycloakUser;
	}

	public void setKeycloakUser(String keycloakUser) {
		this.keycloakUser = keycloakUser;
	}

	public String getKeycloakPassword() {
		return keycloakPassword;
	}

	public void setKeycloakPassword(String keycloakPassword) {
		this.keycloakPassword = keycloakPassword;
	}
}
