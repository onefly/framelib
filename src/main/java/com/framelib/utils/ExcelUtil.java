package com.framelib.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据传入的list数据创建excel文件并下载
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.utils.ExcelUtil.java
 * @ClassName : ExcelUtil
 * @Author : caozhifei
 * @CreateDate : 2014年5月6日 上午11:22:15
 */
@SuppressWarnings("deprecation")
public class ExcelUtil {
	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	private static CellStyle titleStyle; // 标题行样式
	private static Font titleFont; // 标题行字体
	private static CellStyle dateStyle; // 条件行样式
	private static Font dateFont; // 日期行字体
	private static CellStyle headStyle; // 表头行样式
	private static Font headFont; // 表头行字体
	private static CellStyle contentStyle; // 内容行样式
	private static Font contentFont; // 内容行字体
	/**
	 * 根据数据集合生成excel文件并下载
	 *  @Method_Name    : downloadExcel
	 *  @param fileName excel文件名称，工作簿和生成的内容标题都用这个名称
	 *  @param colNames 需要导出的excel列名数组，为null时导出map中所有的key
	 *  @param map 列名与集合中实体类属性的对应关系
	 *  @param searchParam 查询条件参数 
	 *  @param list 需要导出的数据集合
	 *  @param response
	 *  @throws Exception 
	 *  @return         : void
	 *  @Creation Date  : 2014年5月6日 上午11:38:03
	 *  @version        : v1.00
	 * @Author         : caozhifei 
	 *  @Update Date    : 2014年5月6日 上午13:55:03
	 *  @Update Author  : zhangyan log 为null时导出map中所有的key
	 */
	public static void downloadExcel(String fileName, String[] colNames,
			LinkedHashMap<String, String> map, String searchParam, List<?> list,
			HttpServletResponse response) throws Exception {
		
		String[] colContents = null;
		if(colNames==null){
			colNames = (String[]) map.keySet().toArray(new String[map.keySet().size()]);
			colContents = (String[]) map.values().toArray(new String[map.values().size()]);
		}else{
			colContents = new String[colNames.length];
			for (int i = 0; i < colNames.length; i++) {
				colContents[i] = map.get(colNames[i]);
			}
		}
		log.debug("excel colNames is "+java.util.Arrays.toString(colNames));
		
		log.debug("excel colContents is "+java.util.Arrays.toString(colContents));
		byte[] in = ExcelUtil.createExcel(fileName, colNames, colContents,
				searchParam, list);
		// 进行转码，使其支持中文文件名
		String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		response.setHeader("content-disposition", "attachment;filename="
				+ codedFileName + ".xls");
		BufferedOutputStream bos = new BufferedOutputStream(
				response.getOutputStream());
		bos.write(in);
		bos.flush();
		bos.close();
		log.debug("download excel success,the excel file name is "+fileName);
	}

	
	/**
	 * 根据集合数据创建excel文件流
	 *  @Method_Name    : createExcel
	 *  @param fileName 文件名称
	 *  @param colNames 列名数组
	 *  @param colContents 对应的数据属性数组
	 *  @param searchParam 查询条件
	 *  @param list 需要导出的数据集合
	 *  @return
	 *  @throws Exception 
	 *  @return         : byte[] 文件流
	 *  @Creation Date  : 2014年5月6日 上午11:44:12
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	private static byte[] createExcel(String fileName, String[] colNames,
			String[] colContents, String searchParam, List<?> list)
			throws Exception {
		HSSFWorkbook wb = null;
		wb = init(wb);
		
		LinkedHashMap<String, List<?>> map = new LinkedHashMap<String, List<?>>();
		// map.put("账户组访问网站", dataList);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<String[]> headNames = new ArrayList<String[]>();
		// 设置Excel文件的列名称
		// 设置工作薄名称
		map.put(fileName, list);
		headNames.add(colNames);
		List<String[]> fieldNames = new ArrayList<String[]>();
		// 设置列内容
		fieldNames.add(colContents);
		ExportSetInfo setInfo = new ExportSetInfo();
		setInfo.setObjsMap(map);
		setInfo.setFieldNames(fieldNames);
		// 设置Excel内容的标题
		setInfo.setTitles(new String[] { fileName });
		// 设置显示的查询条件
		setInfo.setSearch(new String[] { searchParam });
		setInfo.setHeadNames(headNames);
		setInfo.setOut(baos);
		// 将需要导出的数据输出到baos
		ExcelUtil.export2Excel(setInfo, wb);
		return baos.toByteArray();
	}

	/**
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @Description: 将Map里的集合对象数据输出Excel数据流
	 */
	private static void export2Excel(ExportSetInfo setInfo, HSSFWorkbook wb)
			throws IOException, IllegalArgumentException,
			IllegalAccessException {
		wb = init(wb);
		Set<Entry<String, List<?>>> set = setInfo.getObjsMap().entrySet();
		String[] sheetNames = new String[setInfo.getObjsMap().size()];
		int sheetNameNum = 0;
		for (Entry<String, List<?>> entry : set) {
			sheetNames[sheetNameNum] = entry.getKey();
			sheetNameNum++;
		}
		HSSFSheet[] sheets = getSheets(setInfo.getObjsMap().size(), sheetNames,
				wb);
		int sheetNum = 0;
		for (Entry<String, List<?>> entry : set) {
			// Sheet
			List<?> objs = entry.getValue();
			// 标题行
			createTableTitleRow(setInfo, sheets, sheetNum);
			// 日期行
			createTableDateRow(setInfo, sheets, sheetNum);
			// 表头
			creatTableHeadRow(setInfo, sheets, sheetNum);
			// 表体
			String[] fieldNames = setInfo.getFieldNames().get(sheetNum);
			int rowNum = 3;
			for (Object obj : objs) {
				HSSFRow contentRow = sheets[sheetNum].createRow(rowNum);
				contentRow.setHeight((short) 300);
				HSSFCell[] cells = getCells(contentRow, setInfo.getFieldNames()
						.get(sheetNum).length);
				int cellNum = 1; // 去掉一列序号，因此从1开始
				if (fieldNames != null) {
					for (int num = 0; num < fieldNames.length; num++) {
						Object value = ReflectionUtil.invokeGetterMethod(obj,
								fieldNames[num]);
						cells[cellNum].setCellValue(value == null ? "" : value
								.toString());
						cellNum++;
					}
				}
				rowNum++;
			}
			// adjustColumnSize(sheets, sheetNum, fieldNames); // 自动调整列宽
			sheetNum++;
		}
		wb.write(setInfo.getOut());
	}

	/**
	 * 初始化HSSFWorkbook
	 *  @Method_Name    : init
	 *  @param wb
	 *  @return 
	 *  @return         : HSSFWorkbook
	 *  @Creation Date  : 2014年5月6日 上午11:46:00
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	private static HSSFWorkbook init(HSSFWorkbook wb) {
		wb = new HSSFWorkbook();

		titleFont = wb.createFont();
		titleStyle = wb.createCellStyle();
		dateStyle = wb.createCellStyle();
		dateFont = wb.createFont();
		headStyle = wb.createCellStyle();
		headFont = wb.createFont();
		contentStyle = wb.createCellStyle();
		contentFont = wb.createFont();

		initTitleCellStyle();
		initTitleFont();
		initDateCellStyle();
		initDateFont();
		initHeadCellStyle();
		initHeadFont();
		initContentCellStyle();
		initContentFont();
		return wb;
	}

	/**
	 * @Description: 自动调整列宽
	 */
	@SuppressWarnings("unused")
	private static void adjustColumnSize(HSSFSheet[] sheets, int sheetNum,
			String[] fieldNames) {
		for (int i = 0; i < fieldNames.length + 1; i++) {
			sheets[sheetNum].autoSizeColumn(i, true);
		}
	}

	/**
	 * @Description: 创建标题行(需合并单元格)
	 */
	private static void createTableTitleRow(ExportSetInfo setInfo,
			HSSFSheet[] sheets, int sheetNum) {
		CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, setInfo
				.getFieldNames().get(sheetNum).length);
		sheets[sheetNum].addMergedRegion(titleRange);
		HSSFRow titleRow = sheets[sheetNum].createRow(0);
		titleRow.setHeight((short) 800);
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(setInfo.getTitles()[sheetNum]);
	}

	/**
	 * @Description: 创建条件行(需合并单元格)
	 */
	private static void createTableDateRow(ExportSetInfo setInfo,
			HSSFSheet[] sheets, int sheetNum) {
		CellRangeAddress dateRange = new CellRangeAddress(1, 1, 0, setInfo
				.getFieldNames().get(sheetNum).length);
		sheets[sheetNum].addMergedRegion(dateRange);
		HSSFRow dateRow = sheets[sheetNum].createRow(1);
		dateRow.setHeight((short) 350);
		HSSFCell dateCell = dateRow.createCell(0);
		dateCell.setCellStyle(dateStyle);
		dateCell.setCellValue(setInfo.getSearch()[sheetNum]);
	}

	/**
	 * @Description: 创建表头行(需合并单元格)
	 */
	private static void creatTableHeadRow(ExportSetInfo setInfo,
			HSSFSheet[] sheets, int sheetNum) {
		// 表头
		HSSFRow headRow = sheets[sheetNum].createRow(2);
		headRow.setHeight((short) 350);
		// 序号列
		HSSFCell snCell = headRow.createCell(0);
		snCell.setCellStyle(headStyle);
		snCell.setCellValue("序号");
		// 列头名称
		for (int num = 1, len = setInfo.getHeadNames().get(sheetNum).length; num <= len; num++) {
			HSSFCell headCell = headRow.createCell(num);
			headCell.setCellStyle(headStyle);
			headCell.setCellValue(setInfo.getHeadNames().get(sheetNum)[num - 1]);
		}
	}

	/**
	 * @Description: 创建所有的Sheet
	 */
	private static HSSFSheet[] getSheets(int num, String[] names,
			HSSFWorkbook wb) {
		HSSFSheet[] sheets = new HSSFSheet[num];
		for (int i = 0; i < num; i++) {
			sheets[i] = wb.createSheet(names[i]);
		}
		return sheets;
	}

	/**
	 * @Description: 创建内容行的每一列(附加一列序号)
	 */
	private static HSSFCell[] getCells(HSSFRow contentRow, int num) {
		HSSFCell[] cells = new HSSFCell[num + 1];

		for (int i = 0, len = cells.length; i < len; i++) {
			cells[i] = contentRow.createCell(i);
			cells[i].setCellStyle(contentStyle);
		}
		// 设置序号列值，因为出去标题行和日期行，所有-2
		cells[0].setCellValue(contentRow.getRowNum() - 2);

		return cells;
	}

	/**
	 * @Description: 初始化标题行样式
	 */
	private static void initTitleCellStyle() {
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(titleFont);
		titleStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.getIndex());
	}

	/**
	 * @Description: 初始化日期行样式
	 */
	private static void initDateCellStyle() {
		dateStyle.setAlignment(CellStyle.ALIGN_LEFT);
		dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		dateStyle.setFont(dateFont);
		dateStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.getIndex());
	}

	/**
	 * @Description: 初始化表头行样式
	 */
	private static void initHeadCellStyle() {
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setFont(headFont);
		headStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
		headStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headStyle.setBorderRight(CellStyle.BORDER_THIN);
		headStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
	}

	/**
	 * @Description: 初始化内容行样式
	 */
	private static void initContentCellStyle() {
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_THIN);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);
		contentStyle.setBorderRight(CellStyle.BORDER_THIN);
		contentStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		contentStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		contentStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		contentStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		contentStyle.setWrapText(true); // 字段换行
	}

	/**
	 * @Description: 初始化标题行字体
	 */
	private static void initTitleFont() {
		titleFont.setFontName("华文楷体");
		titleFont.setFontHeightInPoints((short) 20);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setCharSet(Font.DEFAULT_CHARSET);
		titleFont.setColor(IndexedColors.BLUE_GREY.getIndex());
	}

	/**
	 * @Description: 初始化条件行字体
	 */
	private static void initDateFont() {
		dateFont.setFontName("隶书");
		dateFont.setFontHeightInPoints((short) 10);
		dateFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		dateFont.setCharSet(Font.DEFAULT_CHARSET);
		dateFont.setColor(IndexedColors.BLUE_GREY.getIndex());
	}

	/**
	 * @Description: 初始化表头行字体
	 */
	private static void initHeadFont() {
		headFont.setFontName("宋体");
		headFont.setFontHeightInPoints((short) 10);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headFont.setCharSet(Font.DEFAULT_CHARSET);
		headFont.setColor(IndexedColors.BLUE_GREY.getIndex());
	}

	/**
	 * @Description: 初始化内容行字体
	 */
	private static void initContentFont() {
		contentFont.setFontName("宋体");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLUE_GREY.getIndex());
	}

	/**
	 * @Description: 封装Excel导出的设置信息
	 * @author: 曹志飞
	 */
	public static class ExportSetInfo {
		private LinkedHashMap<String, List<?>> objsMap;

		private String[] titles;

		private String[] search;

		private List<String[]> headNames;

		private List<String[]> fieldNames;

		private OutputStream out;

		public LinkedHashMap<String, List<?>> getObjsMap() {
			return objsMap;
		}

		/**
		 * @param objMap
		 *            导出数据
		 * 
		 *            泛型 String : 代表sheet名称 List : 代表单个sheet里的所有行数据
		 */
		public void setObjsMap(LinkedHashMap<String, List<?>> objsMap) {
			this.objsMap = objsMap;
		}

		public List<String[]> getFieldNames() {
			return fieldNames;
		}

		public String[] getSearch() {
			return search;
		}

		public void setSearch(String[] search) {
			this.search = search;
		}

		/**
		 * @param clazz
		 *            对应每个sheet里的每行数据的对象的属性名称
		 */
		public void setFieldNames(List<String[]> fieldNames) {
			this.fieldNames = fieldNames;
		}

		public String[] getTitles() {
			return titles;
		}

		/**
		 * @param titles
		 *            对应每个sheet里的标题，即顶部大字
		 */
		public void setTitles(String[] titles) {
			this.titles = titles;
		}

		public List<String[]> getHeadNames() {
			return headNames;
		}

		/**
		 * @param headNames
		 *            对应每个页签的表头的每一列的名称
		 */
		public void setHeadNames(List<String[]> headNames) {
			this.headNames = headNames;
		}

		public OutputStream getOut() {
			return out;
		}

		/**
		 * @param out
		 *            Excel数据将输出到该输出流
		 */
		public void setOut(OutputStream out) {
			this.out = out;
		}
	}
}
