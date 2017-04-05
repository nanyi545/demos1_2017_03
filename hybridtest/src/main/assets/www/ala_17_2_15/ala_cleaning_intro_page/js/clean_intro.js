
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

    $('a.callphone').click(function() {
        callPhone()
    });


    $('a.appoint-btn').click(function() {
        appointMentNow()
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


    /******   begin of        pop-up basket   *****/


    //  show the  pop-up basket
    $('div.basket').click(function() {
        removeItemsInMapper();
        $('div.popup').removeClass('hidden');
        displayBasketList();
    });

    // close the pop-up basket
    $('div.popup').click(function(e) {
        if (e.target === this){
            $('div.popup').addClass('hidden');
        }
    });


    $('.left-action').click(function(){
        dispConfirmClear();
    });

    $('.right-action').click(function(){
        $('div.popup').addClass('hidden');
    });


   /******   end of          pop-up basket   *****/



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

//    console.log('currentItemIndex:'+currentItemIndex+'   currentTagIndex:'+currentTagIndex);

    createList();
	displayList();
    initMapper();


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




/**
data for the displaying list
**/
var productList;

/**
number of items selected
**/
var totalCount=0;

/**
price of all selected items
**/
var totalPrice=0;


/**
this data keeps info on  :  which items in the  [productList]  should be displayed in the pop-up basket
**/
var basketMapper;



function createList(){
	var productCount=4;
	productList=new Array(productCount);

	productList[0]=createItem(1,'大内裤','圆领t恤，薄衬衫,1234567,呵呵',10,'8.10折','原价¥108.00元','http://img10.360buyimg.com/n0/g5/M00/02/1E/rBEDik_Wv5YIAAAAAAJTszqAQOgAAAk0wNssiMAAlPL588.jpg');
	productList[1]=createItem(2,'中内裤','红色内裤',67,'8.20折','原价¥108.00元','http://p3.vanclimg.com/product/6/2/2/6220204/big/2008_10_21_18_28_31_4904.jpg');
	productList[2]=createItem(3,'小内裤','black 内裤',140,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg');
    productList[3]=createItem(4,'小内裤','小的内裤',1000,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg');
}


function createItem(productId,name,description,price,discount,originalPrice,imgUrl){

    var item=new Object();
    item.id=productId;
    item.name=name;
    item.description=description;
    item.price=price;
    item.discount=discount;
    item.originalPrice=originalPrice;
    item.count=0;
    item.url=imgUrl;

    return item;
}


function displayList(){

    $('.product-list .product-item').remove();   // remvoe all current .product-item elements  in .product-list
	var productCount=productList.length;
    for (var i=0;i<productCount;i++){
	    displayItem(i);
    }


    updateSelectionCallbacks();
    updateSelectionCounts();

}

function updateSelectionCallbacks(){

    $('.right-symble').click(function(){
        var tempIndex=$(this).index('.right-symble');
        productList[tempIndex].count+=1;
        updateMapper(tempIndex);

        updateSelectionCounts();
     });

    $('.right .add').click(function(){
        var tempIndex=$(this).index('.right .add');
        productList[tempIndex].count+=1;
        updateMapper(tempIndex);

        updateSelectionCounts();
     });

    $('.right .minus').click(function(){
        var tempIndex=$(this).index('.right .minus');
        productList[tempIndex].count-=1;
        
        if(productList[tempIndex].count>0){
            updateMapper(tempIndex);
        } else {
            removeItemInMapper(tempIndex);
        }

        updateSelectionCounts();
     });

}






function updateSelectionCounts(){


    var productCount=productList.length;

    totalCount=0;
    totalPrice=0;


    for (var i=0;i<productCount;i++){

//        console.log('i:'+i+'  count:'+productList[i].count);

    	if (productList[i].count==0){

            $('.indicator').eq(i).addClass('hidden');
            $('.indicator').eq(i).text('0');
            $('.right').eq(i).addClass('hidden');
            $('.right-symble').eq(i).removeClass('hidden');


    	} else if(productList[i].count>0){


            $('.indicator').eq(i).removeClass('hidden');
            $('.indicator').eq(i).text(''+productList[i].count);
            $('.num').eq(i).text(''+productList[i].count);
            $('.right').eq(i).removeClass('hidden');
            $('.right-symble').eq(i).addClass('hidden');

            totalCount+=productList[i].count;
            totalPrice+=productList[i].count*productList[i].price;
    	}
    }

    if (totalCount==0){
        $('.introduce .appoint .basket .total-count').addClass('hidden');
        $('.introduce .appoint .basket .total-count').text(''+totalCount);
    }else if (totalCount>0){
        $('.introduce .appoint .basket .total-count').removeClass('hidden');
        $('.introduce .appoint .basket .total-count').text(''+totalCount);
    }


    $('div.line1 .large').text(''+totalPrice.toFixed(2));

    if(totalPrice>29){
        $('div.line2 span').text('免运费');
    } else {
        $('div.line2 span').text('运费¥15，满29元免运费');
    }


}





function displayItem(index){


	$('<div class="product-item">'+
		'<div class="left-img">'+
			'<div class="indicator">0</div>'+
		'</div>  '+
		'<div class="product-txt">'+
			'<div class="product-name mtx1">'+productList[index].name+'</div>'+
			'<div class="product-description mtx1">'+productList[index].description+'</div>'+
			'<div class="product-price mtx1">¥'+productList[index].price+'<span>'+productList[index].discount+'</span>'+'</div>'+
			'<div class="product-original-price mtx1">'+productList[index].originalPrice+'</div>'+
		'</div> '+
		'<div class="right hidden">'+
			'<div class="minus"><b>-</b></div>'+
			'<div class="num"><b>1</b></div>'+
			'<div class="add"><b>+</b></div>'+
		'</div> '+
		'<div class="right-symble"><b>+</b></div>'+
	'</div>').appendTo('.product-list');


    //   set product img ...
    $('.left-img').eq(index).css({
        'background':'url('+productList[index].url+') no-repeat',
        'background-size':'100% 100%'
    });


}


function initMapper(){
    basketMapper=new Array();
}

function updateMapper(productListIndex){
    if(basketMapper==undefined){
        basketMapper=new Array();
    }
    var currentSize=basketMapper.length;
    // console.log('currentSize:'+currentSize+'  productListIndex:'+productListIndex);
    var indexAlreadyAdded=false;
    if (currentSize==0){     //  当前mapper为空，新加数据在末尾
        indexAlreadyAdded=false;
        basketMapper[currentSize]=new Object();
        basketMapper[currentSize].indexInProductList=productListIndex;
    } else {  
        for(var tempIndex=0;tempIndex<currentSize;tempIndex++){
            if (basketMapper[tempIndex].indexInProductList==productListIndex){
                indexAlreadyAdded=true;
                break;
            }
        }
        if (indexAlreadyAdded){  
            reorderMapper(tempIndex);   //  新加数据已经在mapper中， 找到该数据，移到末尾，  其他数据依次前移
        } else {
            basketMapper[currentSize]=new Object();    //  新加数据不在mapper中，加在末尾
            basketMapper[currentSize].indexInProductList=productListIndex;            
        }
    }

    // logMapper('after update');
}



function logMapper(tag){
    var endSize=basketMapper.length;
    var mapStr=tag+'\n';
    for (var i=0;i<endSize;i++){
        mapStr=mapStr+'basketMapper   index:'+i+'   indexInProductList:'+basketMapper[i].indexInProductList+'\n';
    }
    console.log(mapStr);
}


function removeItemInMapper(productListIndex){
    var mapperIndex=0;
    for (var i=0;i<basketMapper.length;i++){
        if (basketMapper[i].indexInProductList==productListIndex){
            mapperIndex=i;
            break;
        }
    }
    // logMapper('before splice');
    basketMapper.splice(mapperIndex, 1);
    // logMapper('after splice');
}





function removeItemsInMapper(){

    var mapperIndex=0;
    var containsEmptyEntry=false;

    for (var i=0;i<basketMapper.length;i++){
        if (productList[basketMapper[i].indexInProductList].count==0){
            mapperIndex=i;
            containsEmptyEntry=true;
            break;
        }
    }

    if (containsEmptyEntry){
        basketMapper.splice(mapperIndex, 1);
        console.log('--removeItemsInMapper remove 1 item in mapper--');
        removeItemsInMapper();
    } else {
        return;
    }


}




function reorderMapper(priorityIndex){
    var currentSize=basketMapper.length;
    if (priorityIndex==(currentSize-1)){
        return;
    }
    var tempObject=new Object();
    tempObject.indexInProductList=basketMapper[priorityIndex].indexInProductList;

    for (var i=priorityIndex;i<currentSize-1;i++){
        basketMapper[i]=basketMapper[i+1];
    }
    basketMapper[currentSize-1]=tempObject;
}



                            

function displayBasketList(){
    $('.selection-list .selection-item').remove();   // remvoe all current .selection-item elements  in .selection-list
	var mapperCount=basketMapper.length;

    for (var i=mapperCount-1;i>=0;i--){
        displayBasketItem(mapperCount-1-i , basketMapper[i].indexInProductList);
    }


    updateBasketCallbacks();

}


function updateBasketCallbacks(){

    $('.selector').click(function() {
        var tempIndex=$(this).index('.selector');
        var mapperCount=basketMapper.length;
        var index=basketMapper[mapperCount-1-tempIndex].indexInProductList;
        if (productList[index].count==0){
            productList[index].count=1;
            $('.selection-right').eq(tempIndex).removeClass('hidden');
            $('.selection-num-b').eq(tempIndex).text('1');
            $('.selection-right-symble').eq(tempIndex).addClass('hidden');
            $(this).addClass('selected').removeClass('selected-no');
        } else {
            productList[index].count=0;
            $('.selection-right').eq(tempIndex).addClass('hidden');
            $('.selection-right-symble').eq(tempIndex).removeClass('hidden');
            $(this).addClass('selected-no').removeClass('selected');

        }
        updateSelectionCounts();
    });



    $('.selection-right-symble').click(function(){
        var tempIndex=$(this).index('.selection-right-symble');
        var mapperCount=basketMapper.length;
        productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count+=1;

        $(this).addClass('hidden');

        $('.selection-right').eq(tempIndex).removeClass('hidden');
        $('.selector').eq(tempIndex).addClass('selected').removeClass('selected-no');
        $('.selection-num-b').eq(tempIndex).text(productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count);
        updateSelectionCounts();

     });


    $('.selection-add').click(function(){
        var tempIndex=$(this).index('.selection-add');
        var mapperCount=basketMapper.length;
        var index=basketMapper[mapperCount-1-tempIndex].indexInProductList;

        productList[index].count+=1;
        $('.selection-num-b').eq(tempIndex).text(productList[index].count);
        $('.selector').eq(tempIndex).addClass('selected').removeClass('selected-no');
        updateSelectionCounts();

     });


    $('.selection-minus').click(function(){
        var tempIndex=$(this).index('.selection-minus');
        var mapperCount=basketMapper.length;
        productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count-=1;


        if (productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count==0){
            $('.selection-right').eq(tempIndex).addClass('hidden');
            $('.selection-right-symble').eq(tempIndex).removeClass('hidden');
            $('.selector').eq(tempIndex).addClass('selected-no').removeClass('selected');
        }

        $('.selection-num-b').eq(tempIndex).text(productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count);

        updateSelectionCounts();

     });





}





function dispConfirmClear(){
    console.log('---call confirm--');

    var r=confirm("确认清空所有衣物吗？");
    if(r==true){

        var listCount=productList.length;
        for (var i=0;i<listCount;i++){
            productList[i].count=0;
        }
        $('div.popup').addClass('hidden');
        updateSelectionCounts();
        removeItemsInMapper();


    } else {



    }


}






function displayBasketItem(basketIndex, productListIndex ){

	$('<div class="selection-item">'+
	    '<div class="selector selected"></div>'+
		'<div class="selection-content-wrapper">'+
            '<div class="selection-img"></div>'+
            '<div class="selection-txt">'+
                '<span class="black-txt">'+ productList[productListIndex].name +'</span>'+
                '<span class="red-txt">¥'+ productList[productListIndex].price.toFixed(2) +'</span>'+
            '</div>  '+
            '<div class="selection-right ">'+
                '<div class="selection-minus"><b>-</b></div>'+
                '<div class="selection-num"><b class="selection-num-b">'+productList[productListIndex].count+'</b></div>'+
                '<div class="selection-add"><b>+</b></div>'+
            '</div>  '+
            '<div class="selection-right-symble hidden"><b>+</b></div>'+
		'</div>  '+
	'</div>').appendTo('.selection-list');


    //   set product img ...
    $('.selection-img').eq(basketIndex).css({
        'background':'url('+productList[productListIndex].url+') no-repeat',
        'background-size':'100% 100%'
    });






}




