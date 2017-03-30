

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