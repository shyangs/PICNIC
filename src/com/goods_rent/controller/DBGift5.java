package com.goods_rent.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


public class DBGift5 extends HttpServlet {
	Connection con;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("image/gif");
		req.setCharacterEncoding("Big5");
		ServletOutputStream out = res.getOutputStream();
		
		//System.out.println(MEM_NO2);
		try {
			String GR_NO = req.getParameter("GR_NO");
			
			String GR_NO2 = new String(GR_NO.getBytes("ISO-8859-1"), "Big5");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT GR_IMG FROM GOODS_RENT WHERE GR_NO = '" + GR_NO2 + "'");

			if (rs.next()) {
				
				ByteArrayInputStream in = new ByteArrayInputStream(rs.getBytes("gr_img"));
				byte[] buf = new byte[4 * 1024]; // 4K buffer
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
			} else {
				InputStream in = getServletContext().getResourceAsStream("/NoData/nopic.jpg");
				byte[] b = new byte[in.available()];
				in.read(b);
				out.write(b);
				in.close();
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			InputStream in = getServletContext().getResourceAsStream("/NoData/nopic.jpg");
			byte[] b = new byte[in.available()];
			in.read(b);
			out.write(b);
			in.close();
		}
	}

	public void init() throws ServletException {
		try {
			Context ctx = new javax.naming.InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ba101_5");
			con = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
