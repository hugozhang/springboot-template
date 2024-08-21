package com.winning.hmap.portal.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;


public class UpDownloadUtils {

	public static void toDownload(HttpServletRequest request,HttpServletResponse response, File downFiles, String webshowName) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		try {
			fis = Files.newInputStream(downFiles.toPath());
			bis = new BufferedInputStream(fis);
			fos = response.getOutputStream();
			bos = new BufferedOutputStream(fos);
			setFileDownloadHeader(request, response, webshowName);
			int byteRead = 0;
			byte[] buffer = new byte[8192];
			while ((byteRead = bis.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, byteRead);
			}

			bos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fis.close();
			bis.close();
			fos.close();
			bos.close();
		}
	}

	private static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
											  String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + encodedfileName );
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
