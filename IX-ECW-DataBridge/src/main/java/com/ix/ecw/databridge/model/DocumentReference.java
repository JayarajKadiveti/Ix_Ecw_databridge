package com.ix.ecw.databridge.model;

public class DocumentReference {

	
	String PatientId;
	
	String FileName;
	
	String DocumentType = "csv";

	public String getPatientId() {
		return PatientId;
	}

	public void setPatientId(String patientId) {
		PatientId = patientId;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getDocumentType() {
		return DocumentType;
	}

	public void setDocumentType(String documentType) {
		DocumentType = documentType;
	}

	
	
	
	
}
