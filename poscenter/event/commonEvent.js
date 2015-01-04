
var commonEvent = {};

//跳转到新增活动页面
commonEvent.toAddEventView = function(eventType){
	var url;
	if(eventType == 1){
		url = '/pos/buyGiftEvent!toEventFormView.do';
	}else if(eventType == 2){
		url = '/pos/swapBuyEvent!toSwapBuyEventFormView.do';
	}else if(eventType == 3){
		url = '/pos/moneyDiscountEvent!toEventFormView.do';
	}else if(eventType == 4){
		url = '/pos/countDiscountEvent!toEventFormView.do';
	}else if(eventType == 5){
		url = '/pos/comboEvent!toEventFormView.do';
	}
	window.location.href = $('#initPath').val()+url;
}

//返回列表
commonEvent.backEventList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}
