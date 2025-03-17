package com.spring_boot_final.metasumer.controller;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring_boot_final.metasumer.model.FishingDetailVO;
import com.spring_boot_final.metasumer.model.FishingVO;
import com.spring_boot_final.metasumer.model.ProductVO;
import com.spring_boot_final.metasumer.service.FishingService;

@Controller
public class FishingController {
	@Autowired
	FishingService fishingService;
	
	/*
	// 초기 페이지 설정	
	@RequestMapping("/fishing") public String fishing(Model model) { 
		return "redirect:/fishing/fishingCtgList/1"; 	 
	}
	*/
	
	/*
	// 카테고리별 낚시 정보
	@RequestMapping("/fishing/fishingCtgList/{fishingCtgId}")
	public String fishingCtgList(@PathVariable String fishingCtgId, Model model) {
		ArrayList<FishingVO> fishingList = fishingService.fishingCtgList(fishingCtgId);
		ArrayList<FishingDetailVO> fishingDetailList = fishingService.fishingDetail(fishingCtgId);
		model.addAttribute("fishingList", fishingList);
		model.addAttribute("fishingDetailList", fishingDetailList);

		return "fishingInfo/fishingListView";
	}
	*/
		
	// 메인-서브 카테고리
    @GetMapping("/fishing/fishingCtgList/{fishingCtgId}/{fishingDetailCtgId}")
    public String fishingDetailCtgList(@PathVariable String fishingCtgId,
                                       @PathVariable String fishingDetailCtgId, 
                                       Model model) {
        ArrayList<FishingVO> fishingList = fishingService.fishingCtgList(fishingCtgId);
        ArrayList<FishingDetailVO> fishingDetailList = fishingService.fishingDetail(fishingCtgId);

        // 세부 카테고리 필터링
        Iterator<FishingVO> iterator = fishingList.iterator();
        while (iterator.hasNext()) {
            FishingVO fishing = iterator.next();
            if (!fishing.getFishingDetailCtgId().equals(fishingDetailCtgId)) {
                iterator.remove();
            }
        }       
        
        // 랜덤 상품 추천
        ArrayList<ProductVO> randomProducts = fishingService.getRandomProducts(fishingDetailCtgId);

        model.addAttribute("fishingList", fishingList);
        model.addAttribute("fishingDetailList", fishingDetailList);
        model.addAttribute("randomProducts", randomProducts);

        return "fishingInfo/fishingInfoView";
    }

}
