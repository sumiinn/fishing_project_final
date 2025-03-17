<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<title>어사모: 위치 정보: 교통정보 확인</title>
		<c:import url = "/WEB-INF/views/layout/head.jsp"></c:import>
		<link rel="stylesheet"   type="text/css"  href="<c:url value='/css/location.css'/>">
		<script src="<c:url value='/js/location.js'/>"></script>
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c6d5e3c470f3b7c1de98371694a6fef7&libraries=services"></script>
	</head>
	<body>
		<div id="wrap">
			<!-- top.jsp import -->
			<c:import url = "/WEB-INF/views/layout/top.jsp"></c:import>
			
			<section>
				<div class="locationWrap">
					<div id="locationCtgBox">
						<ul id="locationCtgList" class="locCtgBar">
							<li data-tab="/location/overview" class="locCtg1 locCtg active" >현재 위치</li>
							<li data-tab="/location/nearFish" class="locCtg2 locCtg" >근처 낚시터</li>
							<li data-tab="/location/nearFood" class="locCtg3 locCtg" >주변 먹거리</li>
							<li data-tab="/location/nearPlay" class="locCtg4 locCtg" >주변 놀거리</li>
							<li data-tab="/location/nearHotel" class="locCtg5 locCtg" >주변 숙박시설</li>
							<li data-tab="/location/traffic" class="locCtg6 locCtg" >교통 상황</li>
							<li data-tab="/location/nationwide" class="locCtg7 locCtg" >전국 낚시터</li>
						</ul>
					</div>
					<div class="mapBox">
						<div class="map_wrap">
						    <div id="map" style="width:100%;height:700px;position:relative;overflow:hidden;"></div>
						    <div id="menu_wrap">
						   		<div id="centerAddr"></div>
						   	</div>
						</div>
					</div>
				</div>
				<script>
				var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			    mapOption = { 
			        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
			        level: 1 // 지도의 확대 레벨 
			    }; 

				var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
				// HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
				if (navigator.geolocation) {
				    
				    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
				    navigator.geolocation.getCurrentPosition(function(position) {
				        
				        var lat = position.coords.latitude, // 위도
				            lon = position.coords.longitude; // 경도
				        
				        var locPosition = new kakao.maps.LatLng(lat, lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
				        
				        // 마커와 인포윈도우를 표시합니다
				        displayMarker(locPosition);
				            
				      });
				    
				} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
				    
				    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),    
				        message = 'geolocation을 사용할수 없어요..'
				        
				    displayMarker(locPosition, message);
				}
				
				// 주소-좌표 변환 객체를 생성합니다
				var geocoder = new kakao.maps.services.Geocoder();

				var marker = new kakao.maps.Marker(), // 클릭한 위치를 표시할 마커입니다
				    infowindow = new kakao.maps.InfoWindow({zindex:1}); // 클릭한 위치에 대한 주소를 표시할 인포윈도우입니다

				// 현재 지도 중심좌표로 주소를 검색해서 지도 좌측 상단에 표시합니다
				searchAddrFromCoords(map.getCenter(), displayCenterInfo);

				// 지도를 클릭했을 때 클릭 위치 좌표에 대한 주소정보를 표시하도록 이벤트를 등록합니다
				kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
				    searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
				        if (status === kakao.maps.services.Status.OK) {
				            var detailAddr = !!result[0].road_address ? '<div class="addrDetail">도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
				            detailAddr += '<div class="addrDetail">지번 주소 : ' + result[0].address.address_name + '</div>';
				            
				            var content = '<div class="bAddr addrDetail" style="height:70px;">' +
				                            '<div class="addrDetail"><span class="title">법정동 주소정보</span></div>' + 
				                            detailAddr + 
				                        '</div>';

				            // 마커를 클릭한 위치에 표시합니다 
				            marker.setPosition(mouseEvent.latLng);
				            marker.setMap(map);

				            // 인포윈도우에 클릭한 위치에 대한 법정동 상세 주소정보를 표시합니다
				            infowindow.setContent(content);
				            infowindow.open(map, marker);
				        }   
				    });
				});
				
				// 지도에 마커와 인포윈도우를 표시하는 함수입니다
				function displayMarker(locPosition, message) {
	
				    // 마커를 생성합니다
				    var marker = new kakao.maps.Marker({  
				        map: map, 
				        position: locPosition
				    }); 
				    
				    var iwContent = message, // 인포윈도우에 표시할 내용
				        iwRemoveable = true;
				    
				    // 지도 중심좌표를 접속위치로 변경합니다
				    map.setCenter(locPosition);      
				}    
				
				// 중심 좌표나 확대 수준이 변경됐을 때 지도 중심 좌표에 대한 주소 정보를 표시하도록 이벤트를 등록합니다
				kakao.maps.event.addListener(map, 'idle', function() {
				    searchAddrFromCoords(map.getCenter(), displayCenterInfo);
				});

				function searchAddrFromCoords(coords, callback) {
				    // 좌표로 행정동 주소 정보를 요청합니다
				    geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
				}

				function searchDetailAddrFromCoords(coords, callback) {
				    // 좌표로 법정동 상세 주소 정보를 요청합니다
				    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
				}

				// 지도 좌측상단에 지도 중심좌표에 대한 주소정보를 표출하는 함수입니다
				function displayCenterInfo(result, status) {
				    if (status === kakao.maps.services.Status.OK) {
				        var infoDiv = document.getElementById('centerAddr');

				        for(var i = 0; i < result.length; i++) {
				            // 행정동의 region_type 값은 'H' 이므로
				            if (result[i].region_type === 'H') {
				                infoDiv.innerHTML = result[i].address_name;
				                break;
				            }
				        }
				    }
				}
				</script>
			</section>
        
			<!-- bottom.jsp import -->
			<c:import url = "/WEB-INF/views/layout/bottom.jsp"></c:import>
		</div>
	</body>
</html>