/** ******判断字符串子否为空********** */
function isNull(id){  
	var value = $("#" + id).val();
	if(value == null || trim(value)=="") return true;
	else return false;
} 

/** ******判断字符串是否为空或''null''********* */		
function isNotNull(id,str,allowNull){
	var value = $("#" + id).val();  
	if ((isNull(id) || trim(value)=="null") && !allowNull){  
		alert(str+"不能为空!");  
		return false;  
	}  
	else return true;  
}
		
	/** ********去除左右空格********** */  
	function trim(str){  
		 return str.replace(/(^\s*)|(\s*$)/g, "");  
	} 
	
		/** **********检查性别是否为空************** */
		function checkGender(){
		    var condITion='';
		    var allRadio = $(":radio");
			for (var i=0 ; i <allRadio.length;i++){
				if (allRadio[i].checked ){
				    condITion=true;
				    break;
				}
			}
		    if(!condITion){
		        alert("请选择员工性别");
		        return false;
		    }else{
		    	return true;
		    }
	 	}
	 	/** ********检查手机号码、座机号码********** */
	 	function checkPhone(id,str,allowNull){
	 		if(isNotNull(id,str,allowNull)){
	 			var phone = $("#"+id).val();
	 			// 判断手机号格式
	 			var mobilePattern = /^1[358][0-9]{9}$/;
	 			// 判断座机格式 //座机格式是 010-98909899
				var phonePattern = /^(\d{3,4}\-?)?\d{7,8}$/;
	 			if(mobilePattern.test(phone) || phonePattern.test(phone)){
	 				return true;
	 			}else{
	 				alert("请输入正确格式的手机号或者座机号");
	 				$("#"+id).focus();
	 				return false;
	 			}
	 		}else{
	 			return false
	 		}	
	 	}
	 	
	 	/** ********检查邮箱-一个文本框多个邮箱***";"隔开******* */
	 	function checkMoreEmail(id){
	 		 var regEmail = /^[a-zA-Z0-9_\.]+@[a-zA-Z0-9-]+[\.a-zA-Z]+$/; // Email
			 var  obj=document.getElementById(id);
		  if(typeof(obj) == "undefined")
		  {
		     alert("The object is not exist!");
		     return false;
		  }
		  else if(obj.value.replace(/ /g,"").replace(/　/g,"") != "")
		  {
		      var t = obj.value.lastIndexOf(";");     
		      switch(t)
		      {
		         case -1:
		         if(regEmail.test(obj.value) == false)
		          {
		            obj.focus();
		            alert("邮件类型错误");
		            return false;
		          }
		         break;
		         case obj.value.length - 1:
		            obj.focus();
		            alert("删除最后1个;号");
		            return false;
		         break;                  
		         default:
		           var emailArray = obj.value.split(";");
		           for(var i=0; i<=emailArray.length-1; i++)
		           {
		             if(regEmail.test(emailArray[i]) == false)
		             {
		               obj.focus();
		               alert("邮件类型错误!\r\n" + emailArray[i]);
		               return false;
		             }  
		           }
		        }
		     }
		  return true;  
	 	}
	 	/** ********检查邮箱-一个文本框多个邮箱*","隔开********* */
	 	function checkMoreEmail2(id){ var regEmail = /^[a-zA-Z0-9_\.]+@[a-zA-Z0-9-]+[\.a-zA-Z]+$/; // Email
		 var  obj=document.getElementById(id);
		  if(typeof(obj) == "undefined")
		  {
		     alert("The object is not exist!");
		     return false;
		  }
		  else if(obj.value.replace(/ /g,"").replace(/　/g,"") == "")
		  {
		      obj.focus();
		      alert("请输入email!");
		      return false;
		  }
		  else
		  {
		      var t = obj.value.lastIndexOf(",");     
		      switch(t)
		      {
		         case -1:
		         if(regEmail.test(obj.value) == false)
		          {
		            obj.focus();
		            alert("邮件类型错误");
		            return false;
		          }
		         break;
		         case obj.value.length - 1:
		            obj.focus();
		            alert("删除最后1个,号");
		            return false;
		         break;                  
		         default:
		           var emailArray = obj.value.split(",");
		           for(var i=0; i<=emailArray.length-1; i++)
		           {
		             if(regEmail.test(emailArray[i]) == false)
		             {
		               obj.focus();
		               alert("邮件类型错误!\r\n" + emailArray[i]);
		               return false;
		             }  
		           }
		        }
		     }
		  return true;    
	 	}