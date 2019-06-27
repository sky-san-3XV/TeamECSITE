<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<meta charset="UTF-8">
<script src="./js/header.js"></script>
<header>
   <div id="header">
   <div id="titlelogo">
      BLUE
   </div>
   <div id="headmenu">
      <ul>
         <s:form id="headid" name="headid">

            <s:if test="#session.mCategoryDTOList.size()>0 && #session.mCategoryDTOList!=null">

                 <li><s:select name="categoryId" list="#session.mCategoryDTOList" listKey="categoryId" listValue="categoryName" id="categoryId" class="selectcat" /></li>

            </s:if>

             <li><s:textfield name="keywords" class="txt-keywords" placeholder="検索ワード" /></li>
             <li><s:submit value="商品検索" class="submit_btn" onclick="SearchItem();" /></li>

            <s:if test="#session.logined==1" >

                 <li><s:submit value="ログアウト" class="submit_btn" onclick="Logout();" /></li>

            </s:if>

            <s:else>

                 <li><s:submit value="ログイン" class="submit_btn" onclick="Login();" /></li>

            </s:else>

            <li><s:submit value="カート" class="submit_btn" onclick="Cart();" /></li>
            <li><s:submit value="商品一覧"  class="submit_btn" onclick="Product();" /></li>

            <s:if test="#session.logined==1">

                 <li><s:submit value="マイページ" class="submit_btn" onclick="Mypage();" /></li>

            </s:if>
         </s:form>

</ul>
</div>
</div>
</header>