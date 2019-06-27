package com.internousdev.blue.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.blue.dto.CartInfoDTO;
import com.internousdev.blue.util.DBConnector;

public class CartInfoDAO {

	public ArrayList<CartInfoDTO> getCartInfo(String userId){  //全件取得 jspの変数はcartListで

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		ArrayList<CartInfoDTO> CartInfo=new ArrayList<CartInfoDTO>();

		String SQL="select ci.id,pi.product_name,pi.product_name_kana,pi.image_file_path,pi.image_file_name,pi.price,pi.release_company,pi.regist_date,ci.product_id,ci.product_count from product_info pi left join cart_info ci on pi.product_id=ci.product_id where ci.user_id=? order by ci.update_date DESC,ci.regist_date DESC";

		try{

			PreparedStatement ps=con.prepareStatement(SQL);
			ps.setString(1, userId);
			ResultSet rs=ps.executeQuery();

			while(rs.next()){

				CartInfoDTO dto=new CartInfoDTO();
				dto.setId(rs.getInt("id"));
				dto.setProductName(rs.getString("product_name"));
				dto.setProductNameKana(rs.getString("product_name_kana"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setPrice(rs.getInt("price"));
				dto.setReleaseCompany(rs.getString("release_company"));
				dto.setDate(rs.getDate("regist_date"));
				dto.setProductId(rs.getInt("product_id"));
				dto.setProductCount(rs.getInt("product_count"));
				int p=dto.getPrice();
				int c=dto.getProductCount();
				dto.setGoukei(p*c);    //商品の1個あたりの値段*個数でカートの一番右の項目を出すやつ
				CartInfo.add(dto);

			}

         }catch(Exception e){

			e.printStackTrace();
         }

		finally{
			try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();

		}
			}

		return CartInfo;
	}

	public int AddCart(String userId,String productId,int price,int productCount){ //商品追加

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();
		int AddKensu=0;
		int proGoukei=price*productCount;

		String sql="insert into cart_info (user_id,product_id,product_count,price,regist_date,update_date) values(?,?,?,?,now(),now())";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);
			ps.setInt(3, productCount);
			ps.setInt(4, proGoukei);
			AddKensu=ps.executeUpdate();

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();

		}
		}

      return AddKensu;

	}

	public int CartGoukeiPrice(String userId){

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		int cartgoukeiprice=0;

		String sql="select price from cart_info where user_id=?";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs=ps.executeQuery();

			while(rs.next()){

				int price=rs.getInt("price");
				cartgoukeiprice+=price;

			}

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();
		}
		}

		return cartgoukeiprice;

	}

	public void DeleteCartItem(String Id){  //削除 idはaction内でarraylistで取得 購入確定後にカートの中身を削除する場合はユーザIDに紐付いているカートIDを入れてあげればOK

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		String sql="delete from cart_info where id=?";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, Id);
			ps.executeUpdate();

		}catch(Exception e){

		    e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();
		}
		}

		}

	public List<String> getCartIdbyUserId(String userId){   //settlementcompleteで使いそう 特定のユーザIDに紐付いているカートIDの取得

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();
		List<String> cartId=new ArrayList<>();

		String sql="select id from cart_info where user_id=? order by id DESC";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs=ps.executeQuery();

			while(rs.next()){

				cartId.add(rs.getString("id"));
			}
		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		}

		return cartId;

	}

	public boolean SearchSameItem(String userId,String productId){  //同じアイテムが既に存在すればtrueを返す
		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		int SameItemNum=0;
		boolean result=false;

		String sql="select * from cart_info where user_id=? and product_id=?";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);
			ResultSet rs=ps.executeQuery();

			while(rs.next()){

				SameItemNum++;

			}

			if(SameItemNum==0){

				result=false;

			}else{

				result=true;

			}

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();
		}
		}

		return result;
	}

	public void SetSameItemCount(String productId,int productCount,String userId,int price){  //AddCartの際追加するものと同じアイテムがある場合は件数を更新(追加)する

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		int Addprice=productCount*price;

		String sql="update cart_info set product_count=product_count+? , update_date=now(),price=price+? where user_id=? and product_id=?";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setInt(2, Addprice);
			ps.setString(3, userId);
			ps.setString(4,productId);
			ps.executeUpdate();

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();

		}
		}

	}

	public int UpdateCartItemUserId(String userId,String randomid){  //ランダムIDのカート情報をログイン後の正規ユーザID情報に書き換える

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();
		String sql="update cart_info set user_id=? where user_id=?";
		int num=0;

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, randomid);
			num=ps.executeUpdate();   //0なら失敗1以上なら成功

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		}

		return num;

	}

	public List<String> getchouhukuProId(String userId){   //重複してる商品IDの一覧を取得

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();
		List<String> sameProIdList=new ArrayList<>();
		String sql="select product_id from cart_info where user_id=? group by product_id having count(*)>1";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
		    ResultSet rs=ps.executeQuery();

		    while(rs.next()){

		    	sameProIdList.add(rs.getString("product_id"));
		    }

		}catch(Exception e){

			e.printStackTrace();

	}
		finally{

		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		}

		return sameProIdList;

	}

    public List<String> getCartIdbyProId(String userId,String productId){ //重複商品IDに紐付いているカートIDを昇順で取得

    	DBConnector dbc=new DBConnector();
    	Connection con=dbc.getConnection();
    	List<String> cartId=new ArrayList<>();

    	String sql="select id from cart_info where user_id=? and product_id=? order by id ASC";

    	try{

    		PreparedStatement ps=con.prepareStatement(sql);
    		ps.setString(1, userId);
    		ps.setString(2, productId);
    		ResultSet rs=ps.executeQuery();

    		while(rs.next()){

    			cartId.add(rs.getString("id"));
    		}
    	}catch(Exception e){

    		e.printStackTrace();

    	}finally{
    	try{

    		con.close();

    	}catch(Exception e){

    		e.printStackTrace();

    	}
    	}

    	return cartId;

    }

	public void omatomecart(String userId,String productId,String cart_id){ //被ってる物の値段と数を合計値に書き換える 書き換え先は一番最初のカートIDを推奨

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		String sql="update cart_info AS ci JOIN(select product_id,sum(price) as sumprice,sum(product_count) as sumcount from cart_info where user_id=? and product_id=?)AS ans ON ci.product_id=ans.product_id set ci.product_count=ans.sumcount,ci.price=ans.sumprice where id=? ";

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);
			ps.setString(3, cart_id);   //どのカート情報を書き換えるかのやつ 上のやつのlist(0)を使うのが分かりやすい
			ps.executeUpdate();

		}catch(Exception e){

			e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();

		}
		}

	}

	public void DelCartUserId(String userId){

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();
		String sql="delete from cart_info where user_id=?";

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.executeUpdate();

		}catch(Exception e){

		e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();

		}
		}
	}

	public void DeleteCart(String productId,String userId){  //削除(productId版）

		DBConnector dbc=new DBConnector();
		Connection con=dbc.getConnection();

		String sql="delete from cart_info where product_id=? and user_id=?";

		try{

			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, productId);
			ps.setString(2, userId);
			ps.executeUpdate();

		}catch(Exception e){

		    e.printStackTrace();

		}finally{
		try{

			con.close();

		}catch(Exception e){

			e.printStackTrace();
		}
		}

		}

}
