

function initPage(){



	$('div.item2').click(function() {
        showHint();
    });


    $('div.exitMsg').click(function() {
        hideHint();
    });


	$('div.item1').click(function() {
        doCleaning();
    });




	var testArray=new Array();
	console.log("-----var testArray=new Array()----- size:"+testArray.length);
	testArray[0] = new Object(); 
	console.log("-----testArray[0] = new Object()----- size:"+testArray.length);
	testArray[1] = new Object(); 
	console.log("-----testArray[1] = new Object()----- size:"+testArray.length);
	testArray=testArray.slice(0, -1); 
	console.log("-----testArray=testArray.slice(0, -1)----- size:"+testArray.length);


}




function showHint(){
	$("div.noticeFixed").css('visibility','visible');
}



function hideHint(){
	$("div.noticeFixed").css('visibility','hidden');
}


function doCleaning(){
	alert("cleaning");
}