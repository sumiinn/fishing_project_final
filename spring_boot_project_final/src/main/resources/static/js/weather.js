/**
 * weather.js
 */
 
 $(document).ready(function() {
 	
 	/* (시작) 0. 탭 메뉴 선택 시 각 탭 사이트로 이동 */
 	const weatherCtgItem = document.querySelectorAll('.weatherCtg');
 	
 	weatherCtgItem.forEach( function(item, index) {
		item.addEventListener('click', function() {
			window.location.href = $(this).attr('data-tab');
		});
	});
	/* 0. 탭 메뉴 선택 시 각 탭 사이트로 이동 (끝) */
 
 
 	/* 1. (시작) 상단 위치 카테고리 클릭 시 해당 카테고리에 강조 표시 */
 	const weatherCtgList = document.querySelectorAll('.weatherCtgBar .weatherCtg');
 	
 	weatherCtgList.forEach( function(weatherCtg, index) {
 		weatherCtg.addEventListener('click', function() {
 			weatherCtgChange(index + 1);
 		});
 	});
 	
 	function weatherCtgChange(num) {
 		document.querySelector('.weatherCtg.active').classList.remove('active');
 		document.querySelector('.weatherCtg' + num).classList.add('active');
 	}
 	/* 1. 상단 위치 카테고리 클릭 시 해당 카테고리에 강조 표시 (끝) */


	/* 2. (시작) 현재 위치 데이터로 받아서 날씨로 알려줌 */
	let nowWeather;
	let nowTemp;
	
 	const getWeatherByCurrentLocation = async (lat, lon) => {
		let url = 'https://api.openweathermap.org/data/2.5/weather?lat=' + lat + '&lon=' + lon + '&lang=kr&appid=19d78b702ef255e0d5bb81968100e305&units=metric';
		let response = await fetch(url);
		let data = await response.json();
		let jsonString = JSON.stringify(data);
		let jsonData = JSON.parse(jsonString);
		
		// $('.item1').text(jsonData.name);
		var sunriseTime = Unix_timestamp(jsonData.sys.sunrise);
		var sunsetTime = Unix_timestamp(jsonData.sys.sunset);
		$('.item4').text(sunriseTime);
		$('.item9').text(sunsetTime);
		
		nowWeather = jsonData.main.temp;
		nowTemp = jsonData.weather[0].description;
		$('.item5').text(nowWeather + " ℃");
		$('.item10').text(jsonData.main.temp_max + " ℃");
		$('.item6').text(nowTemp);
		$('.item7').text(jsonData.main.humidity + " %");
		$('.item8').text(jsonData.wind.speed + " m/s");
			
 	};
 	/* 2. 현재 위치 데이터로 받아서 날씨로 알려줌 (끝) */
 	
 	
 	/* 3. (시작) 현재 위치 데이터로 받아서 날씨 확인 */
 	let defaultPos = new naver.maps.LatLng(37.5666103, 126.9783882);
 	let mapOptions = { // map setting 설정값 저장 변수
	    zoom: 13, // 지도의 초기 줌 레벨
    	minZoom: 6, // 지도의 최소 줌 레벨
    	draggable: true,
    	pinchZoom: true,
    	scrollWheel: true,
    	disableKineticPan: false, // 관성 드래깅
	    mapTypeControl: true, // 맵 타입 컨르롤러 (일반, 위성) (사용자 조작용)
	    mapTypeControlOptions: {
	        style: naver.maps.MapTypeControlStyle.BUTTON,
	        position: naver.maps.Position.TOP_RIGHT
	    },
	    zoomControl: true, // 줌 컨트롤러 (사용자 조작용)
	    zoomControlOptions: {
	        position: naver.maps.Position.TOP_LEFT
	    },
	    scaleControl: true, // 스케일 컨트롤러 (정보 제공용 : 줌에 따라서 스케일 간격 자동 변경됨)
	    scaleControlOptions: {
	        position: naver.maps.Position.BOTTOM_RIGHT
	    },
	    logoControl: true, // 로고 컨트롤러 (정보 제공용 : 클릭 시 저작권 관련 탭으로 이동)
	    logoControlOptions: {
	        position: naver.maps.Position.BOTTOM_RIGHT
	    },
	    mapDataControl: true, // 네이버 회사 로고 컨트롤러 (정보 제공용)
	    mapDataControlOptions: {
	        position: naver.maps.Position.BOTTOM_LEFT
	    },
	    mapTypeId: naver.maps.MapTypeId.NORMAL
	};
	
	let map = new naver.maps.Map('weatherMapBox', mapOptions);
	map.setCursor('pointer');
	
	let marker = new naver.maps.Marker({ // 위치 권한 주지 않았을 때 기본 마커
		map: map,
		position: defaultPos,
        anchorSize: new naver.maps.Size(25, 5),
       	pixelOffset: new naver.maps.Point(0, -10)
	}); 
	
	let infowindow = new naver.maps.InfoWindow({ // 현재 위치 표시
	    anchorSkew: true
	});
	
	window.navermap_authFailure = function () {
	   alert("네이버 지도가 연결되지 않았습니다.");
	}
	
	let location; // onSuccessGeolocation, coordinateToAddress 에서 사용
	
	if (navigator.geolocation) {
	        /**
	         * navigator.geolocation 은 Chrome 50 버젼 이후로 HTTP 환경에서 사용이 Deprecate 되어 HTTPS 환경에서만 사용 가능 합니다.
	         * http://localhost 에서는 사용이 가능하며, 테스트 목적으로, Chrome 의 바로가기를 만들어서 아래와 같이 설정하면 접속은 가능합니다.
	         * chrome.exe --unsafely-treat-insecure-origin-as-secure="http://example.com"
	         */
	         
	        navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
	        
    } else {
        var center = map.getCenter();
        infowindow.setContent('<div style="padding:20px; color:#39A7FF; font-weight:bold">' +
        								   '<h5 style="margin-bottom:5px;">Geolocation not supported</h5></div>');
        infowindow.open(map, center);
    }
	
	function onSuccessGeolocation(position) {
		location = new naver.maps.LatLng(position.coords.latitude,
	                                         position.coords.longitude);
	
	    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
	    map.setZoom(15); // 지도의 줌 레벨을 변경합니다.
	    
		let contentString = [ // 현재 위치 표시하는 마커의 설정값
			'<div style="padding:10px; font-weight:bold"><h3 style="color:#39A7FF; text-align:center letter-spacing: -1px;">jsonData.main.temp + " ℃"</h3></div>'
		].join();
		    
	    infowindow.setContent(contentString); // geolocation.getCurrentPosition() 위치
	
	    infowindow.open(map, location);
	   // console.log('Coordinates: ' + location.toString()); // 현재 위치 좌표
	    coordinateToAddress();
	    // naver.maps.onJSContentLoaded = coordinateToAddress;
	}
	
	function getCurrentLocation() {
 		navigator.geolocation.getCurrentPosition( (position) => {
 			let lat = position.coords.latitude;
 			let lon = position.coords.longitude;
 			//$('.item2').text(lat);
 			//$('.item3').text(lon);
 			
 			getWeatherByCurrentLocation(lat, lon);
 		});
 	}
 	
 	function onErrorGeolocation() {
	    var center = map.getCenter();
	
	    infowindow.setContent('<div style="padding:20px;">' +
	        '<h5 style="margin-bottom:5px;color:#f00;">Geolocation failed!</h5>'+ "latitude: "+ center.lat() +"<br />longitude: "+ center.lng() +'</div>');
	
	    infowindow.open(map, center);
	}
	
		function makeAddress(item) { // 전달받은 json 데이터로 주소 만들기
	    if (!item) {
	        return;
	    }
	
	    var name = item.name,
	        region = item.region,
	        land = item.land,
	        isRoadAddress = name === 'roadaddr';
	
	    var sido = '', sigugun = '', dongmyun = '', ri = '', rest = '';
	
	    if (hasArea(region.area1)) {
	        sido = region.area1.name;
	    }
	
	    if (hasArea(region.area2)) {
	        sigugun = region.area2.name;
	    }
	
	    if (hasArea(region.area3)) {
	        dongmyun = region.area3.name;
	    }
	
	    if (hasArea(region.area4)) {
	        ri = region.area4.name;
	    }
	
	    if (land) {
	        if (hasData(land.number1)) {
	            if (hasData(land.type) && land.type === '2') {
	                rest += '산';
	            }
	
	            rest += land.number1;
	
	            if (hasData(land.number2)) {
	                rest += ('-' + land.number2);
	            }
	        }
	
	        if (isRoadAddress === true) {
	            if (checkLastString(dongmyun, '면')) {
	                ri = land.name;
	            } else {
	                dongmyun = land.name;
	                ri = '';
	            }
	
	            if (hasAddition(land.addition0)) {
	                rest += ' ' + land.addition0.value;
	            }
	        }
	    }
	
	    return [sido, sigugun, dongmyun, ri, rest].join(' ');
	}
	function hasArea(area) { // 주소 제작용 함수
	    return !!(area && area.name && area.name !== '');
	}
	
	function hasData(data) { // 주소 제작용 함수
	    return !!(data && data !== '');
	}
	
	function checkLastString (word, lastString) { // 주소 제작용 함수
	    return new RegExp(lastString + '$').test(word);
	}
	
	function hasAddition (addition) { // 주소 제작용 함수
	    return !!(addition && addition.value);
	}
	/* 3. 현재 위치 데이터로 받아서 날씨로 알려줌 (끝) */
	
 	
 	
 	
 	// 타임스탬프 값을 년월일로 변환
	function Unix_timestamp(t){
	    var date = new Date(t*1000);
	    var year = date.getFullYear();
	    var month = "0" + (date.getMonth()+1);
	    var day = "0" + date.getDate();
	    var hour = "0" + date.getHours();
	    var minute = "0" + date.getMinutes();
	    var second = "0" + date.getSeconds();
	    return year + "년 " + month.substr(-2) + "월 " + day.substr(-2) + "일 " + hour.substr(-2) + "시 " + minute.substr(-2) + "분 ";
	}
	
	// 위도 경도를 주소로 변환
	function coordinateToAddress() { // 좌표 데이터 넘겨서 주소값 받아오기
	    naver.maps.Service.reverseGeocode({
	        location: location,
	    }, function(status, response) {
	        if (status === naver.maps.Service.Status.ERROR) {
	            return alert('Something Wrong!');
	        }
	
	        var items = response.v2.results,
	            address = '',
	            htmlAddresses = [];
			
			// console.log(items); // 결과 json 데이터로 확인
			
	        for (var i=0, ii=items.length, item, addrType; i<ii; i++) {
	            item = items[i];
	            address = makeAddress(item) || '';
	            addrType = item.name === 'roadaddr' ? '[도로명 주소]' : '[지번 주소]';
	
	            htmlAddresses.push(address + '\n');
	        }
			
			$('.item1').html('<span style="font-weight:bold;">' + htmlAddresses[3] + '소테츠 스프라지르 어저구 호텔</span>');

	    });
	}
	 	
 	getCurrentLocation();
 	
 	/* 3. 현재 위치 데이터로 받아서 날씨 확인 (끝)  */
 	
 	
 	/* 4. (시작) 기상 특보에서 각 특보 내용 색상 설정  */
 	const subtitleList = document.querySelectorAll('.subtitle');
 	
 	subtitleList.forEach( function(subtitle, index) {
 		if( subtitle.innerText.includes('강풍') ) {
 			$(this).classList.add('gale');
 			alert("있는뎅");
 		} else {
 			alert("없네용");
 		}
 		
 		console.log(subtitle.innerText + "dfds");
 	});
 	
 	/* 4. 기상 특보에서 각 특보 내용 색상 설정 (끝)  */
 	
 });
