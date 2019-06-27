package com.internousdev.blue.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.blue.dao.CartInfoDAO;
import com.internousdev.blue.dao.ProductInfoDAO;
import com.internousdev.blue.dto.CartInfoDTO;
import com.internousdev.blue.dto.ProductInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware {

	Map<String,Object> session;
	ArrayList<CartInfoDTO> cartList=new ArrayList<CartInfoDTO>();
	CartInfoDAO dao=new CartInfoDAO();

	private String productId;
	private int productCount;
	private String user_id;
	private String message="";
	private int cartgoukeiprice;

	public String execute(){

		String result=ERROR;

		// TODO 処理追加
		if(productCount<1 || productCount>5){ //1から5個の商品しかカートに追加できないためエラー
			return ERROR;
		}

		if(!session.containsKey("userId") && !session.containsKey("tempUserId") ){  //セッションタイムアウト
			return "sessionTimeout";
		}

		// TODO 処理追加
		ProductInfoDAO productInfoDAO=new ProductInfoDAO();
		int intProductId = Integer.parseInt(productId);
		ProductInfoDTO productInfoDTO = productInfoDAO.getProductInfoByProductId(intProductId);


		int loginFig=Integer.parseInt(session.get("logined").toString());
		if(loginFig==1){
			user_id=session.get("userId").toString();
		}else{
			user_id=session.get("tempUserId").toString();
		}

		int currentcartprice=dao.CartGoukeiPrice(user_id);
		int nextcartprice=(productInfoDTO.getPrice()*productCount)+currentcartprice;

		if(nextcartprice<0 || nextcartprice>2147483647){    //カート合計金額がマイナスないしintの限界値を超える場合は追加処理をせずにエラー画面に遷移する

			return ERROR;
		}

		boolean same=dao.SearchSameItem(user_id, productId); //カートに入れようとしている商品と同じ商品があるかを検索している

		if(same==false){   //同じアイテムが存在しない場合は普通に追加、取得

			int num=dao.AddCart(user_id, productId,productInfoDTO.getPrice(),productCount);

			if(num<=0){

				return ERROR;

			}

		}else{           //同じものが存在する場合は数と金額を更新する

			dao.SetSameItemCount(productId, productCount, user_id, productInfoDTO.getPrice());

		}

	    cartList=dao.getCartInfo(user_id);

	    cartgoukeiprice=dao.CartGoukeiPrice(user_id);

	    result=SUCCESS;

	    if(cartgoukeiprice<0){   //オーバーフローだったり負の値の暫定処理

	    	return ERROR;

	    }
	    if(cartList.size()==0){  //エラーで追加処理が不能だった場合かつカート情報が空の場合の処理

	    	message="カート情報がありません。";
	    	result=SUCCESS;

	    }

		return result;

	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public ArrayList<CartInfoDTO> getCartList() {
		return cartList;
	}

	public void setCartList(ArrayList<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCartgoukeiprice() {
		return cartgoukeiprice;
	}

	public void setCartgoukeiprice(int cartgoukeiprice) {
		this.cartgoukeiprice = cartgoukeiprice;
	}
}
