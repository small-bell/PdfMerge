package com.smallbell.pdfmerge.utils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 * 合并pdf工具类
 */
public class PdfMergeUtil {
	public static void merge(String workpath) throws IOException {
		PDFMergerUtility mergePdf = new PDFMergerUtility();

        String destinationFileName = "merge.pdf";
       
        File baseFile = new File(workpath);
        File[] filesInFolder = list(baseFile);
        for (int i = 0; i < filesInFolder.length; i++) {
        	if (!filesInFolder[i].getName().endsWith(".pdf")) {
				continue;
			}
            mergePdf.addSource(workpath + filesInFolder[i].getName());
        }
        mergePdf.setDestinationFileName(workpath + destinationFileName);
        mergePdf.mergeDocuments();
	}
	
	 public static File[] list(File file) {
		 if (file.isDirectory()) {
			 File [] lists = file.listFiles();
			 return lists;
		 }
		return null;
	}
	 
	public static void main(String[] args) throws IOException {
		String workpath = "C:\\Users\\Administrator\\Desktop\\test\\";
		merge(workpath);
	}

	

}
