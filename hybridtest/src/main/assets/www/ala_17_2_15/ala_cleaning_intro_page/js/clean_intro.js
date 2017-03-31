
/**
currentItemIndex=0 --> 日常洗衣
currentItemIndex=1 --> 鞋靴清洗
currentItemIndex=2 --> 家具家纺  
**/
var currentItemIndex=0;


/**
when currentItemIndex=0   , currentTagIndex =0,1,2
when currentItemIndex=1   , currentTagIndex =0,1
when currentItemIndex=2   , currentTagIndex =0,1,2
**/
var currentTagIndex=0;


function initPage(){


    $('a.left').click(function() {
        ret();
    });


    $('div.clean-item').click(function(){
        $('div.clean-item').removeClass('clean-item-current');
        $(this).addClass('clean-item-current');
        currentItemIndex = $(this).index();
        currentTagIndex=0;

        displayTagsBasedOnItemIndex();
    });


    $('div.tags-3-1').click(function(){
        $('div.tags-3-1').removeClass('selected');
        $(this).addClass('selected');
        currentTagIndex = $(this).index();
        displayTagsBasedOnItemIndex();
    });

    $('div.tags-2-1').click(function(){
        $('div.tags-2-1').removeClass('selected');
        $(this).addClass('selected');
        currentTagIndex = $(this).index();
        displayTagsBasedOnItemIndex();
    });

    displayTagsBasedOnItemIndex();


    testObj();
}


function displayTagsBasedOnItemIndex(){
    $('div.tags-wrapper').addClass('hidden').removeClass('visible');
    switch(currentItemIndex)
    {
        case 0:
            $('#tags-1').addClass('visible');
            $('#tags-1').children().removeClass('selected');
            $('#tags-1').children().eq(currentTagIndex).addClass('selected');
        break;
        case 1:
            $('#tags-2').addClass('visible');
            $('#tags-2').children().removeClass('selected');
            $('#tags-2').children().eq(currentTagIndex).addClass('selected');

        break;
        case 2:
            $('#tags-3').addClass('visible');
            $('#tags-3').children().removeClass('selected');
            $('#tags-3').children().eq(currentTagIndex).addClass('selected');
        break;
        default:break;
    }
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




function testObj(){
    person=new Object();
    person.firstname="Bill";
    person.lastname="Gates";
    person.age=56;
    person.eyecolor="blue";


}