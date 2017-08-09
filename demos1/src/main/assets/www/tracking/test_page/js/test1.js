

expandBy=1.01;
contract=1/1.01;
initialWidth=1000; // initial width 1000 px

startTime=0;
endTime=1000*60*10;  


// ----------create view logs --------------
viewLogs=new Array();

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


function showViewLogs(){
        $('.view_item_holder .view_item').remove();
        for (var i=0;i<viewLogs.length;i++){             // add view logs
            $('<div class="view_item"></div>').appendTo('div.view_item_holder')
        }

        var viewLogItems = $("div.view_item_holder").find(".view_item");    //   reposition/resize view logs  according to start time/duration
        viewLogItems.each(function(ind,ele){
            var me=$(this);
            me.css('left',(getStartPercent(viewLogs[ind].start)) + '%');
            me.css('width',(getWidthPercent(viewLogs[ind].start,viewLogs[ind].end))  + '%');
            me.css('top',(ind%3*33) + '%');
            me.html(viewLogs[ind].logItemName);

            var startDate = new Date(viewLogs[ind].start);
            var endDate = new Date(viewLogs[ind].end);
            var seconds=(viewLogs[ind].end-viewLogs[ind].start)/1000;
//            me.prop('title', startDate.toString()+'---'+endDate.toString());
            me.prop('title', 'duration:'+seconds+"s");
        });
}


// ----------create net-IO logs --------------
netLogs=new Array();

function createNetLogs(){
    netLogs=new Array();
    for(var i=0;i<6;i++){
        netLogs[i]=createNetLog((i+2)*1000*60,(i+2.2)*1000*60,(i%2==1));
    }
}


function createNetLog(start,end,success){
    var netLog=new Object();
    netLog.start=start;
    netLog.end=end;
    netLog.success=success;
    return netLog;
}

function showNetLogs(){

    $('.net_item_holder .net_item').remove();

    for (var i=0;i<netLogs.length;i++){               // add net logs
        $('<div class="net_item"></div>').appendTo('div.net_item_holder')
    }

    var netLogItems = $("div.net_item_holder").find(".net_item");    //   reposition/resize net logs  according to start time/duration
    netLogItems.each(function(ind,ele){
        var me=$(this);
        me.css('left',(getStartPercent(netLogs[ind].start)) + '%');
        me.css('width',(getWidthPercent(netLogs[ind].start,netLogs[ind].end))  + '%');
        me.css('top',(ind%3*33) + '%');
        if (netLogs[ind].success){
            me.css("background-color","green");
        } else {
            me.css("background-color","red");
        }
        // me.html("");
    });


}


// ----------create action logs --------------
actLogs=new Array();

function createActLogs(){
    actLogs=new Array();
    for(var i=0;i<6;i++){
        actLogs[i]=createActLog((i+1)*1000*60);
    }
}


function createActLog(start){
    var actLog=new Object();
    actLog.start=start;
    return actLog;
}

function showActionLogs(){
    $('.act_item_holder .act_item').remove();

    for (var i=0;i<actLogs.length;i++){               // add act logs
        $('<div class="act_item"></div>').appendTo('div.act_item_holder')
    }

    var actLogItems = $("div.act_item_holder").find(".act_item");    //   reposition/resize act logs  according to start time/duration
    actLogItems.each(function(ind,ele){
        var me=$(this);
        me.css('left',(getStartPercent(actLogs[ind].start)) + '%');
        me.css('width', '1%');
        me.css('top',(ind%3*33) + '%');
        me.css("background-color","green");
        // me.html("");
    });

}



// ----------create error logs --------------
errLogs=new Array();

function createErrLogs(){
    errLogs=new Array();
    for(var i=0;i<1;i++){
        errLogs[i]=createErrLog((i+7.6)*1000*60);
    }
}

function createErrLog(start){
    var errLog=new Object();
    errLog.start=start;
    return errLog;
}

function showErrorLogs(){

    $('.err_item_holder .err_item').remove();

    for (var i=0;i<errLogs.length;i++){               // add err logs
        $('<div class="err_item"></div>').appendTo('div.err_item_holder')
    }

    var errLogItems = $("div.err_item_holder").find(".err_item");    //   reposition/resize err logs  according to start time/duration
    errLogItems.each(function(ind,ele){
        var me=$(this);
        console.log("ind:"+ind+"  added");
        me.css('left',(getStartPercent(errLogs[ind].start)) + '%');
        me.css('width', '1%');
        me.css("background-color","red");
        // me.html("");
    });

}


function updateLogs(){
    showViewLogs();
    showNetLogs();
    showActionLogs();
    showErrorLogs();
}




function getStartPercent(start){
    return (start- startTime)/(endTime-startTime)*100;
}

function getWidthPercent(start, end){
    return (end- start)/(endTime-startTime)*100;
}


function regiser(){
    alert("register!");
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

    //createViewLogs();  //----------------------create view log data --------------------------
    //showViewLogs();
    //createNetLogs(); //------------------ create net log data --------------------------
    //showNetLogs();
    //createActLogs();  //--------------- create act log data --------------------------
    //showActionLogs();
    //createErrLogs();  //--------------- create err log data --------------------------
    //showErrorLogs();



  document.getElementById('files').addEventListener('change', handleFileSelect, false);

  var testTime=1495252223213;
  var testDate = new Date(testTime);
  console.log(testDate.toString());

  var testTime2=1495252273213;
  console.log((testTime2-testTime));

}

  function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object

    var reader = new FileReader();
    console.log("-----------")

    reader.onload=function(f){
        var jsonStr=this.result
        var session = JSON.parse(jsonStr);

        startTime=session.sessionStart;
        endTime=session.sessionEnd;
        netLogs=session.netIoLogs;
        viewLogs=session.viewLogs;
        actLogs=session.userActionLogs;
        errLogs=session.errorLogs;

        updateLogs();
    }

    reader.readAsText(files[0]);

  }




//  change display scale .... 
function changeScale(change){
    //   width of time indicator 
    $('div.time_indicator').width(change*$('div.time_indicator').width());

    //   width of total view items 
    $('div.view_item_holder').width(change*$('div.view_item_holder').width());
    $('div.net_item_holder').width(change*$('div.net_item_holder').width());
    $('div.act_item_holder').width(change*$('div.act_item_holder').width());
    $('div.err_item_holder').width(change*$('div.err_item_holder').width());
}