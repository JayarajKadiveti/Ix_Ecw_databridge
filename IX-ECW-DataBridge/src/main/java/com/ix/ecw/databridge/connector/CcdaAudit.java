package com.ix.ecw.databridge.connector;

import com.google.gson.JsonObject;

/**
 * The Interface CcdaAudit.
 */
public interface CcdaAudit {

	/**
	 * Save audit.
	 *
	 * @param eventText the event text
	 * @param data      the data
	 */
	public void saveAudit(JsonObject auditJson);
}
