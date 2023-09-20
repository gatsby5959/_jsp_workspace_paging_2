<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>게시글 삽입용 인풋페이지 Board Register Page</h1>
<form action="/brd/insert" method="post" enctype="multipart/form-data"> <%--이미지를 넣기 위해 ecntype추가 230919 --%>
	title제목:<input type="text" name="title"><br>
	writer작성자:<input type="text" name="writer" value="${ses.id}" readonly="readonly"><br>
	content내용:<textarea rows="5" cols="60" name="content"></textarea><br>
	image_file: <input type="file" name = "image_file" accept="image/png, image/jpg, image/jpeg, image/gif"> <br> <%--이미지 230919 --%> 
	<button type="submit">등록버튼</button>
</form>
</body>
</html>