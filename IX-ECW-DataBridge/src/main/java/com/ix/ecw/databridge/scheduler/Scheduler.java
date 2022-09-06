package com.ix.ecw.databridge.scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.ix.ecw.databridge.client.AgentManagementClient;
import com.ix.ecw.databridge.connector.SFTPConnector;
import com.ix.ecw.databridge.model.AgentConfiguration;
import com.ix.ecw.databridge.model.AgentManagement;
import com.ix.ecw.databridge.model.DataSource;
import com.ix.ecw.databridge.model.DocumentReference;
import com.ix.ecw.databridge.model.ExtractionDetails;
import com.ix.ecw.databridge.model.PatientList;
import com.ix.ecw.databridge.model.SftpStatus;
import com.ix.ecw.databridge.utils.ClientConstant;
import com.ix.ecw.databridge.utils.ExcelUtil;
import com.ix.ecw.databridge.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Scheduler {

	/** The Constant LOGGER. */
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

	/** The sftp connector. */
	@Autowired
	private SFTPConnector sftpConnector;

	/** The data set url. */
	@Value("${dataset.serrvice_url}")
	private String dataSetUrl;

	/** The update dataset. */
	@Value("${update.dataset}")
	private String updateDataset;

	/** The connector id. */
	@Value("${connectorId}")
	private Long connectorId;

	@Value("${aws.s3.extractiondirectory}")
	String awsS3ExtractionDirectory;

	@Value("${aws.s3.pushdirectory}")
	String awsS3PushDirectory;

	@Value("${azure.storage.extractiondirectory}")
	String azureStorageExtractionDirectory;

	@Autowired
	AgentManagementClient agentManagementClient;

	@Autowired
	AgentManagement agentManagement;

	@Autowired
	AgentConfiguration agentConfiguration;

	private Map<String, String> sftpMap = null;

	List<DocumentReference> documentReferences = new ArrayList<>();

//	@Scheduled(cron = "*/10 * * * * *")
//	public void getData() {
//
//		File dir = new File(System.getProperty("user.dir") + "\\eCW_DownloadedFiles\\");
//		if (dir.exists()) {
//			listFilesForFolder(dir);
//		}
//
//	}
//
//	public void listFilesForFolder(final File folder) {
//
//		for (final File fileEntry : folder.listFiles()) {
//			if (fileEntry.isDirectory()) {
//				listFilesForFolder(fileEntry);
//			} else if (fileEntry.isFile()) {
//				System.out.println(fileEntry.getName());
//			} else {
//				System.out.println("No Files found");
//
//			}
//		}
//	}

	public void runExtractionService() {

		try {
			List<ExtractionDetails> extractionDetailsList = agentManagementClient.getExtractionDetailsList();
			if (extractionDetailsList != null) {
				extractionDetailsList.forEach(extractionDetails -> {
					agentManagementClient.createAudit(extractionDetails, ClientConstant.INPROGRESS, "");
					agentManagementClient.updateExtractionStatus(extractionDetails.getExtractionId(),
							ClientConstant.INPROGRESS);
				});

				runDataSetAndSourceService(extractionDetailsList);
			} else {
				logger.info("\n No extractions found.");
			}

		} catch (Exception ex) {
			logger.error("\n Exception in TaskSchedular -> runExtractionService()", ex);
		}
	}

	public void runDataSetAndSourceService(List<ExtractionDetails> extractionDetailsList) {
		logger.info("\n extractionDetailsList-------------->" + extractionDetailsList);

		logger.info("\n\n******runDataSetAndSourceService********\n\n");
		// ClassLoader classLoader = new TaskSchedular().getClass().getClassLoader();
		// File privateKey = new
		// File(classLoader.getResource(sftpPrivateKey).getFile());
		// File privateKey = new File(agentConfiguration.getSftpPrivateKey());
		extractionDetailsList.forEach(extractionDetails -> {
			logger.info("\n\n ***************\nStarted extraction of Data from Agent for extraction Id :: "
					+ extractionDetails.getExtractionId() + "\n ***************\n\n");
			String status = "";
			String inputDirectoryToSftp = System.getProperty("user.dir") + "\\eCW_DownloadedFiles\\";
			// String sftpKey = privateKey.getAbsolutePath();
			logger.info("*****SFTP KEY ***** " + sftpMap.get("keyPath"));
			// sftpKey = sftpKey.replaceAll("\\\\", "/");
			String descriptionDetails = "";
			try {
				boolean isValidalidDirectory = Boolean.FALSE;
				DataSource dataSources = agentManagementClient
						.getDataSourceDetails(extractionDetails.getDataSourceId());
				Date lastUpdatedTime = agentManagementClient
						.getLastExtractionDateTimeByDataSourceId(dataSources.getDataSourceId(), connectorId.intValue());
				if (dataSources.getConnectorId() == connectorId.intValue()) {
					logger.info("***** CCDA Extrcation Type  ***** " + dataSources.getCcdaExtractionType());

					if (StringUtils.isNotBlank(inputDirectoryToSftp)) {
						logger.info(" ====Checking inputDirectoryToSftp=== " + inputDirectoryToSftp);
						inputDirectoryToSftp = inputDirectoryToSftp.replaceAll("\\\\", "/");
						boolean isFileCreated = new File(inputDirectoryToSftp).isDirectory();
						boolean isAllXmals = isDirContainsOnlyXmlExt(inputDirectoryToSftp);
						logger.info("File check" + isFileCreated);
						logger.info("File check -isAllXmals " + isAllXmals);
//						isValidalidDirectory = Files.exists(Paths.get(inputDirectoryToSftp))
//								&& (new File(inputDirectoryToSftp)).isDirectory()
//								&& isDirContainsOnlyXmlExt(inputDirectoryToSftp);
						isValidalidDirectory = (new File(inputDirectoryToSftp)).isDirectory();
						logger.info(" ====Checking inputDirectoryToSftp is valid or not === " + isValidalidDirectory);
					}
					if (isValidalidDirectory) {
						logger.info(" ::: dataSources.getCcdaPushedType() ::: "
								+ dataSources.getCcdaPushedType().toString());
//						if (dataSources.getCcdaPushedType().equalsIgnoreCase(ClientConstant.FILESYSTEM)) {
						logger.info("\n Invoking SFTP uploadFiles() in TaskSchedular \n");
						logger.info("\n <<---sftpHOST ::  " + sftpMap.get("host") + "\n sftpPort  :: "
								+ "\n key path :: " + sftpMap.get("keyPath") + "\n" + sftpMap.get("port")
								+ "\n sftpUser :: " + sftpMap.get("userName")
								+ "\n extractionDetails.getFilesLocation().replaceAll(\"\\\\\\\\\", \"/\")  :: "
								+ extractionDetails.getFilesLocation().replaceAll("\\\\", "/")
								+ "\n inputDirectoryToSftp ::: " + inputDirectoryToSftp + "\n extractionDetails ::  "
								+ extractionDetails + "\n isValidalidDirectory ::  " + isValidalidDirectory);
						try {
							if (sftpMap != null && StringUtils.isNotBlank(sftpMap.get("host"))
									&& StringUtils.isNotBlank(sftpMap.get("keyPath"))
									&& StringUtils.isNotBlank(sftpMap.get("port"))
									&& StringUtils.isNotBlank(sftpMap.get("userName"))) {
								String code = sftpConnector.uploadFiles(sftpMap,
										extractionDetails.getFilesLocation().replaceAll("\\\\", "/"),
										inputDirectoryToSftp, extractionDetails, lastUpdatedTime);
								if (code.equals(ClientConstant.SUCCESS_CODE_TWO_HUNDRED)) {
									status = ClientConstant.COMPLETED;
									descriptionDetails = "Files transferred successfully";

								} else if (code.equals(ClientConstant.INTERNAL_SERVER_ERROR)) {
									status = ClientConstant.FAILED;
									descriptionDetails = "Internal Server error";
								}
							} else {
								status = ClientConstant.FAILED;
								descriptionDetails = "SFTP Details are missing";
							}
						} catch (Exception ex) {
							logger.error(
									"\n  Error in Files upload issue.Please check sftpHOST, sftpPort, sftpUser, sftpPassword, sftpKey.",
									ex);
							descriptionDetails = "Files upload issue.Please check sftpHOST, sftpPort, sftpUser, sftpPassword, sftpKey.";
							status = ClientConstant.FAILED;
						}
//						} else {
//							if ((new File(inputDirectoryToSftp)).isFile()) {
//								descriptionDetails = "Expected directory path but got file path from DataSource.";
//							} else {
//								descriptionDetails = "Could not find directory in CCDA Agent Server.";
//							}
//							status = ClientConstant.FAILED;
//						}
					}
				}
			} catch (Exception ex) {
				logger.error("\n Error in runDataSetAndSourceService of TaskSchedular ", ex);
				descriptionDetails = "Files upload issue.Please check sftpHOST, sftpPort, sftpUser, sftpPassword, sftpKey.";
				status = ClientConstant.FAILED;
			}
			agentManagementClient.createAudit(extractionDetails, status, descriptionDetails);
			logger.info("Sending Status +==== " + status);
			agentManagementClient.updateExtractionStatus(extractionDetails.getExtractionId(), status);
			logger.info("\n\n ***************\nCompleted extraction of Data from Agent for extraction Id :: "
					+ extractionDetails.getExtractionId() + "\n ***************\n\n");
		});

	}

	public boolean isDirContainsOnlyXmlExt(String dir) {
		boolean flag = true;
		try {
			File[] filesInDirectory = new File(dir).listFiles();
			for (File f : filesInDirectory) {
				String filePath = f.getAbsolutePath();
				String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
				logger.info("====File Extension===" + fileExtenstion);

//				if (!"xml".equalsIgnoreCase(fileExtenstion)) {
//					flag = false;
//				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception inisDirContainsOnlyXmlExt of TaskSchedular class ", ex);
		}
		return flag;
	}

	@PostConstruct
	public void init() {
		logger.info("\n Start Scheduler invoking -> @PostConstruct init()");
		try {

			ResponseEntity<?> status = agentManagementClient.doSelfRegisteration();
			agentManagementClient.createAudit(null, "CCDA AGENT", status.getStatusCode() + " : " + status.getBody());
			logger.info(" =========== self registeration status code ====== " + status.getStatusCode() + " message :: "
					+ status.getBody());
			if (status != null && (status.getStatusCode().equals(HttpStatus.OK)
					|| status.getStatusCode().equals(HttpStatus.ALREADY_REPORTED))) {
				getSftpDetailsAndConvertToMapAndFile();
				// runHeartBeatScheduler();
				runPendingExtractionScheduler();

			} else {
				agentManagementClient.createAudit(null, "CCDA agent self registeration",
						status.getStatusCode() + " : Hence application is shutting down.");
				agentManagementClient.shutDownApplication();
			}
		} catch (Exception e) {
			logger.error("\n Caught exception in @PostConstruct init() of TaskSchedular class", e);
			agentManagementClient.createAudit(null, "CCDA agent self registeration",
					"Caught exception in @PostConstruct init() of TaskSchedular class "
							+ " : Hence application is shutting down.");
			agentManagementClient.shutDownApplication();
		}
		logger.info("\n Completed Scheduler invoking -> @PostConstruct init()");
	}

	private void runHeartBeatScheduler() {
		logger.info("\n Start heart beat ScheduledExecutorService");
		try {
			ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
			Runnable drawRunnable = new Runnable() {
				public void run() {
					try {
						agentManagementClient.updateHearBeatAndFilesArrivalStatus();
					} catch (Exception e) {
						logger.error(
								"\n Caught exception in in run() method heartbeat ScheduledExecutorService of TaskSchedular class ",
								e);
					}
				}
			};
			schedule.scheduleAtFixedRate(drawRunnable, 0, Integer.parseInt(agentConfiguration.getHeartBeatTimePeriod()),
					TimeUnit.SECONDS);
		} catch (Exception ex) {
			logger.error("\n Caught exception in runHeartBeatScheduler of TaskSchedular class ", ex);
		}
		logger.info("\n Completed heart beat ScheduledExecutorService");
	}

	private void runPendingExtractionScheduler() {
		logger.info("\n Start pending extraction ScheduledExecutorService ");
		try {
			ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
			Runnable drawRunnable = new Runnable() {
				public void run() {
					try {
						File dir = new File(System.getProperty("user.dir") + "\\eCW_DownloadedFiles\\");
						if (dir.exists()) {
							File[] filesInDirectory = new File(dir.getPath()).listFiles();
							if (filesInDirectory.length > 0) {

								List<DocumentReference> documentReferences = new ArrayList<>();
								for (File file : filesInDirectory) {
									String patientId = file.getName().substring(file.getName().lastIndexOf("_")+1,
											file.getName().lastIndexOf("."));
									String fileName = file.getName().substring(file.getName().lastIndexOf("\\")+1);
									DocumentReference documentReference = new DocumentReference();
									documentReference.setPatientId(patientId);
									documentReference.setFileName(fileName);
									documentReferences.add(documentReference);

								}

								if (documentReferences.size() > 0) {

									List<PatientList> patientArrayList = Utils.getPatientListFromJson();
									
									
									prepareDocumentRefCsv(documentReferences);
									preparePatientCsv(patientArrayList);

								}

								runExtractionService();

							}
						}

					} catch (Exception e) {
						logger.error(
								"\n Caught exception in run() method pending extraction ScheduledExecutorService of TaskSchedular class ",
								e);
					}
				}
			};
			schedule.scheduleAtFixedRate(drawRunnable, 0,
					Integer.parseInt(agentConfiguration.getExtractionTimePeriod()), TimeUnit.SECONDS);
		} catch (Exception ex) {
			logger.error("\n Caught exception in runPendingExtractionScheduler of TaskSchedular class ", ex);
		}
		logger.info("\n Completed pending extraction ScheduledExecutorService ");
	}

	private void getSftpDetailsAndConvertToMapAndFile() {
		try {
			ResponseEntity<?> json = agentManagementClient.getSftpDetails();
			if (json.getBody() != null && StringUtils.isNotBlank(json.getBody().toString())) {
				JSONObject sftpJson = new JSONObject(json.getBody().toString());
				sftpMap = new HashMap<String, String>();
				sftpMap.put("userName", sftpJson.get("sftpUserName").toString());
				sftpMap.put("host", sftpJson.get("sftpHost").toString());
				sftpMap.put("port", sftpJson.get("sftpPort").toString());
				sftpMap.put("password", sftpJson.get("sftpPassword").toString());
				sftpMap.put("datalakeAwsAccessKey", sftpJson.get("awsAccessKey").toString());
				sftpMap.put("datalakeAwsSecretKey", sftpJson.get("awsSecretKey").toString());
				sftpMap.put("datalakeAwsBucketName", sftpJson.get("awsBucketName").toString());
				sftpMap.put("datalakeAwsRegion", sftpJson.get("awsRegion").toString());
				ApplicationHome home = new ApplicationHome(getClass());
				sftpMap.put("keyPath", home.getDir() + "/" + sftpJson.get("sftpFileName").toString());

				String message = convertFile(sftpJson.get("sftpKey").toString(), home.getDir(),
						sftpJson.get("sftpFileName").toString());

			} else {
				logger.info("\n ================ Could not create sftp map and file ================ ");
			}
		} catch (Exception ex) {
			logger.error("\n Caught exception in getSftpDetails() of TaskSchedular class ", ex);
		}

		logger.info("\n SFTP details " + sftpMap);

		logger.info("\n Completed getSftpDetails() of TaskSchedular class ");
	}

	// Convert a Base64 string and create a file
	public String convertFile(String fileString, File directroy, String fileName) {
		String message = null;
		logger.info("\n Start convertFile() of TaskSchedular class ");
		try {
			byte[] bytes = Base64.getDecoder().decode(fileString);
			File file = new File(directroy + File.separator + fileName);
			FileOutputStream fop;
			fop = new FileOutputStream(file);
			fop.write(bytes);
			fop.flush();
			fop.close();
			message = "File created successfully.";
		} catch (Exception e) {
			logger.error("\n Caught exception in convertFile() of TaskSchedular class ", e);
		}
		logger.info("\n End convertFile() of TaskSchedular class ");
		return message;
	}

	
	
	private void preparePatientCsv(List<PatientList> patientLists) {
		ICsvBeanWriter beanWriter = null;
	     
	    try
	    {
	      beanWriter = new CsvBeanWriter(new FileWriter( System.getProperty("user.dir")+"\\eCW_DownloadedFiles\\PatientDetails.csv"), CsvPreference.STANDARD_PREFERENCE);
	      final String[] header = new String[] { "id", "patientId", "patientFirstName", "patientMiddleName",
					"patientLastName", "patientGender", "patientDOB", "patientMRN", "datasourceId" };
	 
	 
	      // write the header
	      beanWriter.writeHeader(header);
	 
	      // write the beans data
	      for (PatientList patientList : patientLists) {
	        beanWriter.write(patientList, header);
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }  finally {
	      try {
	        beanWriter.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	  
	}
	
	
	private void prepareDocumentRefCsv(List<DocumentReference> documentReferences) {
		ICsvBeanWriter beanWriter = null;
	     
	    try
	    {
	      beanWriter = new CsvBeanWriter(new FileWriter( System.getProperty("user.dir")+"\\eCW_DownloadedFiles\\DocumentReference.csv"), CsvPreference.STANDARD_PREFERENCE);
	      final String[] header = new String[] {"PatientId", "FileName", "DocumentType" };
	 
	 
	      // write the header
	      beanWriter.writeHeader(header);
	 
	      // write the beans data
	      for (DocumentReference documentReference : documentReferences) {
	        beanWriter.write(documentReference, header);
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }  finally {
	      try {
	        beanWriter.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	}

}
