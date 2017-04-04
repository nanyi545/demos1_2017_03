
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
        $('div.popup').removeClass('hidden');
        displayBasketList();
    });

    // close the pop-up basket
    $('div.popup').click(function(e) {
        if (e.target === this){
            $('div.popup').addClass('hidden');
        }
    });


    $('.selector').click(function() {
        $(this).toggleClass('selected');
        $(this).toggleClass('selected-no');
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
        updateSelectionCounts();
     });

    $('.right .add').click(function(){
        var tempIndex=$(this).index('.right .add');
        productList[tempIndex].count+=1;
        updateSelectionCounts();
     });

    $('.right .minus').click(function(){
        var tempIndex=$(this).index('.right .minus');
        productList[tempIndex].count-=1;
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

    	    updateMapper(i);

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
    console.log('currentSize:'+currentSize);
    var indexAlreadyAdded=false;
    basketMapper[currentSize]=new Object();
    basketMapper[currentSize].indexInProductList=productListIndex;

}


function mapperContainsIndex(productListIndex){
    var contained=false;
    if (basketMapper.length==0) {
        contained=false;
        return contained;
    } else {
        var totalSize=basketMapper.length;

    }
    return contained;
}


                            

function displayBasketList(){

    $('.selection-list .selection-item').remove();   // remvoe all current .selection-item elements  in .selection-list


	var basketItemCount=basketMapper.length;
    for (var i=0;i<basketItemCount;i++){
	    displayBasketItem(i,basketMapper[i].indexInProductList);
    }



}


function displayBasketItem(basketIndex, productListIndex ){

	$('<div class="selection-item">'+
	    '<div class="selector selected"></div>'+
		'<div class="selection-content-wrapper">'+
            '<div class="selection-img"></div>'+

		'</div>  '+
	'</div>').appendTo('.selection-list');


    //   set product img ...
    $('.selection-img').eq(basketIndex).css({
        'background':'url('+productList[productListIndex].url+') no-repeat',
        'background-size':'100% 100%'
    });

}


