

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


    listene2Scroll();
}




function ret(){
    alert("return to cell phone ...");
}







var currentId="item1";
var currendIndex=0;;

function listene2Scroll(){

    var initialUrl=window.location.href;

    var headerHeight=$('#header').height();
    var navigationMenu=$('#nav-menu');


    $('#list').scroll(function(){
        var top=$('#list').scrollTop();
        logInAndroid('current scroll:'+top+'   currentId:'+currentId);
        console.log('current scroll:'+top   +'   currentId:'+currentId);


        var items = $("#list").find(".service-item");
        
    
        items.each(function(ind,ele){
            var m=$(this);   
            var itemTop=m.offset().top-headerHeight;

            logInAndroid(' ...each fun...    ind:'+ind+'   itemTop:'+itemTop);
            if ((itemTop>-30)&&(itemTop<30)){
                currentId="#"+m.attr("id");
                currendIndex=ind;

                console.log('currentId:'+currentId+'   ind:'+ind);
                logInAndroid('currentId:'+currentId+'   ind:'+ind);

                return false;  // return false --> similar to  break the loop 
            } else {
                return true;   // return true --> continue for the rest of the loop 
            }

        });

//        window.location.href=initialUrl;
        logInAndroid('--------here---------  '+window.location.href);

        $(".navigation-item").removeClass("navigation-item-current");
        $(".navigation-item").eq(currendIndex).addClass("navigation-item-current");

//        var node = document.getElementsByClassName("navigation-item")[currendIndex];
//        node.className+=' navigation-item-current';


    });


}

