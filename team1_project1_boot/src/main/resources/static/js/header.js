$(function () {
	/* 카테고리 드롭다운 */
    var $menuman = $(".dth-man");
    var $subman = $(".sub-man");

    // 마우스 hover 드롭다운 메뉴
    $menuman.on({
        mouseenter: function () {
            $subman.addClass("wrap-on");
        },
        mouseleave: function () {
            $subman.removeClass("wrap-on");
        },
    });

    var $menuwoman = $(".dth-woman");
    var $subwoman = $(".sub-woman");

    // 마우스 hover 드롭다운 메뉴
    $menuwoman.on({
        mouseenter: function () {
            $subwoman.addClass("wrap-on");
        },
        mouseleave: function () {
            $subwoman.removeClass("wrap-on");
        },
    });
    
    var $menulifestyle = $(".dth-lifestyle");
    var $sublifestyle = $(".sub-lifestyle");

    // 마우스 hover 드롭다운 메뉴
    $menulifestyle.on({
        mouseenter: function () {
            $sublifestyle.addClass("wrap-on");
        },
        mouseleave: function () {
            $sublifestyle.removeClass("wrap-on");
        },
    });
    
    /* 오른쪽 마이페이지 드롭다운 */
    var mp_default = '54px';  // 1단계 메뉴 높이
    var mp_hover = '132px'; // 2단계 메뉴 높이
    var $mypage = $('.my-page');
    
    // gnb 초기화(2단계 메뉴 숨기기)
    $mypage.css('height', mp_default);
    
    // 마우스 hover 드롭다운 메뉴
    $mypage.on({
        mouseenter: function(){
        $(this).stop().animate({ height: mp_hover }, 200);
        },
        mouseleave: function(){
        $(this).stop().animate({ height: mp_default }, 200);
        }
    });
    
    
    /* 카트 담긴 갯수 호출 */
    getCartCountHeader()
    
});

function getCartCountHeader(){
	$.ajax({
		url : "/member/memberdata"
	}).done((data)=>{
		$(".cartcnt").html(data.cartcnt);
	});
}