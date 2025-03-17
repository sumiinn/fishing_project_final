<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
	<div id="headerBox">
		<div id="topMenuWrap" class="topMenuBoth">
			<div id="topMenuLeftBox">
				<div id="logoBox">
					<a href="<c:url value='/'/>" id="logoItem">
						<img alt="로고 이미지" src="/project_images/logo.png" id="logoImg">
					</a>
				</div> <!-- logoBox 종료 -->
				<c:if test="${not empty sessionScope.sid }">
				<div id="infoMenuBox">
					<a href="<c:url value='/location/overview'/>">
						<span class="infoMenuItem">현재 나의 위치</span></a>
					<a href="<c:url value='/weather/current'/>">
						<span class="infoMenuItem">현재 날씨</span></a>
					<a href="#" onclick="searchFish()">
						<span class="infoMenuItem">물고기 찾기</span></a>
				</div> <!-- infoMenuBox 종료 -->
				</c:if>
			</div> <!-- topMenuLeftBox 종료 -->
			<div id="topMenuRightBox">
				<!-- 로그인하지 않은 경우 : 로그인, 회원가입만 보여줌 -->
				<c:if test="${empty sessionScope.sid }">
					<a href="<c:url value='/member/loginForm'/>">로그인</a>
					<a href="<c:url value='/member/joinForm'/>">회원가입</a>
				</c:if>
				<!-- 로그인 성공하는 경우 : 로그아웃, 장바구니, 마이페이지, 게시판 등 보여줌 -->
				<c:if test="${not empty sessionScope.sid }">
					<!-- ${sessionScope.sid}님 환영합니다!  -->
					<c:if test="${sessionScope.memType eq 'admin'}">
						<a href="<c:url value='/admin/adminPage'/>">관리자 페이지</a>
					</c:if>
					<c:if test="${sessionScope.memType eq 'business'}">
						<a href="<c:url value='/business/businessMain'/>">사업자 페이지</a>
					</c:if>
					<a href="<c:url value='/dashboard'/>">예약</a>
					<a href="<c:url value='/myFishRecords/myFishRecordsListView'/>">낚시갤러리</a>
					<a href="<c:url value='/myPage/cartList'/>">장바구니</a>
					<a href="<c:url value='/member/myPage'/>">마이페이지</a>
					<a href="<c:url value='/member/userInterestTag'/>">낚시 취향</a>
					<c:if test="${sessionScope.memType eq 'general'}">
						<a href="<c:url value='/memberOcr/businessAuth'/>">사업자등록</a>
					</c:if>
					<a href="<c:url value='/member/logout'/>">로그아웃</a>
				</c:if>			
			</div> <!-- topMenuRightBox 종료 -->
		</div>
		<div id="searchWrap" class="topMenuBoth">
			<div id="titleBox">
				<a href="<c:url value='/'/>">
					<span class="infoMenuItem">물고기를 사랑하는 모임</span></a>
			</div>
			<div id="searchBox">
				<form id="topSearch" class="topSearch" name="topSearch" method="get"
						  action="<c:url value='/searchResult'/>">
					<input type="text" id="searchTxt" name="searchTxt"
							   placeholder="검색어를 입력하세요">
					<button type="submit" name="submitBtn" class="hideBtn">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</form>
			</div> 
		</div> <!-- searchWrap 종료 -->
	</div>
</header> <!-- header 끝 -->

<nav>
	<div class="topMenuBoth" id="mainMenuBox" >
		<ul class="mainMenuItem" id="mainMenuItem" >
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg1">실시간 예약</div>
				<div class="subMainMenu" id="subListBox1">
					<div class="subContainerBox">
						<div class="subListBox">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishingSpot/freshwater'/>">민물 낚시</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishingSpot/saltwater'/>">바다 낚시</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishingSpot/freshwater'/>">실내 낚시</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg2"><a href="<c:url value='/fishing/fishingCtgList/1/1001'/>">낚시 정보</a></div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox2">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishing/fishingCtgList/1/1001'/>">초보자 가이드</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishing/fishingCtgList/2/2001'/>">낚시 노하우</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishing/fishingCtgList/3/3001'/>">장비 사용법</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fishing/fishingCtgList/4/4001'/>">실전 스킬</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg3"><a href="<c:url value='/fish'/>">물고기 정보</a></div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox3">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fish'/>">전체 어종</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value=' /fish/fishCtgList/3'/>">일반 어종</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fish/fishCtgList/1'/>">인기 어종</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/fish/fishCtgList/2'/>">희귀 어종</a></li>
								<li class="subListItem"><a class="subListTxt" >해외 어종</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg4">구매 장터</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox4">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/product/productList'/>">낚시 용품</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/freeboard/fleamarketList/0'/>">벼룩시장</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg5">날씨 정보</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox5">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/weather/tidetable'/>">물때</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/weather/current'/>">현재 위치 날씨</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/weather/warning'/>">기상 특보</a></li>
								<li class="subListItem"><a class="subListTxt" href="https://www.khoa.go.kr/swtc/main.do" target='_blank'>해상 낚시</a></li>
								<li class="subListItem"><a class="subListTxt" href="https://www.weather.go.kr/w/ocean/forecast/daily-forecast.do" target='_blank'>바다 낚시 지수</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg6">위치 정보</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox6">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/location/overview'/>">현재 위치</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/location/nearFish'/>">근처 낚시터</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/location/nearFood'/>">주변 정보</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/location/traffic'/>">교통 상황</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/location/nationwide'/>">전국 낚시터</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg7">안전 정보</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox7">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/safety/overview'/>">안전 수칙</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/safety/ERinfo'/>">비상 연락처</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg8">이벤트 정보</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox8">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/event/myFish'/>">크기 겨루기</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
			<li class="mainMenuList">
				<div class="mainMenuCtg" id="mainMenuCtg9">커뮤니티</div>
				<div class="subMainMenu">
					<div class="subContainerBox">
						<div class="subListBox" id="subListBox9">
							<ul class="subList">
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/freeboard/freeboardView/2'/>">자유게시판</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/freeboard/freeboardView/1'/>">공지사항</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/freeboard/freeboardView/3'/>">자주묻는 질문</a></li>
								<li class="subListItem"><a class="subListTxt" href="<c:url value='/freeboard/freeboardView/4'/>">고객센터</a></li>
							</ul>
						</div>
					</div>
				</div>
			</li>
		</ul>
	</div> 
</nav> <!-- nav 끝 -->