package com.fiftienthlecture.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class User {
	
	public int user_id;
	public String user_name;
	public int videos_borrowed;
	public int debt;
	
	
	public static LinkedList<Video> videos = new LinkedList<Video>();
	
	public static Connection getConnection() throws Exception{
		try{
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		}catch (Exception e){
			
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		Statement stmt = con.createStatement();
		//I'm lazy like that
		Video emptySlot = new Video();
		videos.add(emptySlot);
		ResultSet rs = stmt.executeQuery("SELECT * from videos;");
		while (rs.next()) {
			Video video = new Video();
			video.setVideo_copies(rs.getInt("video_copies"));
			video.setVideo_id(rs.getInt("video_id"));
			video.setVideo_name(rs.getString("video_name"));
			videos.add(video);
		}
	}
	

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getVideos_borrowed() {
		return videos_borrowed;
	}

	public void setVideos_borrowed(int videos_borrowed) {
		this.videos_borrowed = videos_borrowed;
	}

	public int getDebt() {
		return debt;
	}

	public void setDebt(int debt) {
		this.debt = debt;
	}
	
	public void withdraw(int videoId) throws Exception{
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con2 = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		Statement usrstmt = con2.createStatement();

		java.util.Date today = new java.util.Date();
		Date cOutdate = new Date(today.getTime());
		System.out.println(cOutdate);
		//ResultSet noMoreVidsLeft = stmt.executeQuery("select * from videos where video_id = " + videoId + " and video_copies = 0;");
		if(this.videos_borrowed<5 && videos.get(videoId).video_copies >0){
			usrstmt.executeUpdate("insert into users_to_videos (user_id, video_id)" + 
								" values ("+ this.user_id + ", " + videoId+");");
			usrstmt.executeUpdate("update videos set video_copies="+ (videos.get(videoId).getVideo_id() -1) +" where video_id="+ videoId+";");
			System.out.println( this.user_name + " borrowed "+ videos.get(videoId).getVideo_name() + " on " + cOutdate);
			if(cOutdate.equals(today)){
			usrstmt.executeUpdate("update users set videos_borrowed=" + (this.videos_borrowed+1) +" where user_id="+ this.user_id+";");
			} else if (this.videos_borrowed>=5){
				System.out.println("This dude " + this.user_name + " can't get more vids today");
			}
		}else{
			System.out.println("Can't do it");
		}
	}
	
	public void deposit(int videoId) throws Exception{
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		Statement stmt = con.createStatement();
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String checkInDate = format.format(date);
		ResultSet hasWithdrawn = stmt.executeQuery("select * from users_to_videos where user_id=" + this.user_id + " and video_id=" + videoId+";");
		if (hasWithdrawn.next()){
			stmt.executeUpdate("delete from users_to_videos where user_id=" + this.user_id + " and video_id=" + videoId+";");
			stmt.executeUpdate("update videos set video_copies="+ (videos.get(videoId).getVideo_id() +1) +" where video_id="+ videoId+";");
			System.out.println(this.user_name + " returned " + videos.get(videoId).getVideo_name() + " on " + checkInDate );
		} else {
			System.out.println("You didn't get this book in the first place...");
		}

	}
	
	public void checkDebt() throws Exception{
		String url = "jdbc:mysql://localhost:3306/videostore";
		String user = "root";
		String password = "Tolinator69";
		Connection con = DriverManager.getConnection(url, user, password);
		Class.forName("com.mysql.jdbc.Driver");
		Statement stmt = con.createStatement();
		if (this.videos_borrowed>= 5){
			this.debt+= 2;
			stmt.executeUpdate("update users set debt=" + (this.debt+1) +" where user_id="+ this.user_id+";");
		}
		
	}
}
