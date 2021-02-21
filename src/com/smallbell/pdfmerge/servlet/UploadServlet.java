package com.smallbell.pdfmerge.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.smallbell.pdfmerge.utils.FileUtil;
import com.smallbell.pdfmerge.utils.PdfMergeUtil;
import com.smallbell.pdfmerge.utils.ZipUtil;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 上传文件存储目录
	private static final String UPLOAD_DIRECTORY = "C:\\Users\\Administrator\\Desktop\\test";

	// 上传配置
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	public UploadServlet() {
		super();
	}

	/**
	 * 下載合併好的文件
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("content-Type", "application/octet-stream");
		response.setHeader("content-disposition", 
				"attachment;fileName="+URLEncoder.encode("merge.pdf", "UTF-8"));
		InputStream in = new FileInputStream(UPLOAD_DIRECTORY + "\\merge.pdf");
		ServletOutputStream out = response.getOutputStream();
		byte[] bs = new byte[1024];
		int len = -1;
		while ((len = in.read(bs)) != -1) {
			out.write(bs, 0, len);
		}
		out.close();
		in.close();
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * 先清空目标文件夹然后进行上传合并
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 先清空文件夾
		FileUtil.deleteTarget(UPLOAD_DIRECTORY + "\\");
		// 检测是否为多媒体上传
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error: 上传格式错误");
			writer.flush();
			return;
		}

		// 配置上传参数
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);

		upload.setFileSizeMax(MAX_FILE_SIZE);

		upload.setSizeMax(MAX_REQUEST_SIZE);

		upload.setHeaderEncoding("UTF-8");

		String uploadPath = UPLOAD_DIRECTORY;

		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		String filePath = "";
		try {
			List<FileItem> formItems = upload.parseRequest(request);

			if (formItems != null && formItems.size() > 0) {
				for (FileItem item : formItems) {
					if (!item.isFormField()) {
						String fileName = new File(item.getName()).getName();
						filePath = uploadPath + File.separator + fileName;
						File storeFile = new File(filePath);
						// 在控制台输出文件的上传路径
						System.out.println(filePath);
						// 保存文件到硬盘
						item.write(storeFile);
						request.setAttribute("message", "文件上传成功!");
					}
				}
			}
		} catch (Exception ex) {
			request.setAttribute("message", "错误信息: " + ex.getMessage());
		}

		try {
			ZipUtil.unZipFiles(filePath, UPLOAD_DIRECTORY + "\\");
			PdfMergeUtil.merge(UPLOAD_DIRECTORY + "\\");
		} catch (Exception e) {
			request.setAttribute("message", "解压或合并失败");
		}
		

		// 跳转到 message.jsp
		getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
	}

}
