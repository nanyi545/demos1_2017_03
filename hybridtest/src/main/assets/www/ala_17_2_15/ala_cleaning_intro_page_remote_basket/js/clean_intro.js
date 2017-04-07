
/**
大分类
currentItemIndex=0 --> 日常洗衣
currentItemIndex=1 --> 鞋靴清洗
currentItemIndex=2 --> 家具家纺  
**/
var currentItemIndex=0;


/**
小分类
when currentItemIndex=0   
when currentItemIndex=1   
when currentItemIndex=2   
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


    getBasketFromRemote();

    displayTagsBasedOnItemIndex();


    /******   begin of        pop-up basket   *****/


    //  show the  pop-up basket
    $('div.basket').click(function() {

        $('div.popup').removeClass('hidden');

        getBasketFromRemote();
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
            $('#tags-1').children().eq(0).children().removeClass('selected');
            $('#tags-1').children().eq(0).children().eq(currentTagIndex).addClass('selected');
        break;
        case 1:
            $('#tags-2').addClass('visible');
            $('#tags-2').children().eq(0).children().removeClass('selected');
            $('#tags-2').children().eq(0).children().eq(currentTagIndex).addClass('selected');
        break;
        case 2:
            $('#tags-3').addClass('visible');
            $('#tags-3').children().eq(0).children().removeClass('selected');
            $('#tags-3').children().eq(0).children().eq(currentTagIndex).addClass('selected');
        break;
        default:break;
    }

//    console.log('currentItemIndex:'+currentItemIndex+'   currentTagIndex:'+currentTagIndex);

    getListFromRemote();



	displayList();

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
购物车数据 
**/
var basketList;

/**
number of items selected
**/
var totalCount=0;

/**
price of all selected items
**/
var totalPrice=0;





function getListFromRemote(){
	productList=new Array();
	productList[0]=createItem(1,'大内裤','圆领t恤，薄衬衫,1234567,呵呵',10,'8.10折','原价¥108.00元','http://img10.360buyimg.com/n0/g5/M00/02/1E/rBEDik_Wv5YIAAAAAAJTszqAQOgAAAk0wNssiMAAlPL588.jpg',2);
	productList[1]=createItem(2,'中内裤','红色内裤',67,'8.20折','原价¥108.00元','http://p3.vanclimg.com/product/6/2/2/6220204/big/2008_10_21_18_28_31_4904.jpg',3);
	productList[2]=createItem(3,'小内裤','black 内裤',140,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg',3);
    productList[3]=createItem(4,'小内裤','小的内裤',1000,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg',1);
}




function createItem(productId,name,description,price,discount,originalPrice,imgUrl,initialCount){
    var item=new Object();
    item.id=productId;
    item.name=name;
    item.description=description;
    item.price=price;
    item.discount=discount;
    item.originalPrice=originalPrice;
    item.count=initialCount;
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

        productList[tempIndex].count +=1;
        changeProductCount(tempIndex,productList[tempIndex].count);
        updateSelectionCounts();
     });

    $('.right .add').click(function(){
        var tempIndex=$(this).index('.right .add');

        productList[tempIndex].count +=1;
        changeProductCount(tempIndex,productList[tempIndex].count);
        updateSelectionCounts();
     });

    $('.right .minus').click(function(){
        var tempIndex=$(this).index('.right .minus');

        productList[tempIndex].count +=-1;
        changeProductCount(tempIndex,productList[tempIndex].count);
        updateSelectionCounts();
     });

}




/**
主页面中  改变商品数量
**/
function changeProductCount(productIndex, newCount){
    productCountInterface(productList[productIndex].id,newCount);
}


/**
改变商品数量接口

**/

function productCountInterface(productID,newCount){
    console.log("   productID:"+productID+"   newCount:"+newCount);


}




/**

根据当前商品数量，更新数量指示

**/


function updateSelectionCounts(){

    var productCount=productList.length;

    for (var i=0;i<productCount;i++){

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
    	}
    }


    totalCount=0;
    totalPrice=0;

    for(var j=0;j<basketList.length;j++){
        totalCount+=basketList[j].count;
        totalPrice+=basketList[j].count*basketList[j].price;
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





function getBasketFromRemote(){
    basketList=new Array();
    basketList[0]=createItem(1,'大内裤','圆领t恤，薄衬衫,1234567,呵呵',10,'8.10折','原价¥108.00元','http://img10.360buyimg.com/n0/g5/M00/02/1E/rBEDik_Wv5YIAAAAAAJTszqAQOgAAAk0wNssiMAAlPL588.jpg',2);
    basketList[1]=createItem(2,'中内裤','红色内裤',67,'8.20折','原价¥108.00元','http://p3.vanclimg.com/product/6/2/2/6220204/big/2008_10_21_18_28_31_4904.jpg',3);
    basketList[2]=createItem(3,'小内裤','black 内裤',140,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg',3);
    basketList[3]=createItem(4,'小内裤','小的内裤',1000,'8.30折','原价¥108.00元','http://b.img.youboy.com/20108/13/g2_4992337.jpg',1);
}


                            

function displayBasketList(){
    $('.selection-list .selection-item').remove();   // remvoe all current .selection-item elements  in .selection-list

    for (var i=0; i<basketList.length;i++){
        displayBasketItem(i);
    }

    updateBasketCallbacks();

}


/**
购物车中  改变商品数量
**/
function changeProductCountInBasket(basketIndex,newCount){


    var basketItemId=basketList[basketIndex].id;
    var basketItemInCurrentPage=false;
    var productIndex=-1;

    for (var i=0;i<productList.length;i++){
        if(productList[i].id==basketItemId){
            productIndex=i;
            basketItemInCurrentPage=true;
        }
    }

    if (basketItemInCurrentPage){   /** 如果购物车中商品  ，同时也在当前页面，需要 同时更新productList[]的count **/
        productList[productIndex].count=newCount;
        updateSelectionCounts();
    } 


    basketList[basketIndex].count=newCount;
    productCountInterface(basketList[basketIndex].id,newCount);

} 





function updateBasketCallbacks(){

    $('.selector').click(function() {
        var tempIndex=$(this).index('.selector');

        if (basketList[tempIndex].count==0){
            basketList[tempIndex].count=1;
            changeProductCountInBasket(tempIndex,basketList[tempIndex].count);

            $('.selection-right').eq(tempIndex).removeClass('hidden');
            $('.selection-num-b').eq(tempIndex).text('1');
            $('.selection-right-symble').eq(tempIndex).addClass('hidden');
            $(this).addClass('selected').removeClass('selected-no');
        } else {
            basketList[tempIndex].count=0;
            changeProductCountInBasket(tempIndex,basketList[tempIndex].count);

            $('.selection-right').eq(tempIndex).addClass('hidden');
            $('.selection-right-symble').eq(tempIndex).removeClass('hidden');
            $(this).addClass('selected-no').removeClass('selected');

        }
        updateSelectionCounts();
    });



    $('.selection-right-symble').click(function(){
        var tempIndex=$(this).index('.selection-right-symble');

        basketList[tempIndex].count+=1;
        changeProductCountInBasket(tempIndex,basketList[tempIndex].count);

        $(this).addClass('hidden');

        $('.selection-right').eq(tempIndex).removeClass('hidden');
        $('.selector').eq(tempIndex).addClass('selected').removeClass('selected-no');
        $('.selection-num-b').eq(tempIndex).text(productList[basketMapper[mapperCount-1-tempIndex].indexInProductList].count);
        updateSelectionCounts();

     });


    $('.selection-add').click(function(){
        var tempIndex=$(this).index('.selection-add');

        basketList[tempIndex].count+=1;
        changeProductCountInBasket(tempIndex,basketList[tempIndex].count);


        $('.selection-num-b').eq(tempIndex).text(productList[index].count);
        $('.selector').eq(tempIndex).addClass('selected').removeClass('selected-no');
        updateSelectionCounts();

     });


    $('.selection-minus').click(function(){
        var tempIndex=$(this).index('.selection-minus');

        basketList[tempIndex].count-=1;
        changeProductCountInBasket(tempIndex,basketList[tempIndex].count);

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

        $('div.popup').addClass('hidden');

    } else {

    }


}






function displayBasketItem(basketIndex){

	$('<div class="selection-item">'+
	    '<div class="selector selected"></div>'+
		'<div class="selection-content-wrapper">'+
            '<div class="selection-img"></div>'+
            '<div class="selection-txt">'+
                '<span class="black-txt">'+ basketList[basketIndex].name +'</span>'+
                '<span class="red-txt">¥'+ basketList[basketIndex].price.toFixed(2) +'</span>'+
            '</div>  '+
            '<div class="selection-right ">'+
                '<div class="selection-minus"><b>-</b></div>'+
                '<div class="selection-num"><b class="selection-num-b">'+basketList[basketIndex].count+'</b></div>'+
                '<div class="selection-add"><b>+</b></div>'+
            '</div>  '+
            '<div class="selection-right-symble hidden"><b>+</b></div>'+
		'</div>  '+
	'</div>').appendTo('.selection-list');


    //   set product img ...
    $('.selection-img').eq(basketIndex).css({
        'background':'url('+basketList[basketIndex].url+') no-repeat',
        'background-size':'100% 100%'
    });


}




