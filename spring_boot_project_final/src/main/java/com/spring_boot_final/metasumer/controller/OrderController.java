package com.spring_boot_final.metasumer.controller;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring_boot_final.metasumer.model.FishingSpotAreaVO;
import com.spring_boot_final.metasumer.model.MemberVO;
import com.spring_boot_final.metasumer.model.OrderVO;
import com.spring_boot_final.metasumer.model.ProductVO;
import com.spring_boot_final.metasumer.model.ReservationVO;
import com.spring_boot_final.metasumer.service.CartService;
import com.spring_boot_final.metasumer.service.FishingSpotService;
import com.spring_boot_final.metasumer.service.OrderService;
import com.spring_boot_final.metasumer.service.ProductService;
import com.spring_boot_final.metasumer.service.ReservationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
  @Autowired
  private ProductService prdService;
  @Autowired
  private OrderService orderService;
  @Autowired
  private ReservationService reservationService;
  @Autowired
  private FishingSpotService fishingSpotService;
  @Autowired
  private CartService cartService;
  
  @RequestMapping("/order/productOrder")
  public String productOrder(@RequestParam("prdNo") String prdNo,
                             @RequestParam("prdPrice") int prdPrice,
                             @RequestParam("quantity") int quantity,
                             HttpSession session, Model model) {
      String memId = (String) session.getAttribute("memId");
 
      if (memId != null) {
          MemberVO member = orderService.getMemberById(memId);
          model.addAttribute("member", member);
          
          ProductVO product = prdService.detailViewProduct(prdNo);
          
          // 총 상품 가격 계산
          int productTotal = prdPrice * quantity;

          // 배송비 결정
          int deliveryCost = productTotal >= 50000 ? 0 : 3000;

          // 총 결제 금액 계산
          int finalTotalPrice = productTotal + deliveryCost;

          // 모델에 추가
          model.addAttribute("product", product);
          model.addAttribute("quantity", quantity);
          model.addAttribute("productTotal", productTotal);
          model.addAttribute("deliveryCost", deliveryCost);
          model.addAttribute("finalTotalPrice", finalTotalPrice);
          
          return "/order/productOrder";  
      } else {
          return "redirect:/member/loginForm";  // 로그인 페이지로 리다이렉트
      }
  }
  
  /*
  @RequestMapping("/order/productComplete")
  public ModelAndView productComplete(
      @RequestParam("prdNo") String prdNo,
      @RequestParam(value = "quantity", defaultValue = "0") int quantity,
      @RequestParam(value = "ordererName", defaultValue = "") String ordererName,
      @RequestParam(value = "ordererHp1", defaultValue = "") String ordererHp1,
      @RequestParam(value = "ordererHp2", defaultValue = "") String ordererHp2,
      @RequestParam(value = "ordererHp3", defaultValue = "") String ordererHp3,
      @RequestParam(value = "ordererEmail", defaultValue = "") String ordererEmail,
      @RequestParam(value = "shippingZipcode", defaultValue = "") String shippingZipcode,
      @RequestParam(value = "shippingAddress1", defaultValue = "") String shippingAddress1,
      @RequestParam(value = "shippingAddress2", defaultValue = "") String shippingAddress2,
      @RequestParam(value = "finalTotalPrice", defaultValue = "0") int finalTotalPrice,
      @RequestParam(value = "deliveryCost", defaultValue = "0") int deliveryCost,
      @RequestParam(value = "prdPrice", defaultValue = "0") int prdPrice
  ) {
      // 상품 정보 조회
      ProductVO product = prdService.detailViewProduct(prdNo);
      int bizId = product.getBizId(); // 상품의 사업자 ID를 가져온다.

      // OrderVO 객체 생성 및 데이터 설정
      OrderVO order = new OrderVO();
      order.setPrdNo(prdNo);
      order.setQuantity(quantity);
      order.setOrderDate(new Date()); // 현재 시간
      order.setBuyerName(ordererName);
      order.setBuyerHP(ordererHp1 + "-" + ordererHp2 + "-" + ordererHp3);
      order.setBuyerEmail(ordererEmail);
      order.setShippingZipcode(shippingZipcode);
      order.setShippingAddress1(shippingAddress1);
      order.setShippingAddress2(shippingAddress2);
      order.setBizId(bizId);
      order.setTotalPrice(finalTotalPrice);

      // Order 저장
      orderService.saveOrder(order);

      // SalesVO 객체 생성 및 데이터 설정
      SalesVO sale = new SalesVO();
      sale.setPrdNo(prdNo);
      sale.setSaleDate(new Date()); // 현재 날짜
      sale.setSaleQuantity(quantity);
      sale.setSalePrice(prdPrice * quantity);
      sale.setBizId(bizId);

      // Sales 저장
      salesService.saveSale(sale);
      
      
      return new ModelAndView("redirect:/product/productList");
  }
  */
  
  @RequestMapping("/order/productCompletes")
  public String orderCompletes(OrderVO ordVo, @RequestParam String hp1, 
                              @RequestParam String hp2, @RequestParam String hp3, 
                              @RequestParam int quantity, @RequestParam String prdNo,
                              Model model) {
    // 전화번호
    String hp = hp1 + "-" + hp2 + "-" + hp3;
    ordVo.setOrdRcvPhone(hp);

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
    ordVo.setOrdNo(ordNo);    
    
    ordVo.setOrdQty(quantity);

    // 주문 정보 저장
    cartService.insertOrderInfo2(ordVo);
    
    // 재고 업데이트
    try {
        ProductVO product = prdService.detailViewProduct(prdNo);
        int updatedStock = product.getPrdStock() - quantity;

        if (updatedStock < 0) {
            // 재고 부족 시 예외 처리
            throw new RuntimeException("재고가 부족합니다.");
        }

        ProductVO updatedProduct = new ProductVO();
        updatedProduct.setPrdNo(prdNo);
        updatedProduct.setPrdStock(updatedStock);
        orderService.updateProductStock(updatedProduct);  // 재고 업데이트
    } catch (Exception e) {
        // 에러 처리
        model.addAttribute("status", "error");
        model.addAttribute("message", "재고 업데이트 중 오류 발생: " + e.getMessage());
        return "myPage/orderCompleteView";
    }

    // 주문 완료 페이지에서 사용할 Date 타입 설정
    SimpleDateFormat dayTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strTime2 = dayTime2.format(new Date(timeNum));

    // 모델에 추가
    model.addAttribute("ordNo", ordNo);
    model.addAttribute("ordDate", strTime2);

    return "myPage/orderCompleteView";
  }
  
  
  ////////////////////////////////////////////////////////////
  // 예약
  
  @RequestMapping("/order/reservationComplete")
  public String reservationComplete(
      @RequestParam(value = "spotId", defaultValue = "0") int spotId,
      @RequestParam(value = "resDate", defaultValue = "1970-01-01") String resDateString,
      @RequestParam(value = "bizId", defaultValue = "0") int bizId,
      @RequestParam(value = "areaId") String[] areaIds,
      @RequestParam(value = "resQuantity") int[] resQuantities, // 각 구역별 수량 배열
      @RequestParam(value = "finalTotalPrice", defaultValue = "0") int finalTotalPrice,
      @RequestParam(value = "resNum", defaultValue = "1") int resNum,
      HttpSession session,
      Model model
) {
  String memId = (String) session.getAttribute("memId");

  // 문자열을 Date 객체로 변환
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  java.sql.Date resDate = null;
  try {
      resDate = new java.sql.Date(dateFormat.parse(resDateString).getTime());
  } catch (ParseException e) {
      e.printStackTrace();
      return "error";
  }

  // areaId에 대한 정보를 미리 가져오기
  Map<Integer, FishingSpotAreaVO> areaMap = new HashMap<>();
  for (String areaIdStr : areaIds) {
      int areaId = Integer.parseInt(areaIdStr.trim());
      FishingSpotAreaVO area = fishingSpotService.getFishingSpotAreaById(areaId);
      if (area != null) {
          areaMap.put(areaId, area);
      }
  }

  // 각 구역별로 예약을 처리
  for (int i = 0; i < areaIds.length; i++) {
      int areaId = Integer.parseInt(areaIds[i]);
      int quantity = resQuantities[i];  // 해당 구역의 수량을 가져옴
      FishingSpotAreaVO area = areaMap.get(areaId);

      if (area != null) {
          ReservationVO reservation = new ReservationVO();

          reservation.setMemId(memId);
          reservation.setMemName(orderService.getMemberById(memId).getMemName());
          reservation.setSpotId(spotId);
          reservation.setResDate(resDate);
          reservation.setResNum(resNum);  // resNum 값이 제대로 전달되도록 처리
          reservation.setResPrice(area.getAreaPrice() * quantity);  // 구역 가격 * 수량
          reservation.setResCoupon(""); // 쿠폰 정보가 있는 경우 추가
          reservation.setResStatus("pending");
          reservation.setBizId(bizId);
          reservation.setResQuantity(quantity);  // 각 구역별 수량 저장
          reservation.setAreaId(areaId);
          reservation.setResStart(area.getStartTime());
          reservation.setResEnd(area.getEndTime());

          // Reservation 테이블에 저장
          reservationService.saveReservation(reservation);
      }
  }

  model.addAttribute("message", "결제되었습니다.");
  return "redirect:/myPage/reservation";
}

  
}

  
  