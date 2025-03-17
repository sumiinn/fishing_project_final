package com.spring_boot_final.metasumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring_boot_final.metasumer.service.LocationSearchService;

@Controller
public class LocationController {
	private LocationSearchService locationSearchService;
	
	public LocationController(LocationSearchService locationSearchService) {
		this.locationSearchService = locationSearchService;
	}

	// 위치 정보 기본 페이지 : 현재 위치 정보 제공
	@RequestMapping("/location/overview") 
	public String locationInfo() {
		return "locInfo/locationView";
	}
	
	// 근처 낚시터
	@RequestMapping("/location/nearFish")
	public String nearFishingLocation(Model model) {
		String searchResult = locationSearchService.searchLoc();
		model.addAttribute("searchResult", searchResult);
		return "locInfo/nearFishView";
	}
	
	// 주변 먹거리
	@RequestMapping("/location/nearFood")
	public String nearFoodLocation() {
		return "locInfo/nearFoodView";
	}
	
	// 주변 놀거리
	@RequestMapping("/location/nearPlay")
	public String nearPlayLocation() {
		return "locInfo/nearPlayView";
	}
	
	// 주변 숙박시설
	@RequestMapping("/location/nearHotel")
	public String nearHotelLocation() {
		return "locInfo/nearHotelView";
	}
	
	// 교통 상황
	@RequestMapping("/location/traffic")
	public String trafficLocation() {
		return "locInfo/trafficLocationView";
	}
	
	// 전국 낚시터
	@RequestMapping("/location/nationwide")
	public String nationwideLocation() {
		return "locInfo/nationwideLocationView";
	} 
}
