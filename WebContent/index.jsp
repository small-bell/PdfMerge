<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>上传文件</title>
</head>
<body>
	<form method="post" action="/PdfMerge/UploadServlet" enctype="multipart/form-data">
	   	选择一个文件:
	    <input type="file" name="uploadFile" />
	    <br/><br/>
	   	<input type="submit" value="上传" />
	</form>
</body>
</html>