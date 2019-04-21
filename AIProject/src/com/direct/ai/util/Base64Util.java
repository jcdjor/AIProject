package com.direct.ai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;


public class Base64Util {
	
	/**
	 * Servlet Part转Base64
	 * 
	 * @param part
	 * @return
	 */
	public static String part2Base64(Part part) {
		String image = null;
		InputStream is = null;
		try {
			is = part.getInputStream();
			byte[] osb = new byte[(int) part.getSize()];
			is.read(osb);
			// 转为Base64
			image = com.baidu.aip.util.Base64Util.encode(osb);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return image;
	}
	
	/**
	 * 文件转base64
	 * 
	 * @param filename
	 * @return
	 */
	public static String file2Base64(String filename) {
		File file = new File(filename);
		return file2Base64(file);
	}
	
	/**
	 * 文件转base64
	 * 
	 * @param file
	 * @return
	 */
	public static String file2Base64(File file) {
		String image = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] osb = new byte[(int) file.length()];
			fis.read(osb);
			// 转为Base64
			image = com.baidu.aip.util.Base64Util.encode(osb);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return image;
	}
}
