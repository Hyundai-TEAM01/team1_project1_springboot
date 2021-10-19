var submitCnt = 1;

function order(){
    
    $("form").attr("method","post");
    $("form").attr("action","/order/newOrder");
    
    let porderphone = $("select[name='p-start']").val() + $("input[name='phone2']").val() + $("input[name='phone3']").val();
    let pordertel = $("select[name='t-start']").val() + $("input[name='tel2']").val() + $("input[name='tel3']").val();
    pordertel = pordertel.length < 10 ? "":pordertel;
    
    let porderemail = $("input[name='porderemail']").val()+'@'+$("#s_email").val();
    porderemail = porderemail.split("@")[0] === "" ? "" : porderemail;
    
    
    let payment = $("input:checked[name='porderpayment']").attr("id");
    
    
    if(document.getElementById("infoForm").reportValidity()){            			            	
        $("tr").remove('.phone-wrap');
        $("tr").remove('.tel-wrap');
        $("tr").remove('.email-wrap');
        
        let mp = $("[name=porderdiscount]").val();
        mp = mp === "" ? 0 : mp;
        $("[name=porderdiscount]").val(mp);
        
        let plist = "";
        $(".info-body tr").each((idx,item)=>{
            plist += $(item).attr("id")+",";
        })
        plist = plist.slice(0,-1);
        
        $("form").append("<input type='text' name='porderphone' value='"+porderphone+"'>");   
        $("form").append("<input type='text' name='pordertel' value='"+pordertel+"'>");   
        $("form").append("<input type='text' name='porderemail' value='"+porderemail+"'>");  
        $("form").append("<input type='text' name='pordermphone' value='"+$(".mphone").html().replace("-","")+"'>");  					
        $("form").append("<input type='text' name='plist' value='"+plist+"'>");  	
        
        if(payment === "cash"){
            $("form").append("<input type='text' name='porderpayinstallment' value='입금 완료'>"); 
        }
        
        submitCnt = submitCnt -1;
        
        if(submitCnt >= 0){					
            $("form").submit();
        }
        

    }
}





// 우편번호 검색 api , https://postcode.map.daum.net/guide 사용
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ""; // 주소 변수
            var extraAddr = ""; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === "R") {
                // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else {
                // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            // if(data.userSelectedType === 'R'){
            //     // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            //     // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            //     if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
            //         extraAddr += data.bname;
            //     }
            //     // 건물명이 있고, 공동주택일 경우 추가한다.
            //     if(data.buildingName !== '' && data.apartment === 'Y'){
            //         extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            //     }
            //     // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            //     if(extraAddr !== ''){
            //         extraAddr = ' (' + extraAddr + ')';
            //     }
            //     // 조합된 참고항목을 해당 필드에 넣는다.
            //     document.getElementById("sample6_extraAddress").value = extraAddr;

            // } else {
            //     document.getElementById("sample6_extraAddress").value = '';
            // }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $(".addr1").val(data.zonecode);
            $(".addr2").val(addr);
            // 커서를 상세주소 필드로 이동한다.
            $(".addr3").focus();
        },
    }).open();
}

function wonChange(num) {
    return String(num).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}

// 가져온 상품 html로 변환
function createProduct(product){
    let html = "";
    html += '<tr id='+product.cartdetailno+'>';
    html += '<td>';
    html += '<div class="ui items">';
    html += '<div class="item">';
    html += '<div class="ui small image">';
    html += '<img src="'+ product.imgurl +'" style="width: 80px; height: 120px" />';
    html += '</div>';
    html += '<div class="middle aligned content">';
    html += '<div class="description">';
    html += '<p>'+ product.pbrand +'</p>';
    html += '<p>'+ product.pname+'</p>';
    html += '<p class="grey small">';
    html += 'color&nbsp:&nbsp';
    html += '<span class="p_color">'+product.pcolor+'</span>';
    html += '&nbsp/&nbspsize&nbsp:&nbsp';
    html += '<span class="p_size">'+ product.psize +'</span>';
    html += '</p>';
    html += '</div>';
    html += '</div>';
    html += '</div>';
    html += '</div>';
    html += '</td>';
    html += '<td class="center aligned">';
    html += '<div>';
    html += '<div class="ui input input-wrap">';
    html += '<input type="text" class="center aligned amount" readonly value="'+product.amount+'" />';
    html += '</div>';
    html += '</div>';
    html += '</td>';
    html += '<td class="center aligned"><i class="won sign icon small"></i><span class="price">'+wonChange(parseInt(product.amount)*parseInt(product.pprice))+'</span></td>';
    html += '</tr>';
    $("tbody.info-body").append(html);
}

function setSumPrice(){
    let sum = 0;
    $("span.price").each((idx,item)=>{
        sum += parseInt($(item).html().replaceAll(",",""));
    });
    
    if(sum < 30000){
		$(".post-price").html(2500);
	}	
    
    $(".p-price").html(wonChange(sum));
}

function setTotalPrice(){
    let pPrice = parseInt($(".p-price").html().replaceAll(",",""));
    let postPrice = parseInt($(".post-price").html().replaceAll(",",""));
    let discountPrice = parseInt($(".m-discount").length == 0 ? 0 : $(".m-discount").html());
    let sum = pPrice+postPrice - discountPrice;
    $(".total-price").html(wonChange(sum));
    $(".save-m-point").html(wonChange(Math.ceil(sum*0.05)));
}