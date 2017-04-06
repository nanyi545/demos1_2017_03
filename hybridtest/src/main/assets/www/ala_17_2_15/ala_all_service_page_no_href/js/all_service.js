

function initPage(){



    $('a.left').click(function() {
        ret();
    });


    $('.navigation-item').click(function() {
        var index=$(this).index('.navigation-item');
        $('.navigation-item').removeClass('navigation-item-current');
        $(this).addClass('navigation-item-current');
        $('#list').scrollTop(fixedPositions[index]);
    });

    findFixedPositions();

    listene2Scroll();
}




function ret(){
    alert("return to cell phone ...");
}



var fixedPositions;

function findFixedPositions(){
    var headerHeight=$('#header').height();
    fixedPositions=new Array();
    $('.service-item').each(function(ind,ele){
        var m=$(this);   
        fixedPositions[ind]=m.offset().top-headerHeight;
        console.log('ind:'+ind+'    top:'+fixedPositions[ind]);
    });
}





var currentId="item1";
var currendIndex=0;;

function listene2Scroll(){

    var initialUrl=window.location.href;

    var headerHeight=$('#header').height();
    var navigationMenu=$('#nav-menu');


    $('#list').scroll(function(){
        var top=$('#list').scrollTop();

        var items = $("#list").find(".service-item");
        
        items.each(function(ind,ele){
            var m=$(this);   
            var itemTop=m.offset().top-headerHeight;

            if ((itemTop>-30)&&(itemTop<30)){
                currentId="#"+m.attr("id");
                currendIndex=ind;

                return false;  // return false --> similar to  break the loop 
            } else {
                return true;   // return true --> continue for the rest of the loop 
            }

        });


        $(".navigation-item").removeClass("navigation-item-current");
        $(".navigation-item").eq(currendIndex).addClass("navigation-item-current");

    });


}

