# 🐠 Fishing Project
  - 프로젝트명 : 어사모(물고기를 사랑하는 모임)
  - 팀명 : 메타슈머(metasumer)
  - ~~http://49.50.160.150:8080/~~ (네이버 클라우드 사용 종료로 서버를 내렸습니다.)
    
## 📌 프로젝트 설명
   AI API를 활용한 낚시 관련 실시간 정보 제공 및 낚시인 커뮤니티 웹서비스
   
  - 배경 및 문제 
    - 기존 낚시 사이트 대부분이 정보 제공을 복잡하게 나열해, 필요한 정보를 찾기 어렵습니다
    - 더불어 사용자가 실시간으로 자신이 원하는 정보를 바로 찾기 어렵습니다
    - 해외 정보 및 어종/낚시 방법 등 상세한 정보를 한 눈에 제공하는 서비스에 대한 요구가 있습니다
   
  - 목표 
    - 사용자에게 맞춤 정보를 제공하며 커뮤니티를 활성화합니다
    - 사업자 페이지 및 사용자 맞춤 광고를 제공해 수익을 창출합니다
  
## ⚙ 개발 환경
   - `Java 17`
   - `JDK 17`
   - Framework : Spring Boot (3.x)
   - DataBase : MySQL
   - ORM : MyBatis
   - 버전 관리 : Git (GitHub 활용)
   
## 🏗 시스템 아키텍처

## 📊 ERD 설계
![ERD_어사모](https://github.com/user-attachments/assets/e69df066-b155-45d6-8ed7-e01b0a4b9661)

## 📋 담당 주요 기능
  ### ✅ 상품 구매 기능
 
  #### 🗂 구조 다이어그램
  <details>
  <summary>📌 구조 살펴보기</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)

  - [CartController](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/controller/CartController.java)
  - CartService<br>
    : [CartService](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/service/CartService.java) implements [ICartService](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/service/ICartService.java)
  - [ICartDAO](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/dao/ICartDAO.java)
  - [CartMapper](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/resources/mappers/CartMapper.xml)
  </details>

  #### 🛠 주요 기능 상세
  <details>
  <summary>장바구니</summary><br> 

  [기능 설명]
  - 로그인한 사용자가 상품을 장바구니에 담고 수량을 변경하거나 삭제할 수 있습니다.
  - 동일한 상품이 담길 경우 중복을 체크하여 수량이 증가됩니다.
  - `@ResponseBody`와 Ajax 비동기 처리를 적용하여 새로고침 없이 변경사항을 확인할 수 있습니다.

  [주요 코드]
  ```java
  // 장바구니에 상품 추가 
@PostMapping("/myPage/insertCart")
  @ResponseBody
  ```
  </details>

  <details>
  <summary>주문</summary><br>

  [기능 설명]
  
  - 장바구니에서 선택한 상품만 주문서로 전달 후 주문이 진행됩니다.
  - 현재 시간 + 랜덤 숫자로 주문번호를 생성해 주문 테이블과 주문 상세 테이블에 저장됩니다.
  - 주문 완료 후, 주문 내역은 기간별(3개월, 6개월, 1년)로 조회할 수 있습니다.

  [주요 코드]
  ```java
  // 주문 번호 생성 및 주문 정보 저장
@RequestMapping("/myPage/orderComplete")
	public String orderComplete(OrderInfoVO ordInfoVo, @RequestParam String hp1, 
			                    @RequestParam String hp2, @RequestParam String hp3, 
			                    @RequestParam(value = "cartNos") ArrayList<Integer> cartNos, Model model) {
		// 전화번호
		String hp = hp1 + "-" + hp2 + "-" + hp3;
		ordInfoVo.setOrdRcvPhone(hp);

		// 주문번호
		long timeNum = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = dayTime.format(new Date(timeNum));

		// 랜덤 숫자 4개 생성
		String rNum = "";
		for (int i = 1; i <= 4; i++) {
			rNum += (int) (Math.random() * 10);
		}

		// 주문번호 설정
		String ordNo = strTime + "_" + rNum;
		ordInfoVo.setOrdNo(ordNo);
		
		// 선택된 장바구니 아이템 설정
          ordInfoVo.setCartNos(cartNos);          
          // 주문 정보 저장
          cartService.insertOrderInfo(ordInfoVo);             

		// 모델에 추가
		model.addAttribute("ordNo", ordNo);

		return "myPage/orderCompleteView";
	}
```
  </details>

  ### ✅ 마이페이지

  #### 🗂 구조 다이어그램
  <details>
  <summary>📌 구조 살펴보기</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)
  </details>

  #### 🛠 주요 기능 상세
  <details>
  <summary>나의 물고기 기록 조회</summary><br>
    
  [기능 설명]

  - 페이징 처리로 최근 기록을 확인할 수 있습니다.
  - 최근 10개의 물고기 기록을 크기 데이터로 가공하여 차트로 시각화합니다.
  - 월별 낚시 기록을 카운팅하고, 월간 총합 데이터를 차트로 제공합니다.

  [주요 코드]
  ```java
  ArrayList<MyPageVO> mfList = myPageService.MyFishRecordsList(memId);

// 월별 count 배열 생성
int[] monthFishCnt = new int[12];
SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

for (MyPageVO record : mfList) {
    Date createdDate = record.getCreatedDate();
    int month = Integer.parseInt(monthFormat.format(createdDate)) - 1;
    monthFishCnt[month]++;
}

// 최근 10개 기록만 추출
if (mfList.size() > 10) {
    mfList = new ArrayList<>(mfList.subList(0, 10)); 
}

ArrayList<String> fishNames = new ArrayList<>();
ArrayList<Double> fishSizes = new ArrayList<>();

for (MyPageVO record : mfList) {
    fishNames.add(record.getFishName());
    String sizeStr = record.getFishSize().replace("cm", "").trim();
    fishSizes.add(Double.parseDouble(sizeStr));
}
```
  </details>

  <details>
  <summary>나의 낚시 성공률(재미 요소)</summary><br> 
    
  [기능 설명]

  - 물고기 종류와 조황 요소(물때, 위치, 날씨, 오늘의 운세 등)를 입력하면 낚시 성공률을 계산해주는 기능입니다.
  - 실제 확률 계산 알고리즘이 아닌, 단순 점수 계산을 통해 게임성 재미 요소로 구현했습니다.

  [주요 코드]
  ```java
@PostMapping("/myPage/successCalc")
@ResponseBody
public double calculateSuccessRate(@RequestParam("fishName") String fishName,
                                   @RequestParam("tide") String tide,
                                   @RequestParam("location") String location,
                                   @RequestParam("weather") String weather,
                                   @RequestParam("skill") String skill,
                                   @RequestParam("fortune") String fortune) {

    String fishNo = myPageService.getFishNoByName(fishName);

    int totalScore = myPageService.getTideScore(fishNo, tide)
                   + myPageService.getLocationScore(fishNo, location)
                   + myPageService.getWeatherScore(fishNo, weather)
                   + myPageService.getSkillScore(skill)
                   + myPageService.getFortuneScore(fortune);

    int successRate = (int) ((totalScore / 25.0) * 100);

    return successRate;
}
```
  </details>

  <details>
  <summary>회원 정보 관리</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  <details>
  <summary>구매/예약 내역 확인</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  ### ✅ 어종 정보

  #### 🗂 구조 다이어그램
  <details>
  <summary>📌 구조 살펴보기 (클릭)</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)
  </details>

  #### 🛠 기능 상세
  <details>
  <summary>어종 검색</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  <details>
  <summary>어종별 상세페이지</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  ### ✅ 낚시 정보

  #### 🗂 구조 다이어그램
  <details>
  <summary>📌 구조 살펴보기 (클릭)</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)
  </details>

  #### 🛠 기능 상세
  
## 📹 시연 영상
  
[![낚시 커뮤니티 웹사이트 시연영상(어사모)](https://img.youtube.com/vi/w0A9_2jcWUs/0.jpg)](https://youtu.be/w0A9_2jcWUs)

## 🙋‍♀️ 팀원
  - 이준기 : https://github.com/basislee
  - 장연주 : https://github.com/aktbskrks
  - 조민성 : https://github.com/Chominsung0823
  - 최수민 : https://github.com/sumiinn

