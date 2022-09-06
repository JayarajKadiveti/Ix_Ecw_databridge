package com.ix.ecw.databridge.client;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ix.ecw.databridge.connector.CcdaAudit;
import com.ix.ecw.databridge.model.AgentConfiguration;
import com.ix.ecw.databridge.model.AgentManagement;
import com.ix.ecw.databridge.model.DataSet;
import com.ix.ecw.databridge.model.DataSource;
import com.ix.ecw.databridge.model.ExtractionDetails;
import com.ix.ecw.databridge.model.KeycloakAuthResponse;
import com.ix.ecw.databridge.utils.ClientConstant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class AgentManagementClient {

	/** The Constant LOGGER. */
	private static final Logger logger = LoggerFactory.getLogger(AgentManagementClient.class);
	private static final String GRANT_TYPE = "grant_type";
	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	public static final String CLIENT_CREDENTIALS = "password";
	public static final String SCOPE = "scope";
	public static final String TOKEN = "token";
	public static final String REALM = "realms/";
	private static final String X_REQUEST_ID = "ccda-agent";
	private static final String APPLICATION_URL_FORM_ENCODED = "application/x-www-form-urlencoded";
	@Value("${spring.application.name}")
	private String appName;

	@Value("${agent.selfregisteration}")
	private String selfRegisterationUrl;

	@Value("${agent.heartbeat.update}")
	private String heartBeatUpdateUrl;

	@Value("${agent.newfile.update}")
	private String newFileUpdateUrl;

	@Value("${create.extrction.url}")
	private String createExtractionUrl;

	@Value("${extrction.dataset.url}")
	private String dataSetUrl;

	/** The data source url. */
	@Value("${datasource.serviceurl}")
	private String dataSourceUrl;

	@Value("${last.extraction.time.url}")
	String lastExtractionTimeUrl;

	/** The extraction url. */
	@Value("${extraction.url}")
	private String extractionUrl;

	/** The update extraction url. */
	@Value("${update.extrction.url}")
	private String updateExtractionUrl;
	/** The connector id. */
	@Value("${connectorId}")
	private Long connectorId;

	@Value("${agent.sftp.details}")
	String agentSftpDetails;

	@Autowired
	private CcdaAudit ccdaAudit;

	@Autowired
	AgentConfiguration agentConfiguration;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	ConfigurableApplicationContext configurableApplicationContext;

	public ResponseEntity<?> updateHearBeatAndFilesArrivalStatus() {
		logger.info("\n Start updateHearBeatAndFilesArrivalStatus() ");
		ResponseEntity<?> statusMessage = null;
		try {
			String hearbeatUrl = agentConfiguration.getIxPlatformBaseUrl() + heartBeatUpdateUrl;
			getRequestBodyAndCallEndPoint(hearbeatUrl, false);
		} catch (HttpClientErrorException e) {
			logger.error("Exception in updateHearBeatAndFilesArrivalStatus ", e);
			statusMessage = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getResponseHeaders(),
					e.getStatusCode());
		} catch (Exception ex) {
			logger.error("Exception in updateHearBeatAndFilesArrivalStatus of AgentManagementClient class", ex);
		}
		logger.info("\n End updateHearBeatAndFilesArrivalStatus() ");
		return statusMessage;
	}

	public ResponseEntity<?> updateNewfilesExist(boolean isNewFile) {
		logger.info("\n Start updateNewfilesExist() ");
		ResponseEntity<?> statusMessage = null;
		try {
			String newFileExistsUrl = agentConfiguration.getIxPlatformBaseUrl() + newFileUpdateUrl;
			getRequestBodyAndCallEndPoint(newFileExistsUrl, isNewFile);
		} catch (HttpClientErrorException e) {
			logger.error("Exception in updateNewfilesExist ", e);
			statusMessage = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getResponseHeaders(),
					e.getStatusCode());
		} catch (Exception ex) {
			logger.error("Exception in updateNewfilesExist  of AgentManagementClient class", ex);
		}
		logger.info("\n End updateNewfilesExist() ");
		return statusMessage;
	}

	/**
	 * 
	 * @return
	 */
	public ResponseEntity<?> doSelfRegisteration() {
		logger.info("\n Start doSelfRegisteration() ");
		ResponseEntity<?> statusMessage = null;
		logger.info(agentConfiguration.getDatasourceId() + " doSelfRegisteration dataSourceId");
		try {
			AgentManagement agentSelfRegistration = getAgentManagementData();
			HttpHeaders headers = new HttpHeaders();
			// set the content Type
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + authenticate().getAccessToken());
			String xRequestId = agentConfiguration.getKeycloakClientId()+":"+agentConfiguration.getKeycloakClientSecret();
			String base64EncodedString =
		            Base64.getEncoder().encodeToString(xRequestId.getBytes(StandardCharsets.UTF_8));
			logger.info("Encoded String:::::{}",base64EncodedString);
			logger.info("agentSelfRegistration.getSftpFileExtension():::::{}",agentSelfRegistration.getSftpFileExtension());
			logger.info("agentSelfRegistration.getSftpFileName():::::{}",agentSelfRegistration.getSftpFileName());
			headers.set("X-Request-ID", base64EncodedString);
			HttpEntity<AgentManagement> request = new HttpEntity<>(agentSelfRegistration, headers);
			logger.info(
					"RegistrationURL =========>" + agentConfiguration.getIxPlatformBaseUrl() + selfRegisterationUrl);
			String regUrl = agentConfiguration.getIxPlatformBaseUrl() + selfRegisterationUrl;
			logger.info("RegistrationURL =========>" + selfRegisterationUrl);
			statusMessage = restTemplate.postForEntity(regUrl, request, String.class);
		} catch (HttpClientErrorException e) {
			logger.error("Exception in doSelfRegisteration of TaskSchedular ", e);
			statusMessage = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getResponseHeaders(),
					e.getStatusCode());
		} catch (Exception ex) {
			logger.error("Exception in doSelfRegisteration  of AgentManagementClient class ", ex);
		}
		logger.info("\n End doSelfRegisteration() ");
		return statusMessage;
	}

	public void shutDownApplication() {
		logger.info("\n Start shutDownApplication() ");
		try {
			int exitCode = SpringApplication.exit(configurableApplicationContext, new ExitCodeGenerator() {
				@Override
				public int getExitCode() {
					logger.info("\n INSIDE shutDownApplication() ");
					return 0;
				}
			});
			System.exit(exitCode);
		} catch (Exception ex) {
			logger.error("Exception in shutDownApplication of AgentManagementClient class", ex);
		}
		logger.info("\n End shutDownApplication() ");
	}

	ResponseEntity<?> updateHeartBeatORFilesArrivalStatus(AgentManagement agentHeartBeat, String url) {
		ResponseEntity<?> message = null;
		try {
			String xRequestId = agentConfiguration.getKeycloakClientId()+":"+agentConfiguration.getKeycloakClientSecret();
			String base64EncodedString =
		            Base64.getEncoder().encodeToString(xRequestId.getBytes(StandardCharsets.UTF_8));
			logger.info("Encoded String:::::{}",base64EncodedString);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + authenticate().getAccessToken());
			headers.set("X-Request-ID", base64EncodedString);
			HttpEntity<AgentManagement> request = new HttpEntity<>(agentHeartBeat, headers);
			message = restTemplate.postForEntity(url, request, String.class);
		} catch (Exception ex) {
			logger.error("Exception in updateHeartBeatORFilesArrivalStatus  of AgentManagementClient class ", ex);
		}
		return message;
	}

	public int getDataSetByDataSourceId(int dataSourceId) {
		logger.info("\n Start Invoking getDataSetByDataSourceId(int dataSourceId)");
		int dataSetId = 0;
		try {
			dataSetId = restTemplate.getForObject(agentConfiguration.getIxPlatformBaseUrl() + dataSetUrl + dataSourceId,
					Integer.class);
		} catch (Exception ex) {
			logger.error("Exception in getDataSetByDataSourceId  of AgentManagementClient class ", ex);
		}
		return dataSetId;
	}

	public ResponseEntity<?> createExtractionByDatasetId(int datasetId) {
		logger.info("\n Start Invoking createExtractionByDatasetId(int datasetId)");
		ResponseEntity<?> message = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<AgentManagement> request = new HttpEntity<>(headers);
			String url = agentConfiguration.getIxPlatformBaseUrl() + createExtractionUrl.replaceAll("\\s", "");
			message = restTemplate.postForEntity(url + datasetId, request, String.class);
		} catch (Exception ex) {
			logger.error("Exception in createExtractionByDatasetId of AgentManagementClient class ", ex);
		}
		return message;
	}

	private AgentManagement getRequestBodyAndCallEndPoint(String url, boolean isNewFile) {
		logger.info("\n Start getRequestBodyAndCallEndPoint(String url,boolean isNewFile) ");
		AgentManagement agentManagement = new AgentManagement();
		try {
			agentManagement = getAgentManagementData();
			agentManagement.setNewFilesExists(isNewFile);
			updateHeartBeatORFilesArrivalStatus(agentManagement, url);
			logger.info("\n End of getRequestBodyAndCallEndPoint(String url,boolean isNewFile) ");
			return agentManagement;
		} catch (Exception ex) {
			logger.error("Exception in getRequestBodyAndCallEndPoint ", ex);
		}
		return agentManagement;
	}

	private AgentManagement getAgentManagementData() {
		logger.info("\n Start getAgentManagementData() of AgentManagementClient class ");
		AgentManagement agentManagement = new AgentManagement();
		try {
			
			String serviceInstanceId = "_" + appName + "_" + agentConfiguration.getInstanceId();
			/*
			 * String ipaddress = getSystemIP(); String ip = ""; String host = ""; if
			 * (StringUtils.isNotBlank(ipaddress)) { ip = ipaddress.split(",")[0]; host =
			 * ip; } else { createAudit(null, "CCDA agent getSystemIP() ",
			 * "Caught exception in get System IP Address of AgentManagementClient class " +
			 * " : Hence application is shutting down."); shutDownApplication(); }
			 */
			agentManagement.setAgentName(appName + "_" + agentConfiguration.getDatasourceId());
			agentManagement.setIpAddress(agentConfiguration.getAgentSftpHost());
			agentManagement.setHostName(agentConfiguration.getAgentSftpHost());
			agentManagement.setHostUser(agentConfiguration.getAgentSftpUserName());
			agentManagement.setHostPassword(agentConfiguration.getAgentSftpPassWord());
			agentManagement.setHostPort(agentConfiguration.getAgentSftpPort());
			Path path = Paths.get(agentConfiguration.getAgentSftpKeyPath());
			byte[] bytes = Files.readAllBytes(path);
			agentManagement.setPemFilePath(new String(Base64.getEncoder().encode(bytes)));
			ApplicationHome home = new ApplicationHome(getClass());
			agentManagement.setServiceName(home.getDir().toString());
			agentManagement.setServiceInstanceId(appName + "_" + agentConfiguration.getInstanceId());
			agentManagement.setDataSourceId(Integer.parseInt(agentConfiguration.getDatasourceId()));
			String uniqueInstanceId = agentConfiguration.getAgentSftpHost() + "_" + agentConfiguration.getDatasourceId() + serviceInstanceId;
			agentManagement.setUniqueInstanceId(uniqueInstanceId);
			agentManagement.setSftpFileName(agentManagement.getAgentName()+"_"+path.getFileName().toString());
		} catch (Exception ex) {
			logger.error("Exception in getAgentManagementData() of AgentManagementClient class ", ex);
		}
		return agentManagement;
	}

	/**
	 * 
	 * @param datasourceId
	 * @return
	 */
	public Date getLastExtractionDateTimeByDataSourceId(int datasourceId, int connectorId) {
		Date formattedDate = null;
		logger.info("Start getLastExtractionDateTimeByDataSourceId() AgentManagementClient class\n"
				+ lastExtractionTimeUrl);
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl() + lastExtractionTimeUrl;
			logger.info(" :::::: lastExtractionTimeUrl :::::: " + url + datasourceId + "/" + connectorId);
			formattedDate = restTemplate.getForObject(url + datasourceId + "/" + connectorId, Date.class);
			logger.info("LastExtractionDateTime :: formattedDate " + formattedDate);
		} catch (Exception e) {
			logger.error("Exception in getLastExtractionDateTimeByDataSourceId method AgentManagementClient class", e);
		}
		logger.info("End getLastExtractionDateTimeByDataSourceId method of AgentManagementClient class \n");
		return formattedDate;
	}

	/**
	 * get data for Processing Agent it is a API Call for required module(Dataset,
	 * DataSource, Extraction).
	 *
	 * @return the extraction details list
	 */
	public List<ExtractionDetails> getExtractionDetailsList() {
		logger.info("\n Start getting ExtractionDetails -> getExtractionDetailsList()");
		List<ExtractionDetails> extractionDetailsList = null;
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl() + extractionUrl + "datasourceId="
					+ agentConfiguration.getDatasourceId() + "&connectorId=" + connectorId + "&status="
					+ ClientConstant.PENDING;
			logger.info("\n *******************************Getting ExtractionDetails -> Url " + url);
			ExtractionDetails[] extractionResponseArray = restTemplate.getForObject(url, ExtractionDetails[].class);
			for (ExtractionDetails ex : extractionResponseArray) {
				logger.info("\n ============= ExtractionDetails =============== " + ex.getDataSetId());
			}
			if (extractionResponseArray != null)
				extractionDetailsList = Arrays.asList(extractionResponseArray);
		} catch (Exception e) {
			logger.error("\n Inside getting ExtractionDetails -> getExtractionDetailsList() ", e);
		}
		return extractionDetailsList;
	}

	public KeycloakAuthResponse authenticate() {
		KeycloakAuthResponse keycloakResponse = new KeycloakAuthResponse();
		String authUrl = agentConfiguration.getKeycloakAuthServerUrl();
		String realm = agentConfiguration.getKeycloakRealm();
		String url = authUrl + "realms/" + realm + "/protocol/openid-connect/token";
		String authValuesclient = agentConfiguration.getKeycloakClientId() + ":"
				+ agentConfiguration.getKeycloakClientSecret();
		String base64EncodedStringclient = Base64.getEncoder()
				.encodeToString(authValuesclient.getBytes(StandardCharsets.UTF_8));
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse(APPLICATION_URL_FORM_ENCODED);
		RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
		Request request = new Request.Builder().url(url).method("POST", body)
				.addHeader("Content-Type", APPLICATION_URL_FORM_ENCODED)
				.addHeader("Authorization", "Basic " + base64EncodedStringclient)
				.build();
		Response clientResponse;
		try {
			clientResponse = client.newCall(request).execute();
			if (!clientResponse.isSuccessful()) {
				logger.error("Failed to authenticate");
				throw new RuntimeException("Failed to authenticate");
			}
			ObjectMapper objectMapper = new ObjectMapper();
			keycloakResponse = objectMapper.readValue(clientResponse.body().string(), KeycloakAuthResponse.class);
			logger.info("Access Token::::" + keycloakResponse.getAccessToken());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Authentication success");
		return keycloakResponse;
	}

	/**
	 * Gets the data source details.
	 *
	 * @param datasourceId the datasource id
	 * @return value
	 */
	public DataSource getDataSourceDetails(int datasourceId) {
		logger.info("\n Start DataSource getDataSourceDetails(int datasourceId) " + datasourceId);
		DataSource dataSourceDetails = null;
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl() + dataSourceUrl;
			logger.info("\n  DataSource url " + url);
			dataSourceDetails = restTemplate.getForObject(url + datasourceId, DataSource.class);
		} catch (Exception e) {
			logger.error("\n Inside DataSource getDataSourceDetails(int datasourceId) ", e);
		}
		return dataSourceDetails;
	}

	/**
	 * Gets the data set details.
	 *
	 * @param datasetId the dataset id
	 * @return the data set details
	 */
	public DataSet getDataSetDetails(int datasetId) {
		DataSet dataSetDetails = null;
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl() + dataSetUrl;
			dataSetDetails = restTemplate.getForObject(url + datasetId, DataSet.class);
		} catch (Exception e) {
			logger.error("\n Exception inside getDataSetDetails(int datasetId) ", e);
		}
		return dataSetDetails;
	}

	/**
	 * Update extraction status.
	 *
	 * @param etId   the etId
	 * @param status the status
	 * @return value
	 */
	public void updateExtractionStatus(int etId, String status) {
		try {
			String url = agentConfiguration.getIxPlatformBaseUrl() + updateExtractionUrl;
			logger.info("\n Start updateExtractionStatus(int etId, String status)");
			StringBuilder buildUrl = new StringBuilder();
			buildUrl.append(url);
			buildUrl.append(etId);
			buildUrl.append("/");
			buildUrl.append(status);
			logger.info("\n <<<<<<<<<<<<<<<<<<<<< updateExtractionStatus URL :: " + buildUrl.toString());

			restTemplate.put(buildUrl.toString(), String.class);
			logger.info("\n ExtractionStatus updated with extarctionId: " + etId + " status:" + status);
		} catch (Exception e) {
			logger.error("\n Exception in updateExtractionStatus(int etId, String status) ", e);
		}
	}

	/**
	 * Gets the sftp details.
	 * 
	 * @return the sftp details
	 */
	public ResponseEntity<?> getSftpDetails() {
		ResponseEntity<String> sftpDetails = null;
		try {
			String xRequestId = agentConfiguration.getKeycloakClientId()+":"+agentConfiguration.getKeycloakClientSecret();
			String base64EncodedString =
		            Base64.getEncoder().encodeToString(xRequestId.getBytes(StandardCharsets.UTF_8));
			logger.info("Encoded String:::::{}",base64EncodedString);
			HttpHeaders headers = new HttpHeaders();
			// set the content Type
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + authenticate().getAccessToken());
			headers.set("X-Request-ID", base64EncodedString);
			HttpEntity<String> request = new HttpEntity<>(null, headers);
			String url = agentConfiguration.getIxPlatformBaseUrl() + agentSftpDetails;
			sftpDetails = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		} catch (Exception e) {
			logger.error("\n Exception inside getSftpDetails() ", e);
		}
		return sftpDetails;
	}

	/**
	 * Method to Get System IP Address
	 * 
	 * @return IP Address
	 */
	public static String getSystemIP() {

		String systemipaddress = "";
		try {
			URL url_name = new URL(ClientConstant.IP_ADDRESS_URL);
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
			systemipaddress = sc.readLine().trim();
			if (!(systemipaddress.length() > 0)) {
				try {
					InetAddress localhost = InetAddress.getLocalHost();
					systemipaddress = (localhost.getHostAddress()).trim();

				} catch (Exception e) {
					logger.error("\n Exception in getSystemIP() ", e);
				}
			}
		} catch (Exception e) {
			logger.error("\n Exception in getSystemIP() ", e);
		}
		logger.info("\n ========================= Your IP Address: " + systemipaddress + "\n");
		return systemipaddress;
	}

	/**
	 * Creates the audit.
	 *
	 * @param extractionDetails  the extraction details
	 * @param message            the message
	 * @param descriptionDetails the description details
	 */
	public void createAudit(ExtractionDetails extractionDetails, String message, String descriptionDetails) {
		// if (extractionDetails != null) {
		try {
			JsonObject auditJson = new JsonObject();

			String extractionId = (extractionDetails != null
					&& StringUtils.isNotBlank(extractionDetails.getExtractionId().toString()))
							? extractionDetails.getExtractionId().toString()
							: "";
			Gson gson = new Gson();
			HashMap<String, String> data = new HashMap<>();
			auditJson.addProperty("category", "CcdaAgentt-TaskSchedular");
			auditJson.addProperty("eventName", "Automatic CcdaAgent-TaskSchedular");
			auditJson.addProperty("userId", "System");
			auditJson.addProperty("userName", "System");
			data.put("ExtractionId", extractionId);

			StringBuilder description = new StringBuilder();
			if (StringUtils.isNotBlank(extractionId)) {
				description.append("extraction with id ");
				description.append(extractionDetails.getExtractionId().toString());
			}
			if (ClientConstant.INPROGRESS.equalsIgnoreCase(message)) {
				auditJson.addProperty("eventStatus", "Started");
				description.append(", Ccda Extraction started" + descriptionDetails);
			} else if (ClientConstant.COMPLETED.equalsIgnoreCase(message)) {
				auditJson.addProperty("eventStatus", ClientConstant.COMPLETED);
				description.append(", Ccda Extraction completed" + descriptionDetails);
			} else if (ClientConstant.FAILED.equalsIgnoreCase(message)) {
				auditJson.addProperty("eventStatus", ClientConstant.FAILED);
				description.append(", Ccda Extraction failed" + descriptionDetails);
			} else {
				auditJson.addProperty("eventStatus", message);
				description.append(", Ccda Extraction = " + descriptionDetails);
			}
			data.put("Description", description.toString());
			auditJson.addProperty("eventData", gson.toJson(data));
			auditJson.addProperty("serviceName", getClass().getName());
			ccdaAudit.saveAudit(auditJson);

		} catch (Exception ex) {
			logger.error(
					"\n Exception in creating and saving Audit messages of TaskSchedular->"
							+ "(createAudit(ExtractionDetails extractionDetails, String message)) with exception = ",
					ex);
		}
		// }
	}
}
