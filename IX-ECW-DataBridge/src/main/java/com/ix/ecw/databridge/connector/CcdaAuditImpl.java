package com.ix.ecw.databridge.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.ix.ecw.databridge.model.AgentConfiguration;

/**
 * The Class CcdaAuditImpl.
 */
@Component
@PropertySource(value = "classpath:/application.properties")
public class CcdaAuditImpl implements CcdaAudit {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${audit.createOrUpdateAudit}")
	private String createOrUpdateAudit;
	
	@Autowired
	AgentConfiguration agentConfiguration;

	/**
	 * Save Audit
	 * 
	 * @param eventText value3
	 * @param eventData value
	 */
	public void saveAudit(JsonObject auditJson) {
		logger.info("Started save audit, CcdaAuditImpl->(" + "saveAudit(JsonObject auditJson))");
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl()+createOrUpdateAudit;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(auditJson.toString(), headers);
//			restTemplate.postForLocation(url, entity);
		} catch (Exception ex) {
			logger.error("Exception in saveAudit of CcdaAuditImpl");
			ex.printStackTrace();
		}
		logger.info("Completed save audit, CcdaAuditImpl->(" + "saveAudit(JsonObject auditJson)");
	}

}
