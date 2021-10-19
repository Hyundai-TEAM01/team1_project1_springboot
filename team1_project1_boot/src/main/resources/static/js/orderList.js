$(function () {
	// 처음 로딩 시 주문목록 렌더링
	// 7일전 기준, 상품명, "" : 전체, 1페이지
    printOrderList(prevDay(7), getToday(), "productName", "", 1);
    initDate();
    
    // ***************** 검색 쿼리 버튼 클릭 이벤트 관련 *****************
	$(".order-form").on('submit', function(e){
     	e.preventDefault(); // form 태그 페이지 이동 막기
     	const sterm = $("input[name=startdate]").val();
     	const eterm = $("input[name=enddate]").val();
     	const searchType = $("select[name=searchtype]").val();
     	const searchWord = $("input[name=search]").val();
     	printOrderList(sterm, eterm, searchType, searchWord, 1);
  	});
	
	// ***************** 검색 쿼리 날짜 설정 관련 *****************
	// 초기 날짜 7일전으로 설정
	function initDate() {
    	$("input[name=startdate]").val(prevDay(7));
    	$("input[name=enddate]").val(getToday());
	}
	
	// 날짜 선택 버튼
	$(".date-button").click(function(e) {
		e.preventDefault();
		// 선택된 버튼만 class 추가
		$(".date-button").removeClass("date-button-on");
		e.target.classList.add("date-button-on");
		if(e.target.id ==="setdatebtn1") {
			$("input[name=startdate]").val(prevDay(7));
		} 
		else if (e.target.id ==="setdatebtn2") {
			$("input[name=startdate]").val(prevMonth(1));
		} 
		else if (e.target.id ==="setdatebtn3") {
			$("input[name=startdate]").val(prevMonth(3));
		} 
		$("input[name=enddate]").val(getToday());
	})
	
	// 오늘 날짜
	function getToday() {
	   var d = new Date();
	   return getDateStr(d);
	}
	
	// 오늘로부터 며칠전 날짜
	function prevDay(days) {
	   var d = new Date();
	   var dayOfMonth = d.getDate();
	   d.setDate(dayOfMonth - days);
	   return getDateStr(d);
	}
	
	// 오늘로부터 몇개월전 날짜
	function prevMonth(month) {
	   var d = new Date();
	   var monthOfYear = d.getMonth();
	   d.setMonth(monthOfYear - month);
	   return getDateStr(d);
	}
	
	// 날짜 월,일 2자리수 리턴
	function getDateStr(myDate){
	   var year = myDate.getFullYear();
	   var month = ("0"+(myDate.getMonth()+1)).slice(-2);
	   var day = ("0"+myDate.getDate()).slice(-2);
	   return ( year + '-' + month + '-' + day );
	}
	
	// ***************** 주문목록 렌더링 관련 *****************
	// 시간 제거
	function printDate(inputDate) {
		return inputDate.split(" ")[0];
	}
	
	// 금액 형태로 변환
    function wonChange(num) {
        return String(num).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
    }
    
    // 검색결과 하이라이트
    function hightLight(inputStr, searchType, searchWord) {
		if(searchWord.length > 0) {
			if(searchType === 'pname') {
				const regex = new RegExp(searchWord, "gi");
				inputStr = String(inputStr).replace(regex, "<span class='highlight'>" + searchWord + "</span>"); // highlight class
			} else if (searchType === 'porderno') {
				const regex = new RegExp(searchWord, "g");
				inputStr = String(inputStr).replace(regex, "<span class='highlight'>" + searchWord + "</span>"); // highlight class
			}
		} 
		return inputStr;
	}
    
    // 주문목록 렌더링
    function printOrderList(sterm, eterm, searchType, searchWord, pageNo) {
		$.ajax({
		url: 'getorderlist',
		data: {	"sterm" : sterm, // 조회 시작날짜
				"eterm" : eterm, // 조회 종료날짜
				"searchType" : searchType, // 검색구분 : 상품명, 주문번호
				"searchWord" : searchWord, // 검색명
				"pageNo" : pageNo} // 페이지 번호
		})
		.done((data) => {
			if(data && data.result) { // 데이터 잘 불러왔는가
				if(data.result !== 'fail' && data.result.length > 0) {
					let itemStr = "";
					
					// 1 : M 관계 -- 3개의 주문 각 주문 마다 여러 개의 아이템
					data.result.forEach((item) => {
						let porderno = item.porderno;
						let porderdate = item.porderdate;
						let itemCnt = item.orderItems.length;
						itemStr += "<tr>"
						
						itemStr += "<td rowspan=" + itemCnt + '" class="order-num">';
						itemStr += '<p class="o-num">' + hightLight(porderno, searchType, searchWord) + '</p>';
						itemStr += '<span class="sum-date">(' + printDate(porderdate) + ')</span>';
						itemStr += '<a href="orderdetail?code=' + porderno + '" class="btn-view">상세보기</a>';
						itemStr += '</td>';
						item.orderItems.forEach((orderItem) => {
							let imgurl1 = orderItem.imgurl1;
							let pbrand = orderItem.pbrand;
							let pname = orderItem.pname;
							let pcolor = orderItem.pcolor;
							let psize = orderItem.psize;
							let pprice = orderItem.podprice;
							let porderamount = orderItem.podamount;
							let podstatus = orderItem.podstatus;
		
							itemStr += '<td>'
							itemStr += '<div class="pt-list">';
							itemStr += '<a href="#"><img src="' + imgurl1 + '" alt="상품이미지"/></a>';
							itemStr += '<div class="pt-info">';
							itemStr += '<a href="#"><span class="info-brand">[' + pbrand +']</span><span class="info-ptname">' + hightLight(pname, searchType, searchWord)+ '</span></a>';
							itemStr += '<p class="pt-color">color : ' + pcolor + '<span class="and-line">/</span>size : ' +  psize + '</p></div></div></td>';
							itemStr += '<td>'+ porderamount +'</td>';
							itemStr += '<td><i class="won sign icon small"></i>' + wonChange(pprice) + '</td>';
							itemStr += '<td>'+ podstatus + '<span class="sum-date">(' + printDate(porderdate) + ')</span></td>';
				            itemStr += '<td></td>';
				            itemStr += '</tr>';
							});
					});	
					$('#otable').html(itemStr);
					// 페이징 버튼 생성
					if(data.pagination.totalPageNo > 0){
			            setHtml(data.pagination); // 페이징 버튼 렌더링
			            setAction(data.pagination); // 페이징 버튼 기능 추가
			        }else{
			        	$(".paging").html(''); // 페이징 버튼 없애기
			        }
				} else { // 주문목록 불러오기 실패 시
					let itemStr = '<tr></tr>';
					itemStr += '<td class="no-data" colspan="6">주문내역이 없습니다.</td>';
					$('#otable').html(itemStr);
					$(".paging").html('');
				}				
			}
			else {
				let itemStr = '<tr></tr>';
				itemStr += '<td class="no-data" colspan="6">주문내역이 없습니다.</td>';
				$('#otable').html(itemStr);
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
		$("#otable").html(''); // tbody 주문목록 초기화
		const sterm = $("input[name=startdate]").val();
     	const eterm = $("input[name=enddate]").val();
     	const searchType = $("select[name=searchtype]").val();
     	const searchWord = $("input[name=search]").val();
     	printOrderList(sterm, eterm, searchType, searchWord, pageNo);
    };
});
/*
						<tr>
	                        <td rowspan="2" class="order-num">
	                            <p class="o-num">210928P10850546</p>
	                            <span class="sum-date">(2021.09.28)</span>
	                            <a href="${pageContext.request.contextPath}/orderdetail" class="btn-view">상세보기</a>
	                        </td>
	                        <td>
	                            <div class="pt-list">
	                                <a href="#"
	                                    ><img
	                                        src="${pageContext.request.contextPath}/resources/images/SH2B1WSD751M_BL_S01.jpg"
	                                        alt="상품이미지"
	                                /></a>
	                                <div class="pt-info">
	                                    <a href="#">
	                                        <span class="info-brand"
	                                            >[SYSTEM HOMME]</span
	                                        >
	                                        <span class="info-ptname"
	                                            >스트레치 드레스 셔츠</span
	                                        >
	                                    </a>
	                                    <p class="pt-color">
	                                        color : BLUE
	                                        <span class="and-line">/</span>
	                                        size : 95
	                                    </p>
	                                </div>
	                            </div>
	                        </td>
	                        <td>1</td>
	                        <td><i class="won sign icon small"></i>150.500</td>
	                        <td>
	                            결제완료<span class="sum-date">(2021.09.28)</span>
	                        </td>
	                        <td></td>
	                    </tr>
	                    <tr>
	                        <td>
	                            <div class="pt-list">
	                                <a href="#"
	                                    ><img
	                                        src="${pageContext.request.contextPath}/resources/images/MM2B3WSH029H4A_WT_S01.jpg"
	                                        alt="상품이미지"
	                                /></a>
	                                <div class="pt-info">
	                                    <a href="#">
	                                        <span class="info-brand"
	                                            >[CLUB MONACO]</span
	                                        >
	                                        <span class="info-ptname"
	                                            >[21SS] 스트라이프 블록 셔츠</span
	                                        >
	                                    </a>
	                                    <p class="pt-color">
	                                        color : WHITE
	                                        <span class="and-line">/</span>
	                                        size : M
	                                    </p>
	                                </div>
	                            </div>
	                        </td>
	                        <td>1</td>
	                        <td><i class="won sign icon small"></i>96.600</td>
	                        <td>
	                            결제완료<span class="sum-date">(2021.09.28)</span>
	                        </td>
	                        <td></td>
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