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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring_boot_final.metasumer.model.CartVO;
import com.spring_boot_final.metasumer.model.MemberVO;
import com.spring_boot_final.metasumer.model.OrderInfoVO;
import com.spring_boot_final.metasumer.model.OrderProductVO;
import com.spring_boot_final.metasumer.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	CartService cartService;
	
	// 장바구니 목록 보기 
	@GetMapping("/product/cartList")
	public String cartList(Model model, HttpSession session) {
		// 회원에 해당하는 cartList 출력
		String memId = (String)session.getAttribute("sid");
		ArrayList<CartVO> cartList = cartService.cartList(memId);
		
		// 주문서에 출력할 회원 정보 가져오기
		MemberVO memVo = cartService.getMemberInfo(memId);		

		// model 설정
		model.addAttribute("memVo", memVo);
		model.addAttribute("cartList", cartList);
		
		// 카트에 담긴 상품 개수 출력
		long prdCount = cartList.stream().map(CartVO::getPrdNo).distinct().count();
		model.addAttribute("prdCount", prdCount);

		return "myPage/cartListView";
	}
	
	// 장바구니에 상품 추가	
	@PostMapping("/myPage/insertCart")
	@ResponseBody
	public Map<String, Object> insertCart(@RequestParam String prdNo, @RequestParam int cartQty, HttpSession session) {
		// 로그인 성공 시
		String memId = (String) session.getAttribute("sid");
		
		Map<String, Object> response = new HashMap<>();
	    
	    if (memId == null) {
	    	response.put("status", "fail");
	        response.put("message", "로그인이 필요합니다.");
	        return response;
	    }
		
		CartVO vo = new CartVO();
		
		vo.setMemId(memId);
		vo.setPrdNo(prdNo);
	    vo.setCartQty(cartQty);

	    int count = cartService.checkPrdInCart(prdNo, memId);

	    if (count == 0) {
	        cartService.insertCart(vo);
	        response.put("status", "success");
	        response.put("message", "장바구니에 추가되었습니다.");
	    } else {
	        cartService.updateQtyInCart(vo);
	        response.put("status", "success");
	        response.put("message", "장바구니에 추가되었습니다.");
	    }

	    return response; 
	}
	
	// 장바구니 수량 변경
	@PostMapping("/myPage/updateCart")
	@ResponseBody
	public int updateCart(@RequestParam int[] cartNo,
                          @RequestParam int[] cartQty) {
		int result = 0;
		
		// 수량 업데이트 (변경 버튼 누르면)
	    for (int i = 0; i < cartNo.length; i++) {	    	
	        CartVO vo = new CartVO();
	        vo.setCartNo(cartNo[i]);
	        vo.setCartQty(cartQty[i]);
	        cartService.updateCart(vo);	        
	        result = 1;
	    }
	    
	    return result;	    
	}
	
	// 장바구니에서 상품 삭제
	@DeleteMapping("/myPage/deleteCart")
	@ResponseBody
	public int deleteCart(@RequestParam("chkbox[]") ArrayList<String> chkArr) {
		int result = 0;

		// 서비스에게 ArrayList 그대로 전달
		if (chkArr != null) {
			cartService.deleteCart(chkArr);
			result = 1;
		}

		return result;
	}
	
	// 주문서 열기
	@PostMapping("/myPage/orderForm")
	public String orderForm(@RequestParam("cartNo[]") ArrayList<Integer> cartNo, @RequestParam("cartQty[]") ArrayList<Integer> cartQty, 
			                Model model, HttpSession session) {
		String memId = (String) session.getAttribute("sid");

		// 주문서에 출력할 회원 정보 가져오기
		MemberVO memVo = cartService.getMemberInfo(memId);
		// 전화번호 설정
		String[] hp = (memVo.getMemHP()).split("-");
		
		// 선택한 상품만 주문서로 이동
		ArrayList<CartVO> selectedCartList = cartService.selectedCartList(cartNo);	  			
	    
	    // model 설정
	    model.addAttribute("memVo", memVo);
	    model.addAttribute("hp1", hp[0]);
	    model.addAttribute("hp2", hp[1]);
	    model.addAttribute("hp3", hp[2]);
	    model.addAttribute("cartNos", cartNo);
	    model.addAttribute("cartList", selectedCartList);	

		return "myPage/orderForm";
	}

	// 주문완료
	@PostMapping("/myPage/orderComplete")
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

		// 주문 완료 페이지에서 사용할 Date 타입 설정
		SimpleDateFormat dayTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime2 = dayTime2.format(new Date(timeNum));

		// 모델에 추가
		model.addAttribute("ordNo", ordNo);
		model.addAttribute("ordDate", strTime2);

		return "myPage/orderCompleteView";
	}
	
	// 주문 내역 보기 
	@GetMapping("/myPage/orderList")
	public String orderList(String period, Model model, HttpSession session) {
		// 회원에 해당하는 orderList 출력
		String memId = (String) session.getAttribute("sid");
		
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

	    ArrayList<OrderProductVO> orderList = cartService.orderListFiltered(memId, startDate, endDate);
	    MemberVO memVo = cartService.getMemberInfo(memId);

	    model.addAttribute("memVo", memVo);
	    model.addAttribute("orderList", orderList);

	    return "myPage/orderListView"; 		
	}
	
	@DeleteMapping("/myPage/cartDelete")
	@ResponseBody  
	public String deleteCartItem(@RequestParam("cartNo") int cartNo, HttpSession session) {	    
	    String result = "fail";
	    
	    try {
	        String memId = (String) session.getAttribute("memId");
	        
	        cartService.deleteCartItem(memId, cartNo);
	        
	        result = "success";
	    } catch (Exception e) {
	    	result = "fail";
	    }
	    
	    return result;  
	}

}
