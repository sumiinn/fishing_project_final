package com.spring_boot_final.metasumer.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring_boot_final.metasumer.model.MemberVO;
import com.spring_boot_final.metasumer.model.MyPageVO;
import com.spring_boot_final.metasumer.model.ReservationListVO;
import com.spring_boot_final.metasumer.service.MyPageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {
	@Autowired
	MyPageService myPageService;
	
	@GetMapping("/member/myPage")	
	public String fishInfo(HttpServletRequest request, 
			               @RequestParam(defaultValue = "1") int page,
			               Model model) {	
		// 나의 물고기 기록 list 가져오기
		HttpSession session = request.getSession();
		String memId = (String) session.getAttribute("memId");				

		int recordsPerPage = 4;
		int totalRecords = myPageService.getRecordsCount(memId); // 총 기록 수
       
        if (totalRecords > 0) {
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage); // 총 페이지 수

            // 페이지 범위 초과 방지
            if (page > totalPages) {
                page = totalPages; 
            } else if (page < 1) {
                page = 1;
            }

            int offset = (page - 1) * recordsPerPage;
            
            ArrayList<MyPageVO> mfList = myPageService.MyFishRecordsPerPage(memId, offset, recordsPerPage);
            
            model.addAttribute("mfList", mfList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages); 
        } else {
            // 기록이 없을 경우 빈 리스트와 기본 페이지 정보를 설정
            model.addAttribute("mfList", new ArrayList<MyPageVO>());
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 1);
        }
    	
        return "myPage/myPageListView";

	}
	
	@GetMapping("/member/myPageCountData")
	@ResponseBody
	public Map<String, Object> getPageData(HttpServletRequest request,
			                               @RequestParam(defaultValue = "1") int page) {
	    HttpSession session = request.getSession();
	    String memId = (String) session.getAttribute("memId");

	    int recordsPerPage = 4;
	    int totalRecords = myPageService.getRecordsCount(memId); // 총 기록 수
	    
	    Map<String, Object> data = new HashMap<>();
	    
	    if (totalRecords > 0) {
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage); // 총 페이지 수

            // 페이지 범위 초과 방지
            if (page > totalPages) {
                page = totalPages;
            } else if (page < 1) {
                page = 1;
            }

            int offset = (page - 1) * recordsPerPage;
            
            ArrayList<MyPageVO> mfList = myPageService.MyFishRecordsPerPage(memId, offset, recordsPerPage);                       
    	    
    	    data.put("mfList", mfList);
    	    data.put("currentPage", page);
    	    data.put("totalPages", totalPages);
        } else {
            // 기록이 없을 경우 빈 리스트와 기본 페이지 정보를 설정
        	data.put("mfList", new ArrayList<MyPageVO>());
        	data.put("currentPage", 1);
        	data.put("totalPages", 1);
        }	    

	    return data;
	}

	@GetMapping("/member/myPageData")
	@ResponseBody
	public Map<String, Object> getFishData(HttpServletRequest request) {    
	    // 물고기 기록 list 가져오기
		HttpSession session = request.getSession();
		String memId = (String) session.getAttribute("memId");
		
		ArrayList<MyPageVO> mfList = myPageService.MyFishRecordsList(memId);
	    
	    // 물고기 이름, 크기 배열 생성
	    ArrayList<String> fishNames = new ArrayList<>();
	    ArrayList<Double> fishSizes = new ArrayList<>();	    
	    
	    //월별 count 배열 생성
	    int[] monthFishCnt = new int[12];
	    // 월 추출
	    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	    
	    for (MyPageVO record : mfList) {	       	        	    	
	    	// 월 추출 후 물고기 count
	        Date createdDate = record.getCreatedDate();
	        String monthStr = monthFormat.format(createdDate);
	        // 배열 인덱스 넣기 위해 -1
	        int month = Integer.parseInt(monthStr) - 1;
	        
	        monthFishCnt[month]++;	  	        	        
	    }
	    
	    // 최근 10개의 기록만 추출
	    int maxRecords = 10;
	    
	    if (mfList.size() > maxRecords) {
	        mfList = new ArrayList<>(mfList.subList(0, maxRecords)); 
	    }
	    
	    for(MyPageVO record : mfList) {	  
	    	// 물고기 이름 가져오기
	    	fishNames.add(record.getFishName());
	    	
	        // cm -> "" 변환 후 Double로 변환
	        String sizeStr = record.getFishSize().replace("cm", "").trim();
	        Double size = Double.parseDouble(sizeStr);
	        
	        // 물고기 크기 가져오기
	        fishSizes.add(size);	
	    }

	    // 데이터를 JSON 형식으로 변환
	    Map<String, Object> data = new HashMap<>();
	    
	    data.put("fishNames", fishNames);
	    data.put("fishSizes", fishSizes);
	    data.put("monthFishCnt", monthFishCnt);

	    return data;
	}
	
	@GetMapping("/myPage/myFishingSuccess")
	public String fishingSuccess() {
		return "myPage/myFishingSuccess";
	}
	
	@PostMapping("/myPage/successCalc")
    @ResponseBody
    public double calculateSuccessRate(@RequestParam("fishName") String fishName,
                                       @RequestParam("tide") String tide,
                                       @RequestParam("location") String location,
                                       @RequestParam("weather") String weather,
                                       @RequestParam("skill") String skill,
                                       @RequestParam("fortune") String fortune) {

        // fishName을 통해 fishNo 가져오기
        String fishNo = myPageService.getFishNoByName(fishName);

        // 각 요소별 점수 계산
        int tideScore = myPageService.getTideScore(fishNo, tide);
        int locationScore = myPageService.getLocationScore(fishNo, location);
        int weatherScore = myPageService.getWeatherScore(fishNo, weather);
        int skillScore = myPageService.getSkillScore(skill);
        int fortuneScore = myPageService.getFortuneScore(fortune);

        // 총 점수 계산(퍼센티지)
        int totalScore = tideScore + locationScore + weatherScore + skillScore + fortuneScore;
        int successRate = (int) ((totalScore / 25.0) * 100);

        return successRate;
    }
	
	
	// 회원 정보 조회
	@GetMapping("/myPage/myInfo")
	public String myInfo(Model model, HttpSession session) {
		String memId = (String) session.getAttribute("sid");

		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);

		// model 설정
		model.addAttribute("memVo", memVo);

		return "myPage/memberInfoForm";
	}
		
	// 비밀 번호 확인 폼
	@GetMapping("/myPage/checkPwdForm")
	public String checkPassword(Model model,
                                HttpSession session) {
        String memId = (String)session.getAttribute("sid");	          		
		
		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);		
		
		// model 설정
		model.addAttribute("memVo", memVo);		
				
		return "myPage/checkPwdForm";
	}
		
	// 비밀번호 확인
	@PostMapping("/myPage/checkPwd")
	@ResponseBody
	public String loginCheck(@RequestParam HashMap<String, Object> param,
							 HttpSession session) {
		String result = myPageService.passwordCheck(param);
		
		return result;
	}
	
	// 회원 정보 수정 폼
	@GetMapping("/myPage/updateMemberForm")
	public String updateMember(Model model,
                               HttpSession session) {
        String memId = (String)session.getAttribute("sid");	          		
		
		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);
		// 전화번호 설정
		String[] hp = (memVo.getMemHP()).split("-");
		
		// model 설정
		model.addAttribute("memVo", memVo);
		model.addAttribute("hp1", hp[0]);
		model.addAttribute("hp2", hp[1]);
		model.addAttribute("hp3", hp[2]);
		return "myPage/updateMemberForm";
	}
	
	// 회원정보 수정	
	@PostMapping("/myPage/updateComplete")
	@ResponseBody
	public String updateComplete(@RequestParam HashMap<String, Object> param,
                                 HttpSession session) {
		String memId = (String)session.getAttribute("sid");
	    param.put("memId", memId);
	    
	    String result = "fail";
	    
	    // DB 업데이트 서비스 호출
	    boolean updateSuccess = myPageService.updateMemberInfo(param);
	    
	    if(updateSuccess) {
	    	result = "success";
	    }
	    
	    return result;
	}
	
	// 회원정보 수정 완료 폼
	@GetMapping("/myPage/updateCompleteForm")
	public String updateCompleteForm() {		
		return "myPage/updateCompleteForm";
	}
	
	// 비밀번호 변경 폼
	@GetMapping("/myPage/changePwdForm")
	public String changePwdForm(Model model,
                               HttpSession session) {		
        String memId = (String)session.getAttribute("sid");	          		
		
		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);
		
		// model 설정
		model.addAttribute("memVo", memVo);
                  				
		return "myPage/changePwdForm";
	}
	
	// 회원정보 수정
	@PostMapping("/myPage/changePwdComplete")
	@ResponseBody
	public String changePwdComplete(@RequestParam HashMap<String, Object> param, HttpSession session) {
		String memId = (String) session.getAttribute("sid");
		param.put("memId", memId);

		String result = "fail";

		// DB 업데이트 서비스 호출
		boolean updateSuccess = myPageService.changePwd(param);

		if (updateSuccess) {
			result = "success";
		}

		return result;
	}
	
	// 나의 예약	
	@GetMapping("/myPage/reservation")
	public String reservation(String period, Model model, HttpSession session) {	
        String memId = (String)session.getAttribute("sid");	   
        
        LocalDate now = LocalDate.now();
	    String startDate = null;
	    String endDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

	    if (period != null) {
	        switch (period) {
	            case "3m":
	                startDate = now.minusMonths(3).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                break;
	            case "6m":
	                startDate = now.minusMonths(6).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                endDate = now.minusMonths(3).minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                break;
	            case "1y":
	                startDate = now.minusYears(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                endDate = now.minusMonths(6).minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                break;
	            case "3y":
	                startDate = now.minusYears(3).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                endDate = now.minusYears(1).minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                break;
	        }
	    }

	    ArrayList<ReservationListVO> reservationList = myPageService.reservationList(memId, startDate, endDate);
		
		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);		
		
		// model 설정
		model.addAttribute("memVo", memVo);
		model.addAttribute("reservationList", reservationList);
		
		return "myPage/reservationListView";
	} 
	
	// 나의 이벤트
	@GetMapping("/myPage/event")
	public String event(Model model, HttpSession session) {
		String memId = (String) session.getAttribute("sid");

		// 회원 정보 가져오기
		MemberVO memVo = myPageService.getMemberInfo(memId);

		// model 설정
		model.addAttribute("memVo", memVo);		

		return "myPage/eventListView";
	}

}
