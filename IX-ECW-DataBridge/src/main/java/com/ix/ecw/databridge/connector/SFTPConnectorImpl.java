package com.ix.ecw.databridge.connector;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ix.ecw.databridge.client.AgentManagementClient;
import com.ix.ecw.databridge.model.DocumentReference;
import com.ix.ecw.databridge.model.ExtractionDetails;
import com.ix.ecw.databridge.model.PatientList;
import com.ix.ecw.databridge.model.SftpStatus;
import com.ix.ecw.databridge.utils.ClientConstant;
import com.ix.ecw.databridge.utils.ExcelUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * The Class SFTPConnectorImpl.
 */
@Repository("SFTPConnector")
public class SFTPConnectorImpl implements SFTPConnector {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AgentManagementClient agentManagementClient;

	/**
	 * Upload Files
	 * 
	 * @param sftpMap
	 * @param sftpWorkingDir
	 * @param filesDir
	 * @param extractionDetails
	 * @param lastExtractionDateTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String uploadFiles(Map sftpMap, String sftpWorkingDir, String filesDir,
			ExtractionDetails extractionDetails, Date lastExtractionDateTime) {
		logger.info("\n sftpWorkingDir:" + sftpWorkingDir + "\n filesDir:" + filesDir + "\n extractionDetails:"
				+ extractionDetails.getFilesLocation() + "\n Started uploading files to SFTP  of SFTPConnectorImpl");
		java.util.Properties config = new java.util.Properties();
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		logger.info("\n preparing the host information for sftp.");
		String status = "";
		try {
			JSch jsch = new JSch();
			logger.info("\n sftpKey:" + sftpMap.get("keyPath").toString());
			if (StringUtils.isNotBlank(sftpMap.get("keyPath").toString())) {
				jsch.addIdentity(sftpMap.get("keyPath").toString());
			}
			logger.info(
					"\n sftpHOST:" + sftpMap.get("host").toString() + "\n sftpPort:" + sftpMap.get("port").toString());

			session = jsch.getSession(sftpMap.get("userName").toString(), sftpMap.get("host").toString(),
					Integer.parseInt(sftpMap.get("port").toString()));
			if (session != null) {
				if (StringUtils.isNotBlank(sftpMap.get("password").toString()))
					session.setPassword(sftpMap.get("password").toString());
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
				logger.info("\n Host connected.");
				channel = session.openChannel("sftp");
				channel.connect();
				logger.info("\n sftp channel opened and connected.");
				channelSftp = (ChannelSftp) channel;
				channelSftp.cd(sftpWorkingDir);
				File localDir = new File(filesDir);
				String successfulfiles = "";
				String failedfiles = "";
				String messageString = "";
				if (localDir.exists() && localDir.isDirectory()) {
					Collection<File> fileList = FileUtils.listFiles(localDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
					boolean isNewFileFound = false;
					if (!fileList.isEmpty()) {
						// Date lastExtractionDateTime =
						// CommonUtil.convertStringToDate(lastUpdatedTime);
						for (File file : fileList) {
							if (lastExtractionDateTime != null) {
								if ((file.lastModified() > lastExtractionDateTime.getTime())) {
									isNewFileFound = true;
									if (file.exists()) {
										logger.info("\n *************************uploading - " + file.getName()
												+ " \n ********************** file.getAbsolutePath() ::"
												+ file.getAbsolutePath());
										channelSftp.put(file.getAbsolutePath(), file.getName());
										successfulfiles += file.getName() + ", ";
										status = ClientConstant.SUCCESS_CODE_TWO_HUNDRED;

										Files.delete(file.toPath());
									} else {
										failedfiles += file.getName() + ", ";
										status = ClientConstant.INTERNAL_SERVER_ERROR;
									}
								}
							} else {
								if (file.exists()) {
									logger.info("\n uploading - " + file.getName() + " \n file.getAbsolutePath() ::"
											+ file.getAbsolutePath());
									channelSftp.put(file.getAbsolutePath(), file.getName());
									successfulfiles += file.getName() + ", ";
									status = ClientConstant.SUCCESS_CODE_TWO_HUNDRED;

									
									Files.delete(file.toPath());

								} else {
									failedfiles += file.getName() + ", ";
									status = ClientConstant.INTERNAL_SERVER_ERROR;
								}
							}
						}

						if (!isNewFileFound && lastExtractionDateTime != null) {
							status = ClientConstant.SUCCESS_CODE_TWO_HUNDRED;
							messageString = "- NO FILES WERE FOUND FOR LAST EXTRACTION TIME  : "
									+ lastExtractionDateTime;
						}
					} else {
						messageString = " - Files doesn't exists for transformation";
						status = ClientConstant.SUCCESS_CODE_TWO_HUNDRED;
					}
					if (!(successfulfiles.trim()).equals("")) {
						messageString = "- file transfer Successful for Files : " + successfulfiles;
					} else if (!failedfiles.trim().equals("")) {
						messageString = "- file transfer failed for Files : " + failedfiles;
					} else if (!(failedfiles.trim()).equals("") && !(successfulfiles.trim()).equals("")) {
						messageString = "- NO FILES WERE TRANSFERRED : ";
					}
					logger.info("\n File(s) transfered successfully to host.");
				} else {
					status = ClientConstant.INTERNAL_SERVER_ERROR;
					messageString = "- Invalid Directory";
				}

				if (ClientConstant.SUCCESS_CODE_TWO_HUNDRED.equals(status)) {
					agentManagementClient.createAudit(extractionDetails, "COMPLETED", messageString);
				} else {
					agentManagementClient.createAudit(extractionDetails, "FAILED", messageString);
				}
			} else {
				status = ClientConstant.INTERNAL_SERVER_ERROR;
				agentManagementClient.createAudit(extractionDetails, "FAILED", "- Invalid Session");
			}
		} catch (Exception ex) {
			status = ClientConstant.INTERNAL_SERVER_ERROR;
			logger.error("\n Exception in uploading files to SFTP of SFTPConnectorImpl ::  ", ex);
		} finally {
			if (channelSftp != null) {
				channelSftp.exit();
				logger.info("\n sftp Channel exited.");
			}
			if (channel != null) {
				channel.disconnect();
				logger.info("\n Channel disconnected.");
			}
			if (session != null) {
				session.disconnect();
				logger.info("\n Host Session disconnected.");
			}
		}
		logger.info("\n Completed uploading files to SFTP of SFTPConnectorImpl");
		logger.info("\n ====================== STATUS IS ============================ " + status);
		
		return status;
	}

}
