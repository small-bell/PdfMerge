package com.smallbell.pdfmerge.utils;

import java.io.File;

/**
 * 文件删除操作工具类
 */
public class FileUtil {
	public static boolean delFiles(File file) {
		boolean result = false;
		if (file.isDirectory()) {
			File[] childrenFiles = file.listFiles();
			for (File childFile : childrenFiles) {
				result = delFiles(childFile);
				if (!result) {
					return result;
				}
			}
		}
		result = file.delete();
		return result;
	}
	
	public static void deleteTarget(String path) {
		File file = new File(path);
		delFiles(file);
		if(!file.exists()){
			file.mkdir();
		}
	}

	public static void main(String[] args) {
		
	}
}
