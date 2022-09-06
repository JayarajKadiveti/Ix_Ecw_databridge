package com.ix.ecw.databridge.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.util.StringUtils;

/**
 * Acts as a wrapper to the apache-poi Excel implementation. Helps in
 * customizing excel sheets with headers and choosing the initial row and column
 * numbers.
 *
 */
public class ExcelUtil {

	/**
	 * Creates an Excel Workbook with multiple sheets
	 * 
	 * @param sheets - List of sheets
	 * 
	 * @return Workbook - Excel workbook
	 */
	@SuppressWarnings("unchecked")
	public static Workbook createWorkbook(List<Map> sheets) {

		Workbook excelWorkbook = new XSSFWorkbook();

		for (Map sheetData : sheets) {

			String sheetName = sheetData.get("sheetName") != null ? sheetData.get("sheetName").toString() : null;
			List<String> headers = (List<String>) sheetData.get("headers");
			List<Map<String, Object>> data = (List<Map<String, Object>>) sheetData.get("data");
			int rowInit = sheetData.containsKey("rowInit") ? (Integer) sheetData.get("rowInit") : 0;
			int columnInit = sheetData.containsKey("columnInit") ? (Integer) sheetData.get("columnInit") : 0;

			Sheet excelSheet = StringUtils.isEmpty(sheetName) ? excelWorkbook.createSheet()
					: excelWorkbook.createSheet(sheetName);
			
			excelSheet.autoSizeColumn(100000);

			Row row = excelSheet.createRow(rowInit++);

			CellStyle headCellStyle = getHeaderCellStyle(excelWorkbook);

			if (headers != null && headers.size() > 0) {
				for (int i = 0; i < headers.size(); i++) {

					Cell cell = row.createCell(i + columnInit);

					cell.setCellStyle(headCellStyle);

					cell.setCellValue(headers.get(i));
					
					excelSheet.autoSizeColumn(i);
					

				}

			} else {
				throw new IllegalArgumentException("No headers present for sheet: " + excelSheet.getSheetName());
			}

			CellStyle rowCellStyle = getRowCellStyle(excelWorkbook);
		
			

			if (data != null) {

				for (Map rowData : data) {
					Boolean empty = false;
					row = excelSheet.createRow(rowInit++);
					CellStyle emptyRow = null;
					

					for (int i = 0; i < headers.size(); i++) {

						Cell cell = row.createCell(i + columnInit);
						Object value = rowData.get(headers.get(i));
						

						if (emptyRow != null) {
							cell.setCellStyle(emptyRow);
						} else {
								
							
							cell.setCellStyle(rowCellStyle);

						}
						
						
						if (value instanceof String)

							cell.setCellValue((String) value);
						else if (value instanceof Integer)
							cell.setCellValue((Integer) value);
						else if (value instanceof Long)
							cell.setCellValue((Long) value);
						else if (value instanceof Short)
							cell.setCellValue((Short) value);
						else if (value instanceof Byte)
							cell.setCellValue((Byte) value);
						else if (value instanceof Double)
							cell.setCellValue((Double) value);
						else if (value instanceof Float)
							cell.setCellValue((Float) value);
						else if (value instanceof Date) {
							Date date = (Date) value;
							cell.setCellValue(date.toString());
						}
						
						excelSheet.autoSizeColumn(i);
						
						

					}
				}
				
				//set last row bottom border
				
				int lastRowNum = excelSheet.getLastRowNum();
				Row lastrow = excelSheet.getRow(lastRowNum);
				
				if(row.equals(lastrow)) {
					CellStyle rowCellStyle1 = getRowCellStyle(excelWorkbook);
					rowCellStyle1.setBorderBottom(BorderStyle.THIN);
					for(int i = 0; i < headers.size(); i++) {
						lastrow.getCell(i).setCellStyle(rowCellStyle1);

					}
				}
			
			}
		
			
		}
		return excelWorkbook;
	}

	/**
	 * Creates an Excel Workbook with a single sheet
	 * 
	 * @param sheet - Map representation of Excel sheet to be created
	 * 
	 * @return Workbook Excel workbook
	 */
	public static Workbook createSingleSheetWorkbook(Map sheet) {
		return createWorkbook(Arrays.asList(sheet));
	}

	/**
	 * Creates a Map representation Excel sheet
	 * 
	 * @param headers - Column headers for the Excel Sheet
	 * @param data    - data for columns
	 * @return Map representation of Excel sheet
	 */
	public static Map createSheet(List headers, List<Map<String, Object>> data) {
		Map sheet = new HashMap<String, Object>();

		sheet.put("headers", headers);
		sheet.put("data", data);

		return sheet;
	}

	/**
	 * Creates a Map representation Excel sheet
	 * 
	 * @param sheetName - name of the Excel sheet
	 * @param headers   - Column headers for the Excel Sheet
	 * @param data      - data for columns
	 * @return Map representation of Excel sheet
	 */
	public static Map createSheet(String sheetName, List headers, List<Map<String, Object>> data) {
		Map sheet = new HashMap<String, Object>();

		sheet.put("sheetName", sheetName);
		sheet.put("headers", headers);
		sheet.put("data", data);

		return sheet;
	}

	/**
	 * Creates a Map representation Excel sheet
	 * 
	 * @param headers    - Column headers for the Excel Sheet
	 * @param data       - data for columns
	 * @param rowInit    - initial row number starts with 0
	 * @param columnInit - initial column number starts with 0
	 * @return
	 */
	public static Map createSheet(List headers, List<Map<String, Object>> data, int rowInit, int columnInit) {
		Map sheet = new HashMap<String, Object>();

		sheet.put("headers", headers);
		sheet.put("data", data);
		sheet.put("rowInit", rowInit);
		sheet.put("columnInit", columnInit);

		return sheet;
	}

	/**
	 * Creates a Map representation Excel sheet
	 * 
	 * @param sheetName  - name of the Excel sheet
	 * @param headers    - Column headers for the Excel Sheet
	 * @param data       - data for columns
	 * @param rowInit    - initial row number starts with 0
	 * @param columnInit - initial column number starts with 0
	 * @return
	 */
	public static Map createSheet(String sheetName, List headers, List<Map<String, Object>> data, int rowInit,
			int columnInit) {
		Map sheet = new HashMap<String, Object>();

		sheet.put("sheetName", sheetName);
		sheet.put("headers", headers);
		sheet.put("data", data);
		sheet.put("rowInit", rowInit);
		sheet.put("columnInit", columnInit);

		return sheet;
	}

	public static CellStyle getHeaderCellStyle(Workbook excelWorkbook)

	{
		CellStyle style = excelWorkbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		style.setFillForegroundColor(IndexedColors.AQUA.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		

		Font font = excelWorkbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		
		font.setBold(true);

		short fontsize = 12;
		font.setFontHeightInPoints(fontsize);
		style.setFont(font);
		return style;

	}

	public static CellStyle getRowCellStyle(Workbook excelWorkbook)

	{
		CellStyle style = excelWorkbook.createCellStyle();
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
//		style.setBorderTop(BorderStyle.THIN);
//		style.setBorderBottom(BorderStyle.THIN);
		

		style.setWrapText(true);


		Font font = excelWorkbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());

		style.setFont(font);
		return style;

	}

	public static Cell changeCellBackgroundColor(Cell cell) {
		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle == null) {
			cellStyle = cell.getSheet().getWorkbook().createCellStyle();
		}
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cell.setCellStyle(cellStyle);
		return cell;
	}

	/**
	 * Save the Excel Workbook in to specified location
	 * 
	 * @param workbook - workbook to save
	 * @param location - location to save the workbook along with filename and
	 *                 extension
	 * 
	 * @throws IOException
	 */
	public static void saveWorkbook(Workbook workbook, String location) throws IOException {
		FileOutputStream out = new FileOutputStream(new File(StringUtils.hasLength(location) ? location : "document"));
		workbook.write(out);
		out.close();
	}

	/**
	 * Convert the Excel Workbook into ByteArray/Blob
	 * 
	 * @param workbook - workbook to be converted
	 * @return blob - Binary Large OBject
	 */
	public static byte[] toBlob(Workbook workbook) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			workbook.write(baos);
		} catch (IOException e) {
			System.out.println("unable to write file to output streem");
		}
		byte[] blob = baos.toByteArray();
		return blob;
	}
}