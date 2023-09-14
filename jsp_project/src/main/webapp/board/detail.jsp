<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
</head>
<body>
	<h1>여긴 디테일 페이지입니다.</h1>
	<table border=1 class="table table-dark">
		<tr>
			<th>BNO</th>
			<td>${bvo.bno}</td>
		</tr>
		<tr>
			<th>TITLE</th>
			<td>${bvo.title}</td>
		</tr>
		<tr>
			<th>WRITER</th>
			<td>${bvo.writer}</td>
		</tr>
		<tr>
			<th>CONTENT</th>
			<td>${bvo.content}</td>
		</tr>
		<tr>
			<th>REGDATE</th>
			<td>${bvo.regdate}</td>
		</tr>
		<tr>
			<th>MODDATE</th>
			<td>${bvo.moddate}</td>
		</tr>
	</table>
	<a href="/brd/modify?bno=${bvo.bno}"><button class="btn btn-outline-success">수정버튼</button></a>
	<a href="/brd/remove?bno=${bvo.bno}"><button class="btn btn-outline-secondary">삭제버튼</button></a>
	
	
	<!-- 일단주석처리 안씀 -->
	<!-- <br> -->
	<!-- <br> -->
	<!-- <br> -->
	<!-- <table border=1 class="table table-dark"> -->
	<!-- <tr> -->
	<!--  	<th>댓글 작성자</th> -->
	<!--  	<td> 내용 </td> -->
	<!--  	<td>댓글 작성일</td> -->
	<!-- </tr> -->
	<!-- <tr> -->
	<!--  	<th>홍길동</th> -->
	<!--  	<td> 아버지를 아버지라 부르지 못해서 짜증나요 못해먹겠다 </td> -->
	<!--  	<td>1499년 5월 12일 세종15년</td> -->
	<!-- </tr> -->
	<!-- <tr> -->
	<!--  	<th>이순신</th> -->
	<!--  	<td> 내 죽음을 알리지 말라 </td> -->
	<!--  	<td>1592년 5월 12일 선조12년</td> -->
	<!-- </tr> -->
	<!-- <tr> -->
	<!--  	<th>장수왕</th> -->
	<!--  	<td>평양이 좋아용</td> -->
	<!--  	<td>552년 11월 19일</td> -->
	<!-- </tr> -->
	<!-- <tr> -->
	<!--  	<th>계백</th> -->
	<!--  	<td>전라도에 놀라오세요</td> -->
	<!--  	<td>652년4월22일</td> -->
	<!-- </tr> -->
	<!-- </table> -->
	
</body>
</html>