<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/blue.css">
<script src= "./js/cart.js"></script>
<title>カート画面</title>
</head>
<body>
<jsp:include page="header.jsp" />

   <div id="contents">
      <h1>カート画面</h1>

   <s:if test='message!=""'>
      <div class="info blueText"><s:property value="message" escape="false" /></div>
   </s:if>

   <s:else>

   <s:form id="cartForm" class="pt20">
          <table class="list-table historytable">

                <tr>
                   <th>#</th>
                   <th>商品名</th>
                   <th>商品名ふりがな</th>
                   <th>商品画像</th>
                   <th>値段</th>
                   <th>発売会社名</th>
                   <th>発売年月日</th>
                   <th>購入個数</th>
                   <th>合計金額</th>
                </tr>

                <s:iterator value="cartList">

                <tr>
                   <td><s:checkbox name="checkList" class="checkList" value="checked" fieldValue="%{productId}"  onchange="checkValue(this)"/></td>
                    <s:hidden name="productId" value="%{productId}"/>
                   <td><s:property value="productName" /></td>
                   <td><s:property value="productNameKana" /></td>
                   <td><img src='<s:property value="imageFilePath" />/<s:property value="imageFileName" />'></td>
                   <td><s:property value="price" />円</td>
                   <td><s:property value="releaseCompany" /></td>
                   <td><s:property value="date" /></td>
                   <td><s:property value="productCount" /></td>
                   <td><s:property value="goukei" />円</td>
                </tr>

                </s:iterator>

         </table>

          <div class="cartText tR">カート合計金額:<s:property value="cartgoukeiprice" />円</div>

      <div class="submit_btn_box_cart">
        <s:submit value="決済" class="submit_btn" onclick="goSettlementConfirmAction()"/>
      </div>

      <div class="submit_btn_box_cart">
	    <s:submit value="削除" id="deleteButton" class="submit_btn" onclick="goDeleteCartAction()" disabled="true"/>
      </div>
     </s:form>
   </s:else>

</div>

</body>
</html>