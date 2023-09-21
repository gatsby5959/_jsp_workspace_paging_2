//댓글 등록시 bno, writer, content
document.getElementById('cmtAddBtn').addEventListener('click',()=>{
    const cmtText = document.getElementById('cmtText').value;
    console.log(cmtText);
    if(cmtText == null || cmtText ==""){
        alert('댓글을 입력해주세요.');
        return false;
    }else{
        let cmtData = { //키벨류 형식의 오브젝트
            bno: bnoVal,
            writer: document.getElementById('cmtWriter').value,
            content: cmtText
        };
        //전송 function 호출
        postCommentToServer(cmtData).then(result =>{
            //값을 reuslt로 받음
            if(result>0){
                alert("댓글 등록 성공~!!");
               
            }else{
                alert("댓글 등록 실패~!!");
            }
            printCommentList(cmtData.bno);

        })
    }
})


async function postCommentToServer(cmtData){ //비동기 async에이싱크
    try{
        //const url = "/cmt/post/"+bno;
        const url = "/cmt/post"; //전송을 위한 정보는 config 필요
        const config = {
            method:'post',
            headers:{
                'Content-Type':'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtData) //스트링형식 으로 만듬 서버게 받을수 있게...
        };
        const resp = await fetch(url, config); //await기다렷다 받아줘~
        const result = await resp.text(); //0 또는 1 (isOk)  결과는 바로 옴 비동기는 보낸곳?으로 다시 옴
		
        return result;
    }catch(error){
        console.log(error);
    }
}

function spreadCommentList(result){ //result 댓글 list     //일단 디비에서 result가 있어야함
    console.log(result);
    let div = document.getElementById('accordionExample');
    div.innerHTML="";
    // {[],[],[]}
    for(let i=0; i<result.length; i++){
        let str = `<div class="accordion-item">`;
        str += `<h2 class="accordion-header" id="heading${i}">`; /* i번지 해딩이라는 id 추가 */
        str += `<button class="accordion-button" type="button"`;
        str += `data-bs-toggle="collapse" data-bs-target="#collapse${i}"`;
        str += `aria-expanded="true" aria-controls="collapse${i}"`;
        str += `${result[i].cno}, ${result[i].writer}, ${result[i].reg_date}`;
        str += `</button> </h2>`;
        str += `<div id="collapse${i}" class="accordion-collapse collapse show"`;
        str += `data-bs-parent="#accordionExample">`;
        str += `<div class="accordion-body">`;
        str += `<input type="text" class="form-control" id="cmtText" value="${result[i].content}">`;/* 컨텐츠는 단순히 보여주는것이 아니라 입력도 받을수 있게 한다. */       /* https://getbootstrap.kr/docs/5.3/forms/form-control/  폼컨트롤 가져옴*/ 
        str += `<button type="button" data-cno="${result[i].cno}" data-writer="${result[i].writer}"  class="btn btn-warning cmtModBtn">%</button>`; /* https://getbootstrap.kr/docs/5.3/components/buttons/ */
        str += `<button type="button"  data-cno="${result[i].cno}"  class="btn btn-danger cmtDelBtn">X</button>`;
        str += `</div> </div> </div>`;
        div.innerHTML+= str; // 누적해서 담기
    }
}
/*
<div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
        cno, writer, regdate
      </button>
    </h2>
    <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
      <div class="accordion-body">
        content
      </div>
    </div>
*/    

//수정 삭제 버튼확인
document.addEventListener('click',(e)=>{
    console.log(e.target);
    if(e.target.classList.contains('cmtModBtn')){
        let cno = e.target.dataset.cno;
        console.log(cno);

		//수정 구현 (수정할 데이터를 객체로 생성 -> 컨트롤러에서 수정 요청)
		let div = e.target.closest('div');	//타겟을 기준으로 가장 가까운 div 찾기
		let cmtText = div.querySelector('#cmtText').value; //원래cmtText가 군데군데 많음)
		let writer = e.target.dataset.writer;
		
		//비동기통신 함수 호출 -> 처리
		updateCommentFromServer(cno, writer, cmtText).then(result=>{
			if(result > 0){
				alert('댓글 수정 성공~!!');
				printCommentList(bnoVal);
			}else{
				alert('댓글 수정 실패~!!');
			}
		});
		
    }
	if(e.target.classList.contains('cmtDelBtn')){
		let cno = e.target.dataset.cno;
        console.log(cno);
	
		//삭제 구현
		let div = e.target.closest('div');	//타겟을 기준으로 가장 가까운 div 찾기
		//let cmtText = div.querySelector('#cmtText').value; //원래cmtText가 군데군데 많음)
		//let writer = e.target.dataset.writer;
		
		//비동기통신 함수 호출 -> 처리
		removeCommentFromServer(cno).then(result=>{
			if(result > 0){
				alert('댓글 삭제 성공~!!');
				printCommentList(bnoVal);
			}else{
				alert('댓글 삭제 실패~!!');
			}
		});
	}
})

async function removeCommentFromServer(cno){
	try{
        const resp = await fetch('/cmt/remove/' +cno);  //   
        const result = await resp.json(); //결과를 제이슨형태로 받게됨
        return result;       
	}
	catch(error){
		console.log(error);
	}
}


async function updateCommentFromServer(cnoVal, cmtWriter, cmtText){
	try{
		const url = '/cmt/modify';
		const config ={
			method: 'post', 
			headers:{
				'Content-Type':'application/json; charset=uft-8'
			},
			body:JSON.stringify({cno:cnoVal, writer:cmtWriter, content:cmtText})
		}	
		const resp = await fetch(url,config);
		const result = await resp.text(); // 0 1 등으로 받음
		return result;
	}
	catch(error){
		console.log(error);
	}
}



//서버에 댓글 리스트를 달라고 요청    //서버에 댓글 리스트를 달라고 요청 
async function getCommentListFromServer(bno){
    try {
        const resp = await fetch('/cmt/list/'+bno);  //    /cmt/list/151
        const result = await resp.json(); //결과를 제이슨형태로 받게됨
        return result;        
    } catch (error) {
        console.log(error);
    }
}

function printCommentList(bno){
    getCommentListFromServer(bno).then(result=>{  //요청하자마자 결과도착하면...?
        console.log(result);
        if(result.length>0){
            spreadCommentList(result);
        }else{
            let div = document.getElementById('accordionExample');
            div.innerHTML = `comment가 없습니다!댓글이 없습니다!`;
        }
    })
}






