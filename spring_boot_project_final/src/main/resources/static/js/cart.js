/**
 * cart.js
 */
 
 $(function(){
      let amount = $('.amount');
	  let price = $('.price');
	  let shipping = 2500;
	  let sum = 0;
	  
      updateTotalAmount();
      
      // 선택한 상품만 주문
      $('#orderAll').on('click', function(event) {
          event.preventDefault();
          
          // 주문 금액이 0원 일때 주문 막기
          let totalAmount = Number($('.totalAmount').text().replace(/[^0-9]/g, ''));
          
          if (totalAmount == 0) {
              alert('장바구니에 상품을 담아주세요.');
              return; // 페이지 넘어가는 거 막기
          }

          // 선택한 상품만 서버로 전송하기 위한 새로운 form 생성
          let newForm = $('<form>', {
              'action': '/myPage/orderForm',
              'method': 'post'
          });
  
          // 선택된 상품의 cartNo와 cartQty 추가
          $('.itemCheckbox:checked').each(function() {
              let cartNo = $(this).val();
              let cartQty = $(this).closest('.cartItem').find('.quantity').val();

              newForm.append($('<input>', { // 자식 태그 생성. 문자열로 append 하는 방식도 있음
                  'type': 'hidden',
                  'name': 'cartNo[]',
                  'value': cartNo
              }));

              newForm.append($('<input>', {
                  'type': 'hidden',
                  'name': 'cartQty[]',
                  'value': cartQty
              }));
          });

          // 생성한 form을 body에 추가
          newForm.appendTo('body');
          // form 제출
          newForm.submit();
      });
      
      // [전체 선택] 체크박스 체크했을 때
   	  $('#selectAll').on('click', function() {
   		let chk = $('#selectAll').prop("checked"); // 체크 여부 확인
   			
   		// 체크 되었다면 (true) : 모든 개별 체크박스를 true로 설정
   		if(chk) {
   			$('.itemCheckbox').prop("checked", true);
   		} else {
   			$('.itemCheckbox').prop("checked", false);
   		}
   	  });
   		  	  
   	  //[전체 선택] 체크박스 체크했을때
	  $('.itemCheckbox').on('click', function(){
	 	let total = $('.itemCheckbox').length;
	 	let checked = $('.itemCheckbox:checked').length; 
	 		
	 	if( total != checked){ 
	 		$('#selectAll').prop("checked", false); // 해제
	 	}else{
	 		$('#selectAll').prop("checked", true); // 체크
	 	}
	  });
	  
      // 주문수량 변경 처리	  	
	  // 장바구니 수량 업데이트 (버튼 클릭 시 수행)
      $('.qtyUpdate').on('click', function() {  
         // 주문수량 수정 시
         $.each($('.quantity'), function(i) {        
            let qty = $(this).val();
            
            // 상품 구매 금액 업데이트
            price[i].dataset.amount = (price[i].dataset.price * qty); // 가격 value 업데이트
            price[i].innerHTML = (price[i].dataset.price * qty).toLocaleString() + ' 원'; // 표시 (천단위)
            
            // 총 상품 금액 업데이트
            amount[i].dataset.amount = (price[i].dataset.price * qty); // 가격 value 업데이트
            amount[i].innerHTML = (price[i].dataset.price * qty).toLocaleString(); // 표시 (천단위)                            
            
            sumAmount(); // 총 금액 계산           
            finalAmount(); 
         });
       
         // 수량 저장할 배열 선언
         let cartNos = [];
         let cartQtys = [];

         // cartNo, cartQty를 배열에 추가
         $('.quantityWrap').each(function() {
             let cartNo = $(this).find('input[name="cartNo[]"]').val(); // name="cartNo"인 hidden input 가져오기
             let cartQty = $(this).find('input[name="cartQty[]"]').val();                  
           
             cartNos.push(cartNo);
             cartQtys.push(cartQty);
         });     

         // 서버로 전송
         $.ajax({
             type: "POST",
             url: "/myPage/updateCart",
             data: {
                 "cartNo[]": cartNos,
                 "cartQty[]": cartQtys
             },
             dataType: 'text', 
             success: function(result) {
                 if (result == 1) {          
                     location.href = "/myPage/cartList"; // 장바구니 페이지로 이동
                 } else {
                     alert('장바구니 업데이트를 실패하였습니다.');
                 }
             },
             error: function() {
                 alert('실패');
             }
         });
      });	

      // 체크 여부에 따른 가격
      $('.itemCheckbox').on('change', function() {
          sumAmount();
          finalAmount();
      });
      
      // 전체 체크 여부에 따른 가격
      $('#selectAll').on('change', function() {
          sumAmount();
          finalAmount();           
      });
      
      //////////////////////////////////////////////////////////////////////
      // sum 계산하는 함수
      function sumAmount() {
          sum = 0;
          $('.itemCheckbox:checked').each(function() {
              let index = $('.itemCheckbox').index(this);
              sum += Number(amount[index].getAttribute("data-amount"));
          });       
      }      
      
      // 총 금액 출력 함수
      function finalAmount(){        
          let totalAmount = sum;
          
          if(totalAmount == 0){
             shipping = 0;
          }else if(totalAmount >= 50000){
             shipping = 0;
          }else{
             shipping = 2500;
          }
          
          totalAmount = sum + shipping;
          
          // 총 구매 예정 금액 출력
          $('.totalPrice').text(sum.toLocaleString() + '원');
          $('.totalAmount').text(totalAmount.toLocaleString() + '원');
          $('.totalShipping').text(shipping.toLocaleString() + '원');     
          $('.totalAmountOrder').text(totalAmount.toLocaleString() + '원 주문하기');         
      } 
      
      function updateTotalAmount() {
        // 상품 금액 계산
        sumAmount();
        finalAmount();
      }
   
      //////////////////////////////////////////////////////////////////////////////////
      // 삭제 버튼 클릭 시
      $('#deleteSelected').on('click', function(){
         // 체크 여부 확인 : 하나라도 체크하면 true, 아무 것도 체크하지 않으면 false
	     let checked = $('.itemCheckbox').is(':checked');
         if(checked){
           if(confirm('선택한 상품을 장바구니에서 삭제하시겠습니까?')){
		    // 체크된 체크박스의 cartNo를 배열에 추가
	 	    let checkArr = new Array();
	 		  $('.itemCheckbox:checked').each(function() {
	 		   	 checkArr.push($(this).val()); 
	 		  });	
 				
 			  // 서버로 전송
 		  	  $.ajax({
 				  type:"DELETE",
				  url:"/myPage/deleteCart", 
			 	  data : {"chkbox":checkArr}, /* 컨트롤러에서 받는 이름 chkbox  */
			 	  dataType:'text', 
			 	  success:function(result) {
			 		  if(result == 1) {						 		      
			 			  updateTotalAmount();
			 			  location.href= "/myPage/cartList";
			 		  }
			 	  },
			 	  error:function() {
			 		  alert("실패");
			 	  } 	 					
	 		  }); // ajax 종료
	       }else{
		      alert('삭제를 취소합니다.');
	       }
         }else{
           alert("선택된 상품이 없습니다");
         }	   
      });    
      
      $('.closeBtn').click(function(){
        let cartNo = $(this).data('cartno'); // cartNo 가져오기
        
        if(confirm("이 상품을 장바구니에서 삭제하시겠습니까?")) {
            $.ajax({
                type: 'DELETE',
                url: '/myPage/cartDelete',  // 상품 삭제를 처리할 URL
                data: {cartNo: cartNo},
                success: function(result) {
                    if(result == "success") {                                              
                        updateTotalAmount();
                        location.href= "/myPage/cartList";
                    } else {
                        alert("상품 삭제에 실패했습니다.");
                    }
                },
                error: function() {
                    alert("실패");
                }
            });
        }
      });       
                 
 });