<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<title>어사모: 날씨 정보</title>
		<c:import url = "/WEB-INF/views/layout/head.jsp"></c:import>
		<link rel="stylesheet"   type="text/css"  href="<c:url value='/css/weather.css'/>">
		<script src="<c:url value='/js/weather.js'/>"></script>
		<script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=sio4q1ij5f"></script>
		<script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?clientId=sio4q1ij5f&submodules=geocoder"></script>
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
							<li data-tab="/weather/current" class="weatherCtg2 weatherCtg active" >현재 위치 날씨</li>
							<li data-tab="/weather/warning" class="weatherCtg3 weatherCtg" >기상 특보</li>
							<li data-tab="/weather/current" class="weatherCtg4 weatherCtg" >
								<a href="https://www.khoa.go.kr/swtc/main.do" target='_blank'>해상 날씨</a></li>
							<li data-tab="/weather/current" class="weatherCtg5 weatherCtg" >
								<a href="https://www.weather.go.kr/w/ocean/forecast/daily-forecast.do" target='_blank'>바다 낚시 지수</a></li>
						</ul>
					</div>
					<div id="weatherMainBox">
						<div id="weatherInfoBox">
							<div id="weatherDetailBox">
								<table>
									<tr><th>현재 위치</th><td class="item1"></td></tr>
									<!-- <tr><th>위도</th><td class="item2"></td></tr> -->
									<!-- <tr><th>경도</th><td class="item3"></td></tr> -->
									<tr><th>일출 시간</th><td class="item4"></td></tr>
									<tr><th>일몰 시간</th><td class="item9"></td></tr>
									<tr><th>현재 온도</th><td class="item5"></td></tr>
									<tr><th>체감 온도</th><td class="item10"></td></tr>
									<tr><th>날씨</th><td class="item6"></td></tr>
									<tr><th>습도</th><td class="item7"></td></tr>
									<tr><th>바람</th><td class="item8"></td></tr>
								</table>
							</div>
							<div id="weatherMapWrap" style="display:none;">
								<div id="weatherMapBox"></div>
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