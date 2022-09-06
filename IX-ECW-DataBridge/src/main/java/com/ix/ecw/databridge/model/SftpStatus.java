package com.ix.ecw.databridge.model;

import java.util.List;

public class SftpStatus {
	
	
	String status;
	
	List<DocumentReference> documentReferences;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DocumentReference> getDocumentReferences() {
		return documentReferences;
	}

	public void setDocumentReferences(List<DocumentReference> documentReferences) {
		this.documentReferences = documentReferences;
	}
	
	
	

}
