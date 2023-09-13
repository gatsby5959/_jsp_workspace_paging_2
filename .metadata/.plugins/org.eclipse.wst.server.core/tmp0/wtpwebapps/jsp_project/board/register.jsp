<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>게시글 삽입용 인풋페이지</h1>
<form action="/brd/insert" method="post">
	title제목:<input type="text" name="title"><br>
	writer작성자:<input type="text" name="writer" value="${ses.id}" readonly="readonly"><br>
	contents내용:<textarea rows="5" cols="60" name="contents"></textarea><br>
	<button type="submit">등록버튼입니다.</button>
</form>
</body>
</html>