package com.ix.ecw.databridge.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import com.google.gson.Gson;
import com.ix.ecw.databridge.model.PatientList;
import com.sun.codemodel.JCodeModel;

public class Utils {

	public static void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName,
			String javaClassName) throws IOException {
		JCodeModel jcodeModel = new JCodeModel();

		GenerationConfig config = new DefaultGenerationConfig() {
			@Override
			public boolean isGenerateBuilders() {
				return true;
			}

			@Override
			public SourceType getSourceType() {
				return SourceType.JSON;
			}
		};

		SchemaMapper mapper = new SchemaMapper(
				new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
		mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

		jcodeModel.build(outputJavaClassDirectory);

	}

	public static List<PatientList> getPatientListFromJson() {
		List<PatientList> patientArrayList = new ArrayList<>();

		try {
			FileReader reader = new FileReader(System.getProperty("user.dir") + "\\PatientDetailsJsonFile.json");

			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject) parser.parse(reader);
			org.json.simple.JSONArray arr = (org.json.simple.JSONArray) obj.get("PatientData");
			for (int i = 0; i < arr.size(); i++) {

				PatientList patientList = new Gson().fromJson(arr.get(i).toString(), PatientList.class);

				patientArrayList.add(patientList);

			}
		} catch (IOException | ParseException e) {
			System.out.println(" patient json file not available!!!");
			System.exit(0);
		}
		
		
		return patientArrayList;
	}
}
