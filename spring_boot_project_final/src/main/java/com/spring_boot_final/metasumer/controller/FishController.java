package com.spring_boot_final.metasumer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring_boot_final.metasumer.model.FishDocument;
import com.spring_boot_final.metasumer.model.FishVO;
import com.spring_boot_final.metasumer.service.FishSearchService;
import com.spring_boot_final.metasumer.service.FishService;

@Controller
public class FishController {
	@Autowired
	FishService fishService;
	@Autowired
	FishSearchService fishSearchService;
	
	@GetMapping("/fish")
	public String fishInfo(@RequestParam(defaultValue="1") int page, Model model) {		
		int fishPerPage = 9; // 한 페이지에 출력할 물고기 수	    	    
	    int totalFishCount = fishService.getFishCount(); // 총 어종 수
	    int totalPages = (int) Math.ceil((double) totalFishCount / fishPerPage); // 총 페이지 수
	    
	    // 페이지 초과 방지
	    if (page > totalPages) {
            page = totalPages; 
        } else if (page < 1) {
            page = 1;
        }
	    
	    int offset = (page - 1) * fishPerPage;
	    
	    ArrayList<FishVO> fishList = fishService.listAllFish(offset, fishPerPage);
	    
	    model.addAttribute("fishList", fishList);	      	    
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);		
		
		return "fishInfo/fishListView";
	}
	
	// 카테고리 별 어종 출력
	@GetMapping("/fish/fishCtgList/{fishCtgId}")
	public String fishCtgListView(@PathVariable String fishCtgId, Model model) {
		ArrayList<FishVO> fishList = fishService.fishCtgList(fishCtgId);

		// 뷰 페이지에 출력하기 위해 Model 설정
		model.addAttribute("fishList", fishList);

		return "fishInfo/fishListView";
	}
	
	// 어종 상세 페이지 이동
	@GetMapping("/fish/detailViewFish/{fishNo}")
	public String detailViewFish(@PathVariable("fishNo") String fishNo, Model model) {
		FishVO fish = fishService.detailViewFish(fishNo);
		
		// 뷰 페이지에 출력하기 위해 Model 설정
		model.addAttribute("fish", fish);

		return "fishInfo/fishDetailView";
	}
	
	// 어종 검색 API	
	@GetMapping("/fish/fishSearch")
	public ResponseEntity<List<FishDocument>> fishSearch(@RequestParam("keyword") String keyword) {
		List<FishDocument> fishList = fishSearchService.searchFish(keyword);
        
        // 검색 결과 없을 때
        if (fishList.isEmpty()) { 
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(fishList);
        }
    }

}
