package com.hdos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * Copyright: Copyright 2000 - 2010 soft Tech. Co. Ltd. All Rights Reserved.
 * Date: 2010-11-26 Author: shenlu Version: IM客服平台V1.0 Description: 读写excel的方法
 */
public class POIExcelUtil {
	public static void main(String[] args) throws Exception {
		readXmlFile("D:/20150310003C.xml");
		// List<Map<String, String>> listmap
		// =ExcelUtil.readExcelFile("D:\\OrderFinish99.xls", 0);
//		List<String[]> listmap = POIExcelUtil.readExcelFile(
//				"H:/myhdos/.metadata/.me_tcat/webapps/hdsc/upload/files/84521425193159295.xls", 0);
		// List<Map<String, String>> listmap =
		// ExcelUtil.readExcelFile("D:\\OrderFinish88.xlsx", 0);
		// List<Map<String, String>> listmap = ExcelUtil.readExcel("D:\\88.xls",
		// 0);
//		
//		for (String[] map : listmap) {
//			for (int i = 0; i < map.length; i++) {
//				System.out.print(map[i] + "\t");
//			}
//			System.out.print("\n");
//		}
		/*
		List<Map<String, String>> listmap1 = new ArrayList();
		Map mp1 = new HashMap();
		mp1.put("0", "今年");
		mp1.put("1", "18");

		Map mp11 = new HashMap();
		mp11.put("0", "明22年");
		mp11.put("1", "1911");
		listmap1.add(mp1);
		listmap1.add(mp11);

		String[] header = new String[] { "姓名", "年龄" };

		String[] header2 = new String[] { "姓名2", "年龄3" };
*/
		// Workbook wb = writeExcelFile(null, "sheet1", "d:\\ds.xls",null,
		// header, listmap1);
		// wb = writeExcelFile(wb, "sheet2", "d:\\ds.xls",null, header2,
		// listmap1);//2003支持多sheet的建立

		// Workbook wb2 = writeExcelFile(null, "sheet1", "D:/ds1.xlsx", header,
		// listmap1); // wb2 =
		// wb2=writeExcelFile(wb2, "sheet1", "D:/ds1.xlsx", header2,
		// listmap1);//2007不支持多sheet的建立

	}

	/**
	 * 获取sheet表中的数据
	 * 
	 * @param sheet
	 * @return　map 格式以0.1.2....列标做为key
	 */
	private static List<String[]> getData(Sheet sheet, int getdatanum) {

		List<String[]> ls = new ArrayList<String[]>();

		/*
		 * Pattern pattern = Pattern.compile("([\\d]+)"); Matcher matcher;
		 */

		Iterator<Row> rit = sheet.iterator();

		String[] lineStr = null;
		int counts = 0;
		while (rit.hasNext()) {
			
			Row row = rit.next();
			int rowsize = row.getPhysicalNumberOfCells();
			lineStr = new String[rowsize]; // 用于接收每列的数据。
			for (int i = 0; i < rowsize; i++) {
				Cell cell = row.getCell(i);
				
				String k = ""; // 用于接收每个单元格的数据。
				if (cell == null) {
					lineStr[i] = k; // 赋值。
					continue;
				}
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					k = "";
					break;
				case Cell.CELL_TYPE_ERROR:
					k = Byte.toString(cell.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					k = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						k = sdf.format(cell.getDateCellValue());
					} else {
//						k = Integer.toString((int) cell.getNumericCellValue());
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue());  
						k = bd.toPlainString();
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					k = Boolean.toString(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					k = cell.getCellFormula();
					break;
				default:
					k = "";
				}
				if ((k != null) && (!"".equals(k))) {

					lineStr[i] = k; // 赋值。
				} else {
					lineStr[i] = ""; // 赋值。
				}

			}

			if (lineStr.length!=0) { // 判断是不是为空

				ls.add(lineStr);

			}

		}

		return ls;

	}

	/**
	 * 读取excel文件，可以是2003,2007
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @return
	 */
	public static List<String[]> readExcelFile(InputStream filePath,
			int sheetNum, int getdatanum) {
	InputStream ins = null;
		Workbook book = null;
		List<String[]> list = null;
		try {
			//book = new XSSFWorkbook(filePath);
			book = WorkbookFactory.create(filePath);
			list = getData(book.getSheetAt(sheetNum), getdatanum);
			ins.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 读取excel文件，可以是2003,2007
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @return
	 */
	public static List<String[]> readExcelFile(String filePath,
			int sheetNum, int getdatanum) {

		InputStream ins = null;
		Workbook book = null;
		List<String[]> list = null;
		try {
			ins = new FileInputStream(filePath);
			//book = new XSSFWorkbook();
			book = WorkbookFactory.create(ins);
			list = getData(book.getSheetAt(sheetNum), getdatanum);
			ins.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 
	 * @description (读取证书文件)
	 * @auther pjq
	 * @time 2015-3-10
	 * @return
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static List readXmlFile(String filePath) throws Exception{
		List list = new ArrayList();
		 Element element = null;
		  // 可以使用绝对路劲
		 File f = new File(filePath);
		  
		  // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
		  DocumentBuilder db = null;
		  DocumentBuilderFactory dbf = null;
		   // 返回documentBuilderFactory对象
		   dbf = DocumentBuilderFactory.newInstance();
		   // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		   db = dbf.newDocumentBuilder();

		   // 得到一个DOM并返回给document对象
		   Document dt = db.parse(f);
		   // 得到一个elment根元素
		   element = dt.getDocumentElement();
		   // 获得根节点
		   System.out.println("根元素：" + element.getNodeName());

		   // 获得根元素下的子节点
		   NodeList childNodes = element.getChildNodes();
		   String [] data={};
		   list.add(element.getNodeName());
		   list.add(element.getChildNodes().getLength());
		   list.add(childNodes.item(1).getChildNodes().item(9).getTextContent());
		return list;
	}

	/**
	 * 
	 * 写excel 文件
	 * 
	 * @param filePath
	 *            文件路径
	 * 
	 * @param sheetNum
	 *            活动的sheet编号，编号从0开始
	 * 
	 * @return
	 */

	public static Workbook writeExcelFile(Workbook wb, String sheetName,
			String filePath, String title, String footer, String[] header,
			List<Map<String, String>> sdata, String cl) {

		try {

			if (filePath.endsWith("xls")) {
				if (wb == null) {
					wb = new HSSFWorkbook();
				}
			} else if (filePath.endsWith("xlsx")) {
				if (wb == null) {
					wb = new XSSFWorkbook();
				}
			}

			Sheet sheet = wb.createSheet(sheetName);

			int k = 0;
			if (title != null && !title.equals("")) {
				Row row = sheet.createRow(0);
				row.setHeight((short) 600);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setTitle = wb.createCellStyle();
				setTitle.setAlignment(CellStyle.ALIGN_CENTER);

				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 20);
				setTitle.setFont(font);
				cell.setCellValue(title);
				cell.setCellStyle(setTitle);
				k = 1;
			}
			Row row1 = sheet.createRow(k);
			row1.setHeight((short) 400);
			for (short i = 0; i < header.length; i++) {
				Cell cell = row1.createCell(i);
				CellStyle set = wb.createCellStyle();
				// set.setAlignment(CellStyle.ALIGN_CENTER);
				// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 11);
				if (i > header.length - 8 && i < header.length) {
					sheet.setColumnWidth(i, 3500);
				} else if (i > 2 && i < header.length - 7) {
					sheet.setColumnWidth(i, 1000);
				}
				set.setFont(font);
				cell.setCellStyle(set);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(header[i]);
			}
			for (int j = 0; j < sdata.size(); j++) {
				Row rows = sheet.createRow(++k);
				Map mp = sdata.get(j);
				for (short index = 0; index < mp.size(); index++) {
					Cell cell = rows.createCell(index);
					// CellStyle set = wb.createCellStyle();
					// Font font = wb.createFont();
					// font.setColor(Font.COLOR_RED);
					// set.setFont(font);
					// set.setAlignment(CellStyle.ALIGN_CENTER);
					// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					if (mp.get(String.valueOf(index)) != null
							&& !"null".equals(mp.get(String.valueOf(index))
									.toString())) {
						if (cl != null && index < mp.size() - 7 && index > 2) {
							String value = mp.get(String.valueOf(index))
									.toString();
							String[] arr = value.split(",");
							if (arr[0] != null && arr[0].trim().equals("m")) {
								cell.setCellValue(arr[1]);
							} else {
								if (arr.length == 1) {
									cell.setCellValue(arr[0]);
								} else {
									cell.setCellValue(arr[1]);
								}
							}
						} else {
							cell.setCellValue(mp.get(String.valueOf(index))
									.toString());
						}
					} else {
						cell.setCellValue("");
					}
					// cell.setCellStyle(set);
				}

			}
			if (footer != null && !footer.equals("")) {
				Row row = sheet.createRow(++k);
				sheet.addMergedRegion(new CellRangeAddress(k, k, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setFooter = wb.createCellStyle();
				setFooter.setAlignment(CellStyle.ALIGN_CENTER);
				Font font = wb.createFont();
				font.setColor(Font.COLOR_RED);
				setFooter.setFont(font);
				cell.setCellValue(footer);
				cell.setCellStyle(setFooter);

			}
			OutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return wb;

	}
	/*
	 * 导出简略的终端数据
	 */
	public static Workbook writeExcelFilev1(Workbook wb, String sheetName,
			String filePath, String title, String footer, String[] header,
			List<Map<String, String>> sdata, String cl) {
		wb=null;
		try {

			if (filePath.endsWith("xls")) {
				if (wb == null) {
					wb = new HSSFWorkbook();
				}
			} else if (filePath.endsWith("xlsx")) {
				if (wb == null) {
					wb = new XSSFWorkbook();
				}
			}

			Sheet sheet = wb.createSheet(sheetName);

			int k = 0;
			if (title != null && !title.equals("")) {
				Row row = sheet.createRow(0);
				row.setHeight((short) 600);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setTitle = wb.createCellStyle();
				setTitle.setAlignment(CellStyle.ALIGN_CENTER);

				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 14);
				setTitle.setFont(font);
				cell.setCellValue(title);
				cell.setCellStyle(setTitle);
				k = 1;
			}
			Row row1 = sheet.createRow(k);
			row1.setHeight((short) 400);
			for (short i = 0; i < header.length-1; i++) {
				Cell cell = row1.createCell(i);
				CellStyle set = wb.createCellStyle();
				// set.setAlignment(CellStyle.ALIGN_CENTER);
				// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 11);
				if (i > header.length - 8 && i < header.length) {
					sheet.setColumnWidth(i, 4500);
				} else if (i > 2 && i < header.length - 7) {
					sheet.setColumnWidth(i, 1000);
				}
				set.setFont(font);
				cell.setCellStyle(set);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(header[i]);
			}
			Row rows1 = sheet.createRow(++k);
			Map mp1 = sdata.get(0);
			for (short index = 0; index < mp1.size(); index++) {
				Cell cell = rows1.createCell(index);
				// CellStyle set = wb.createCellStyle();
				// Font font = wb.createFont();
				// font.setColor(Font.COLOR_RED);
				// set.setFont(font);
				// set.setAlignment(CellStyle.ALIGN_CENTER);
				// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				//System.out.println(String.valueOf(index)+"============"+index);
				if (mp1.get(String.valueOf(index)) != null
						&& !"null".equals(mp1.get(String.valueOf(index))
								.toString())) {
					
					if (cl != null ) {//&& index < mp.size() - 7 && index > 2
						
						String value = mp1.get(String.valueOf(index))
								.toString();
						String[] arr = value.split(",");
						if (arr[0] != null && arr[0].trim().equals("m")) {
							cell.setCellValue(arr[1]);
						} else {
							if (arr.length == 1) {
								cell.setCellValue(arr[0]);
							} else {
								cell.setCellValue(arr[1]);
							}
						}
					} else {
						cell.setCellValue(mp1.get(String.valueOf(index))
								.toString());
					}
					cell.setCellValue(mp1.get(String.valueOf(index))
							.toString());
				} else {
					cell.setCellValue("");
				}
				// cell.setCellStyle(set);
			}
			Row rows2 = sheet.createRow(++k);
			Map mp2 = sdata.get(0);
			Cell cell2 = rows2.createCell(0);
			CellStyle set2 = wb.createCellStyle();
			// set.setAlignment(CellStyle.ALIGN_CENTER);
			// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
			Font font2 = wb.createFont();
			font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font2.setFontHeightInPoints((short) 11);
			set2.setFont(font2);
			cell2.setCellStyle(set2);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell2.setCellValue(header[5]);
			for (int j = 1; j < sdata.size(); j++) {
				Row rows = sheet.createRow(++k);
				Map mp = sdata.get(j);
				for (short index = 0; index < mp.size(); index++) {
					Cell cell = rows.createCell(index);
					// CellStyle set = wb.createCellStyle();
					// Font font = wb.createFont();
					// font.setColor(Font.COLOR_RED);
					// set.setFont(font);
					// set.setAlignment(CellStyle.ALIGN_CENTER);
					// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//System.out.println(String.valueOf(index)+"============"+index);
					if (mp.get(String.valueOf(index)) != null
							&& !"null".equals(mp.get(String.valueOf(index))
									.toString())) {
						
						if (cl != null ) {//&& index < mp.size() - 7 && index > 2
							
							String value = mp.get(String.valueOf(index))
									.toString();
							String[] arr = value.split(",");
							if (arr[0] != null && arr[0].trim().equals("m")) {
								cell.setCellValue(arr[1]);
							} else {
								if (arr.length == 1) {
									cell.setCellValue(arr[0]);
								} else {
									cell.setCellValue(arr[1]);
								}
							}
						} else {
							cell.setCellValue(mp.get(String.valueOf(index))
									.toString());
						}
						cell.setCellValue(mp.get(String.valueOf(index))
								.toString());
					} else {
						cell.setCellValue("");
					}
					// cell.setCellStyle(set);
				}
			}
			if (footer != null && !footer.equals("")) {
				Row row = sheet.createRow(++k);
				sheet.addMergedRegion(new CellRangeAddress(k, k, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setFooter = wb.createCellStyle();
				setFooter.setAlignment(CellStyle.ALIGN_CENTER);
				Font font = wb.createFont();
				font.setColor(Font.COLOR_RED);
				setFooter.setFont(font);
				cell.setCellValue(footer);
				cell.setCellStyle(setFooter);

			}
			OutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return wb;
	}
	public static Workbook writeExcelFilev(Workbook wb, String sheetName,
			String filePath, String title, String footer, String[] header,
			List<Map<String, String>> sdata, String cl) {
		wb=null;
		try {

			if (filePath.endsWith("xls")) {
				if (wb == null) {
					wb = new HSSFWorkbook();
				}
			} else if (filePath.endsWith("xlsx")) {
				if (wb == null) {
					wb = new XSSFWorkbook();
				}
			}

			Sheet sheet = wb.createSheet(sheetName);

			int k = 0;
			if (title != null && !title.equals("")) {
				Row row = sheet.createRow(0);
				row.setHeight((short) 600);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setTitle = wb.createCellStyle();
				setTitle.setAlignment(CellStyle.ALIGN_CENTER);

				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 14);
				setTitle.setFont(font);
				cell.setCellValue(title);
				cell.setCellStyle(setTitle);
				k = 1;
			}
			Row row1 = sheet.createRow(k);
			row1.setHeight((short) 400);
			for (short i = 0; i < header.length; i++) {
				Cell cell = row1.createCell(i);
				CellStyle set = wb.createCellStyle();
				// set.setAlignment(CellStyle.ALIGN_CENTER);
				// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 11);
				if (i > header.length - 8 && i < header.length) {
					sheet.setColumnWidth(i, 3500);
				} else if (i > 2 && i < header.length - 7) {
					sheet.setColumnWidth(i, 1000);
				}
				set.setFont(font);
				cell.setCellStyle(set);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(header[i]);
			}
			for (int j = 0; j < sdata.size(); j++) {
				Row rows = sheet.createRow(++k);
				Map mp = sdata.get(j);
				for (short index = 0; index < mp.size(); index++) {
					Cell cell = rows.createCell(index);
					// CellStyle set = wb.createCellStyle();
					// Font font = wb.createFont();
					// font.setColor(Font.COLOR_RED);
					// set.setFont(font);
					// set.setAlignment(CellStyle.ALIGN_CENTER);
					// set.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//System.out.println(String.valueOf(index)+"============"+index);
					if (mp.get(String.valueOf(index)) != null
							&& !"null".equals(mp.get(String.valueOf(index))
									.toString())) {
						
						if (cl != null ) {//&& index < mp.size() - 7 && index > 2
							
							String value = mp.get(String.valueOf(index))
									.toString();
							String[] arr = value.split(",");
							if (arr[0] != null && arr[0].trim().equals("m")) {
								cell.setCellValue(arr[1]);
							} else {
								if (arr.length == 1) {
									cell.setCellValue(arr[0]);
								} else {
									cell.setCellValue(arr[1]);
								}
							}
						} else {
							cell.setCellValue(mp.get(String.valueOf(index))
									.toString());
						}
						cell.setCellValue(mp.get(String.valueOf(index))
								.toString());
					} else {
						cell.setCellValue("");
					}
					// cell.setCellStyle(set);
				}
			}
			if (footer != null && !footer.equals("")) {
				Row row = sheet.createRow(++k);
				sheet.addMergedRegion(new CellRangeAddress(k, k, 0,
						header.length - 1));
				Cell cell = row.createCell(0);
				CellStyle setFooter = wb.createCellStyle();
				setFooter.setAlignment(CellStyle.ALIGN_CENTER);
				Font font = wb.createFont();
				font.setColor(Font.COLOR_RED);
				setFooter.setFont(font);
				cell.setCellValue(footer);
				cell.setCellStyle(setFooter);

			}
			OutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return wb;
	}
	/**
	 * 
	 * 写excel 2007　文件
	 * 
	 * @param filePath
	 *            文件路径
	 * 
	 * @param sheetNum
	 *            活动的sheet编号，编号从0开始
	 * 
	 * @return
	 */

	public static XSSFWorkbook writeXSSFWorkbook(XSSFWorkbook wb,
			String sheetName, String[] header, List<Map<String, String>> sdata) {

		try {

			if (wb == null) {
				wb = new XSSFWorkbook();
			}
			// CreationHelper createHelper = wb.getCreationHelper();

			Sheet sheet = wb.createSheet(sheetName);

			Row row = sheet.createRow(0);
			for (short i = 0; i < header.length; i++) {
				Cell cell = row.createCell(i);

				cell.setCellValue(header[i]);
			}
			for (int j = 0; j < sdata.size(); j++) {
				Row rows = sheet.createRow(j + 1);
				Map mp = sdata.get(j);
				for (short index = 0; index < mp.size(); index++) {
					Cell cell = rows.createCell(index);

					if (mp.get(index + "") != null)
						cell.setCellValue(mp.get(index + "").toString());
					else
						cell.setCellValue("");
				}

			}

			/*
			 * FileOutputStream fileOut = new FileOutputStream(filePath);
			 * wb.write(fileOut); fileOut.close();
			 */

		} catch (Exception e) {

			e.printStackTrace();

		}

		return wb;

	}

	/**
	 * 
	 * 写excel 97-2003
	 * 
	 * @param filePath
	 *            文件路径
	 * 
	 * @param sheetNum
	 *            活动的sheet编号，编号从0开始
	 * 
	 * @return
	 */

	public static HSSFWorkbook writeHSSFWorkbook(HSSFWorkbook wb,
			String sheetName, String[] header, List<Map<String, String>> sdata) {

		try {

			if (wb == null) {
				wb = new HSSFWorkbook();
			}
			// CreationHelper createHelper = wb.getCreationHelper();
			Sheet sheet = wb.createSheet(sheetName);

			Row row = sheet.createRow(0);
			for (short i = 0; i < header.length; i++) {
				Cell cell = row.createCell(i);

				cell.setCellValue(header[i]);
			}
			for (int j = 0; j < sdata.size(); j++) {
				Row rows = sheet.createRow(j + 1);
				Map mp = sdata.get(j);
				for (short index = 0; index < mp.size(); index++) {
					Cell cell = rows.createCell(index);

					if (mp.get(index + "") != null)
						cell.setCellValue(mp.get(index + "").toString());
					else
						cell.setCellValue("");
				}

			}

			/*
			 * FileOutputStream fileOut = new FileOutputStream(filePath);
			 * wb.write(fileOut); fileOut.close();
			 */

		} catch (Exception e) {

			e.printStackTrace();

		}
		System.out.println(wb);
		return wb;

	}
	public static Workbook exportExcelFile(Workbook wb,String sheetName,
			String filePath, String title, String footer, String[] header,int cellwidth,
			List<Map> datalist) {
		if(wb==null) wb = new HSSFWorkbook();
	   Sheet sheet = wb.createSheet(sheetName);
	   int rownum = 0;
	   rownum=addTitle(wb, sheet, rownum, title, header.length);
	   rownum=addHeader(wb, sheet, rownum, header,cellwidth);
	   rownum=addRow(wb, sheet, rownum, datalist);
	   rownum=addFooter(wb, sheet, rownum, footer, header.length);
		try {
			OutputStream fileOut= new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	//添加标题
	  public static int addTitle(Workbook wb,Sheet sheet,int rownum,String title,int length){
		  if(title!=null&&!title.equals("")){
			    Row row = sheet.createRow(rownum);
				row.setHeight((short) 600);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,length - 1));
				Cell cell = row.createCell(0);
				CellStyle setTitle = wb.createCellStyle();
				setTitle.setAlignment(CellStyle.ALIGN_CENTER);

				Font font = wb.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short) 14);
				setTitle.setFont(font);
				cell.setCellValue(title);
				cell.setCellStyle(setTitle);
				rownum+=1;
		  }
			return rownum;
		}
	//添加头部
			public static int addHeader(Workbook wb,Sheet sheet,int rownum,String[] header,int[] headwidth){
				if(header!=null){
					 Row row = sheet.createRow(rownum);
					 row.setHeight((short) 400);
					 for (int i = 0; i < header.length; i++) {
							Cell cell = row.createCell(i);
							CellStyle set = wb.createCellStyle();
							Font font = wb.createFont();
							font.setBoldweight(Font.BOLDWEIGHT_BOLD);
							font.setFontHeightInPoints((short) 11);
							sheet.setColumnWidth(i, headwidth[i]);
							set.setFont(font);
							cell.setCellStyle(set);
							cell.setCellValue(header[i]);
						}
					rownum+=1;
				} 
				 return rownum;
			}
			public static int addHeader(Workbook wb,Sheet sheet,int rownum,String[] header){
				if(header!=null){
					 Row row = sheet.createRow(rownum);
					 row.setHeight((short) 400);
					 for (short i = 0; i < header.length; i++) {
							Cell cell = row.createCell(i);
							CellStyle set = wb.createCellStyle();
							Font font = wb.createFont();
							font.setBoldweight(Font.BOLDWEIGHT_BOLD);
							font.setFontHeightInPoints((short) 11);
							if (i > header.length - 8 && i < header.length) {
								sheet.setColumnWidth(i, 4000);
							} else if (i > 2 && i < header.length - 7) {
								sheet.setColumnWidth(i, 1000);
							}
							set.setFont(font);
							cell.setCellStyle(set);
							cell.setCellValue(header[i]);
						}
					rownum+=1;
				} 
				 return rownum;
			}
	//添加头部
		public static int addHeader(Workbook wb,Sheet sheet,int rownum,String[] header,int width){
			if(header!=null){
				 Row row = sheet.createRow(rownum);
				 row.setHeight((short) 400);
				 for (short i = 0; i < header.length; i++) {
						Cell cell = row.createCell(i);
						CellStyle set = wb.createCellStyle();
						Font font = wb.createFont();
						font.setBoldweight(Font.BOLDWEIGHT_BOLD);
						font.setFontHeightInPoints((short) 11);
						sheet.setColumnWidth(i, width);
						set.setFont(font);
						cell.setCellStyle(set);
						cell.setCellValue(header[i]);
					}
				rownum+=1;
			} 
			 return rownum;
		}
		//添加数据行（map存放的key为字符串的数字从0开始）
		public static int addRow(Workbook wb,Sheet sheet,int rownum,List<Map> datalist){
			if(datalist!=null){
				for (int i= 0; i < datalist.size(); i++){
					Row rows = sheet.createRow(rownum);
					Map mp = datalist.get(i);
					for (int index = 0; index < mp.size(); index++) {
						 Cell cell = rows.createCell(index);
						 String val=mp.get(""+index)+"";
						 cell.setCellValue(val);
					}
					rownum+=1;
				}
			}
			return rownum;
		}
		//添加尾部
		public static int addFooter(Workbook wb,Sheet sheet,int rownum,String footer,int length){
			 if(footer!=null&&!footer.equals("")){
			    Row row = sheet.createRow(rownum);
				sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0,length - 1));
				Cell cell = row.createCell(0);
				CellStyle setFooter = wb.createCellStyle();
				setFooter.setAlignment(CellStyle.ALIGN_CENTER);
				Font font = wb.createFont();
				font.setColor(Font.COLOR_RED);
				setFooter.setFont(font);
				cell.setCellValue(footer);
				cell.setCellStyle(setFooter);
				rownum+=1;
			 }
			return rownum;
		}
}
