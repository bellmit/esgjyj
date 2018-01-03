package com.eastsoft.esgjyj.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * POI 操作 EXCEL 工具类。
 * @author mengbin
 * @author fengxd
 * @author songyj
 * @author zhangzx
 * @since 2014/10/20
 * @version 1.1.0, 2017/6/26
 */
public class ExcelUtil {
	/**
	 * Excel 97-2003 (.xls)
	 */
	public static final int XLS = 0;
	/**
	 * Excel 97-2003 (.xls)
	 */
	public static final int XLSX = 1;
	/**
	 * Excel 97-2003 (.xls) 最大行数
	 */
	public static final int XLS_MAX_ROWS = 65536;
	/**
	 * Excel (.xlsx) 最大行数
	 */
	public static final int XLSX_MAX_ROWS = 1048576;
	
	public static final short HEADER_BACKGROUND_COLOR = IndexedColors.PALE_BLUE.index;
	public static final short HEADER_FONT_COLOR = IndexedColors.DARK_RED.index;
	public static final short DEFAULT_ROW_HEIGHT = 500;
	public static final short DEFAULT_COLUMN_WIDTH = 20 * 256;
	
	public static final String PAGE_ENCODING = "GBK";
	public static final String BAD_FILE_FORMAT_EXCEPTION = "Excel文件格式不正确！";
	public static final String COLUMN_COUNT_NOT_SAME = "单行列数不一致！";
	
	/**
	 * 打开指定路径文件，返回工作簿对象。
	 * <p>支持 XLS 、 XLSX 。
	 * @param path 指定路径。
	 * @return 工作簿对象。
	 * @throws IOException 读取文件时抛出异常。
	 */
	public static Workbook open(String path) throws IOException {
		Workbook workbook = null;
		
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			String suffix = path.substring(path.lastIndexOf(".")).toLowerCase();
			if (".xls".equalsIgnoreCase(suffix)) {
				workbook = new HSSFWorkbook(in);
			} else if (".xlsx".equalsIgnoreCase(suffix)) {
				workbook = new XSSFWorkbook(in);
			} else {
				throw new IOException(BAD_FILE_FORMAT_EXCEPTION);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return workbook;
	}
	
	/**
	 * 读取 EXCEL 文件获得对象列表。
	 * @param path EXCEL 文件路径。
	 * @param clazz 返回的对象类型。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @return 对象列表。
	 * @throws IOException 读取文件时抛出异常。
	 * @throws InstantiationException 实例化对象时抛出异常。
	 * @throws IllegalAccessException 实例化对象或通过反射调用方法时抛出异常。
	 * @throws InvocationTargetException 通过反射调用方法时抛出异常。
	 * @see #xlsDto(InputStream, int, Class, int, int)
	 */
	public static <T> List<T> xlsDto(String path, Class<T> clazz, int firstRowIndex, int lastRowIndex)
			throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> list = new ArrayList<T>();
		
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			String suffix = path.substring(path.lastIndexOf(".")).toLowerCase();
			int fileType;
			if (".xls".equals(suffix)) {
				fileType = ExcelUtil.XLS;
			} else if ("xlsx".equals(suffix)) {
				fileType = ExcelUtil.XLSX;
			} else {
				throw new IOException(BAD_FILE_FORMAT_EXCEPTION);
			}
			list = ExcelUtil.xlsDto(in, fileType, clazz, firstRowIndex, lastRowIndex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 读取 EXCEL 文件获得对象列表。
	 * @param in 输入流对象。
	 * @param fileType {@link ExcelUtil#XLS} 或 {@link ExcelUtil#XLSX} 。
	 * @param clazz 返回的对象类型。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @return 对象列表。
	 * @throws IOException 读取文件时抛出异常。
	 * @throws InstantiationException 实例化对象时抛出异常。
	 * @throws IllegalAccessException 实例化对象或通过反射调用方法时抛出异常。
	 * @throws InvocationTargetException 通过反射调用方法时抛出异常。
	 */
	public static <T> List<T> xlsDto(InputStream in, int fileType, Class<T> clazz, int firstRowIndex, int lastRowIndex)
			throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> list = new ArrayList<T>();
		
		Workbook workbook = null;
		try {
			if (ExcelUtil.XLS == fileType) {
				workbook = new HSSFWorkbook(in);
			} else if (ExcelUtil.XLSX == fileType) {
				workbook = new XSSFWorkbook(in);
			} else {
				throw new IOException(BAD_FILE_FORMAT_EXCEPTION);
			}
			
			// java反射获得类的属性
			Field[] fields = clazz.getDeclaredFields();

			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Cell cell;
			T bean;
			String name;
			Object value;
			for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
				row = sheet.getRow(rowIndex);
				
				// 创建一个新对象
				bean = clazz.newInstance();
				for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
					cell = row.getCell(fieldIndex);
					
					name = fields[fieldIndex].getName();
					value = ExcelUtil.getCellValue(cell, fields[fieldIndex].getType().getName());
					BeanUtils.setProperty(bean, name, value);
				}
				list.add(bean);
			}
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 读取 EXCEL 文件获得 String[][] 数组。
	 * @param path Excel 文件路径。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @param firstColIndex 起始列索引，从 0 开始。
	 * @param lastColIndex 结束列索引，从 0 开始。
	 * @return String[][] 数组。
	 * @throws IOException 读取文件时抛出异常。
	 * @see #open(String)
	 * @see #xlsToStringArray(Workbook, int, int, int, int)
	 */
	public static String[][] xlsToStringArray(String path, int firstRowIndex, int lastRowIndex, int firstColIndex, int lastColIndex)
			throws IOException {
		String[][] array = new String[0][0];
		
		Workbook workbook = null;
		try {
			workbook = ExcelUtil.open(path);
			array = ExcelUtil.xlsToStringArray(workbook, firstRowIndex, lastRowIndex, firstColIndex, lastColIndex);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return array;
	}
	
	/**
	 * 读取 EXCEL 文件获得 String[][] 数组。
	 * @param bytes Excel 文件字节码。
	 * @param fileType {@link ExcelUtil#XLS} 或 {@link ExcelUtil#XLSX} 。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @param firstColIndex 起始列索引，从 0 开始。
	 * @param lastColIndex 结束列索引，从 0 开始。
	 * @return String[][] 数组。
	 * @throws IOException 读取文件时抛出异常。
	 * @see #xlsToStringArray(Workbook, int, int, int, int)
	 */
	public static String[][] xlsToStringArray(byte[] bytes, int fileType, int firstRowIndex, int lastRowIndex, int firstColIndex, int lastColIndex)
			throws IOException {
		String[][] array = new String[0][0];
		
		InputStream is = null;
		Workbook workbook = null;
		try {
			is = new ByteArrayInputStream(bytes);
			if (ExcelUtil.XLS == fileType) {
				workbook = new HSSFWorkbook(is);
			} else if (ExcelUtil.XLSX == fileType) {
				workbook = new XSSFWorkbook(is);
			} else {
				throw new IOException(BAD_FILE_FORMAT_EXCEPTION);
			}
			array = ExcelUtil.xlsToStringArray(workbook, firstRowIndex, lastRowIndex, firstColIndex, lastColIndex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return array;
	}
	
	/**
	 * 导出 EXCEL 文件到 HTTP 响应对象。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不包含后缀）。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 JavaBean。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[], Collection, String)
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String filename, String sheetName,
			String[] headers, Collection<?> collection, String dateFormat) throws IOException {
		OutputStream out = null;
		try {
			ExcelUtil.resetResponseToDownload(request, response, filename, PAGE_ENCODING, 1 + collection.size());
			out = response.getOutputStream();
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出 EXCEL 文件到指定路径。
	 * @param filename 导出路径。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 JavaBean。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static void export(String filename, String sheetName, String[] headers, Collection<?> collection,
			String dateFormat) throws IOException {
		OutputStream out = null;
		try {
			File file = ExcelUtil.createFile(filename, 1 + collection.size());
			out = new FileOutputStream(file);
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成 EXCEL 文件并获得字节码。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 JavaBean。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @return EXCEL 文件字节码。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static byte[] export(String sheetName, String[] headers, Collection<?> collection,
			String dateFormat) throws IOException {
		byte[] bytes = null;
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bytes;
	}

	/**
	 * 导出 EXCEL 文件到 HTTP 响应对象。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不包含后缀）
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[], String[][])
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String filename,
			String sheetName, String[] headers, String[][] data) throws IOException {
		OutputStream out = null;
		try {
			ExcelUtil.resetResponseToDownload(request, response, filename, PAGE_ENCODING, 1 + data.length);
			out = response.getOutputStream();
			ExcelUtil.export(out, sheetName, headers, data);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出 EXCEL 文件到指定路径。
	 * @param filename 导出路径。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[], String[][])
	 */
	public static void export(String filename, String sheetName, String[] headers, String[][] data)
			throws IOException {
		OutputStream out = null;
		try {
			File file = ExcelUtil.createFile(filename, 1 + data.length);
			out = new FileOutputStream(file);
			ExcelUtil.export(out, sheetName, headers, data);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成 EXCEL 文件并获得字节码。
	 * @param filename 导出路径。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @return EXCEL 文件字节码。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static byte[] export(String sheetName, String[] headers, String[][] data)
			throws IOException {
		byte[] bytes = null;
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			export(out, sheetName, headers, data);
			out.flush();
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bytes;
	}
	
	/**
	 * 导出 EXCEL 文件到 HTTP 响应对象，支持合并单元格。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不包含后缀）。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行，规则如下：
	 * <p>1、以 "$" 开头表示该列为隐藏列，生成 EXCEL 时直接跳过；
	 * <p>2、包含 "#" 表示该列数据中值相同的行合并单元格；
	 * <p>3、包含 "^" 表示该标题单元格占多行，"^" 后的数字代表所占行数；
	 * <p>4、包含 "*" 表示该标题单元格占多列，"*" 后的数字代表所占列数；
	 * <p>5、标题单元格必须按照【"$" + 单元格内容 + "#" + "^" + "*"】的顺序，如："$SN"、"承办部门#^2*3"；
	 * <p>6、被合并的单元格用 "&amp;nbsp;" 作为单元格的值。
	 * @param collection 数据集合，集合中为 JavaBean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], String[][])
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String filename,
			String sheetName, String[][] headers, Collection<?> collection, String dateFormat) throws IOException {
		OutputStream out = null;
		try {
			ExcelUtil.resetResponseToDownload(request, response, filename, PAGE_ENCODING, headers.length + collection.size());
			out = response.getOutputStream();
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出 EXCEL 文件到指定路径。
	 * @param filename 导出路径。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 JavaBean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], Collection, String)
	 */
	public static void export(String filename, String sheetName, String[][] headers, Collection<?> collection,
			String dateFormat) throws IOException {
		OutputStream out = null;
		try {
			File file = ExcelUtil.createFile(filename, headers.length + collection.size());
			out = new FileOutputStream(file);
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成 EXCEL 文件并获得字节码，支持合并单元格。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 JavaBean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @return EXCEL 文件字节码。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], Collection, String)
	 */
	public static byte[] export(String sheetName, String[][] headers, Collection<?> collection,
			String dateFormat) throws IOException {
		byte[] bytes = null;
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ExcelUtil.export(out, sheetName, headers, collection, dateFormat);
			out.flush();
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bytes;
	}

	/**
	 * 导出 EXCEL 文件到 HTTP 响应对象，支持合并单元格。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不包含后缀）。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行，规则如下：
	 * <p>1、以 "$" 开头表示该列为隐藏列，生成 EXCEL 时直接跳过；
	 * <p>2、包含 "#" 表示该列数据中值相同的行合并单元格；
	 * <p>3、包含 "^" 表示该标题单元格占多行，"^" 后的数字代表所占行数；
	 * <p>4、包含 "*" 表示该标题单元格占多列，"*" 后的数字代表所占列数；
	 * <p>5、标题单元格必须按照【"$" + 单元格内容 + "#" + "^" + "*"】的顺序，如："$SN"、"承办部门#^2*3"；
	 * <p>6、被合并的单元格用 "&amp;nbsp;" 作为单元格的值。
	 * @param data 数据行。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], String[][])
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String filename,
			String sheetName, String[][] headers, String[][] data) throws IOException {
		OutputStream out = null;
		try {
			ExcelUtil.resetResponseToDownload(request, response, filename, ExcelUtil.PAGE_ENCODING, headers.length + data.length);
			out = response.getOutputStream();
			ExcelUtil.export(out, sheetName, headers, data);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出 EXCEL 文件到指定路径，支持合并单元格。
	 * @param filename 导出路径。
	 * @param sheetName sheet表名
	 * @param headers 标题行，规则如下：
	 * <p>1、以 "$" 开头表示该列为隐藏列，生成 EXCEL 时直接跳过；
	 * <p>2、包含 "#" 表示该列数据中值相同的行合并单元格；
	 * <p>3、包含 "^" 表示该标题单元格占多行，"^" 后的数字代表所占行数；
	 * <p>4、包含 "*" 表示该标题单元格占多列，"*" 后的数字代表所占列数；
	 * <p>5、标题单元格必须按照【"$" + 单元格内容 + "#" + "^" + "*"】的顺序，如："$SN"、"承办部门#^2*3"；
	 * <p>6、被合并的单元格用 "&amp;nbsp;" 作为单元格的值。
	 * @param data 数据行。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], String[][])
	 */
	public static void export(String filename, String sheetName, String[][] headers, String[][] data)
			throws IOException {
		OutputStream out = null;
		try {
			File file = ExcelUtil.createFile(filename, headers.length + data.length);
			out = new FileOutputStream(file);
			ExcelUtil.export(out, sheetName, headers, data);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成 EXCEL 文件并获得字节码，支持合并单元格。
	 * @param sheetName sheet表名
	 * @param headers 标题行，规则如下：
	 * <p>1、以 "$" 开头表示该列为隐藏列，生成 EXCEL 时直接跳过；
	 * <p>2、包含 "#" 表示该列数据中值相同的行合并单元格；
	 * <p>3、包含 "^" 表示该标题单元格占多行，"^" 后的数字代表所占行数；
	 * <p>4、包含 "*" 表示该标题单元格占多列，"*" 后的数字代表所占列数；
	 * <p>5、标题单元格必须按照【"$" + 单元格内容 + "#" + "^" + "*"】的顺序，如："$SN"、"承办部门#^2*3"；
	 * <p>6、被合并的单元格用 "&amp;nbsp;" 作为单元格的值。
	 * @param data 数据行。
	 * @return EXCEL 文件字节码。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 * @see #export(OutputStream, String, String[][], String[][])
	 */
	public static byte[] export(String sheetName, String[][] headers, String[][] data)
			throws IOException {
		byte[] bytes = null;
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ExcelUtil.export(out, sheetName, headers, data);
			out.flush();
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bytes;
	}
	
	/**
	 * 将工作簿导出至 HTTP 响应对象。
	 * @param wb 工作簿对象。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不含后缀）。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static void export(Workbook workbook, HttpServletRequest request, HttpServletResponse response, String filename)
			throws IOException {
		int rows = workbook instanceof XSSFWorkbook ? ExcelUtil.XLS_MAX_ROWS + 1 : 1;
		
		OutputStream out = null;
		try {
			ExcelUtil.resetResponseToDownload(request, response, filename, ExcelUtil.PAGE_ENCODING, rows);
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 将工作簿导出至指定路径。
	 * @param wb 工作簿对象。
	 * @param filename 指定路径。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static void export(Workbook wb, String filename) throws IOException {
		int rows = wb instanceof XSSFWorkbook ? ExcelUtil.XLS_MAX_ROWS + 1 : 1;
		
		OutputStream out = null;
		try {
			File file = ExcelUtil.createFile(filename, rows);
			out = new FileOutputStream(file);
			wb.write(out);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获得工作簿字节码。
	 * @param workbook 工作簿对象。
	 * @throws IOException 将工作簿输出到输出流时抛出异常。
	 */
	public static byte[] export(Workbook workbook) throws IOException {
		byte[] bytes = null;
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			workbook.write(out);
			out.flush();
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bytes;
	}
	
	/**
	 * 合并单元格。
	 * @param sheet Sheet 表对象。
	 * @param regionArray 合并区域数组，region 形如 "A1:E3" 表示合并从 A1 至 E3 的区域。
	 * @see #formatCellRangeAddress(String)
	 */
	public static void merge(Sheet sheet, String[] regionArray) {
		for (String region : regionArray) {
			sheet.addMergedRegion(ExcelUtil.formatCellRangeAddress(region));
		}
	}
	
	/**
	 * 合并单元格。
	 * @param sheet Sheet 表对象。
	 * @param regionArray 合并区域数组。
	 */
	public static void merge(Sheet sheet, CellRangeAddress[] regionArray) {
		for (CellRangeAddress region : regionArray) {
			sheet.addMergedRegion(region);
		}
	}
	
	/**
	 * 获取单元格的值。
	 * @param cell 单元格对象。
	 * @param type 数据类型。
	 * @return 单元格的值。
	 */
	public static Object getCellValue(Cell cell, String type) {
		if ("char".equals(type) || "java.lang.String".equals(type)) {
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue();
		}
		if ("short".equals(type) || "java.lang.Short".equals(type)) {
			cell.setCellType(CellType.STRING);
			String value = cell.getStringCellValue();
			return Short.parseShort(value);
		}
		if ("int".equals(type) || "java.lang.Integer".equals(type)) {
			cell.setCellType(CellType.STRING);
			String value = cell.getStringCellValue();
			return Integer.parseInt(value);
		}
		if ("long".equals(type) || "java.lang.Long".equals(type)) {
			cell.setCellType(CellType.STRING);
			String value = cell.getStringCellValue();
			return Long.parseLong(value);
		}
		if ("float".equals(type) || "java.lang.Float".equals(type)) {
			Double value = cell.getNumericCellValue();
			return Float.parseFloat(value.toString());
		}
		if ("double".equals(type) || "java.lang.Double".equals(type)) {
			return cell.getNumericCellValue();
		}
		if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
			return cell.getBooleanCellValue();
		}
		if ("java.util.Date".equals(type)) {
			return cell.getDateCellValue();
		}
		return null;
	}
	
	/**
	 * 复制单元格。
	 * @param srcCell 源单元格。
	 * @param destCell 目标单元格。
	 * @param onlyStyle 仅复制样式。
	 */
	public static void copyCell(Cell srcCell, Cell destCell, boolean onlyStyle) {
		destCell.setCellStyle(srcCell.getCellStyle());
		CellType cellType = srcCell.getCellTypeEnum();
		destCell.setCellType(cellType);
		if (onlyStyle) {
			return;
		}
		
		if (cellType.equals(CellType.BOOLEAN)) {
			destCell.setCellValue(srcCell.getBooleanCellValue());
			return;
		}
		if (cellType.equals(CellType.ERROR)) {
			destCell.setCellErrorValue(srcCell.getErrorCellValue());
			return;
		}
		if (cellType.equals(CellType.FORMULA)) {
			destCell.setCellFormula(parseFormula(srcCell.getCellFormula()));
			return;
		}
		if (cellType.equals(CellType.NUMERIC)) {
			destCell.setCellValue(srcCell.getNumericCellValue());
		}
		if (cellType.equals(CellType.STRING)) {
			destCell.setCellValue(srcCell.getRichStringCellValue());
			return;
		}
	}
	
	/**
	 * 复制行。
	 * @param srcRow 源行。
	 * @param destRow 目标行。
	 * @param colCnt 列数。
	 * @param onlyStyle 仅复制样式。
	 * @see #copyCell(Cell, Cell, boolean)
	 */
	public static void copyRow(Row srcRow, Row destRow, int colCnt, boolean onlyStyle) {
		destRow.setHeight(srcRow.getHeight());
		
		Cell srcCell, destCell;
		for (int i = 0; i < colCnt; i++) {
			srcCell = srcRow.getCell(i);
			destCell = destRow.getCell(i);
			if (destCell == null) {
				destCell = destRow.createCell(i);
			}
			ExcelUtil.copyCell(srcCell, destCell, onlyStyle);
		}
	}
	
	/**
	 * 转换函数字段。
	 * <p>解决 POI 的 bug ：如果公式里面的函数不带参数，比如 now() 或 today() ，则通过 getCellFormula() 取出来的值为
	 * now(ATTR(semiVolatile)) 和 today(ATTR(semiVolatile)) ，这样的值写入Excel时会出错。该方法的功能很简就是把
	 * ATTR(semiVolatile) 删掉。
	 * @param formula 公式。
	 * @return 转换后的值。
	 */
	public static String parseFormula(String formula) {
		final String REPLACE_STR = "ATTR(semiVolatile)";
		formula = formula.replaceAll(REPLACE_STR, "");
		return formula;
	}
	
	/**
	 * 把形如 "A1:E3" 形式的合并区域格式化成 {@link CellRangeAddress} 对象。
	 * @param region 形如 "A1:E3" 形式的合并区域。
	 * @return {@link CellRangeAddress} 对象。
	 */
	public static CellRangeAddress formatCellRangeAddress(String region) {
		int index = region.indexOf(":");
		int colStart = (int) (region.charAt(0)) - (int) ('A');
		int colEnd = (int) (region.charAt(index + 1)) - (int) ('A');
		int rowStart = Integer.parseInt(region.substring(1, index));
		int rowEnd = Integer.parseInt(region.substring(index + 2));
		
		CellRangeAddress cra = new CellRangeAddress(rowStart, rowEnd, colStart, colEnd);
		return cra;
	}
	
	/**
	 * 获得标题行单元格样式。
	 * @param wb 工作簿对象。
	 * @return {@link CellStyle} 对象。
	 */
	public static CellStyle getHeadStyle(Workbook workbook) {
		// 生成标题样式
		CellStyle headStyle = workbook.createCellStyle();
		// 设置样式
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setFillForegroundColor(HEADER_BACKGROUND_COLOR);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBold(false);
		font.setColor(HEADER_FONT_COLOR);
		headStyle.setFont(font);
		
		return headStyle;
	}
	
	/**
	 * 获得数据行单元格样式。
	 * @param wb 工作簿对象。
	 * @return {@link CellStyle} 对象。
	 */
	public static CellStyle getBodyStyle(Workbook workbook) {
		// 生成记录样式
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setAlignment(HorizontalAlignment.CENTER);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		bodyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyle.setWrapText(true);
		// 生成字体
		Font font2 = workbook.createFont();
		font2.setBold(false);
		// 把字体应用到当前的样式
		bodyStyle.setFont(font2);
		
		return bodyStyle;
	}
	
	/**
	 * 插入图片到单元格。
	 * <p><strong>注意：仅支持 .xls ，不支持 .xlsx 。</strong>
	 * @param filename excel 文件路径。
	 * @param sheetName sheet 表名。
	 * @param imageBytes 图片字节码。
	 * @param firstColIndex 起始列索引，从 0 开始。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastColIndex 结束列索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @throws IOException 打开文件时抛出异常。
	 */
	public static void insertImage(String filename, String sheetName, byte[] imageBytes,
			int firstColIndex, int firstRowIndex, int lastColIndex, int lastRowIndex) throws IOException {
		Workbook wb = null;
        FileOutputStream out = null;
		try {
			wb = ExcelUtil.open(filename);
			HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);
			// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	        // anchor主要用于设置图片的属性  
	        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255,
	        		(short) firstColIndex, firstRowIndex, (short) lastColIndex, lastRowIndex);     
	        anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
	        // 插入图片
	        patriarch.createPicture(anchor, wb.addPicture(imageBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
	        
			out = new FileOutputStream(filename);
	        // 写入excel文件
			wb.write(out);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 重置 HTTP 响应对象以下载。
	 * @param response HTTP 响应对象。
	 * @param filename 文件名（不含后缀）。
	 * @param charsetName 字符编码。
	 * @param rows 行数。
	 * @throws IOException
	 */
	public static void resetResponseToDownload(HttpServletRequest request, HttpServletResponse response,
			String filename, String charsetName, int rows) throws IOException {
		String contentType = "";
		if (rows <= ExcelUtil.XLS_MAX_ROWS) {
			filename += ".xls";
			contentType = "application/vnd.ms-excel";
		} else if (rows <= ExcelUtil.XLSX_MAX_ROWS) {
			filename += ".xlsx";
			contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml";
		} else {
			response.setHeader("Cache-Control", "no-cache");
		    response.setContentType("text/html;charset=UTF-8");
	    	response.getWriter().write("alert('数据行数 " + rows + " 超过 EXCEL 最大容量！')");
	    	return;
		}
		
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			filename = java.net.URLEncoder.encode(filename, "UTF-8");
		} else {
			filename = new String(filename.getBytes(charsetName), "ISO8859-1");
		}
		response.setContentType(contentType + ";charset=iso8859-1");
		response.setHeader("Content-disposition", "attachment; filename=" + filename);
	}
	
	/**
	 * 读取 EXCEL 文件获得 String[][] 数组。
	 * @param workbook 工作簿对象。
	 * @param firstRowIndex 起始行索引，从 0 开始。
	 * @param lastRowIndex 结束行索引，从 0 开始。
	 * @param firstColIndex 起始列索引，从 0 开始。
	 * @param lastColIndex 结束列索引，从 0 开始。
	 * @return String[][] 数组。
	 */
	private static String[][] xlsToStringArray(Workbook workbook, int firstRowIndex, int lastRowIndex, int firstColIndex, int lastColIndex) {
		String[][] array = new String[lastRowIndex - firstRowIndex + 1][lastColIndex - firstColIndex + 1];
		
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;
		for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
			row = sheet.getRow(rowIndex);
			
			for (int colIndex = firstColIndex; colIndex <= lastColIndex; colIndex++) {
				cell = row.getCell(colIndex);
				array[rowIndex - firstRowIndex][colIndex - firstColIndex] = cell.getStringCellValue();
			}
		}
		
		return array;
	}
	
	/**
	 * 创建文件。
	 * @param filename 文件路径。
	 * @throws IOException 创建文件时抛出异常。
	 */
	private static File createFile(String filename, int rows) throws IOException {
		if (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx")) {
			filename = filename.substring(0, filename.lastIndexOf("."));
			if (rows <= ExcelUtil.XLS_MAX_ROWS) {
				filename += ".xls";
			} else {
				filename += ".xlsx";
			}
		}
		
		File file = new File(filename);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	/**
	 * 把对象集合转成字符串二维数组。
	 * @param collection 对象集合，集合中为 Javabean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @return 字符串二维数组。
	 * @throws IOException
	 */
	private static String[][] convertCollectionToStringArray(Collection<?> dataSet, String dateFormat)
			throws IOException {
		List<String[]> lineList = new ArrayList<>();
		String[] line;
		int colCnt = 0;
		Iterator<?> it = dataSet.iterator();
		Object bean, value;
		Field[] fields;
		String[] fieldNames;
		Class<?>[] fieldTypes;
		Field field;
		String fieldName, methodName, type;
		Method getMethod;
		if (dateFormat == null || dateFormat.trim() == "") {
			dateFormat = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		while (it.hasNext()) {
			bean = it.next();
			if (bean == null) {
				continue;
			}

			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			fields = bean.getClass().getDeclaredFields();
			fieldNames = new String[fields.length];
			fieldTypes = new Class<?>[fields.length];
			for (int i = 0; i < fields.length; i++) {
				fieldNames[i] = fields[i].getName();
				fieldTypes[i] = fields[i].getType();
			}
			
			if (lineList.size() == 0) {
				colCnt = fields.length;
			} else if (colCnt != fields.length) {
				throw new IOException(COLUMN_COUNT_NOT_SAME);
			}
			
			line = new String[colCnt];
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];
				fieldName = field.getName();
				methodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				value = null;
				
				try {
					getMethod = bean.getClass().getMethod(methodName);
					value = getMethod.invoke(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (value == null) {
					line[i] = "";
					continue;
				}

				// 判断值的类型后进行强制类型转换
				type = fieldTypes[i].getName();
				if ("char".equals(type) || "java.lang.String".equals(type)) {
					line[i] = (String)value;
				} else if ("short".equals(type)
						|| "int".equals(type)
						|| "long".equals(type)
						|| "float".equals(type)
						|| "double".equals(type)
						|| value instanceof java.lang.Number) {
					line[i] = String.valueOf(value);
				} else if ("boolean".equals(type)
						|| "java.lang.Boolean".equals(type)) {
					line[i] = (Boolean)value ? "是" : "否";
				} else if ("java.util.Date".equals(type)) {
					line[i] = sdf.format((Date)value);
				}
			}
			lineList.add(line);
		}
		
		String[][] data = new String[lineList.size()][colCnt];
		for (int i = 0; i < lineList.size(); i++) {
			data[i] = lineList.get(i);
		}
		return data;
	}
	
	/**
	 * 导出 EXCEL 到输出流。
	 * @param out 输出流对象。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @throws IOException
	 * @see #createWorkbook(String, String[], String[][])
	 */
	private static void export(OutputStream out, String sheetName, String[] headers, String[][] data)
			throws IOException {
		Workbook wb = null;
		try {
			wb = ExcelUtil.createWorkbook(sheetName, headers, data);
			wb.write(out);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出 EXCEL 到输出流。
	 * @param out 输出流对象。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 javabean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException
	 * @see #convertCollectionToStringArray(Collection, String)
	 * @see #export(OutputStream, String, String[], String[][])
	 */
	private static void export(OutputStream out, String sheetName, String[] headers,
			Collection<?> collection, String dateFormat) throws IOException {
		String[][] data = ExcelUtil.convertCollectionToStringArray(collection, dateFormat);
		ExcelUtil.export(out, sheetName, headers, data);
	}
	
	/**
	 * 导出 EXCEL 到输出流，支持合并单元格。
	 * @param out 输出流对象。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @throws IOException
	 * @see #createWorkbook(String, String[][], String[][])
	 */
	private static void export(OutputStream out, String sheetName, String[][] headers, String[][] data)
			throws IOException {
		Workbook wb = null;
		try {
			wb = ExcelUtil.createWorkbook(sheetName, headers, data);
			wb.write(out);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成工作簿对象。
	 * sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @return 当 data 的长度小于等于 {@value #XLS_MAX_ROWS} 时，返回 {@link HSSFWorkbook} 对象；
	 *         否则返回 {@link XSSFWorkbook}对象。
	 */
	private static Workbook createWorkbook(String sheetName, String[] headers, String[][] data) {
		// 声明一个工作薄
		Workbook workbook = null;
		if (1 + data.length <= ExcelUtil.XLS_MAX_ROWS) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook();
		}
		// 生成一个表格
		Sheet sheet = workbook.createSheet(sheetName);
		// 生成标题样式
		CellStyle headStyle = ExcelUtil.getHeadStyle(workbook);
		// 生成记录样式
		CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);
		
		int i = 0, colCnt = 0;
		// 产生表格标题行
		if (headers != null) {
			Row row = sheet.createRow(i++);
			row.setHeight(DEFAULT_ROW_HEIGHT);
			Cell cell;
			RichTextString text;
			for (int j = 0; j < headers.length; j++) {
				if (headers[j].startsWith("$")) {	// 隐藏列
					continue;
				}
				
				cell = row.createCell(colCnt++);
				cell.setCellStyle(headStyle);
				if (1 + data.length <= ExcelUtil.XLS_MAX_ROWS) {
					text = new HSSFRichTextString(headers[j]);
				} else {
					text = new XSSFRichTextString(headers[j]);
				}
				cell.setCellValue(text);
				
				// 设置列宽
				sheet.setColumnWidth(j, DEFAULT_COLUMN_WIDTH);
			}
		}
		// 遍历集合数据，产生数据行
		int height = data.length;
		int width = height > 0 ? data[0].length : 0;
		Row row;
		Cell cell;
		for (int j = 0; j < height; j++) {
			row = sheet.createRow(i++);
			row.setHeight(DEFAULT_ROW_HEIGHT);
			colCnt = 0;
			for (int k = 0; k < width; k++) {
				if (headers[k].startsWith("$")) {	// 隐藏列
					continue;
				}
				
				cell = row.createCell(colCnt++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(data[j][k]);
			}
		}
		
		return workbook;
	}
	
	/**
	 * 生成 Excel 文件，支持合并单元格。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param data 数据行。
	 * @return 当 data 的长度小于等于 {@value #XLS_MAX_ROWS} 时，返回 {@link HSSFWorkbook} 对象；
	 *         否则返回 {@link XSSFWorkbook}对象。
	 */
	private static Workbook createWorkbook(String sheetName, String[][] headers, String[][] data) {
		// 声明一个工作薄
		Workbook workbook = null;
		if (headers.length + data.length <= ExcelUtil.XLS_MAX_ROWS) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook();
		}
		// 生成一个表格
		Sheet sheet = workbook.createSheet(sheetName);
		// 生成标题样式
		CellStyle headStyle = ExcelUtil.getHeadStyle(workbook);
		// 生成记录样式
		CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);
		
		List<CellRangeAddress> mergeList = new ArrayList<>();	// 合并单元格区域
		CellRangeAddress merge;
		int rowIndex = 0, colIndex;
		Row row;
		Cell cell;
		String[] line;
		String th, span;
		int rowspan, colspan;
		RichTextString text;
		
		// 产生表格标题行
		for (int i = 0; i < headers.length; i++) {
			row = sheet.createRow(rowIndex);
			row.setHeight(DEFAULT_ROW_HEIGHT);
			line = headers[i];
			colIndex = 0;
			for (int j = 0; j < line.length; j++) {
				th = line[j];
				if (th.startsWith("$")) {	// 隐藏列
					continue;
				}
				
				cell = row.createCell(colIndex);
				cell.setCellStyle(headStyle);
				if (rowIndex == 0) {	// 设置列宽
					sheet.setColumnWidth(colIndex, DEFAULT_COLUMN_WIDTH);
				}
				th = th.replaceAll("#", "");
				rowspan = 1;
				colspan = 1;
				if (th.contains("^")) {	// 多行
					span = "";
					if (th.contains("*")) {	// 多列
						span = th.substring(th.indexOf("^") + 1, th.indexOf("*"));
					} else if (th.indexOf("^") < th.length() - 1) {
						span = th.substring(th.indexOf("^") + 1);
					}
					rowspan = Tools.isEmpty(span) ? 1 : Integer.parseInt(span);
					if (rowspan > 1) {
						merge = new CellRangeAddress(rowIndex, rowIndex + rowspan - 1, colIndex, colIndex);
						mergeList.add(merge);
					}
				}
				if (th.contains("*")) {	// 多列
					colspan = th.indexOf("*") == th.length() - 1 ?
							1 : Integer.parseInt(th.substring(th.indexOf("*") + 1));
					if (colspan > 1) {
						merge = new CellRangeAddress(rowIndex, rowIndex, colIndex, colIndex + colspan - 1);
						mergeList.add(merge);
					}
				}
				
				if (th.contains("^")) {
					th = th.substring(0, th.indexOf("^"));
				} else if (th.contains("*")) {
					th = th.substring(0, th.indexOf("*"));
				}
				th = Tools.isEmpty(th) || th.contains("&nbsp;") ? "" : th;
				if (headers.length + data.length <= ExcelUtil.XLS_MAX_ROWS) {
					text = new HSSFRichTextString(th);
				} else {
					text = new XSSFRichTextString(th);
				}
				cell.setCellValue(text);
				colIndex++;
				
				while (colspan-- > 1) {
					cell = row.createCell(colIndex);
					cell.setCellStyle(headStyle);
					cell.setCellValue("");
					if (rowIndex == 0) {	// 设置列宽
						sheet.setColumnWidth(colIndex, DEFAULT_COLUMN_WIDTH);
					}
					colIndex++;
				}
			}
			rowIndex++;
		}
		
		// 遍历集合数据，产生数据行
		for (int i = 0; i < data.length; i++) {
			row = sheet.createRow(rowIndex++);
			row.setHeight(DEFAULT_ROW_HEIGHT);
			colIndex = 0;
			for (int j = 0; j < data[i].length; j++) {
				if (headers[headers.length - 1][j].startsWith("$")) {	// 隐藏列
					continue;
				}
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(data[i][j]);
			}
		}
		
		// 查询合并单元列
		String old, curr;
		int oldIndex, currIndex;
		colIndex = 0;
		for (int j = 0; j < headers[headers.length - 1].length; j++) {
			th = headers[headers.length - 1][j];
			if (th.startsWith("$")) {	// 隐藏列
				continue;
			}
			
			if (th.contains("#")) {	// 此列值相同的行合并单元格
				old = data[0][j];
				oldIndex = 0;
				curr = "";
				currIndex = 0;
				for (int i = 1; i < data.length; i++) {
					curr = data[i][j];
					currIndex = i;
					if (!curr.equals(old)) {
						if (currIndex - oldIndex > 1) {
							merge = new CellRangeAddress(oldIndex + headers.length, currIndex + headers.length - 1, colIndex, colIndex);
							mergeList.add(merge);
						}
						old = curr;
						oldIndex = currIndex;
					}
				}
				if (curr.equals(old) && currIndex - oldIndex > 1) {
					merge = new CellRangeAddress(oldIndex + headers.length, currIndex + headers.length, colIndex, colIndex);
					mergeList.add(merge);
				}
			}
			colIndex++;
		}
		
		// 合并单元格
		CellRangeAddress[] mergeArray = new CellRangeAddress[mergeList.size()];
		for (int i = 0; i < mergeList.size(); i++) {
			mergeArray[i] = mergeList.get(i);
		}
		ExcelUtil.merge(sheet, mergeArray);
		
		return workbook;
	}
	
	/**
	 * 导出 EXCEL 到输出流，支持合并单元格。
	 * @param out 输出流对象。
	 * @param sheetName sheet 表名。
	 * @param headers 标题行。
	 * @param collection 数据集合，集合中为 javabean 。
	 * @param dateFormat 由 yyyy 年、 MM 月、 dd 日、 HH 小时、 mm 分钟、 ss 秒构成的字符串。
	 * @throws IOException
	 * @see #convertCollectionToStringArray(Collection, String)
	 * @see #export(OutputStream, String, String[][], String[][])
	 */
	private static void export(OutputStream out, String sheetName, String[][] headers,
			Collection<?> collection, String dateFormat) throws IOException {
		String[][] data = ExcelUtil.convertCollectionToStringArray(collection, dateFormat);
		ExcelUtil.export(out, sheetName, headers, data);
	}
}
