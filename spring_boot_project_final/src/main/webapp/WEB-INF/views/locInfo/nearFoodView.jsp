<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<title>어사모: 위치 정보: 주변 먹거리 탐색</title>
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
							<li data-tab="/location/overview" class="locCtg1 locCtg" >현재 위치</li>
							<li data-tab="/location/nearFish" class="locCtg2 locCtg" >근처 낚시터</li>
							<li data-tab="/location/nearFood" class="locCtg3 locCtg active" >주변 먹거리</li>
							<li data-tab="/location/nearPlay" class="locCtg4 locCtg" >주변 놀거리</li>
							<li data-tab="/location/nearHotel" class="locCtg5 locCtg" >주변 숙박시설</li>
							<li data-tab="/location/traffic" class="locCtg6 locCtg" >교통 상황</li>
							<li data-tab="/location/nationwide" class="locCtg7 locCtg" >전국 낚시터</li>
						</ul>
					</div>
					<div class="mapBox">
						<div class="map_wrap">
						    <div id="map" style="width:70%;height:700px;position:relative;overflow:hidden;"></div>
						    <ul id="category">
						        <li id="FD6"> 
						            <span class="category_bg food">음식점</span>
						        </li>
						        <li id="CE7"> 
						            <span class="category_bg cafe">카페</span>
						        </li>    
						        <li id="MT1"> 
						            <span class="category_bg mart">마트</span>
						        </li>  
						        <li id="CS2"> 
						            <span class="category_bg store">편의점</span>
						        </li>      
						    </ul>
						    <div id="menu_wrap">
						   		<ul id="placesList" style="display:none;"></ul>
						   		<div id="pagination"></div>
						   	</div>
						</div>
					</div>
				</div>
				<script>
					// 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
					var placeOverlay = new kakao.maps.CustomOverlay({zIndex:1}), 
					    contentNode = document.createElement('div'), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다 
					    markers = [], // 마커를 담을 배열입니다
					    currCategory = ''; // 현재 선택된 카테고리를 가지고 있을 변수입니다
					 
					var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
					    mapOption = {
					        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
					        level: 3 // 지도의 확대 레벨
					    };  
					
					// 지도를 생성합니다    
					var map = new kakao.maps.Map(mapContainer, mapOption); 
					
					// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
					var mapTypeControl = new kakao.maps.MapTypeControl();

					// 지도에 컨트롤을 추가해야 지도위에 표시됩니다
					// kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
					map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

					// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
					var zoomControl = new kakao.maps.ZoomControl();
					map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
					
					// 장소 검색 객체를 생성합니다
					var ps = new kakao.maps.services.Places(map);   

					// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
					var infowindow = new kakao.maps.InfoWindow({zIndex:1});
					
					// 지도에 idle 이벤트를 등록합니다
					kakao.maps.event.addListener(map, 'idle', searchPlaces);
					
					// 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다 
					contentNode.className = 'placeinfo_wrap';
					
					// 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
					// 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다 
					addEventHandle(contentNode, 'mousedown', kakao.maps.event.preventMap);
					addEventHandle(contentNode, 'touchstart', kakao.maps.event.preventMap);
					
					// 커스텀 오버레이 컨텐츠를 설정합니다
					placeOverlay.setContent(contentNode);  
					
					// 각 카테고리에 클릭 이벤트를 등록합니다 -> 실질적인 이벤트의 시작점
					addCategoryClickEvent();
					
					// HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
					if (navigator.geolocation) {
					    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
					    navigator.geolocation.getCurrentPosition(function(position) {
					        
					        var lat = position.coords.latitude, // 위도
					            lon = position.coords.longitude; // 경도
					        
					        var locPosition = new kakao.maps.LatLng(lat, lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
					        
					     	// 지도 중심좌표를 접속위치로 변경합니다
					        map.setCenter(locPosition); 
					      });
					} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
					    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
					
					   	alert("현재 위치를 불러올 수 없습니다.");
					}
					
					// 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
					function addEventHandle(target, type, callback) {
					    if (target.addEventListener) {
					        target.addEventListener(type, callback);
					    } else {
					        target.attachEvent('on' + type, callback);
					    }
					}
					
					// 카테고리 검색을 요청하는 함수입니다
					function searchPlaces() {
					    if (!currCategory) {
					        return;
					    }
					    
					    // 커스텀 오버레이를 숨깁니다 
					    placeOverlay.setMap(null);
					
					    // 지도에 표시되고 있는 마커를 제거합니다
					    removeMarker();
					    
						 // 검색 결과 목록에 추가된 항목들을 제거합니다
						var listEl = document.getElementById('placesList');
					    removeAllChildNods(listEl);
					    
					    ps.categorySearch(currCategory, placesSearchCB, {useMapBounds:true}); 
					}
					
					// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
					function placesSearchCB(data, status, pagination) {
					    if (status === kakao.maps.services.Status.OK) {
					        // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
					        displayPlaces(data);
					        
					     	// 페이지 번호를 표출합니다
					        displayPagination(pagination);
					     	
					    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
					        var placesList = document.getElementById('placesList');
						    placesList.style.display = 'none';
					        alert('현재 위치에서 검색 결과가 존재하지 않습니다.');
					        return;
					
					    } else if (status === kakao.maps.services.Status.ERROR) {
					        alert('검색 결과 중 오류가 발생했습니다.');
					        return;
					    }
					}
					
					// 지도에 마커를 표출하는 함수입니다
					function displayPlaces(places) {
			            var listEl = document.getElementById('placesList'), 
			            menuEl = document.getElementById('menu_wrap'),
			            fragment = document.createDocumentFragment(), 
			            bounds = new kakao.maps.LatLngBounds(), 
			            listStr = '';
					
					    for ( var i=0; i<places.length; i++ ) {
					        	 // 마커를 생성하고 지도에 표시합니다
					            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
					                marker = addMarker(placePosition, i), 
					                itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다
					            
					            // 마커를 생성하고 지도에 표시합니다
					            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
					                marker = addMarker(placePosition, i), 
					                itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

					            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
					            // LatLngBounds 객체에 좌표를 추가합니다
					            bounds.extend(placePosition);

					            // 마커와 검색결과 항목에 mouseover 했을때
					            // 해당 장소에 인포윈도우에 장소명을 표시합니다
					            // mouseout 했을 때는 인포윈도우를 닫습니다
					            (function(marker, title) {
					                kakao.maps.event.addListener(marker, 'mouseover', function() {
					                    displayInfowindow(marker, title);
					                });
					                kakao.maps.event.addListener(marker, 'mouseout', function() {
					                    infowindow.close();
					                });
					                itemEl.onmouseover =  function () {
					                    displayInfowindow(marker, title);
					                };
					                itemEl.onmouseout =  function () {
					                    infowindow.close();
					                };
					            })(marker, places[i].place_name);

					            fragment.appendChild(itemEl);
					    }
					    
						 // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
					    listEl.appendChild(fragment);
					    menuEl.scrollTop = 0;

					    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
					    // map.setBounds(bounds);
					    
					} /* function : displayPlaces 종료  */
					
					// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
					// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
					function addMarker(position, idx, title) {
					    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
					        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
					        imgOptions =  {
					            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
					            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
					            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
					        },
					        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
					            marker = new kakao.maps.Marker({
					            position: position, // 마커의 위치
					            image: markerImage 
					        });
					
					    marker.setMap(map); // 지도 위에 마커를 표출합니다
					    markers.push(marker);  // 배열에 생성된 마커를 추가합니다
					
					    return marker;
					}
										
					// 지도 위에 표시되고 있는 마커를 모두 제거합니다
					function removeMarker() {
					    for ( var i = 0; i < markers.length; i++ ) {
					        markers[i].setMap(null);
					    }   
					    markers = [];
					}
					
					// 각 카테고리에 클릭 이벤트를 등록합니다
					function addCategoryClickEvent() {
					    var category = document.getElementById('category'),
					        children = category.children;
					
					    for (var i=0; i<children.length; i++) {
					        children[i].onclick = onClickCategory;
					    }
					}
					
					// 카테고리를 클릭했을 때 호출되는 함수입니다
					function onClickCategory() {
					    var id = this.id,
					        className = this.className;
					    
					    var placesList = document.getElementById('placesList');
					    placesList.style.display = 'block';
					
					    placeOverlay.setMap(null);
					
					    if (className === 'on') {
					        currCategory = '';
					        changeCategoryClass();
					        removeMarker();
					    } else {
					        currCategory = id;
					        changeCategoryClass(this);
					        searchPlaces();
					    }
					}
					
					// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
					function changeCategoryClass(el) {
					    var category = document.getElementById('category'),
					        children = category.children,
					        i;
					
					    for ( i=0; i<children.length; i++ ) {
					        children[i].className = '';
					    }
					
					    if (el) {
					        el.className = 'on';
					    } 
					} 
					
					// 검색결과 항목을 Element로 반환하는 함수입니다
					function getListItem(index, places) {

					    var el = document.createElement('li'),
					    itemStr = '<div><span class="markerbg marker_' + (index+1) + '"></span></div>' +
					                '<div class="info">' +
					                '   <div class="infoName">' + places.place_name + '</div>';

					    if (places.road_address_name) {
					        itemStr += '    <div>' + places.road_address_name + '</div>' +
					                    '   <div class="jibun gray">' +  places.address_name  + '</div>';
					    } else {
					        itemStr += '    <div>' +  places.address_name  + '</div>'; 
					    }
					                 
					      itemStr += '  <div class="tel">' + places.phone  + '<span><a class="infoUrl" href="' + places.place_url + '" target="_blank">상세 페이지</a></span></div>' +
					                '</div>';           

					    el.innerHTML = itemStr;
					    el.className = 'item';

					    return el;
					}
					
					// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
					function displayPagination(pagination) {
					    var paginationEl = document.getElementById('pagination'),
					        fragment = document.createDocumentFragment(),
					        i; 

					    // 기존에 추가된 페이지번호를 삭제합니다
					    while (paginationEl.hasChildNodes()) {
					        paginationEl.removeChild (paginationEl.lastChild);
					    }

					    for (i=1; i<=pagination.last; i++) {
					        var el = document.createElement('a');
					        el.href = "#";
					        el.innerHTML = i;

					        if (i===pagination.current) {
					            el.className = 'on';
					        } else {
					            el.onclick = (function(i) {
					                return function() {
					                    pagination.gotoPage(i);
					                }
					            })(i);
					        }

					        fragment.appendChild(el);
					    }
					    paginationEl.appendChild(fragment);
					}

					// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
					// 인포윈도우에 장소명을 표시합니다
					function displayInfowindow(marker, title) {
					    var content = '<div style="padding:5px;z-index:1;" class="markerInfo">' + title + '</div>';

					    infowindow.setContent(content);
					    infowindow.open(map, marker);
					}

					 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
					function removeAllChildNods(el) {   
					    while (el.hasChildNodes()) {
					        el.removeChild (el.lastChild);
					    }
					}
				</script>
			</section>
        
			<!-- bottom.jsp import -->
			<c:import url = "/WEB-INF/views/layout/bottom.jsp"></c:import>
		</div>
	</body>
</html>