package com.spring_boot_final.metasumer.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring_boot_final.metasumer.model.BusinessVO;
import com.spring_boot_final.metasumer.model.FishingSpotAreaVO;
import com.spring_boot_final.metasumer.model.FishingSpotVO;
import com.spring_boot_final.metasumer.service.BusinessService;
import com.spring_boot_final.metasumer.service.FishingSpotService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BusinessController {
  @Autowired
  private BusinessService businessService;
  @Autowired
  private FishingSpotService fishingSpotService;
  
  //사업자 메인 페이지(업체 1개일 때)
//  @RequestMapping("/business/businessMain")
//  public String businessMain(HttpSession session, Model model) {
//    
//    return "business/businessMain";
//  }

  @RequestMapping("/business/businessMain")
  public String businessMain(HttpSession session, Model model) {
      String memId = (String) session.getAttribute("memId");
      List<BusinessVO> businesses = businessService.getBusinessesByMemId(memId);

      // 사업체 목록을 모델에 추가
      model.addAttribute("businesses", businesses);
      return "business/businessMain";
  }
  

  @RequestMapping("/business/getAllBusiness")
  public String getAllBussiness(Model model) {
      ArrayList<BusinessVO> businesses = businessService.getAllBusiness();
      model.addAttribute("businesses", businesses);
      return "business/getAllBusiness";
  }
  
  // ////////////////////////////////////////////////////////////////////////////////////////
  
  // 낚시터 관리 페이지
  @RequestMapping("/business/fishingSpotManagement")
  public String fishingSpotManagement(@RequestParam("bizId") int bizId, Model model) {
    
      // 사업체의 낚시터 목록 조회
      List<FishingSpotVO> fishingSpots = fishingSpotService.getFishingSpotsByBizId(bizId);
      BusinessVO business = businessService.getBusinessByBizId(bizId);

      // 모델에 낚시터 목록 추가
      model.addAttribute("business", business);
      model.addAttribute("fishingSpots", fishingSpots);
      model.addAttribute("bizId", bizId);

      // 낚시터 관리 페이지 반환
      return "business/fishingSpotManagement";
  }
  
  //낚시터 등록 폼 페이지
  @RequestMapping("/business/fishingSpotRegister")
  public String showFishingSpotRegisterForm(Model model, @RequestParam("bizId") int bizId) {
      model.addAttribute("bizId", bizId);
      return "business/fishingSpotRegister";
  }
  
  //낚시터 등록 처리
  @RequestMapping("/business/registerFishingSpot")
  public String registerFishingSpot(
          @RequestParam("spotName") String spotName,
          @RequestParam("spotType") String spotType,
          @RequestParam("spotDescription") String spotDescription,
          @RequestParam("spotPrice") int spotPrice,
          @RequestParam("spotImage") MultipartFile spotImage,
          @RequestParam("openingTime") String openingTimeStr,
          @RequestParam("closingTime") String closingTimeStr,
          @RequestParam("spotZipcode") String spotZipcode,
          @RequestParam("spotAddress1") String spotAddress1,
          @RequestParam("spotAddress2") String spotAddress2,
          @RequestParam("spotHP1") String spotHP1,
          @RequestParam("spotHP2") String spotHP2,
          @RequestParam("spotHP3") String spotHP3,
          @RequestParam("spotFacility") String spotFacility,
          @RequestParam("bizId") int bizId,
          HttpSession session) throws ParseException, IOException {

     
      // 문자열을 Time 객체로 변환
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      Time openingTime = new Time(sdf.parse(openingTimeStr).getTime());
      Time closingTime = new Time(sdf.parse(closingTimeStr).getTime());
      
      // 파일 업로드 처리
      String savedFileName = "";
      if (!spotImage.isEmpty()) {  // 파일이 있을 경우 처리
          savedFileName = saveFile(spotImage);  // saveFile 메소드 호출
      }
      
      // 낚시터 등록 정보 설정
      FishingSpotVO fishingSpot = new FishingSpotVO();
      fishingSpot.setSpotName(spotName);
      fishingSpot.setSpotType(spotType);
      fishingSpot.setSpotDescription(spotDescription);
      fishingSpot.setSpotPrice(spotPrice);
      fishingSpot.setSpotImage(savedFileName);
      fishingSpot.setOpeningTime(openingTime);
      fishingSpot.setClosingTime(closingTime);
      fishingSpot.setSpotZipcode(spotZipcode);
      fishingSpot.setSpotAddress1(spotAddress1);
      fishingSpot.setSpotAddress2(spotAddress2);
      fishingSpot.setSpotHP1(spotHP1);
      fishingSpot.setSpotHP2(spotHP2);
      fishingSpot.setSpotHP3(spotHP3);
      fishingSpot.setSpotFacility(spotFacility);
      fishingSpot.setBizId(bizId);

      // 낚시터 등록 서비스 호출
      fishingSpotService.insertFishingSpot(fishingSpot);


      // 낚시터 관리 페이지로 리다이렉트
      return "redirect:/business/fishingSpotManagement?bizId=" + bizId;
  }
  
  //낚시터 수정 페이지로 이동
  @RequestMapping("/business/fishingSpotUpdate")
  public String showFishingSpotUpdateForm(@RequestParam("spotId") int spotId, @RequestParam("bizId") int bizId,
                                          Model model) {
     FishingSpotVO fishingSpot = fishingSpotService.detailFishingSpot(spotId);
     model.addAttribute("bizId", bizId);
     model.addAttribute("fishingSpot", fishingSpot);
     return "business/fishingSpotUpdate";
  }
  
  //낚시터 수정 처리
  @RequestMapping("/business/updateFishingSpot")
  public String updateFishingSpot(
      @RequestParam("spotId") int spotId,
      @RequestParam("spotName") String spotName,
      @RequestParam("spotType") String spotType,
      @RequestParam("spotDescription") String spotDescription,
      @RequestParam("spotPrice") int spotPrice,
      @RequestParam("spotImage") MultipartFile spotImage,
      @RequestParam("openingTime") String openingTimeStr,
      @RequestParam("closingTime") String closingTimeStr,
      @RequestParam("spotZipcode") String spotZipcode,
      @RequestParam("spotAddress1") String spotAddress1,
      @RequestParam("spotAddress2") String spotAddress2,
      @RequestParam("spotHP1") String spotHP1,
      @RequestParam("spotHP2") String spotHP2,
      @RequestParam("spotHP3") String spotHP3,
      @RequestParam("spotFacility") String spotFacility,
      @RequestParam("bizId") int bizId,
      HttpSession session) throws ParseException, IOException {

    // 기존 낚시터 정보 불러오기
    FishingSpotVO fishingSpot = fishingSpotService.detailFishingSpot(spotId);
  
    // 문자열을 Time 객체로 변환
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Time openingTime = new Time(sdf.parse(openingTimeStr).getTime());
    Time closingTime = new Time(sdf.parse(closingTimeStr).getTime());
  
    // 파일 업로드 처리 (이미지 업데이트 시)
    if (!spotImage.isEmpty()) {
        String savedFileName = saveFile(spotImage);
        fishingSpot.setSpotImage(savedFileName);
    }
  
    // 낚시터 정보 업데이트
    fishingSpot.setSpotName(spotName);
    fishingSpot.setSpotType(spotType);
    fishingSpot.setSpotDescription(spotDescription);
    fishingSpot.setSpotPrice(spotPrice);
    fishingSpot.setOpeningTime(openingTime);
    fishingSpot.setClosingTime(closingTime);
    fishingSpot.setSpotZipcode(spotZipcode);
    fishingSpot.setSpotAddress1(spotAddress1);
    fishingSpot.setSpotAddress2(spotAddress2);
    fishingSpot.setSpotHP1(spotHP1);
    fishingSpot.setSpotHP2(spotHP2);
    fishingSpot.setSpotHP3(spotHP3);
    fishingSpot.setSpotFacility(spotFacility);
    fishingSpot.setBizId(bizId);
  
    // 낚시터 업데이트 처리
    fishingSpotService.updateFishingSpot(fishingSpot);
  
    // 업데이트 후 낚시터 관리 페이지로 리다이렉트
    return "redirect:/business/fishingSpotManagement?bizId=" + bizId;
  }
  
  //낚시터 삭제 처리
  @RequestMapping("/business/deleteFishingSpot")
  public String deleteFishingSpot(@RequestParam("spotId") int spotId, 
                                  @RequestParam("bizId") int bizId,
                                  Model model) {
     model.addAttribute("bizId", bizId);
     fishingSpotService.deleteFishingSpot(spotId);
     return "redirect:/business/fishingSpotManagement?bizId=" + bizId;
  }

// //////////////////////////////////////////////////////////////////////////////
  
  // 낚시터 상품 관리 페이지
  @RequestMapping("/business/fishingSpotAreaManagement")
  public String showFishingSpotAreaManagement(@RequestParam("spotId") int spotId, Model model) {
     List<FishingSpotAreaVO> areas = fishingSpotService.getFishingSpotAreasBySpotId(spotId);
     FishingSpotVO spot = fishingSpotService.detailFishingSpot(spotId);
     model.addAttribute("spotName", spot.getSpotName());
     model.addAttribute("spotId", spotId);
     model.addAttribute("areas", areas);
     return "business/fishingSpotAreaManagement";
  }
  
  // 낚시터 상품 등록 폼 열기
  @RequestMapping("/business/fishingSpotAreaRegister")
  public String showFishingSpotAreaRegisterForm(@RequestParam("spotId") int spotId, Model model) {
      model.addAttribute("spotId", spotId);
      return "business/fishingSpotAreaRegister";
  }
  
  // 낚시터 상품 등록 처리
  @RequestMapping("/business/registerFishingSpotArea")
  public String registerFishingSpotArea(
      @RequestParam("spotId") int spotId,
      @RequestParam("areaName") String areaName,
      @RequestParam("areaPrice") int areaPrice,
      @RequestParam("minCapacity") int minCapacity,
      @RequestParam("maxCapacity") int maxCapacity,
      @RequestParam("startTime") String startTimeStr,
      @RequestParam("endTime") String endTimeStr,
      @RequestParam("fishType") String fishType) throws ParseException {

    // 문자열을 Time 객체로 변환
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Time startTime = new Time(sdf.parse(startTimeStr).getTime());
    Time endTime = new Time(sdf.parse(endTimeStr).getTime());
    
  
    // FishingSpotAreaVO 객체 생성 및 설정
    FishingSpotAreaVO fishingSpotArea = new FishingSpotAreaVO();
    fishingSpotArea.setSpotId(spotId);
    fishingSpotArea.setAreaName(areaName);
    fishingSpotArea.setAreaPrice(areaPrice);
    fishingSpotArea.setMinCapacity(minCapacity);
    fishingSpotArea.setMaxCapacity(maxCapacity);
    fishingSpotArea.setStartTime(startTime);
    fishingSpotArea.setEndTime(endTime);
    fishingSpotArea.setFishType(fishType);
  
    // 낚시터 구역 등록 처리
    fishingSpotService.insertFishingSpotArea(fishingSpotArea);
    
    return "redirect:/business/fishingSpotAreaManagement?spotId=" + spotId;
}
  
  //낚시터 구역 수정 폼 열기
  @RequestMapping("/business/fishingSpotAreaUpdate")
  public String updateFishingSpotAreaForm(@RequestParam("areaId") int areaId,
                                          Model model) {
      // 수정할 구역 정보 가져오기
      FishingSpotAreaVO area = fishingSpotService.getFishingSpotAreaById(areaId);
      model.addAttribute("area", area);
      return "business/fishingSpotAreaUpdate";
  }
  
  //낚시터 구역 수정 처리
  @RequestMapping("/business/updateFishingSpotArea")
  public String updateFishingSpotArea(FishingSpotAreaVO area, @RequestParam("spotId") int spotId) {
      // 구역 정보 업데이트
      fishingSpotService.updateFishingSpotArea(area);
      return "redirect:/business/fishingSpotAreaManagement?spotId=" + spotId;
  }

  // 낚시터 구역 삭제 처리
  @RequestMapping("/business/deleteFishingSpotArea")
  public String deleteFishingSpotArea(@RequestParam("areaId") int areaId, @RequestParam("spotId") int spotId) {
      // 구역 삭제 처리
      fishingSpotService.deleteFishingSpotArea(areaId);
      return "redirect:/business/fishingSpotAreaManagement?spotId=" + spotId;
  } 


  // /////////////////////////////////////////////////////////////////////
  private String saveFile(MultipartFile file) throws IOException {
    String uploadPath = "C:/springWorkspace/metasumer_images/";

    String originalFileName = file.getOriginalFilename();
    originalFileName = originalFileName.replace("[", "_").replace("]", "_");

    UUID uuid = UUID.randomUUID();
    String savedFileName = uuid.toString() + "_" + originalFileName;
    File uploadFile = new File(uploadPath + savedFileName);

    file.transferTo(uploadFile);

    return savedFileName;
  }

}
