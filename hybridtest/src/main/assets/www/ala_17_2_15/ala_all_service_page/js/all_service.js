

function initPage(){



    $('a.left').click(function() {
        ret();
    });


    $('.navigation-item').click(function() {
        var currentIndex=$(this).index('.navigation-item');
        console.log('currentIndex:'+currentIndex);

        $('.navigation-item').removeClass('navigation-item-current');
        $(this).addClass('navigation-item-current');
        
    });


}




function ret(){
    alert("return to cell phone ...");
}



