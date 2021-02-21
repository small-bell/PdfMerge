<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>message</title>
</head>
<body>
	<h2>${message}</h2>
	<form method="get" action="/PdfMerge/UploadServlet">
	   	<input type="submit" value="下载" />
	</form>
</body>
</html>