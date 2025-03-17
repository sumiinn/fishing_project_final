package com.spring_boot_final.metasumer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring_boot_final.metasumer.model.BusinessVO;
import com.spring_boot_final.metasumer.model.InterestTagVO;
import com.spring_boot_final.metasumer.model.MyFishRecordsVO;
import com.spring_boot_final.metasumer.model.ProductVO;
import com.spring_boot_final.metasumer.service.IndexService;
import com.spring_boot_final.metasumer.service.SearchTrendService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	private SearchTrendService searchService;

	public IndexController(SearchTrendService searchService) {
		this.searchService = searchService;
	}

	@Autowired
	IndexService iService;

	@RequestMapping("/")
	public String tempIndex(Model model, HttpSession session) {

		String memId = (String) session.getAttribute("sid");

		ArrayList<MyFishRecordsVO> mfList = iService.listBestMemFishRecords();
		ArrayList<BusinessVO> bizList = iService.listBestBusinessRank();
		ArrayList<MyFishRecordsVO> newMfList = iService.listNewMemFishRecords();
		ArrayList<ProductVO> newPrdList = iService.listNewProducts();
		ArrayList<ProductVO> recPrdList = iService.listUserRecProducts();

		List<InterestTagVO> tagBanner = iService.tagBannerList(memId);
		
		List<String> NB = Arrays.asList("nb_001.png", "nb_002.png", "nb_003.png", "nb_004.png", "nb_005.png");
		List<String> URL = Arrays.asList("/myFishRecords/myFishRecordsListView", "/member/userTagSelection", "/", "/", "/");

		if (tagBanner == null || tagBanner.isEmpty()) {
			model.addAttribute("NB", NB);
			model.addAttribute("URL", URL);
		} else {
			model.addAttribute("tagBanner", tagBanner);
		}
		
		model.addAttribute("mfList", mfList);
		model.addAttribute("bizList", bizList);
		model.addAttribute("newMfList", newMfList);
		model.addAttribute("newPrdList", newPrdList);
		model.addAttribute("recPrdList", recPrdList);


		return "index";
	}
	
	@ResponseBody
	@RequestMapping("/topSearch")
	public String topSearch(@RequestParam String keyword, Model model) {
		model.addAttribute(keyword);
		return "topSearchResult";
	}

}
