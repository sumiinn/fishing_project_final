package com.spring_boot_final.metasumer.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring_boot_final.metasumer.model.BusinessAuthVO;
import com.spring_boot_final.metasumer.service.MemberService;
import com.spring_boot_final.metasumer.service.OCRService;

@Controller
public class OcrAuthController {
	
	private OCRService ocrService;
	private MemberService mbService;
	
	@Autowired
	public OcrAuthController(OCRService ocrService, MemberService mbService) {
		this.ocrService = ocrService;
		this.mbService = mbService;
	}
	
	@RequestMapping("/memberOcr/businessAuth")
	public String businessAuth() {
		return "member/businessAuth";
	}

	@RequestMapping("/memberOcr/ocr")
	@ResponseBody
	public String ocr(@RequestParam("uploadFile") MultipartFile file) {
		String result = "";

		try {
			//String uploadPath = "D:/springWorkspace/metasumer_images/upload/";
			String uploadPath = "/usr/local/project/upload/"; 
			
			String originalFileName = file.getOriginalFilename();
			String filePathName = uploadPath + originalFileName;

			File newFile = new File(filePathName);
			file.transferTo(newFile);
			
			result = ocrService.ocrGeneral(filePathName);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	@RequestMapping("/memberOcr/checkMemId")
	@ResponseBody
	public String checkMemId(@RequestParam("memId") String memId) {
		
		int check = mbService.checkMemId(memId);
		String response = (check > 0) ? "pending" : "success";
		
		return response;
	}
	
	@RequestMapping("/memberOcr/insertBusinessAuth")
	public String insertBusinessAuth(@RequestParam("memId") String memId, @RequestParam("businessName") String businessName, @RequestParam("bizRegNumber") String bizRegNumber,
									@RequestParam("businessType") String businessType, @RequestParam("delegate") String delegate, @RequestParam("businessAddress") String businessAddress,
									@RequestParam("businessCategory") String businessCategory, @RequestParam("issueDate") String issueDate, @RequestParam("bizRegImg") String bizRegImg,
									@RequestParam("authStatus") String authStatus) {
	
		BusinessAuthVO vo = new BusinessAuthVO();
		vo.setMemId(memId);
		vo.setBusinessName(businessName);
		vo.setBizRegNumber(bizRegNumber);
		vo.setBusinessType(businessType);
		vo.setDelegate(delegate);
		vo.setBusinessAddress(businessAddress);
		vo.setBusinessCategory(businessCategory);
		vo.setIssueDate(issueDate);
		vo.setBizRegImg(bizRegImg);
		vo.setAuthStatus(authStatus);
		
		mbService.insertBusinessAuth(vo);
        
		return "redirect:/";
	}
}
