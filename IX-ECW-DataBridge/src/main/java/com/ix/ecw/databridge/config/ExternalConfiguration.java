package com.ix.ecw.databridge.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ix.ecw.databridge.model.AgentConfiguration;

@Configuration
public class ExternalConfiguration {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	@Bean
	public AgentConfiguration agentConfiguration() {
		logger.info("Inside AgentConfiguration ");
		AgentConfiguration agentConfiguration = new AgentConfiguration();
		try {
			logger.info(" External properties :: " + env.getProperty("spring.config.location"));
			Properties properties = readPropertiesFile(env.getProperty("spring.config.location"));
			agentConfiguration.setIxPlatformBaseUrl(properties.getProperty("ix_platform_base_url"));
			agentConfiguration.setDatasourceId(properties.getProperty("datasource_id"));
			agentConfiguration.setAgentSftpHost(properties.getProperty("agent_sftp_host"));
			agentConfiguration.setAgentSftpUserName(properties.getProperty("agent_sftp_user"));
			agentConfiguration.setAgentSftpPort(properties.getProperty("agent_sftp_port"));
			agentConfiguration.setAgentSftpPassWord(properties.getProperty("agent_sftp_password"));
			//agentConfiguration.setAgentSftpKeyPath(properties.getProperty("agent_sftp_key_path"));
			agentConfiguration.setAgentSftpKeyPath(System.getProperty("user.dir")+"\\Data-Bridge.ppk");
			agentConfiguration.setHeartBeatTimePeriod(properties.getProperty("heart_beat_time_period"));
			agentConfiguration.setExtractionTimePeriod(properties.getProperty("extraction_time_period"));
			agentConfiguration.setFileMonitorTimePeriod(properties.getProperty("file_monitor_time_period"));
			agentConfiguration.setInstanceId(properties.getProperty("instance_id"));
			agentConfiguration.setKeycloakAuthServerUrl(properties.getProperty("keycloak_auth_server_url"));
			agentConfiguration.setKeycloakRealm(properties.getProperty("keycloak_realm"));
			agentConfiguration.setKeycloakClientId(env.getProperty("clientId"));
			agentConfiguration.setKeycloakClientSecret(env.getProperty("clientSecret"));
//			agentConfiguration.setKeycloakUser(env.getProperty("userName"));
//			agentConfiguration.setKeycloakPassword(env.getProperty("password"));
			
//			agentConfiguration.setKeycloakAuthServerUrl(properties.getProperty("keycloak_auth_server_url"));
//			agentConfiguration.setKeycloakRealm(properties.getProperty("keycloak_realm"));
//			agentConfiguration.setKeycloakClientId(properties.getProperty("keycloak_client_id"));
//			agentConfiguration.setKeycloakClientSecret(properties.getProperty("keycloak_client_secret"));
//			agentConfiguration.setKeycloakUser(properties.getProperty("keycloak_user"));
//			agentConfiguration.setKeycloakPassword(properties.getProperty("keycloak_password"));
			
		} catch (Exception e) {
			logger.error("Exception in agentConfiguration() of ExternalConfiguration class ", e);
		}
		return agentConfiguration;

	}
}
