package com.ix.ecw.databridge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientList {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("patientId")
	@Expose
	private String patientId;
	@SerializedName("patientFirstName")
	@Expose
	private String patientFirstName;
	@SerializedName("patientLastName")
	@Expose
	private String patientLastName;
	@SerializedName("patientMiddleName")
	@Expose
	private String patientMiddleName;
	@SerializedName("patientGender")
	@Expose
	private String patientGender;
	@SerializedName("patientDOB")
	@Expose
	private String patientDOB;
	@SerializedName("patientMRN")
	@Expose
	private String patientMRN;
	@SerializedName("datasourceId")
	@Expose
	private Integer datasourceId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientFirstName() {
		return patientFirstName;
	}
	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}
	public String getPatientLastName() {
		return patientLastName;
	}
	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}
	public String getPatientMiddleName() {
		return patientMiddleName;
	}
	public void setPatientMiddleName(String patientMiddleName) {
		this.patientMiddleName = patientMiddleName;
	}
	public String getPatientGender() {
		return patientGender;
	}
	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}
	public String getPatientDOB() {
		return patientDOB;
	}
	public void setPatientDOB(String patientDOB) {
		this.patientDOB = patientDOB;
	}
	public String getPatientMRN() {
		return patientMRN;
	}
	public void setPatientMRN(String patientMRN) {
		this.patientMRN = patientMRN;
	}
	public Integer getDatasourceId() {
		return datasourceId;
	}
	public void setDatasourceId(Integer datasourceId) {
		this.datasourceId = datasourceId;
	}

	
	
}
