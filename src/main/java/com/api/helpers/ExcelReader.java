package com.api.helpers;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelReader {
    private String filePath;
    private String SheetName;
    public ExcelReader(String filePath, String  SheetName) {
        this.filePath = filePath;
        this.SheetName = SheetName;
    }
    private XSSFSheet getSheet() throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet(SheetName);
        return sheet;
    }
    public Map<String, Map<String, String>> getExcelAsMap() throws IOException {
        XSSFSheet sheet = getSheet();
        Map<String, Map<String, String>> completeSheetData = new HashMap<String, Map<String, String>>();
        List<String> columnHeader = new ArrayList<String>();
        Row row = sheet.getRow(0);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            columnHeader.add(cellIterator.next().getStringCellValue());
        }
        int rowCount = row.getPhysicalNumberOfCells();
        int columnCount = row.getLastCellNum();
        for (int i = 1; i <= rowCount; i++) {
            Map<String, String> singleRowData = new HashMap<String, String>();
            Row row1 = sheet.getRow(i);
             if(row1!=null) {
            for (int j = 0; j < columnCount; j++) {
                Cell cell = row1.getCell(j);
                if(cell!=null)
                singleRowData.put(columnHeader.get(j), getCellValueAsString(cell));
            }
            completeSheetData.put(singleRowData.get("BookId"), singleRowData);
        }
        }
        return completeSheetData;
    }
    public String getCellValueAsString(Cell cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue= cell.getCellFormula();
            case BLANK:
                cellValue="BLANK";
            default:
                cellValue ="DEFAULT";
        }
        return cellValue;
    }

    public String getSheetName(int index) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        String sheet = workbook.getSheetName(index);
        return sheet;
    }
    public int getSheetCount() throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        int sheetCount = workbook.getNumberOfSheets();
        return sheetCount;
    }
    public int totolColumnCount() throws IOException {
        XSSFSheet sheet = getSheet();
        Row row = sheet.getRow(0);
        int lastColumnNum = row.getLastCellNum();
        return lastColumnNum;
    }
    
    public static Map<String,String> excelMap(String bookId) throws IOException {
    	String root=System.getProperty("user.dir");
    	String excelsheetPath=root+"/InputExcelSheets/BookDetails.xlsx";
        ExcelReader ex = new ExcelReader(excelsheetPath,PropertyFiles.getProperty("env"));
		 Map<String, Map<String, String>> excelData =ex.getExcelAsMap();
	       Map<String,String> bookmap = excelData.get(bookId);
	        return bookmap;	 
	 }
    
    }
    
 