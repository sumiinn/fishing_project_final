/**
 * top.js
 */
 
  $(document).ready(function() {
  
  	$('.topSearch').on('submit', function() {
  		event.preventDefault();
  		
  		let searchTxt = $('.searchTxt').val();
  		
  		$.ajax({
            type: 'get',
            url: '/topSearch',
            data:{ 
                "keyword": searchTxt
            },
            success:function(result) {                           
                location.href = "/topSearchResult";                                                   
            },
            error:function() {
                alert('실패');
            }
  		});
  		
  	});
  
  }