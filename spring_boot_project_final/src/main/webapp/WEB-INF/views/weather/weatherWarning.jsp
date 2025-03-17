<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<title>어사모: 날씨 정보: 기상 특보</title>
		<c:import url = "/WEB-INF/views/layout/head.jsp"></c:import>
		<link rel="stylesheet"   type="text/css"  href="<c:url value='/css/weather.css'/>">
		<script src="<c:url value='/js/weather.js'/>"></script>
	</head>
	<body>
		<div id="wrap">
			<!-- top.jsp import -->
			<c:import url = "/WEB-INF/views/layout/top.jsp"></c:import>
			
			<section>
				<div class="weatherWrap">
					<div id="weatherCtgBox">
						<ul id="weatherCtgList" class="weatherCtgBar">
							<li data-tab="/weather/tidetable" class="weatherCtg1 weatherCtg" >물때</li>
							<li data-tab="/weather/current" class="weatherCtg2 weatherCtg" >현재 위치 날씨</li>
							<li data-tab="/weather/warning" class="weatherCtg3 weatherCtg active" >기상 특보</li>
							<li data-tab="/weather/nationwide" class="weatherCtg4 weatherCtg" >전국 날씨</li>
							<li data-tab="/weather/warning" class="weatherCtg4 weatherCtg" >
								<a href="https://www.khoa.go.kr/swtc/main.do" target='_blank'>해상 날씨</a></li>
							<li data-tab="/weather/warning" class="weatherCtg5 weatherCtg" >
								<a href="https://www.weather.go.kr/w/ocean/forecast/daily-forecast.do" target='_blank'>바다 낚시 지수</a></li>
						</ul>
					</div>
					<div id="weatherMainBox">
						<div id="warningBox">
							<div class="warningImg">
								<img src="${warningList['img']}" alt="기상 특보 이미지">
							</div>
							<div class="warningTable">
								<table>
									<thead>
										<tr><th>${warningList["title"]}</th></tr>
									</thead>
									<tbody>
										<c:forEach items="${content}" var="content" varStatus="status"	> 
											<tr>
												<td><span class="subtitle">${subtitle[status.index]}</span>
														<span>${content}</span>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</section>
        
			<!-- bottom.jsp import -->
			<c:import url = "/WEB-INF/views/layout/bottom.jsp"></c:import>
		</div>
	</body>
</html>