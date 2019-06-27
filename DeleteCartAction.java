package com.internousdev.blue.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.blue.dao.CartInfoDAO;
import com.internousdev.blue.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware {

	Map<String,Object> session;
	ArrayList<CartInfoDTO> cartList=new ArrayList<CartInfoDTO>();
	CartInfoDAO dao=new CartInfoDAO();

    private String message="";
    private int cartgoukeiprice;
    private String user_id;
    private Collection<String> checkList;

    public String execute(){

    	String result=ERROR;
    	if(!session.containsKey("userId") && !session.containsKey("tempUserId") ){    //セッションタイムアウト

			return "sessionTimeout";
    	}

    		int loginFig=Integer.parseInt(session.get("logined").toString());

			if(loginFig==1){
				user_id=session.get("userId").toString();
			}else{
				user_id=session.get("tempUserId").toString();
			}

    		for(String productId:checkList){

    		   int num=dao.DeleteCart(productId, user_id);

    		   if(num<=0){
    			   return ERROR;
    		   }

    		}

    		   cartList=dao.getCartInfo(user_id); //削除後カートから情報取得

    		   if(cartList.size()>0){

    			   cartgoukeiprice=dao.CartGoukeiPrice(user_id);
    			   if(cartgoukeiprice>0){

   					result=SUCCESS;

   				}else{

   					return ERROR;

   				}


    		    }else{

    			   message="カート情報がありません。";
    			   result=SUCCESS;

    		   }



    	return result;

    }

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public ArrayList<CartInfoDTO> getCartList() {
		return cartList;
	}

	public void setCartList(ArrayList<CartInfoDTO> cartList) {
		this.cartList = cartList;
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Collection<String> getCheckList() {
		return checkList;
	}

	public void setCheckList(Collection<String> checkList) {
		this.checkList = checkList;
	}
}
