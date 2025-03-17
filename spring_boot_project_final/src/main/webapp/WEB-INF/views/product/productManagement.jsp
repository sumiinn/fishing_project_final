<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>상품관리</title>
		<!-- head.jsp import -->
		<c:import url = "/WEB-INF/views/layout/head.jsp"></c:import>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/productManagement.css'/>" />
		<script src="<c:url value='/js/productManagement.js'/>"></script>
	</head>
	<body>
		<div id="wrap">
			<!-- top.jsp import -->
			<c:import url = "/WEB-INF/views/layout/top.jsp"></c:import>
			<section id="product-management">
				<h1>상품 관리</h1>
				
				<!-- 제품 목록 섹션 -->
				<div id="product-list">
					<div class="table-header">
             <div class="table-title">
                 <h2>제품 목록</h2>
             </div>
             <div class="table table-button">
                 <button class="btn btn-add" onclick="location.href='/product/insertProductForm?bizId=${bizId}'">제품 등록</button>
             </div>
         	</div>
					<table class="table productList">
						<thead>
							<tr>
								<th>제품 번호</th>
								<th>제품명</th>
								<th>가격</th>
								<th>재고</th>
								<th>카테고리</th>
								<th>제조사</th>
								<th>설명</th>
								<th>이미지</th>
								<th>수정/삭제</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="product" items="${prdList}">
								<tr>
									<td>${product.prdNo}</td>
									<td>${product.prdName}</td>
									<td>${product.prdPrice}</td>
									<td>${product.prdStock}</td>
									<td>${product.prdCtgName}</td>
									<td>${product.prdMaker}</td>
									<td>${product.prdDescript}</td>
									<td><img src="<c:url value='/project_images/${product.prdImage}'/>"
																alt="${product.prdName}"width="40px" height="40px">
									<td>
										<button type="button" class="btn" onclick="location.href='<c:url value='/product/updateProductForm/${product.prdNo}?bizId=${product.bizId}'/>'">
														수정</button>       
                    <button type="button" class="btn" onclick="deleteCheck('${product.prdNo}', '${product.bizId}');">삭제</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
	
				<!-- 주문 내역 섹션 -->
            <div id="order-history">
                <h2>주문 내역</h2>
                <table class="table orderList">
                    <thead>
                        <tr>
                            <th>주문 번호</th>
                            <th>주문 날짜</th>
                            <th>제품 번호</th>
                            <th>수량</th>
                            <th>받는 사람</th>
                            <th>전화번호</th>
                            <th>주소</th>
                            <th>메시지</th>
                            <th>처리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orderHistory}">
                            <tr>
                                <td>${order.ordNo}</td>
                                <td><fmt:parseDate value="${order.ordDate}" pattern="yyyy-MM-dd HH:mm" var="parsedDate"/>
                    								<fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td>${order.prdNo}</td>
                                <td>${order.ordQty}</td>
                                <td>${order.ordRcvReceiver}</td>
                                <td>${order.ordRcvPhone}</td>
                                <td>${order.ordRcvAddress1} ${order.ordRcvAddress2}</td>
                                <td>${order.ordRcvMsg}</td>
                                <td><button class="btn">처리</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
			</section>
			<!-- bottom.jsp import -->
			<c:import url = "/WEB-INF/views/layout/bottom.jsp"></c:import>		
		</div>
	</body>
</html>