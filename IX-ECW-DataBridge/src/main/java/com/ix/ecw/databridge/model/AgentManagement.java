package com.ix.ecw.databridge.model;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class AgentManagement {
	String agentName;
	String ipAddress;
	String hostName;
	String serviceInstanceId;
	int dataSourceId;
	String uniqueInstanceId;
	Date hearBeat;
	String hostPort;
	String hostPassword;
	String hostUser;
	String pemFilePath;
	String serviceName;
	String sftpFileName;
	String sftpFileExtension;

	public String getHostPort() {
		return hostPort;
	}
	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}
	public String getHostPassword() {
		return hostPassword;
	}
	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}
	public String getHostUser() {
		return hostUser;
	}
	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	boolean newFilesExists;
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}
	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}
	
	public int getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	public Date getHearBeat() {
		return hearBeat;
	}
	public void setHearBeat(Date hearBeat) {
		this.hearBeat = hearBeat;
	}
	public String getUniqueInstanceId() {
		return uniqueInstanceId;
	}
	public void setUniqueInstanceId(String uniqueInstanceId) {
		this.uniqueInstanceId = uniqueInstanceId;
	}
	public boolean isNewFilesExists() {
		return newFilesExists;
	}
	public void setNewFilesExists(boolean newFilesExists) {
		this.newFilesExists = newFilesExists;
	}
	public String getPemFilePath() {
		return pemFilePath;
	}
	public void setPemFilePath(String pemFilePath) {
		this.pemFilePath = pemFilePath;
	}
	public String getSftpFileName() {
		return sftpFileName;
	}
	public void setSftpFileName(String sftpFileName) {
		this.sftpFileName = sftpFileName;
	}
	public String getSftpFileExtension() {
		return sftpFileExtension;
	}
	public void setSftpFileExtension(String sftpFileExtension) {
		this.sftpFileExtension = sftpFileExtension;
	}
}
