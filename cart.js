function checkValue(check){
	var checkList = document.getElementsByClassName("checkList");
	var checkFlag = 0;
	for (  var i = 0;  i < checkList.length;  i++  ) {
		if(checkFlag == 0){
			if(checkList[i].checked) {
				checkFlag = 1;
				break;
			}
		}
	}
	if (checkFlag == 1) {
    	document.getElementById('deleteButton').disabled="";
	} else {
		document.getElementById('deleteButton').disabled="true";
	}
}
function goDeleteCartAction(){
	document.getElementById("cartForm").action="DeleteCartAction";
}
function goSettlementConfirmAction(){
	document.getElementById("cartForm").action="SettlementConfirmAction";
}