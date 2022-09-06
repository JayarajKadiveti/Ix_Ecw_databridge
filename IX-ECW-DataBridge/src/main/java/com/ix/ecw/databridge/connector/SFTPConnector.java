package com.ix.ecw.databridge.connector;

import java.util.Date;
import java.util.Map;

import com.ix.ecw.databridge.model.ExtractionDetails;
import com.ix.ecw.databridge.model.SftpStatus;


/**
 * The Interface SFTPConnector.
 */
public interface SFTPConnector {
	
	/**
	 * Upload Files
	 * @param sftpMap
	 * @param sftpWorkingDir
	 * @param filesDir
	 * @param extractionDetails
	 * @param lastUpdatedTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	String uploadFiles(Map sftpMap, String sftpWorkingDir,
			String filesDir,ExtractionDetails extractionDetails, Date lastUpdatedTime);
}
