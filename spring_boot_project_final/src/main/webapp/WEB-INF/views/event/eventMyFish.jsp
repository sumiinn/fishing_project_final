<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<title>어사모: 이벤트: 크기 겨루기</title>
		<c:import url = "/WEB-INF/views/layout/head.jsp"></c:import>
		<link rel="stylesheet"   type="text/css"  href="<c:url value='/css/event.css'/>">
		<script src="<c:url value='/js/event.js'/>"></script>
	</head>
	<body>
		<div id="wrap">
			<!-- top.jsp import -->
			<c:import url = "/WEB-INF/views/layout/top.jsp"></c:import>
			
			<section>
				<div class="eventWrap">
					<div id="eventCtgBox">
						<ul id="eventCtgList" class="eventCtgBar">
							<li data-tab="/event/myFish" class="eventCtg1 eventCtg active" >회원 낚시갤러리 크기 겨루기</li>
						</ul>
					</div>
					<div id="eventMainBox">
						<div id="eventMyFishPrizeBox">
							<div class="prizeInfo">
								<div class="prizeTitle">올해 <span>${year}</span>년 랭킹 보상</div>
								<div class="prizeTableBox">
									<table class="prizeTable">
										<tr><td>순위</td><td>경품</td></tr>
										<c:forEach items="${yearEvent}" var="yearEvent">
											<tr>
												<th>${yearEvent.prizeRank}등</th>
												<th>${yearEvent.prizeContent}</th>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div> 
							<div class="prizeInfo">
								<div class="prizeTitle">이번 달 <span>${month}</span>월 랭킹 보상</div>
								<div class="prizeTableBox">
									<table class="prizeTable">
										<tr><td>순위</td><td>경품</td></tr>
										<c:forEach items="${monthEvent}" var="monthEvent">
											<tr>
												<th>${monthEvent.prizeRank}등</th>
												<th>${monthEvent.prizeContent}</th>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
						</div>
						<div id="eventMyFishListBox">
							<div id="myFishTotalBox">
								<div class="myFishTotalList">
									<div class="myFishTitle">${year}년 순위</div>
									<div class="myFishItem">
										<table>
											<c:forEach items="${yearMyFish}" var="yearMyFish" begin="0" end="10" step="1" varStatus="status">
												<tr>
													<td>${status.count}등</td>
													<td>${yearMyFish.fishSize}</td>
													<td>${yearMyFish.memNickname}</td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</div>
								<div class="myFishTotalList">
									<div class="myFishTitle">올해 ${month}월 순위</div>
									<div class="myFishItem">
										<table>
											<c:forEach items="${monthMyFish}" var="monthMyFish" begin="0" end="10" step="1" varStatus="status">
												<tr>
													<td>${status.count}등</td>
													<td>${monthMyFish.fishSize}</td>
													<td>${monthMyFish.memNickname}</td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</div>
							</div>
							<div id="myFishDetailBox">
								<div id="myFishContainer">
									<c:forEach items="${monthMyFish}" var="monthMyFish">
										<div class="myFishGrid" onclick="location.href='<c:url value='/myFishRecords/detailViewmyFishRecords/${monthMyFish.recordNo}' />'">
											<div class="gridImage">
												<img alt="물고기 기록 이미지" src="<c:url value='/project_images/${monthMyFish.uploadImage}'/>" >
											</div>
											<div class="gridInfo">
												<table>
													<tr><td>${monthMyFish.fishName}</td>
															<td>${monthMyFish.fishSize}</td><td></td></tr>
													<tr><td colspan="2">${monthMyFish.location}</td>
															<td>${monthMyFish.memNickname}</td></tr>
												</table>
											</div>
										</div>
									</c:forEach>
								</div>
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