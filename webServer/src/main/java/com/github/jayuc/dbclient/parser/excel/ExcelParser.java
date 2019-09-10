package com.github.jayuc.dbclient.parser.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.github.jayuc.dbclient.parser.SourceData;
import com.github.jayuc.dbclient.parser.SourceParser;
import com.github.jayuc.dbclient.parser.TypeHandler;
import com.github.jayuc.dbclient.utils.StringUtil;

public class ExcelParser implements SourceParser {

	@Override
	public List<Object[]> parse(InputStream inputStream) throws Exception {
		SourceData data = parseAndCheck(inputStream, null);
		return data.getAllList();
	}

	@Override
	public List<Object[]> parse(String sourcePath) throws Exception {
		InputStream fs = inputStream(sourcePath);
		return parse(fs);
	}

	@Override
	public SourceData parseAndCheck(InputStream inputStream, List<TypeHandler> typeHandlers) throws Exception {
		Workbook workbook = WorkbookFactory.create(inputStream);
		SourceData result = new SourceData();
		foreachRow(workbook, result, typeHandlers);
		return result;
	}

	@Override
	public SourceData parseAndCheck(String sourcePath, List<TypeHandler> typeHandlers) throws Exception {
		InputStream fs = inputStream(sourcePath);
		return parseAndCheck(fs, typeHandlers);
	}
	
	private InputStream inputStream(String path) throws FileNotFoundException {
		return new FileInputStream(path);
	}
	
	private void foreachRow(Workbook workbook, SourceData result, List<TypeHandler> typeHandlers) {
		if(workbook != null) {
			for(int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum ++) {
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if(sheet == null){
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum  = sheet.getFirstRowNum();
				//获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				//循环除了第一行的所有行
				for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
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
					int normal = 1;  // 1表示数据正常， 2 表示数据异常
					StringBuilder errorSB = new StringBuilder();
					//循环当前行
					for(int cellNum = firstCellNum; cellNum<lastCellNum;cellNum++){
						TypeHandler handler = null;
						if(typeHandlers != null) {
							handler = typeHandlers.get(rowNum);
						}
						Cell cell = row.getCell(cellNum);
						HandlerResult cellResult = getCellValue(cell, handler);
						cells[cellNum] = cellResult.value;
						if(cellResult.status == 2) {
							normal = 2;
							errorSB.append(cellNum + ": " + cellResult.errorInfo + ";");
						}
					}
					result.putAll(cells);
					if(normal == 1) {
						result.putNormal(cells);
					}else if(normal == 2) {
						result.putAbnormal(cells, errorSB.toString());
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
				cellValue = cell.getNumericCellValue();
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
		if(StringUtil.isBlank(errorInfo)) {
			if(handler != null) {
				try {
					result.value = handler.handle(cellValue);
				} catch (Exception e) {
					result.status = 2;
					result.errorInfo = "类型转换出错";
				}
			}
		}else {
			result.status = 2;
			result.errorInfo = errorInfo;
		}
		return result;
	}
	
	private class HandlerResult{
		int status;
		Object value;
		String errorInfo;
		public HandlerResult(int status, Object value) {
			super();
			this.status = status;
			this.value = value;
		}
	}

}
