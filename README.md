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
  <summary>📌 구조 살펴보기 (클릭)</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)

  - [CartController](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/controller/CartController.java)
  - CartService<br>
    : [CartService](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/service/CartService.java) implements [ICartService](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/service/ICartService.java)
  - [ICartDAO](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/java/com/spring_boot_final/metasumer/dao/ICartDAO.java)
  - [CartMapper](https://github.com/fullstack-final-project/project-final-metasumer/blob/develop/spring_boot_project_final/src/main/resources/mappers/CartMapper.xml)
  </details>

  #### 🛠 기능 상세
  <details>
  <summary>장바구니</summary> 

    
  접은 내용(ex 소스 코드)
  </details>

  <details>
  <summary>주문</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  ### ✅ 마이페이지

  #### 🗂 구조 다이어그램
  <details>
  <summary>📌 구조 살펴보기 (클릭)</summary> 
    
  ![코드 구조 drawio](https://github.com/user-attachments/assets/6b08efa7-2c13-424e-b0e5-789016484200)
  </details>

  #### 🛠 기능 상세
  <details>
  <summary>나의 낚시 기록</summary> 
    
  접은 내용(ex 소스 코드)
  </details>

  <details>
  <summary>회원 정보 관리</summary> 
    
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

