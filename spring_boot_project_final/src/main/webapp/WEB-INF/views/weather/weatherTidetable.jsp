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
							<li data-tab="/weather/tidetable" class="weatherCtg1 weatherCtg active" >물때</li>
							<li data-tab="/weather/current" class="weatherCtg2 weatherCtg" >현재 위치 날씨</li>
							<li data-tab="/weather/warning" class="weatherCtg3 weatherCtg" >기상 특보</li>
							<li data-tab="/weather/nationwide" class="weatherCtg4 weatherCtg" >전국 날씨</li>
							<li data-tab="/weather/tidetable" class="weatherCtg4 weatherCtg" >
								<a href="https://www.khoa.go.kr/swtc/main.do" target='_blank'>해상 날씨</a></li>
							<li data-tab="/weather/tidetable" class="weatherCtg5 weatherCtg" >
								<a href="https://www.weather.go.kr/w/ocean/forecast/daily-forecast.do" target='_blank'>바다 낚시 지수</a></li>
						</ul>
					</div>
					<div id="weatherMainBox">
						<div id="tidetableWrap">
							<div id="currentDayBox">
								<table id="currentDayInfo">
									<tr><th>오늘</th>
										<td>${lunarDate.year}년 ${lunarDate.month}월 ${lunarDate.day}일</td></tr>
									<tr><th>음력</th>
										<td>${lunarDate.lunYear}년 ${lunarDate.lunMonth}월 <span id="lunarDay">${lunarDate.lunDay}</span>일</td></tr>
									<tr><th>물때</th>
										<td><span id="tideResult"></span></td></tr>
									<script> 	
									 	/* 5. (시작) 물때에서 현재 날짜에 따른 물때 정보 공개 */
									 	let lunarDay = $('#lunarDay');
									 	let intLunar = parseInt($('#lunarDay'));
									 	let tideResult = document.getElementById('tideResult');
									 	
									 	if ((lunarDay.text() === "15") || (lunarDay.text() === "30")) { tideResult.innerHTML = "사리(6물)"; } 
									 	else if ((lunarDay.text() === "16") || (lunarDay.text() === "31") || (lunarDay.text() === "01")) { tideResult.innerHTML = "7물"; } 
									 	else if ((lunarDay.text() === "02") || (lunarDay.text() === "17")) { tideResult.innerHTML = "8물"; } 
									 	else if ((lunarDay.text() === "03") || (lunarDay.text() === "18")) { tideResult.innerHTML = "9물"; }  
									 	else if ((lunarDay.text() === "04") || (lunarDay.text() === "19")) { tideResult.innerHTML = "10물"; }  
									 	else if ((lunarDay.text() === "05") || (lunarDay.text() === "20")) { tideResult.innerHTML = "11물"; } 
									 	else if ((lunarDay.text() === "06") || (lunarDay.text() === "21")) { tideResult.innerHTML = "12물"; } 
									 	else if ((lunarDay.text() === "07") || (lunarDay.text() === "22")) { tideResult.innerHTML = "13물"; } 
									 	else if ((lunarDay.text() === "08") || (lunarDay.text() === "23")) { tideResult.innerHTML = "조금(14물)"; } 
									 	else if ((lunarDay.text() === "09") || (lunarDay.text() === "24")) { tideResult.innerHTML = "무시(15물)"; } 
									 	else if ((lunarDay.text() === "10") || (lunarDay.text() === "25")) { tideResult.innerHTML = "1물"; } 
									 	else if ((lunarDay.text() === "11") || (lunarDay.text() === "26")) { tideResult.innerHTML = "2물"; }   
									 	else if ((lunarDay.text() === "12") || (lunarDay.text() === "27")) { tideResult.innerHTML = "3물"; }   
									 	else if ((lunarDay.text() === "13") || (lunarDay.text() === "28")) { tideResult.innerHTML = "4물"; }   
									 	else if ((lunarDay.text() === "14") || (lunarDay.text() === "29")) { tideResult.innerHTML = "5물"; }      
									 	
									 	/* 5. 물때에서 현재 날짜에 따른 물때 정보 공개 (끝) */
								 	</script>
								</table>
							</div>
							<div id="tidetablebox">
								<table id="tidetableInfo">
									<tr><td colspan="4" class="tidetableTitle">
										<span>물때</span> (조선시대부터 사용한 조수순환 간이주기표) vs. <span>조석</span> (물높이 변화의 과학적 통계 자료)</td></tr>
									<tr><th>물때</th><th>조석</th><th>만조</th><th>간조</th></tr>
									<tr>
										<td class="tidetableMain">조석이 규칙적으로 음력 보름(15일)을 주기로 되풀이되는 것. 하루에 두 번씩 밀물과 썰물이 들어오고 나가고 , 물이 흐르는 속도와 들고 나는 물의 양</td>
										<td>밀물과 썰물을 통틀어 일컬으며, 해수면이 주기적으로 높아지고 낮아지는 시간. 약 5시간 40분 주기로 하루 두 차례 반복</td>
										<td>밀물이 가장 높은 해수면까지 꽉 차게 들어오는 때. 물이 멈춰있을 가능성이 높음</td>
										<td>바다에서 조수가 빠져나가 해수면이 가장 낮아진 때. 정조시간</td>
									</tr>
									<tr><td colspan="4" class="tidetableTitle"><span>물때</span> : 달과 해의 인력에 따른 바닷물 강약의 변화를 음력 날짜로 구분</td></tr>
									<tr><th>사리</th><th>조금</th><th>사리물때</th><th>조금물때</th></tr>
									<tr class="tidetableNum">
										<td>음력 15일, 음력 30일</td><td>음력 8일, 음력 23일</td>
										<td>사리, 사리 이후 2~3일</td><td>조금, 조금 이후 2~3일</td>
									</tr>
									<tr>
										<td>만조와 간조의 수위 차이가 커서 물이 세게 흐르는 시기. 해와 달의 인력이 해수면을 가장 많이 잡아당기는 날</td>
										<td>만조와 간조의 수위 차이가 작아 물의 흐름이 약한 시기. 해와 달의 인력이 해수면을 가장 적게 잡아당기는 날</td>
										<td>유속이 가장 빠르고 간조와 만조의 차이가 가장 큰 날</td>
										<td>유속이 가장 느리고 간조와 만조의 차이가 가장 적은 날</td>
									</tr>
									<tr><td colspan="4" class="tidetableTitle"><span>물때 각 시점 비교</span> : 장점과 단점</td></tr>
									<tr><th>사리물때</th><th>죽는물때</th><th>사는물때</th><th>조금물때</th></tr>
									<tr class="tidetableNum">
										<td>사리(6물), 7물, 8물, 9물</td><td>10물, 11물, 12물, 13물</td>
										<td>3물, 4물, 5물</td><td>조금(14물), 무시(15물), 1물, 2물</td>
									</tr>
									<tr>
										<td>유속이 빨라 물이 가장 흐리고 어군(떼고기)가 가장 많이 이동하는 날. 대박 노리기 좋은 날. 남해바다와 만조 수위가 깊고 간조에 물이 많이 빠져나가 조개잡이에 최적</td>
										<td>물고기가 아직 활동성이 있음. 유속이 안정적이고 물색이 유지되기에 안정적으로 조황 가능</td>
										<td>유속이 느려지고 물색이 맑아짐. 물이 흐린 해역(뻘물이 자주 생기는 곳)에서 선택하기 최적</td>
										<td>평소 유속이 센 곳에서는 오히려 기회. 서해바다와 배낚시, 돌섬, 갯바위 등에서 최적</td>
									</tr>
									<tr>
										<td>조류가 너무 세서 낚시 가능한 포인트나 시간이 제한적임. 평균해수면이 높은 여름에 해일 발생 위험성 증가. 비상시 퇴로 확보 필요. 갯바위에서는 어려움</td>
										<td>낚시 포인트에 따라 조황이 다름</td>
										<td>그나마 조금물때보다는 조황이 좋음</td>
										<td>조류가 약해서 물고기가 활동적이지 않음. 입질 기회가 적음</td>
									</tr>
								</table>
							</div>
							<div id="tidetableSource">
									<span>출처1 : <a href="https://tpirates.com/콘텐츠/3666/이걸-모르면-낚시에-성공할-수-없습니다">인어교주해적단</a></span> / 
									<span>출처2 : <a href="https://donggun0603.tistory.com/10">티스토리 - 어복PLUS</a></span>
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