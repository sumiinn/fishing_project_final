package com.spring_boot_final.metasumer.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring_boot_final.metasumer.service.WeatherService;
import com.spring_boot_final.metasumer.service.WebScrapingService;

@Controller
public class WeatherController {
	
	private WebScrapingService scrapingService;
	private WeatherService weatherService;
	
	public WeatherController(WebScrapingService scrapingService,
										 	WeatherService weatherService) {
		this.scrapingService = scrapingService;
		this.weatherService = weatherService;
	}
	
	// 현재 위치 날씨 페이지
	@RequestMapping("/weather/current") 
	public String currentWeatherView() {
		return "weather/weatherCurrent";
	}
	
	// 기상 특보 페이지
	@RequestMapping("/weather/warning")
	public String weatherWarningView(Model model) {
		HashMap<String, String> warningList = scrapingService.jsoupText();
		String[] content = warningList.get("content").split("○");
		String[] subtitle = new String[content.length];
		
		// 각 특보 내용의 제목에 스타일 따로 부여하기 위해 -> 제목과 내용으로 나눔
		for(int i=0; i<content.length; i++) {
			String[] temp = content[i].split(":");
			subtitle[i] = temp[0];
			content[i] = content[i].replace(temp[0], "");
			content[i] = content[i].replace(",", " /");
		}
		
		model.addAttribute("warningList", warningList);
		model.addAttribute("content", content);
		model.addAttribute("subtitle", subtitle);
		
		return "weather/weatherWarning";
	}
	
	// 물때 페이지
	@RequestMapping("/weather/tidetable")
	public String weatherTidetableView(Model model) {
		HashMap<String, String> lunarDate = weatherService.getLunarDate();
		
		model.addAttribute("lunarDate", lunarDate);
		
		return "weather/weatherTidetable";
	}
	
}
