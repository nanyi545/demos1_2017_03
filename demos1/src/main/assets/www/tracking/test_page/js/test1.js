

expandBy=1.01;
contract=1/1.01;
initialWidth=1000; // initial width 1000 px

startTime=0;
endTime=1000*60*10;  
viewLogs;


function createViewLogs(){
    viewLogs=new Array();
    for(var i=0;i<6;i++){
        viewLogs[i]=createViewLog(i*1000*60,(i+1)*1000*60);
    }
}

function createViewLog(start,end){
    var viewLog=new Object();
    viewLog.start=start;
    viewLog.end=end;
    return viewLog;
}




function initPage(){
 
/*
    $('div.main_content').click(function() {
        regiser();
    });
*/

    $('div.enlarge').click(function() {
        changeScale(expandBy);
    });

    $('div.contract').click(function() {
        changeScale(contract);
    });


    $('<div class="view_item"></div>').appendTo('div.view_item_holder') 
    $('<div class="view_item"></div>').appendTo('div.view_item_holder') 
    $('<div class="view_item"></div>').appendTo('div.view_item_holder') 
    
    var items = $("div.view_item_holder").find(".view_item");
    items.each(function(ind,ele){
        var me=$(this);   
        me.css('left',(ind*10) + '%');
        me.css('width',(25) + '%')
        me.css('top',(ind%3*33) + '%');
        me.html("hehe")
    });


}



function regiser(){
    alert("register!");
}


function changeScale(change){
    //   width of time indicator 
    $('div.time_indicator').width(change*$('div.time_indicator').width());

    //   width of total view items 
    $('div.view_item_holder').width(change*$('div.view_item_holder').width());
    
    
}