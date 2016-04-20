package com.fiftienthlecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Scanner;

public class VideoStore {
	
	public static java.util.Date thisDate = new java.util.Date();
	public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static LinkedList<User> users = new LinkedList<User>();
	
	public static Connection getConnection() throws Exception{
		try{
			String url = "jdbc:mysql://localhost:3306/videostore";
			String user = "root";
			String password = "Tolinator69";
			Connection con = DriverManager.getConnection(url, user, password);
		}catch (Exception e){
			
		}
		return null;
	}

	public boolean checkOverdue(int id) throws Exception{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
		ResultSet checkBorrowLimit = stmt.executeQuery("select * from users where videos_borrowed = 5 and user_id = " + id);
		if (checkBorrowLimit.next()){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {		
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * from users;");
		User emptySlot = new User();
		users.add(emptySlot);
		Scanner in = new Scanner(System.in);
		while (rs.next()) {
			User user1 = new User();
			user1.user_id = rs.getInt("user_id");
			user1.user_name = rs.getString("user_name");
			user1.debt = rs.getInt("debt");
			user1.videos_borrowed = rs.getInt("videos_borrowed");
			users.add(user1);
		}
		//I'm lazy like that
		Video emptyVidSlot = new Video();
		User.videos.add(emptyVidSlot);
		ResultSet rs2 = stmt.executeQuery("SELECT * from videos;");
		while (rs2.next()) {
			Video video = new Video();
			video.setVideo_copies(rs2.getInt("video_copies"));
			video.setVideo_id(rs2.getInt("video_id"));
			video.setVideo_name(rs2.getString("video_name"));
			User.videos.add(video);
		}
		//Withdraw
		System.out.println("Pick a user to withdraw a video \n 1 \n 2 \n 3");
		int userId = in.nextInt();
		System.out.println("Pick a video for "+ users.get(userId).getUser_name() +" to withdraw  \n 1 \n 2 \n 3");
		int video_id = in.nextInt();
		users.get(userId).withdraw(video_id);
		stmt.close();
		
		//Deposit
	/*	System.out.println("Pick a user to return a video \n 1 \n 2 \n 3");
		int userId = in.nextInt();
		System.out.println("Pick a video for "+ users.get(userId).getUser_name() +" to return  \n 1 \n 2 \n 3");
		int video_id = in.nextInt();
		users.get(userId).deposit(video_id);
		stmt.close();*/
	}
	
}
