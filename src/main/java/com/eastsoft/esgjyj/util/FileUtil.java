package com.eastsoft.esgjyj.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作工具类，对 {@link org.apache.commons.io.FileUtils} 的补充。
 * @author Ben
 * @since 0.0.1-SNAPSHOT
 * @version 1.0.3, 2017/6/1
 */
public class FileUtil {
	/**
	 * 下载文件。
	 * @param request HTTP 请求对象。
	 * @param response HTTP 响应对象。
	 * @param bytes 文件字节码。
	 * @param filename 文件名。
	 * @param charsetName 字符编码。
	 * @throws IOException
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename, String charsetName)
			throws IOException {
		if (filename == null || filename.trim().length() == 0) {
			filename = "file";
		}
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			filename = java.net.URLEncoder.encode(filename, "UTF-8");
		} else {
			filename = new String(filename.getBytes(charsetName), "ISO8859-1");
		}
		
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			throw e;
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
	 * 创建目录。
	 * @param pathname 目录路径。
	 * @return 创建成功时返回文件对象，否则返回 NULL 。
	 */
	public static File createDirectory(String pathname) {
		File dir = new File(pathname);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				return null;
			}
		}
		return dir;
	}
	
	/**
	 * 递归删除目录。
	 * @param pathname 目录路径。
	 * @return 是否成功。
	 */
	public static boolean deleteDirectory(String pathname) {
		// 路径不存在
		File file = new File(pathname);
		if (!file.exists()) {
			return true;
		}
		
		// 指定路径是文件
		if (file.isFile()) {
			boolean flag = file.delete();
			return flag;
		}
		
		// 指定路径是目录，递归删除子目录
		File[] files = file.listFiles();
		for (File tmp : files) {
			if (!deleteDirectory(tmp.getAbsolutePath())) {
				return false;
			}
		}
		// 删除根目录
		boolean flag = file.delete();
		return flag;
	}
	
	/**
	 * 创建文件。
	 * @param pathname 文件路径。
	 * @return 创建成功时返回文件对象，否则返回 NULL 。
	 * @throws IOException 创建文件时抛出异常。
	 */
	public static File createFile(String pathname) throws IOException {
		File file = new File(pathname);
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return null;
			}
		}
		if (!file.exists()) {
			if (!file.createNewFile()) {
				return null;
			}
		}
		return file;
	}
	
	/**
	 * 删除文件。
	 * @param pathname 文件路径。
	 */
	public static final boolean deleteFile(String pathname) {
		File file = new File(pathname);
		if (!file.exists()) {
			return true;
		}
		boolean flag = file.delete();
		return flag;
	}
}