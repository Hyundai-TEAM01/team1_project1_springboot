let gbl_productList;

$(function () {
    init();
});

function init(){
	// 홈페이지 디폴트 출력
	printCategoryProductList(1, "WOMEN_Top_Shirts");
}

function printCategoryProductList(pageNo, ccode){
	if(window.location.search != ''){
		let urlParams = new URLSearchParams(window.location.search);
		pageNo = urlParams.get('pageNo');
		ccode = urlParams.get('ccode');
	}
	$.ajax({
		url: 'getProductList' + '?ccode=' + ccode + '&pageNo=' + pageNo,
		async:false,
		/* data: {"pageNo" : pageNo, "ccode" : ccode}, */
	}).done((data) => {
		if(data.productList.length <= 0){
			$(".itemlist").html('요청 URL이 잘못되었습니다.');
		} else{
			createCategoryProduct(data.productList);
			if(data.pager.totalPageNo>0){
	            setHtml(data.pager);
	            setAction(data.pager);
	        }else{
	        	$(".paging").html('');
	        }
			gbl_productList = data.productList;	
		}
	});
}

function createCategoryProduct(productList){
	let itemlist = $(".itemlist");
	let index = 0;
	itemlist.html('');
	for(product of productList){
		let html = '<li class="column mg-product" target="' + index +'">';
        html += '<a class="productUrl" href="product/' + product.pcode + "?pcolor=" + product.color[0].pcolor + "&ccode=" + product.ccode + '">';
       	html += '<span><img class="main-img" src="/images/loading.gif" data-src="' + product.color[0].imgurl1 + '"></span></a>';
       	html += '<a class="productUrl" href="product/' + product.pcode + "?pcolor=" + product.color[0].pcolor + "&ccode=" + product.ccode + '">';
        html += '<span class="brand">' + product.pbrand + '</span>';
        html += '<span class="title">' + product.pname + '</span>';
        html += '<span class="price"><i class="won sign icon"></i>' + wonChange(product.pprice) + '</span>';
        html += '<span class="flag">';
        
        let todayDate = new Date();
        let pdate = new Date(product.pdate);
        if(Math.ceil((todayDate.getTime()-pdate.getTime())/(1000*3600*24)) < 15){
			html += '<span class="product-new">NEW</span>';
		}
        
        if(product.enabled == '1'){
			html += '<span class="product-soldout">SOLDOUT</span>';
		}
        html += '</span></a>';
        html += '<div class="color-more-wrap">';
        for(color of product.color){
        	html += '<a class="now-choose" href="javascript:chgColorChip('+ index + ", '"  + color.pcolor + "'" +')">';
        	html += '<img class="img-color-more" src="/images/loading.gif" data-src="' + color.colorurl +'"></a>';
        }
        index++;
    	html += '</div>';
    	html += '</li>';
    	
    	itemlist.append(html);	
	}
	$(".main-img, .img-color-more").Lazy({threshold : 200});
}

function chgColorChip(index, colorChip){
	let thisData = gbl_productList[index];
	let li = $(".itemlist li").eq(index);
	for(color of thisData.color){
		if(color.pcolor == colorChip){
			li.find(".main-img").attr("src", color.imgurl1);
		}
	}
	let newUrl = 'product/' + thisData.pcode + "?pcolor=" + colorChip + "&ccode=" + thisData.ccode;
	li.find(".productUrl").attr("href", newUrl);
}

// pager 생성
function setHtml(pager){
	$(".paging").html('');
	
	let pagingHtml = '';
	let firstPageBtn = '<a href="javascript:void(0);" class="prev2"><i class="angle double left icon"></i></a>';
	let prevPageBtn = '<a href="javascript:void(0);" class="prev"><i class="angle left icon"></i></a>';
	let pageBtn = '<span class="paging-num">';
	for(let i = pager.startPageNo; i <= pager.endPageNo; i++){
		let pageNum = i;
		let activeClass = '';
		if(pageNum == pager.pageNo) activeClass = ' on ';
		pageBtn += '<a href="javascript:void(0);" class="pageBtn' + activeClass + '" pageNum="' + pageNum + '">' + pageNum + '</a>';
	}
	
	pageBtn += '</span>';
	
	let nextPageBtn = '<a href="javascript:void(0);" class="next"><i class="angle right icon"></i></a>';
    let endPageBtn = '<a href="javascript:void(0);" class="next2"><i class="angle double right icon"></i></a>';
    
    pagingHtml = firstPageBtn + prevPageBtn + pageBtn + nextPageBtn + endPageBtn;
    
    $(".paging").html(pagingHtml);
}

function setAction(pager){
	let thisArea = $(".paging");
    
    thisArea.find(".prev2").click(function(){
        goPage(1, pager.ccode);
    });

    thisArea.find(".prev").click(function(){
        let pageNum = pager.startPageNo - 1;
        if(pageNum < 1) pageNum = 1;
        goPage(pageNum, pager.ccode);
    });

    thisArea.find(".pageBtn").click(function(){
        goPage($(this).attr("pageNum"), pager.ccode);
    });

    thisArea.find(".next").click(function(){
        let pageNum = pager.endPageNo + 1;
        if(pageNum>pager.totalPageNo) pageNum = pager.endPageNo;
        goPage(pageNum, pager.ccode);
    });

    thisArea.find(".next2").click(function(){
        goPage(pager.totalPageNo, pager.ccode);
    });
}

function goPage(pageNo, ccode){
	history.pushState('', '', 'productList?ccode=' + ccode +"&pageNo=" + pageNo);
	if($(".itemlist").length > 0){
        $(document).scrollTop(0);
    }
	printCategoryProductList(pageNo, ccode);
};


function wonChange(num) {
    return String(num).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}

$(window).on('popstate', function (event) {
	const data = event.originalEvent.state;
	$.ajax({
		url: data.data,
		data: {page:data.page, type: data.type, keyword: data.keyword},
		type: "get",
		success: (result) => {
			$(".content").html(result); 
		//alert(JSON.stringify(data)); 
		//검색 처리 위해서 
		//$("#type").val(data.type||"t"); //$("#keyword").val(data.keyword); 
		} 
	}) 
});