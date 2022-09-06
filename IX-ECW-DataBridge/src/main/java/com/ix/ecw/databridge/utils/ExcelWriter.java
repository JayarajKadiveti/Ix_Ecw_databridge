package com.ix.ecw.databridge.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelWriter {

	static int cellnum;
	static Map<Integer, Row> rowMap;
	static List<String> metaHeader;
	static Map<String, XSSFCellStyle> styleMap;

	
	/*
	 * public static void main(String[] args) {
	 * 
	 * 
	 * String headerJson = JSONUtil.
	 * readJsonFromFile("D://Project-Space/BBMP health/Docs/dailyReportHeaders.json"
	 * ); List<Object> headerList = JSONUtil.jsonToList(headerJson);
	 * 
	 * 
	 * String dataJson = JSONUtil.
	 * readJsonFromFile("D://Project-Space/BBMP health/Docs/dailyReportData.json");
	 * List<Object> datalist = JSONUtil.jsonToList(dataJson);
	 * 
	 * writeToExcel(headerList, datalist, "Patient Data");
	 * 
	 * }
	 */

	public static XSSFWorkbook writeToExcel(List<Object> headerList, List<Object> datalist, String sheetName, XSSFWorkbook workbook1, String title, int rowInit1, int colInit1) {

		int rowInit = rowInit1, rowLast, colInit = colInit1, colLast;
		
		rowMap = new HashMap<>();
		metaHeader = new ArrayList<>();
		styleMap = new HashMap(5);
		
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = null;

		if(workbook1 == null) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = workbook1;
		}

		XSSFSheet sheet = workbook.getSheet(sheetName)!=null?workbook.getSheet(sheetName):workbook.createSheet(sheetName);
//		sheet.setColumnWidth(0, 18000);
		
		if(!title.equalsIgnoreCase("blank"))
		writeTitle(title, sheet);

		rowLast = writeHeader(rowInit, colInit, sheet, headerList);
		colLast = cellnum - 1;
		sheet.autoSizeColumn(0, true);
//		System.out.println(rowLast + " " + colLast);

		writeData(rowLast + 1, colInit, sheet, headerList, datalist);

		setRegionBorderWithMedium(new CellRangeAddress(rowInit, rowLast, colInit, colLast), sheet);

		mergeAndCenterHeaders(rowInit, rowLast, colInit, colLast, sheet);

		// Write the workbook in file system
//		try {
//			Random rand = new Random();
//			int rand_int1 = rand.nextInt(100000);
//			String filename = "dailyReport" + rand_int1 + ".xlsx";
//			FileOutputStream out = new FileOutputStream(
//					new File("D://Project-Space//BBMP health//filestorage//" + filename));
//			workbook.write(out);
//			out.close();
//			System.out.println(filename + " written successfully on disk.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return workbook;
	}

	private static void writeTitle(String string, XSSFSheet sheet) {
		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);
		cell.setCellValue(string);
		cell.setCellStyle(getSytle(cell, "title"));
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
		setRegionBorderWithMedium(new CellRangeAddress(0, 0, 0, 9), sheet);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int writeHeader(int rowInit, int colInit, XSSFSheet sheet, List<Object> headerList) {
		
//		System.out.println("header size: "+headerList.size());
		int rownum = rowInit;
		Row row;
		int lastRowNum = rowInit;

		if (rowMap.containsKey(rownum)) {
			row = rowMap.get(rownum);
		} else {
			row = sheet.createRow(rownum);
			rowMap.put(rownum, row);
			rownum++;
		}
		
//		System.out.println("Row Map: "+rowMap);

		cellnum = colInit;
		for (Object header : headerList) {
			Cell cell = row.createCell(cellnum++);
			XSSFCellStyle style = getSytle(cell, "header");
			cell.setCellStyle(style);
			if (header instanceof String) {
				if(((String) header).length() > 20) {
					sheet.setColumnWidth(cell.getColumnIndex(), 6000);
					style.setWrapText(true);
				}
				cell.setCellValue((String) header);
			}

			else if (header instanceof Integer) {
				if(((String) header).length() > 20) {
					sheet.setColumnWidth(cell.getColumnIndex(), 6000);
					style.setWrapText(true);
				}
				cell.setCellValue((Integer) header);
			}

			else if (header instanceof Map) {
				Map<String, Object> multiHeader = (Map<String, Object>) header;
				for (Map.Entry entry : multiHeader.entrySet()) {
					if(((String) entry.getKey()).length() > 20) {
						sheet.setColumnWidth(cell.getColumnIndex(), 6000);
						style.setWrapText(true);
					}
					cell.setCellValue((String) entry.getKey());
					metaHeader.add((String) entry.getKey());
					List subHeaderList = (List) entry.getValue();
					rownum = writeHeader(rowInit + 1, --cellnum, sheet, subHeaderList);
					lastRowNum = rownum > lastRowNum ? rownum : lastRowNum;
				}
			}
		}
//		System.out.println("Last row no: "+lastRowNum);
		return lastRowNum;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void writeData(int r, int c, XSSFSheet sheet, List headerList, List datalist) {
		int rownum = r;
		Row row;
		for (Object data : datalist) {
			Map<String, Object> dataRow = (Map<String, Object>) data;
			cellnum = c;
			row = sheet.createRow(rownum++);
//			fillRow(c, headerList, dataRow, row, dataBorder);
			for (Object header : headerList) {

				fillCell(header, dataRow, row);

			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void fillCell(Object header, Map dataRow, Row row) {

		if (header instanceof Map) {

			Map<String, Object> multiHeader = (Map<String, Object>) header;
			String headerLabel = "";
			Object subHeader = null;
			for (Map.Entry entry : multiHeader.entrySet()) {
				headerLabel = (String) entry.getKey();
				subHeader = (Object) entry.getValue();
			}

			if (dataRow.get(headerLabel) != null) {

				Object subDataRow = dataRow.get(headerLabel);
				if (subDataRow instanceof Map) {
					Map<String, Object> subDataMap = (Map<String, Object>) subDataRow;
					fillCell(subHeader, subDataMap, row);
				} else if (subDataRow instanceof List) {
					List<Map> subDataRowList = (List<Map>) subDataRow;
					for (Map subDatMap : subDataRowList)
						fillCell(subHeader, subDatMap, row);
				}

			}

		} else if (header instanceof List) {
			List<Object> subHeader = (List<Object>) header;
			for (Object obj : subHeader)
				fillCell(obj, dataRow, row);
		}

		else {
			Cell cell = row.createCell(cellnum++);
			Object obj;
			if (dataRow.get(header) != null) {
				obj = dataRow.get(header);
			} else {
				obj = null;
			}

			if (obj instanceof String)
				cell.setCellValue((String) obj);
			else if (obj instanceof Integer)
				cell.setCellValue((Integer) obj);
			else if (obj instanceof Long )
				cell.setCellValue((Long) obj);
			else if (obj instanceof Short )
				cell.setCellValue((Short) obj);
			else if (obj instanceof Byte)
				cell.setCellValue((Byte) obj);
			else if (obj instanceof Double)
				cell.setCellValue((Double) obj);
			else if (obj instanceof Float)
				cell.setCellValue((Float) obj);
			else if (obj instanceof Date) {
				Date date = (Date)obj;
				cell.setCellValue(date.toString());
			}

			cell.setCellStyle(getSytle(cell, "data"));
		}

	}

	private static void mergeAndCenterHeaders(int rowInit, int rowLast, int colInit, int colLast, Sheet sheet) {

		for (int j = colInit; j <= colLast; j++) {

			String header = sheet.getRow(rowInit).getCell(j).toString();
//			System.out.println("Cell value at cell "+j+": "+sheet.getRow(rowInit).getCell(j));
			if (metaHeader.contains(header)) {
				int r = rowInit;
				int c = j;
				int parentLength = colLast;
				while (metaHeader.contains(header)) {
					for (int k = j; k < parentLength; k++) {
						String cell = sheet.getRow(r).getCell(k+1) == null ? "" : sheet.getRow(r).getCell(k+1).toString();
//						System.out.println("----------" + cell + "---------" + sheet.getRow(r).getCell(k));
						if (!cell.equals("")) {
//							c--;
							break;
						}
						c = k + 1;
					}
					
						sheet.addMergedRegion(new CellRangeAddress(r, r, j, c));
						setRegionBorderWithMedium(new CellRangeAddress(r, r, j, c), sheet);

					sheet.getRow(r).getCell(j).setCellStyle(getSytle(sheet.getRow(r).getCell(j), "header"));

					if (r != rowInit && c < parentLength) {
						header = sheet.getRow(r).getCell(c + 1).toString();
						j = c + 1;
					} else {
						r++;
						parentLength = c;
						header = sheet.getRow(r).getCell(j).toString();
					}
				}

			} else {
				if (!header.equals("")) {
					try {
						sheet.addMergedRegion(new CellRangeAddress(rowInit, rowLast, j, j));
						setRegionBorderWithMedium(new CellRangeAddress(rowInit, rowLast, j, j), sheet);
						sheet.getRow(rowInit).getCell(j).setCellStyle(getSytle(sheet.getRow(rowInit).getCell(j), "header"));
					} catch (IllegalArgumentException e) {
						
					}

				}

			}

		}

	}

	private static XSSFCellStyle getSytle(Cell cell, String cellType) {

		Workbook workbook = cell.getSheet().getWorkbook();
		XSSFCellStyle style = null;

		if (cellType.equals("header")) {
			
			if(styleMap.containsKey("headerStyle"))
				return styleMap.get("headerStyle");
			
			style = (XSSFCellStyle) workbook.createCellStyle();

			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setBold(true);

			style.setBorderTop(BorderStyle.MEDIUM);
			style.setBorderRight(BorderStyle.MEDIUM);
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setBorderLeft(BorderStyle.MEDIUM);
			style.setFont(font);
			
			style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			style.setWrapText(true);
			
			styleMap.put("headerStyle", style);

		} else if (cellType.equals("data")) {
			
			if(styleMap.containsKey("dataStyle"))
				return styleMap.get("dataStyle");
			
			style = (XSSFCellStyle) workbook.createCellStyle();

			style.setBorderTop(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			
			styleMap.put("dataStyle", style);

		} else if (cellType.equals("title")) {
			
			if(styleMap.containsKey("titleStyle"))
				return styleMap.get("titleStyle");
			
			style = (XSSFCellStyle) workbook.createCellStyle();

			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setBold(true);
			font.setFontHeight(15);

			style.setBorderTop(BorderStyle.MEDIUM);
			style.setBorderRight(BorderStyle.MEDIUM);
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setBorderLeft(BorderStyle.MEDIUM);
			style.setFont(font);
			
			style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			styleMap.put("titleStyle", style);

		}

		return style;
	}

	// This method sets the border on a specified region of an excel sheet
	private static void setRegionBorderWithMedium(CellRangeAddress region, Sheet sheet) {
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
	}

}

