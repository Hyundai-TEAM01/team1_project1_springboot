$(function () {
	// 주문번호를 url에서 가져오기
	const urlParams = new URLSearchParams(window.location.search);
	const code = urlParams.get('code');
    $.ajax({
		url: 'getorderdetaillist?code=' + code
	})
	.done((data) => {
		if(data && data.result) { // 데이터 잘 불러왔는가
			let itemStr = "";
			
			data.result.forEach((item) => {
				const mno = item.mno;
				const porderno = item.porderno;
				$(".ono").html(porderno);
				const porderdate = item.porderdate;
				$(".odate").html(porderdate);
				const pordertotalpoint = item.pordertotalpoint;
				let pordersaleprice = 0; // 총 판매가
				$(".podsaleprice").html(pordersaleprice);
				let porderdeliveryprice = 2500; // 배송비
				$(".poddeliveryprice").html(porderdeliveryprice);
				const porderdiscount = item.porderdiscount;
				$(".poddiscount").html(porderdiscount);
				const porderpayprice = item.porderpayprice;
				$(".podpayprice").html(wonChange(porderpayprice));
				const porderpayment = item.porderpayment; // 결제정보
				const porderpayname = item.porderpayname;
				$(".podpayname").html(porderpayname);
				const porderpayno = item.porderpayno;
				$(".podpayno").html(porderpayno);
				const porderpayinstallment = item.porderpayinstallment;
				$(".podpayinstallment").html(porderpayinstallment);
				const porderaddr1 = item.porderaddr1;
				$(".podaddr1").html(porderaddr1);
				const porderaddr2 = item.porderaddr2;
				$(".podaddr2").html(porderaddr2);
				const porderaddr3 = item.porderaddr3;
				$(".podaddr3").html(porderaddr3);
				const pordername = item.pordername;
				$(".podname").html(pordername);
				const porderphone = item.porderphone;
				$(".podphone").html(porderphone);
				const pordertel = item.pordertel;
				$(".podtel").html(pordertel);
				const porderrequest = item.porderrequest;
				$(".podrequest").html(porderrequest);
				const itemCnt = item.orderItems.length;
				let itemStr =  "";
				item.orderItems.forEach((orderItem, idx) => {
					let imgurl1 = orderItem.imgurl1;
					let pbrand = orderItem.pbrand;
					let pname = orderItem.pname;
					let pcolor = orderItem.pcolor;
					let psize = orderItem.psize;
					let pprice = orderItem.podprice;
					let porderamount = orderItem.podamount;
					let podstatus = orderItem.podstatus;
					pordersaleprice += parseInt(pprice); // 총 판매가 갱신
					
					itemStr += '<tr>';
                    itemStr += '<td class="order-num">';
                    itemStr += '<div class="pt-list">';
                    itemStr += '<a href="#"><img src="' + imgurl1 + '" alt="상품이미지"/></a>';
                    itemStr += '<div class="pt-info">';
                    itemStr += '<a href="#"><span class="info-brand">' + pbrand + '</span>';
                    itemStr += '<span class="info-ptname">' + pname + '</span></a>';
                    itemStr += '<p class="pt-color">color : ' + pcolor + '<span class="and-line">/</span>';
                    itemStr += 'size : ' + psize + '</p>';
                    itemStr += '</div>';
                    itemStr += '</div>';
                    itemStr += '</td>';
                    itemStr += '<td>' + porderamount + '</td>';
                    itemStr += '<td><i class="won sign icon small"></i>' + wonChange(pprice) + '</td>';
                    itemStr += idx === 0 ? '<td rowspan="' + itemCnt + '">' + wonChange(pordertotalpoint) + '</td>' : '';
                    itemStr += '<td>' + podstatus + '<span class="sum-date">' + printDate(porderdate) + '</span></td></tr>';
				});
				$('.podsaleprice').html(wonChange(pordersaleprice));
				if(pordersaleprice >= 30000) {
					porderdeliveryprice = 0;	
				}
				$('.poddeliveryprice').html(wonChange(porderdeliveryprice));
				$('#odtable').html(itemStr);
			});
		
		} else {
			$(".ono").html("잘못된 주문 상세조회입니다.");
		}
	}); // done
	
	// 시간 제거
	function printDate(inputDate) {
		return inputDate.split(" ")[0];
	}
	
	// 금액 형태로 변환
    function wonChange(num) {
        return String(num).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
    }
});
/*
			<h1 class="pg-title">주문 상세 조회</h1>
        <div class="ui container">
            <div class="pt-title">
                <div class="o-num">
                    <strong>주문번호 : </strong><span class="ono">210928P10850546</span>
                </div>
                <div class="o-date">주문일시 : <span class="odate"></span></div>
            </div>

            <h3 class="pt-subtitle">주문상품</h3>
            <table class="pt-table o-table">
                <thead>
                    <tr>
                        <th class="pt-infos">상품정보</th>
                        <th>수량</th>
                        <th>판매가</th>
                        <th>적립 한섬마일리지</th>
                        <th>주문상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="order-num">
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
                        <td><i class="won sign icon small"></i>150,500</td>
                        <td rowspan="2">1.000 P</td>
                        <td>
                            결제완료<span class="sum-date">(2021.09.28)</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="order-num">
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
                        <td><i class="won sign icon small"></i>96,600</td>
                        <td>
                            결제완료<span class="sum-date">(2021.09.28)</span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <h3 class="pt-subtitle">결제정보</h3>
            <table class="pt-table pay-table">
                <thead>
                    <tr>
                        <th class="pt-infos">판매가</th>
                        <th>배송비</th>
                        <th>총할인금액</th>
                        <th>총결제금액</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="order-num"><i class="won sign icon small"></i><span class="podsaleprice">247,100</span></td>
                        <td><i class="won sign icon small"></i><span class="poddeliveryprice">0</span></td>
                        <td>-<i class="won sign icon small"></i><span class="poddiscount">0</span></td>
                        <td class="pay-money">
                            <i class="won sign icon small"></i><span class="podpayprice">247,100</span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table class="pt-table paydetail-table">
                <tr>
                    <th>주 결제수단</th>
                    <td><span class="podpayname">비씨카드</span>(<span class="podpayno">5389-****-****-****</span>)</td>
                </tr>
                <tr>
                    <th>실 결제금액</th>
                    <td><i class="won sign icon small"></i><span class="podpayprice">247,100</span></td>
                </tr>
                <tr>
                    <th>할부 개월 수</th>
                    <td><span class="podpayinstallment">일시불</span></td>
                </tr>
            </table>

            <h3 class="pt-subtitle">배송지 정보</h3>
            <table class="pt-table shipping-table">
                <tr>
                    <th>배송지 주소</th>
                    <td>
                    	(<span class="podaddr1">08045</span>) 
                    	<span class="podaddr2">서울특별시 양천구 신정이펜2로 55(신정동, 신정이펜하우스 2단지)</span> 
                    	<span class="podaddr3">909동 1004호</span>
                    </td>
                </tr>
                <tr>
                    <th>수령인</th>
                    <td><span class="podname">허준범</span></td>
                </tr>
                <tr>
                    <th>휴대폰</th>
                    <td><span class="podphone">010-1234-1004</span></td>
                </tr>
                <tr>
                    <th>연락처</th>
                    <td><span class="podtel"></span></td>
                </tr>
                <tr>
                    <th>배송요청사항</th>
                    <td><span class="podrequest">올 때 메로나</span></td>
                </tr>
            </table>
        </div>
            */