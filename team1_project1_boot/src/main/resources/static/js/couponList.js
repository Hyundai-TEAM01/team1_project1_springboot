$(function () {
	// 처음 로딩 시 쿠폰목록 렌더링
	// 검색구분: 미사용: 0, 사용완료: 1, 기간만료: 2, 1페이지
    printCouponList('0', 1);
    
	
	// ***************** 검색 쿼리 설정 관련 *****************
	$("select[name=searchtype]").on('change', function(){
		printCouponList($("select[name=searchtype]").val(), 1);
	})
	
	// ***************** 쿠폰목록 렌더링 관련 *****************
	
	// 오늘 날짜
	function getToday() {
	   var d = new Date();
	   return getDateStr(d);
	}
	
	// 날짜 월,일 2자리수 리턴
	function getDateStr(myDate){
	   var year = myDate.getFullYear();
	   var month = ("0"+(myDate.getMonth()+1)).slice(-2);
	   var day = ("0"+myDate.getDate()).slice(-2);
	   return ( year + '-' + month + '-' + day );
	}
	
	// 쿠폰 발급일로부터 마감일 날짜
	function periodDay(regdate, days) {
	   var d = new Date(regdate);
	   var dayOfMonth = d.getDate();
	   d.setDate(dayOfMonth + days);
	   return getDateStr(d);
	}
	
	// 쿠폰 기간만료 날짜 확인
	function passPeriodDay(regdate, days) {
	   var today = getToday(); // 오늘 날짜
	   var d = periodDay(regdate, days); // 마감 날짜
	   return d <= today;
	}
	
	// 시간 제거
	function printDate(inputDate) {
		return inputDate.split(" ")[0];
	}
	
	// 금액 형태로 변환
    function wonChange(num) {
        return String(num).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
    }
    
    // 쿠폰목록 렌더링
    function printCouponList(searchType, pageNo) {
		$.ajax({
		url: 'getcouponlist',
		data: {	"searchType" : searchType, // 검색구분: 미사용: 0, 사용완료: 1, 기간만료: 2
				"pageNo" : pageNo} // 페이지 번호
		})
		.done((data) => {
			if(data && data.result) { // 데이터 잘 불러왔는가
				if(data.result !== 'fail' && data.result.length > 0) {
					let itemStr = "";
					
					// 1 : 1 관계
					data.result.forEach((item) => {
						let couno = item.couno;
						let couimgurl = item.couimgurl;
						let couname = item.couname;
						let coucontent = item.coucontent;
						let cousaletype = item.cousaletype;
						let coubenefit = item.coubenefit;
						let regdate = item.regdate;
						let couperiod = item.couperiod;
						let used = item.used;
						let usedate = item.usedate;
						// let coupricelimit = item.coupricelimit;
						
						itemStr += '<tr>';
                        itemStr += '<td rowspan="1" class="coupon-num">';
                        itemStr += '<p class="c-num">' + couno + '</p>';
                        itemStr += '</td>';
                        itemStr += '<td>';
                        itemStr += '<div class="cp-list">';
                        itemStr += '<a href="#"><img src="' + couimgurl + '" alt="쿠폰이미지" /></a>';
                        itemStr += '<div class="cp-info">';
                        itemStr += '<a href="#">';
                        itemStr += '<span class="cp-name">' + couname + '</span>';
                        itemStr += '</a>';
                        itemStr += '<p class="cp-content">' + coucontent + '</p>';
                        itemStr += '</div>';
                        itemStr += '</div>';
                        itemStr += '</td>';
                        if(cousaletype === "0") {
							itemStr += '<td>' + coubenefit + '%</td>';
						} else if(cousaletype === "1") {
							itemStr += '<td><i class="won sign icon small"></i>' + wonChange(coubenefit) + '</td>'; 
						} else if(cousaletype === "2") {
							itemStr += '<td>배송비무료</td>'; 
						} else {
							itemStr += '<td>타입에러</td>'; 
						}
                        itemStr += '<td class="expire">'; 
                        itemStr += '<p class="expire-date">';
                        itemStr += '<span class="cp-date sterm">' + printDate(regdate) + '</span>'
                        itemStr += '<span class="and-line">-</span>';
                        itemStr += '<span class="cp-date eterm">' + periodDay(regdate, couperiod) + '</span></p>';
                        // 미사용이면서 기간만료 아닌 경우
                        if(used === "0" && !passPeriodDay(regdate, couperiod)) {
							itemStr += '<p class="used-coupon"><span class="cp-notused">미사용</span></p>';
						} else if(used === "1" ) {
							itemStr += '<p class="used-coupon"><span class="cp-used">사용완료</span></p>';
						} else if(used === "0" && passPeriodDay(regdate, couperiod)) {
							itemStr += '<p class="used-coupon"><span class="cp-used">기간만료</span></p>'; 
						} else {
							itemStr += '<td>타입에러</td>'; 
						} 
						if(!!usedate) {
							itemStr += '<p class="used-date"><span class="cp-date cp-date-used">' + printDate(usedate) + '</span></p>';
						} else {
							itemStr += '<p class="used-date"><span class="cp-date cp-date-used"></span></p>';
						}
                        itemStr += '</td>';
                    	itemStr += '</tr>';
					});	
					$('#ctable').html(itemStr);	
					// 페이징 버튼 생성
					if(data.pagination.totalPageNo > 0){
			            setHtml(data.pagination); // 페이징 버튼 렌더링
			            setAction(data.pagination); // 페이징 버튼 기능 추가
			        }else{
			        	$(".paging").html(''); // 페이징 버튼 없애기
			        }
				} else { // 쿠폰목록 불러오기 실패 시
					let itemStr = '<tr></tr>';
					itemStr += '<td class="no-data" colspan="4">쿠폰내역이 없습니다.</td>';
					$('#ctable').html(itemStr);
					$(".paging").html('');
				}
			}
			else {
				let itemStr = '<tr></tr>';
				itemStr += '<td class="no-data" colspan="4">쿠폰내역이 없습니다.</td>';
				$('#ctable').html(itemStr);
				$(".paging").html('');
			}
			
		}); // done
		
	}
	
	// pager 생성
	function setHtml(pagination){
		$(".paging").html('');
		
		let pagingHtml = '';
		let firstPageBtn = '<a href="javascript:void(0);" class="prev2"><i class="angle double left icon"></i></a>';
		let prevPageBtn = '<a href="javascript:void(0);" class="prev"><i class="angle left icon"></i></a>';
		let pageBtn = '<span class="paging-num">';
		for(let i = pagination.startPageNo; i <= pagination.endPageNo; i++){
			let pageNum = i;
			let activeClass = '';
			if(pageNum == pagination.pageNo) activeClass = ' on ';
			pageBtn += '<a href="javascript:void(0);" class="pageBtn' + activeClass + '" pageNum="' + pageNum + '">' + pageNum + '</a>';
		}
		
		pageBtn += '</span>';
		
		let nextPageBtn = '<a href="javascript:void(0);" class="next"><i class="angle right icon"></i></a>';
        let endPageBtn = '<a href="javascript:void(0);" class="next2"><i class="angle double right icon"></i></a>';
        
        pagingHtml = firstPageBtn + prevPageBtn + pageBtn + nextPageBtn + endPageBtn;
        
        $(".paging").html(pagingHtml);
	}
	
	// 페이지 버튼에 기능 추가
	function setAction(pagination){
		let thisArea = $(".paging");
	    
        thisArea.find(".prev2").click(function(){
            goPage(1);
        });

        thisArea.find(".prev").click(function(){
            let pageNum = pagination.startPageNo - 1;
            if(pageNum < 1) pageNum = 1;
            goPage(pageNum);
        });

        thisArea.find(".pageBtn").click(function(){
            goPage($(this).attr("pageNum"));
        });

        thisArea.find(".next").click(function(){
            let pageNum = pagination.endPageNo + 1;
            if(pageNum > pagination.totalPageNo) pageNum = pagination.endPageNo;
            goPage(pageNum);
        });

        thisArea.find(".next2").click(function(){
            goPage(pagination.totalPageNo);
        });
	}
	
	// 페이지 버튼 클릭 시 pageNo 넘김
	function goPage(pageNo){
		$("#ctable").html(''); // tbody 주문목록 초기화
     	const searchType = $("select[name=searchtype]").val();
     	printCouponList(searchType, pageNo);
    };
});
/*
						<tr>
                        <td rowspan="1" class="coupon-num">
                            <p class="c-num">210928P10850546</p>
                        </td>
                        <td>
                            <div class="cp-list">
                                <a href="#"><img src="/resources/images/coupone.png" alt="상품이미지" /></a>
                                <div class="cp-info">
                                    <a href="#">
                                        <span class="cp-name">선착순50%할인쿠폰</span>
                                    </a>
                                    <p class="cp-content">선착순 10분에게 만 원 이상인 품목, 100일간 적용가능한 50%할인 쿠폰</p>
                                </div>
                            </div>
                        </td>
                        <td>50%</td>
                        <td class="expire"> 
                            <p class="expire-date"><span class="cp-date sterm">2021.09.28</span><span class="and-line">-</span><span class="cp-date eterm">2021.11.01</span></p>
                            <p class="used-coupon"><span class="cp-used">사용완료</span></p>
                            <p class="used-date"><span class="cp-date cp-date-used">(2021.09.28)</span></p>
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="1" class="coupon-num">
                            <p class="c-num">210928P10850546</p>
                        </td>
                        <td>
                            <div class="cp-list">
                                <a href="#"><img src="/resources/images/coupone.png" alt="상품이미지" /></a>
                                <div class="cp-info">
                                    <a href="#">
                                        <span class="cp-name">'만원의 행복'할인쿠폰</span>
                                    </a>
                                    <p class="cp-content">선착순 10분에게 3만원 이상인 품목, 만원 할인쿠폰</p>
                                </div>
                            </div>
                        </td>
                        <td><i class="won sign icon small"></i>10,000</td>
                        <td class="expire">
                            <p class="expire-date"><span class="cp-date sterm">2021.09.28</span><span class="and-line">-</span><span class="cp-date eterm">2021.11.06</span></p>
                            <p class="used-coupon"><span class="cp-notused">미사용</span></p>
                            <p class="used-date"><span class="cp-date cp-date-used"></span></p>
                        </td>
                    </tr>
                    
                    
                    
                    <div class="paging">
						<a href="#" class="prev2"><i class="angle double left icon"></i></a> <a
							href="#" class="prev"><i class="angle left icon"></i></a> <span
							class="paging-num"> <a href="#" class="pageBtn on">1</a> <a
							href="#" class="pageBtn">2</a>
						</span> <a href="#" class="next"><i class="angle right icon"></i></a> <a
							href="#" class="next2"><i class="angle double right icon"></i></a>
					</div>
*/