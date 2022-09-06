package com.ix.ecw.databridge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.ix.ecw.databridge.client.AgentManagementClient;
import com.ix.ecw.databridge.model.AgentConfiguration;
import com.ix.ecw.databridge.model.Connector;
import com.ix.ecw.databridge.model.ECWConfig;
import com.ix.ecw.databridge.model.PatientList;

@Component
public class DataBridgeInitialization {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	AgentConfiguration agentConfiguration;

	private static final Logger logger = LoggerFactory.getLogger(DataBridgeInitialization.class);

	@PostConstruct
	public void init() {
		createDownloadFolder();

		createEcwFile();
		
		getPatientsListBySourceId();

		getJarFile();
	}
	
	
	/*
	 * public void runScheduler() { createDownloadFolder();
	 * 
	 * createEcwFile();
	 * 
	 * getPatientsListBySourceId();
	 * 
	 * getJarFile(); }
	 */
	private void getJarFile() {
		try {
			
			String jarName = "ecw.jar";
			
			Path path = Paths.get(jarName).toAbsolutePath().normalize();

			if (path == null) {
				Files.createDirectories(path);

			}

			InputStream in = new URL("https://tool.xyramsoft.com:444//ticketool_files/ecw.jar").openStream();

			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
			

			Process proc = Runtime.getRuntime().exec("java -jar " + System.getProperty("user.dir")+"\\"+jarName);
			logger.info("\n Processing jar file......... ");

			proc.waitFor();

			logger.info("\n Exceuted jar file......... ");

			// Then retreive the process output
			InputStream inn = proc.getInputStream();
			InputStream err = proc.getErrorStream();

			byte b[] = new byte[inn.available()];
			inn.read(b, 0, b.length);
			System.out.println(new String(b));

			byte c[] = new byte[err.available()];
			err.read(c, 0, c.length);
			System.out.println(new String(c));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createEcwFile() {
		ECWConfig ecwConfig = new ECWConfig();

		ecwConfig = getEcwData(ecwConfig);

		ObjectMapper Obj = new ObjectMapper();
		Obj.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			// Converting the Java object into a JSON string

			String ecwJsonStr = Obj.writeValueAsString(ecwConfig);

			// Displaying Java object into a JSON string

			Path ecwJsonPath = Paths.get("LaunchAppJsonFile.json").toAbsolutePath().normalize();

			if (ecwJsonPath == null) {
				Files.createDirectories(ecwJsonPath);

			}

			try {

				FileWriter ecWfile = new FileWriter(ecwJsonPath.toFile());
				ecWfile.write(ecwJsonStr);
				ecWfile.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public void getPatientsListBySourceId() {
		List<PatientList> patientArrayList = new ArrayList<>();

		try {

			String responseStr = restTemplate
					.getForObject("https://dev.interopx.com/ix-datasource/datasource/getPatientPanel/"
							+ agentConfiguration.getDatasourceId(), String.class);


			JSONArray jsonArray = new JSONArray(responseStr);

			for (int i = 0; i < jsonArray.length(); i++) {

				PatientList patientList = new Gson().fromJson(jsonArray.get(i).toString(), PatientList.class);

				patientArrayList.add(patientList);

			}
			
			ObjectMapper Obj = new ObjectMapper();
			Obj.enable(SerializationFeature.INDENT_OUTPUT);

			Path patientJsonPath = Paths.get("PatientDetailsJsonFile.json").toAbsolutePath().normalize();

			if (patientJsonPath != null) {
				patientJsonPath.toFile().delete();

			}
			
			
			Map map = new HashMap<>();
			map.put("PatientData", patientArrayList);

			String patientJsonStr = Obj.writeValueAsString(map);

			// Displaying Java object into a JSON string

			try {

				FileWriter file = new FileWriter(patientJsonPath.toString(), true);
				file.write(patientJsonStr + " \n");
				file.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ECWConfig getEcwData(ECWConfig ecwConfig) {
		// Insert the data
		ecwConfig.setUrl("https://ecw.online.physicianstrust.net:3057/mobiledoc/jsp/webemr/login/newLogin.jsp");
		ecwConfig.setUsername("Hema");
		ecwConfig.setPassword("Tactical123!");
		return ecwConfig;
	}

	private void createDownloadFolder() {
		File dir = new File(System.getProperty("user.dir") + "\\eCW_DownloadedFiles\\");
		dir.mkdir();

	}

}
