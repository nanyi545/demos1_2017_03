

function initPage(){



    $('a.left').click(function() {
        ret();
    });

    $('a.callphone').click(function() {
        callPhone()
    });


    $('a.appoint-btn').click(function() {
        appointMentNow()
    });

}




function ret(){
    alert("return to cell phone ...");
}




function callPhone(){
    alert("callPhone:4006-5858-28");
}

function appointMentNow(){
    alert("appointMentNow !");

}