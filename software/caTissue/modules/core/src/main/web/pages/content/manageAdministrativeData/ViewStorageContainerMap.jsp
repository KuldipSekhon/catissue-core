<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script language="JavaScript" type="text/javascript"src="newDhtmlx/dhtmlxcommon.js"></script>
	<script  language="JavaScript" type="text/javascript"src="newDhtmlx/dhtmlxgrid.js"></script> 
	<script language="JavaScript" type="text/javascript" src="newDhtmlx/dhtmlxgridcell.js"></script> 
	<link rel="STYLESHEET" type="text/css" href="newDhtmlx/dhtmlxgrid.css">
	<script language="JavaScript" type="text/javascript" src='newDhtmlx/dhtmlxgrid_export.js'></script>
</head>
<%
	String siteName = (String) request.getAttribute("siteName");   
%>


<script>

<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
String [][] childContainerName = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_NAME);
String oneDimLabellingScheme = (String)request.getAttribute("oneDimLabellingScheme");
String twoDimLabellingScheme = (String)request.getAttribute("twoDimLabellingScheme");
String storageContainerIdentifier = (String) request.getAttribute("storageContainerIdentifier");
%>
function setParentWindowValue(elementName,elementValue)
{
	for (var i=0;i < top.opener.document.forms[0].elements.length;i++)
    {	
    	if (top.opener.document.forms[0].elements[i].name == elementName)
		{
			top.opener.document.forms[0].elements[i].value = elementValue;
		}
    }
}

function setCustomListBoxValue(elementId,elementValue)
{
	var id = parent.opener.document.getElementById(elementId);
	
	if(elementId.match("_2"))
	{
		id.value = elementValue;
	}
	else
	{
		id.value = elementValue;
		parent.opener.onCustomListBoxChange(id);
	}
}

function setTextBoxValue(elementId,elementValue)
{
	var id = parent.opener.document.getElementById(elementId);	
	id.value = elementValue;
}

function closeFramedWindow()
{
	top.window.close();
}


function refresh_tree(nodeId)
{	
	window.parent.frames["SCTreeView"].location="<%=Constants.STORAGE_CONTAINER_TREE_ACTION%>?<%=Constants.PAGE_OF%>=<%=pageOf%>&<%=Constants.RELOAD%>=true&<%=Constants.TREE_NODE_ID%>="+nodeId;
}

function containerChanged()
{
	window.parent.containerChangedTrue();	
}
function containerInfoTab()
{	 
	  var isContainerChanged=window.parent.isChanged();
	  if(isContainerChanged == 'no')
		{
		   
			window.parent.frames['StorageContainerView'].location="StorageContainer.do?operation=edit&pageOf=pageOfStorageContainer&containerIdentifier=${requestScope.storageContainerIdentifier}";
		}
     else
		{
			
			window.parent.frames['StorageContainerView'].location="SearchObject.do?pageOf=pageOfTreeSC&operation=search&id=${requestScope.storageContainerIdentifier}";
		}
}	

</script>

<%
	StorageContainerGridObject storageContainerGridObject 
			= (StorageContainerGridObject)request.getAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT);
	int [][]fullStatus = (int [][])request.getAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS);
	int [][] childContainerIds = (int [][])request.getAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS);
    String [][] childContainerType = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_TYPE);
	
	
	String enablePageStr = (String)request.getAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE);
	boolean enablePage = false;
	if (enablePageStr!=null && enablePageStr.equals(Constants.TRUE))
		enablePage = true;
	String storageContainerType = null;
	Integer startNumber = null;
	Long positionOne = (Long)request.getAttribute(Constants.POS_ONE);
	Long positionTwo = (Long)request.getAttribute(Constants.POS_TWO);	
	if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
	{
		storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
		startNumber = (Integer)request.getAttribute(Constants.START_NUMBER);
	}
	
	String oneDimLabel = (String)request.getAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL);
	String twoDimLabel = (String)request.getAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL);
    
    String temp ="";
    String verStrTemp ="";
    String verTempOne ="";
    String verTempTwo ="";
    String filler = " ";
    
        for( int j=0; j<oneDimLabel.length();j++)
    {
        temp =oneDimLabel.substring(j,j+1);
        verStrTemp=temp.concat(filler); 
        verTempOne=verTempOne.concat(verStrTemp);
        
    } 
    temp ="";
    verStrTemp ="";
    
    for( int j=0; j<twoDimLabel.length();j++)
    {
        temp =twoDimLabel.substring(j,j+1);
        verStrTemp=temp.concat(filler); 
        verTempTwo=verTempTwo.concat(verStrTemp);
        
    } 
        
    
    
	int rowSpanValue = storageContainerGridObject.getOneDimensionCapacity().intValue();
	int colSpanValue = storageContainerGridObject.getTwoDimensionCapacity().intValue();
	System.out.println("rowSpanValue : "+ rowSpanValue + " || colSpanValue : " + colSpanValue);
	
	String containerStyle = (String)session.getAttribute(Constants.CONTAINER_STYLE);
	System.out.println(containerStyle);
	String containerStyleId = (String)session.getAttribute(Constants.CONTAINER_STYLEID);
	System.out.println(containerStyleId);
	String xDimStyleId = (String)session.getAttribute(Constants.XDIM_STYLEID);
	String yDimStyleId = (String)session.getAttribute(Constants.YDIM_STYLEID);
	
	String selectedContainerName= (String) session.getAttribute(Constants.SELECTED_CONTAINER_NAME);
    String containerId= (String) session.getAttribute(Constants.CONTAINERID);
    String pos1= (String) session.getAttribute(Constants.POS1);
    String pos2= (String)session.getAttribute(Constants.POS2);
		

	//Mandar: 29Aug06 : For container details
	List collectionProtocolList = (List)request.getAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST );
	List specimenClassList = (List)request.getAttribute(Constants.MAP_SPECIMEN_CLASS_LIST );
	List specimenTypeList = (List)request.getAttribute(Constants.MAP_SPECIMEN_TYPE_LIST );
	
	String specimenClass = null;
	String collectionGroup = null;
	String specimenMapKey = null;
	String specimenCallBackFunction = null;
	String nodeName = null;
 
   if (pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)) {
	   specimenClass = (String) session.getAttribute(Constants.SPECIMEN_CLASS);
	   collectionGroup = (String) session.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP);
	   specimenMapKey = (String) session.getAttribute(Constants.SPECIMEN_ATTRIBUTE_KEY);
	   specimenCallBackFunction =  (String) session.getAttribute(Constants.SPECIMEN_CALL_BACK_FUNCTION);
   }

%>
<script>
function exportContainer(fileType)
{
var outerdata = "<outerdata>";
outerdata += "<row><label font='boldOblique'>Container details</label> <text>  </text></row>";
outerdata += "<row><label>Container name: </label> <text><%=request.getAttribute("containerName")%></text></row>";
outerdata += "<row><label>Container hierarchy: </label> <text><%=request.getAttribute("hierarchy")%></text></row>";
outerdata += "<row><label>  </label> <text>   </text></row>";
outerdata += "<row><label font='boldOblique'>Storage Container Restrictions </label> <text>  </text></row>";
<%
String text = "";
for(int colcnt=0;colcnt<collectionProtocolList.size();colcnt++)
			{
				String data =(String) collectionProtocolList.get(colcnt );
				text += data + " ";
			}
%>
outerdata += "<row><label>Collection Protocol: </label> <text><%=text%> </text></row>"

<%	
	text = "";
	for(int colcnt=0;colcnt<specimenClassList.size();colcnt++)
	{
		if(colcnt!=0){
			text += ","; 
		}
		String data =(String) specimenClassList.get(colcnt );
		text += data + " ";

	}
%>
outerdata += "<row><label>Specimen Class: </label> <text><%=text%> </text></row>"

<%	
	text = "";
	for(int colcnt=0;colcnt<specimenTypeList.size();colcnt++)
	{
		if(colcnt!=0){
			text += ","; 
		}
		String data =(String) specimenTypeList.get(colcnt );
		text += data + " ";
		/*if(colcnt >=10)
		{
			break;
		}*/
	}
%>

outerdata += "<row><label>Specimen Type: </label> <text><%=text%> </text></row>";
outerdata += "<row><label>  </label> <text>  </text></row>";
outerdata += "<row><label>Dimension Label1 (Row): </label> <text> <%=request.getAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL)%></text></row>";
outerdata += "<row><label>Dimension Label2 (Column): </label> <text> <%=request.getAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL)%></text></row></outerdata>";
grid.toPDF('ContainerExportServlet?filename=<%=request.getAttribute("containerName")%>&filetype='+fileType+'&id=<%=storageContainerIdentifier%>', 'color',"","","",outerdata)
//document.getElementById("containerExportFrame").src="ContainerExportServlet?filetype=pdf&id=<%=storageContainerIdentifier%>";
}
</script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<%@ include file="/pages/content/common/ActionErrors.jsp" %>


<!-- target of anchor to skip menus -->
<table summary="" id="containerGridTable" cellpadding="0" cellspacing="0" border="0"  width="100%">
<%
	//System.out.println("CP No. : " +collectionProtocolList.size());
	//System.out.println("SC No. : " +specimenClassList.size());
	int rowSpan = getRowSpan(collectionProtocolList, 3);
//int rowSpan = (int)((collectionProtocolList.size()%3)== 0 ? collectionProtocolList.size()/3 : (collectionProtocolList.size()/3)+1 );
%>

		<tr>
		 <td width="5" valign="bottom" >&nbsp;</td>
           <td >
		     <table  border="0" cellpadding="0" cellspacing="0" width="100%">
                 <tr>
                    <td valign="bottom" ><a href="#"><img src="images/uIEnhancementImages/sc_info1.gif" alt="Container Info" width="111" height="20" border="0" onclick="containerInfoTab()"/></a></td>
                    <td   valign="bottom"><a href="#"><img src="images/uIEnhancementImages/cp_containerMap.gif" alt="View Map" width="111" height="20" border="0" /></a></td>
                     <td width="99%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                   </tr>
              </table>
			 </td>
          </tr>
		  
		
	
	<tr>
	 <td width="5" valign="bottom">&nbsp;</td>
		<td class="cp_tabtable" width="100%">
		 <table  border="0" cellpadding="3" cellspacing="0" width="100%"> 
					 <tr>		
                <td align="left" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
 <%
		int colspanForCPLabel;
	if(collectionProtocolList.size()>specimenClassList.size())
		colspanForCPLabel = collectionProtocolList.size();
    else 
	    colspanForCPLabel = specimenClassList.size();
%>         
	<tr>
          <td  align="left" class="tr_bg_blue1">
		  <!--input type="button" value="Export To PDF" class = "black_ar" onclick="exportContainer('pdf')"/-->
		  <input type="button" value="Export To CSV" class = "black_ar" onclick="exportContainer('csv')"/>
		  </td>
          </tr>        
		<tr>
          <td  align="left" class="tr_bg_blue1"><span class="blue_ar_b"> Storage Container Restrictions</span></td>
          </tr>
                  <tr>
					<td>
					  <table  border="0" cellspacing="0" cellpadding="0" width="100%"> 
						   	<td width="150">
								<table  border="0" cellspacing="2"  cellpadding="3" width="100%"> 
									<tr>
							<td  width="150" class="tabletd1">Collection Protocol</td>
							</tr>
								</table>
							</td>
							 <td>
								<table  border="0" cellspacing="2"  cellpadding="3" > 
									<tr>
<%	
			for(int colcnt=0;colcnt<collectionProtocolList.size();colcnt++)
			{
				String data =(String) collectionProtocolList.get(colcnt );
				
%>
                    <td  class="tabletd1"><%=data %></td>
<%
			}
%>			
							</tr>
								</table>
							</td>
							</tr>
						</table>
					</td>
                  </tr>
                  <tr>
				    <td>
						 <table  border="0" cellspacing="0" cellpadding="0" width="100%"> 
						   <tr>
							<td width="150">
								<table  border="0" cellspacing="2"  cellpadding="3" width="100%"> 
									<tr>
											<td width="150" class="tabletd1">Specimen Class</td>
									</tr>
								</table>
							</td>
							 <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
<%	
			for(int colcnt=0;colcnt<specimenClassList.size();colcnt++)
			{
				String data =(String) specimenClassList.get(colcnt );
				
%>
								<td  class="tabletd1" ><%=data %></td>
<%
			}
%>
								</tr>
								</table>
							</td>
							</tr>
						</table>
						</td>
                  </tr>

				  <tr>
					<td>
					  <table  border="0" cellspacing="0" cellpadding="0">
						 <tr>
							<td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
											<td width="140" class="tabletd1">Specimen Type</td>
												<%	
													for(int colcnt=0;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
												%>
														<td  class="tabletd1" ><%=data %></td>
												<%
														if(colcnt >=10)
														{
															break;
														}
													}
												%>
								    </tr>
								</table>
							 </td>
						</tr>
				
						<tr>
							<td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=11;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=20)
														{
															break;
														}			
													}
										%>
								  </tr>
								</table>
							</td>
					   </tr>
					   <tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
											<%	
														for(int colcnt=21;colcnt<specimenTypeList.size();colcnt++)
														{
															String data =(String) specimenTypeList.get(colcnt );
											%>
															<td  class="tabletd1" ><%=data %></td>
											<%
															if(colcnt >=30)
															{
																break;
															}			
														}
											%>
					
								</tr>
								</table>
							</td>
						</tr>
						<tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=31;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=40)
														{
															break;
														}			
													}
										%>
					
								</tr>
								</table>
							</td>
						</tr>
						<tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=41;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=50)
														{
															break;
														}			
													}
										%>
					
									</tr>
								</table>
							</td>
						</tr>
					</tr>
				
					
				</tr>
			</table>
						</td>
                </table></td>
              </tr>
			   <tr>
                <td class="bottomtd" colspan="2"></td>
              </tr>
			  
			  
				  
				  
				  
		
		<td  width="5"class="black_ar_t" >&nbsp;</td>
		<td class="black_ar" ><b>&nbsp;<%=verTempTwo%>&rarr;</b></td>
		 
	</tr>
				<tr>
					<td width="5" class="black_ar_t" align="center"><b><%=verTempOne%>&darr;<b></td>
					<td width="100%" height="100%" >
				<div id="containerGrid"></div>
			</td>
				</tr>
		<tr>
			
		</tr>		
		</table>
		</td>
	</tr>
</table>
<iframe id = "containerExportFrame" width = "0%" height = "0%" frameborder="0">
	</iframe>
	
<script>
var grid = new dhtmlXGridObject("containerGrid");
function loadGrid()
{
//var grid = new dhtmlXGridObject("containerGrid");
var headerString = "<%=request.getAttribute("gridHeader")%>";
var headerStringArray = headerString.split(",");

var columnWidth;
if(headerStringArray.length>10){
	columnWidth = 80;
}else{
	columnWidth = (100/headerStringArray.length);
}
var widthString = "";
var alignString = "";
var colType = "";
var colSorting = "na";
for(var cnt= 0 ;cnt< headerStringArray.length;cnt++){
	widthString += columnWidth;
	alignString += "center";
	colType += "ro";
	colSorting += "no";
	if(cnt<headerStringArray.length-1){
		widthString += ",";
		alignString += ",";
		colType += ",";
		colSorting += ",";
	}
}

//grid.setImagePath("dhtml_comp/imgs/"); 
grid.setHeader(headerString); 
if(headerStringArray.length>10){
grid.setInitWidths(widthString);
}else{
grid.setInitWidthsP(widthString);
} 

grid.setColAlign(alignString);
//grid.setAwaitedRowHeight(25);
grid.setColTypes(colType); 
grid.setColSorting(colSorting) ;
grid.setSkin("light");
grid.enableAlterCss("even","uneven");
grid.enableRowsHover(true,'grid_hover');
grid.enableAutoHeight(true);
grid.attachEvent("onXLE", function(grid_obj,count){});
//grid.setOnRowDblClickedHandler(selectLabel);
var myObject = eval('( <%=(StringBuffer) request.getAttribute("gridJson")%> )');

/*
var menu = new dhtmlXMenuObject("menuObj");
menu.addNewSibling(null, "export", "Export", false);
menu.addNewChild("export", 0, "pdf", "Pdf", false, "");
*/

grid.enableAlterCss("even","uneven");
grid.init();

grid.parse(myObject,"json");
}
document.body.onload = function(){
loadGrid();
      //alert("LOADED!");
  }

//document.getElementByTag("Body");

</script>
<%!
// method to return the rowspan value for the cell.
private int getRowSpan(List dataList, int columnNumber)
{
int rowSpan = 0;
rowSpan = (int)((dataList.size()%columnNumber)== 0 ? dataList.size()/columnNumber : (dataList.size()/columnNumber)+1 );
return rowSpan;
}


%>