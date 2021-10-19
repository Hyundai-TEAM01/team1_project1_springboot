$(function () {
    const explainContainer = $(".explain");
    const dDayContainer =  $(".d-day");
    const isproceeding = true; // 진행중인가?
	let eventCtn = 1;
	getTime();
    timer = setInterval(getTime, 1000);
    
      if(isproceeding) {
		if(eventCtn === 1) {
			$('.imgbtn').click(function(e) {
	          e.preventDefault();
	          $.ajax({
			      url: 'getcouponevent'
			  })
			  .done((data) => {
				console.log(data);
				if(data.result === 'fail') {
					alert("이벤트가 종료되었습니다.");
				} else if(data.result === 'hascoupon') {
					alert("이미 쿠폰이 발급되었습니다.");
				} else if(data.result === 'error') {
					alert("서버에 오류가 발생하였습니다.");
				} else if(data.result === 'success') {
					alert("쿠폰 발급에 성공하였습니다.");
					eventCtn = 0;
				} else if(data.result === 'notstart') {
					alert("이벤트 시작시간이 아닙니다.");
				} else if(data.result === 'login') {
					alert("로그인이 필요합니다.");
					location.href = "/member/loginForm";
				} else {
					alert("알 수 없는 오류가 발생했습니다.");
					console.log("에러발생");
				}
			  });
	        });
		}
        
      } else {
        explainContainer.html("이벤트가 종료되었습니다.");
        dDayContainer.html("");
        $('.imgbtn').click(function(e) {
          e.preventDefault();
          alert("이벤트가 종료되었습니다.");
        });
      }


      function getTime() {
        const dDay = new Date("2021-10-13:09:00:00+0900");
        const now=new Date();
        const gap=dDay.getTime()-now.getTime();
   

        if(gap > 0 ) { 
          explainContainer.html("- 이벤트 진행시간까지 -");
		  const day = Math.floor(gap/(1000*60*60*24));
		  const hours = Math.floor((gap % (1000*60*60*24))/(1000*60*60));
		  const minutes = Math.floor((gap % (1000*60*60))/(1000*60));
		  const seconds = Math.floor((gap % (1000*60))/1000);

          let text = day + "Day ";
          text += hours < 10 ? ("0" + hours) : hours;
          text += "h ";
          text += minutes < 10 ? ("0" + minutes) : minutes;
          text += "m ";
          text += seconds < 10 ? ("0" + seconds) : seconds;
          text += "s ";
          dDayContainer.html(text);
          // dDayContainer.html(`${day}Day ${hours<10?`0${hours}`:hours}h ${minutes<10?`0${minutes}`:minutes}m ${seconds<10? `0${seconds}`:seconds}s`);
        } else { // d-day 지남
          explainContainer.html("Click!!!");
        }
        
      }
});