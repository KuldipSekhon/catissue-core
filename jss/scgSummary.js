var hourTimeCombo,minutesTimeCombo,rhourTimeCombo,rminutesTimeCombo,siteCombo;

function convertSelectToCombo()
{
 window.dhx_globalImgPath="dhtmlx_suite/imgs/";
dhtmlx.skin ='dhx_skyblue';	
siteCombo = dhtmlXComboFromSelect("site");
siteCombo.setSize("240");
siteCombo.attachEvent("onSelectionChange",processComboChange);
siteCombo.setName("site");

var receiverCombo = dhtmlXComboFromSelect("receiver");
receiverCombo.setSize("240");
receiverCombo.attachEvent("onSelectionChange",processComboChange);
receiverCombo.setName("receiver");

var collectCombo = dhtmlXComboFromSelect("collector");
collectCombo.setSize("240");
collectCombo.attachEvent("onSelectionChange",processComboChange);
collectCombo.setName("collector");

hourTimeCombo = dhtmlXComboFromSelect("hoursTime");
hourTimeCombo.setSize("40");

minutesTimeCombo = dhtmlXComboFromSelect("minuteTime");
minutesTimeCombo.setSize("40");

rhourTimeCombo = dhtmlXComboFromSelect("receivehoursTime");
rhourTimeCombo.setSize("40");

rminutesTimeCombo = dhtmlXComboFromSelect("receiveminuteTime");
rminutesTimeCombo.setSize("40");
}


function processComboChange()
{
 
scgDataJSON[this.name] = this.getSelectedValue();
}

function processData(obj)
{
 scgDataJSON[obj.name] = obj.value;
}

function submitSCG()
{
  //set received date
  scgDataJSON["receivedDate"] = document.getElementById("receivedDate").value+" "+rhourTimeCombo.getSelectedText()+":"+rminutesTimeCombo.getSelectedText();
  scgDataJSON["collectedDate"] = document.getElementById("collectedDate").value+" "+hourTimeCombo.getSelectedText()+ ":" + minutesTimeCombo.getSelectedText();
  scgDataJSON["scgName"] = document.getElementById("scgName").value;
  scgDataJSON["site"] = siteCombo.getSelectedValue();

  var response = dhtmlxAjax.postSync("SaveScgAjaxAction.do","dataJSON="+JSON.stringify(scgDataJSON)); 
  


alert(response.xmlDoc.responseText) ;
 if(response.xmlDoc.responseText=="success")
  {
   document.getElementById("errorDiv").innerHTML = "";
   window.parent.frames[1].pageSubmit();
  }
else
{
  var errorList = eval(response.xmlDoc.responseText);
 var i;
 var errormsg = "";
alert(errorList.length);
  for(i = 0;i<errorList.length;i++)
{
   errormsg = errormsg + "<div>"+errorList[i].msg+"</div>";
}  
   document.getElementById("errorDiv").style.display = "block";
   document.getElementById("errorDiv").innerHTML =document.getElementById("errorDiv").innerHTML
+ errormsg;
   
}

}