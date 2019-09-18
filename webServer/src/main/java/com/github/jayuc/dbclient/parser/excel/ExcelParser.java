package com.github.jayuc.dbclient.parser.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.github.jayuc.dbclient.parser.RowData;
import com.github.jayuc.dbclient.parser.SourceData;
import com.github.jayuc.dbclient.parser.SourceParser;
import com.github.jayuc.dbclient.parser.TypeHandler;
import com.github.jayuc.dbclient.utils.StringUtil;

public class ExcelParser implements SourceParser {

	@Override
	public List<RowData> parse(InputStream inputStream, int ignore) throws Exception {
		SourceData data = parseAndCheck(inputStream, null, ignore);
		return data.getAllList();
	}

	@Override
	public List<RowData> parse(String sourcePath, int ignore) throws Exception {
		InputStream fs = inputStream(sourcePath);
		return parse(fs, ignore);
	}

	@Override
	public List<String[]> parse2(InputStream inputStream, int ignore) throws Exception {
		SourceData data = parseAndCheck(inputStream, null, ignore);
		return data.getAllStringList();
	}

	@Override
	public List<String[]> parse2(String sourcePath, int ignore) throws Exception {
		InputStream fs = inputStream(sourcePath);
		return parse2(fs, ignore);
	}

	@Override
	public SourceData parseAndCheck(InputStream inputStream, Map<Integer, TypeHandler> typeHandlers, int ignore) throws Exception {
		Workbook workbook = WorkbookFactory.create(inputStream);
		SourceData result = new SourceData();
		foreachRow(workbook, result, typeHandlers, ignore);
		return result;
	}

	@Override
	public SourceData parseAndCheck(String sourcePath, Map<Integer, TypeHandler> typeHandlers, int ignore) throws Exception {
		InputStream fs = inputStream(sourcePath);
		return parseAndCheck(fs, typeHandlers, ignore);
	}
	
	private InputStream inputStream(String path) throws FileNotFoundException {
		return new FileInputStream(path);
	}
	
	private void foreachRow(Workbook workbook, SourceData result, Map<Integer, TypeHandler> typeHandlers, int ignore) {
		if(workbook != null) {
			for(int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum ++) {
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if(sheet == null){
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum  = sheet.getFirstRowNum()+1;
				//获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				if(ignore > 0) {
					firstRowNum = ignore;
				}
				if(ignore > lastRowNum) {
					return;
				}
				//循环除了第一行的所有行
				for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){
					//获得当前行
					Row row = sheet.getRow(rowNum);
					if(row == null){
						continue;
					}
					//获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					//获得当前行的列数
					int lastCellNum = row.getPhysicalNumberOfCells();
					Object[] cells = new Object[lastCellNum];
					List<Object> normalCells = new ArrayList<Object>();
					String[] stringCells = new String[lastCellNum];
					int normal = 1;  // 1表示数据正常， 2 表示数据异常
					int hasValue = 0;  // 
					StringBuilder errorSB = new StringBuilder();
					//循环当前行
					for(int cellNum = firstCellNum; cellNum<lastCellNum;cellNum++){
						TypeHandler handler = null;
						if(typeHandlers != null) {
							handler = typeHandlers.get(cellNum+1);
						}
						Cell cell = row.getCell(cellNum);
						HandlerResult cellResult = getCellValue(cell, handler);
						cells[cellNum] = cellResult.value;
						stringCells[cellNum] = cellResult.originValue;
						if(cellResult.status == 2) {
							normal = 2;
							errorSB.append(cellNum + ": " + cellResult.errorInfo + ";");
						}
						if(handler != null) {
							normalCells.add(cellResult.value);
						}
						if(StringUtil.isBlank(cellResult.originValue)) {
							hasValue++;
						}
					}
					if(hasValue < (lastCellNum - firstCellNum)) {
						result.putAll(new RowData(rowNum+1, cells), stringCells);
						if(normal == 1) {
							result.putNormal(new RowData(rowNum+1, normalCells.toArray()), stringCells);
						}else if(normal == 2) {
							result.putAbnormal(cells, errorSB.toString(), stringCells, rowNum+1);
						}
					}
				}
			}
		}
	}
	
	private HandlerResult getCellValue(Cell cell, TypeHandler handler) {
		Object cellValue = "";
		if(cell == null){
			return new HandlerResult(1, cellValue);
		}
		String errorInfo = null;
		//判断数据的类型
		switch (cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC: //数字
				if(org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)){
					Date date = cell.getDateCellValue();
					if(date != null){
						cellValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
					}
				}else {
					cellValue = cell.getNumericCellValue();
				}
				break;
			case Cell.CELL_TYPE_STRING: //字符串
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN: //Boolean
				cellValue = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA: //公式
				cellValue = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_BLANK: //空值 
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR: //故障
				cellValue = "非法字符";
				errorInfo = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				errorInfo = "未知类型";
				break;
		}
		HandlerResult result = new HandlerResult(1, cellValue);
		result.originValue = String.valueOf(cellValue);
		if(StringUtil.isBlank(errorInfo)) {
			if(handler != null) {
				try {
					result.value = handler.handle(cellValue);
				} catch (Exception e) {
					result.value = cellValue;
					result.status = 2;
					result.errorInfo = "类型转换出错";
				}
			}
		}else {
			result.value = "";
			result.status = 2;
			result.errorInfo = errorInfo;
		}
		return result;
	}
	
	private class HandlerResult{
		int status;
		Object value;
		String originValue;
		String errorInfo;
		public HandlerResult(int status, Object value) {
			super();
			this.status = status;
			this.value = value;
		}
	}

}
