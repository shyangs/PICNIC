package com.picmem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class PicmemJNDIDAO implements PicmemDAO_interface {
	private static DataSource ds = null;
	static {

		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	private static final String INSERT_STMT = "insert into PICMEM (PICNIC_NO,MEM_NO,PICMEM_IDEN,PICMEM_STA,MEM_LONGI,MEM_LATIT) values(?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "select * from PICMEM order by PICNIC_NO";
	private static final String GET_ONE_STMT = "select PICNIC_NO,MEM_NO,PICMEM_IDEN,PICMEM_STA,MEM_LONGI,MEM_LATIT from PICMEM where PICNIC_NO =? MEM_NO =? order by PICNIC_NO";
	private static final String DELETE_STMT = "delete from PICMEM where PICNIC_NO =? MEM_NO =?";
	private static final String UPDATE_STMT = "update PICMEM set PICMEM_IDEN=?,PICMEM_STATUS=?,MEM_LONGI=?,MEM_LATIT=? where PICNIC_NO =? MEM_NO =? where PICNIC_NO= ? ,MEM_NO = ?";

	@Override
	public void insert(PicmemVO picmemVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, picmemVO.getPicnic_no());
			pstmt.setString(2, picmemVO.getMem_no());
			pstmt.setString(3, picmemVO.getPicmem_iden());
			pstmt.setString(4, picmemVO.getPicmem_sta());
			pstmt.setDouble(5, picmemVO.getMem_longi());
			pstmt.setDouble(5, picmemVO.getMem_latit());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void update(PicmemVO picmemVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, picmemVO.getPicmem_iden());
			pstmt.setString(2, picmemVO.getPicmem_sta());
			pstmt.setDouble(3, picmemVO.getMem_longi());
			pstmt.setDouble(4, picmemVO.getMem_latit());
			pstmt.setString(5, picmemVO.getPicnic_no());
			pstmt.setString(6, picmemVO.getMem_no());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void delete(String picnic_no, String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
			pstmt.setString(1, picnic_no);
			pstmt.setString(2, mem_no);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public PicmemVO findByPrimaryKey(String picnic_no, String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		PicmemVO picmemVO = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setString(1, picnic_no);
			pstmt.setString(2, mem_no);
			rs = pstmt.executeQuery();

			picmemVO = new PicmemVO();
			picmemVO.setPicmem_iden(rs.getString("PICMEM_IDEN"));
			picmemVO.setPicmem_sta(rs.getString("PICMEM_STA"));
			picmemVO.setMem_longi(rs.getDouble("MEM_LONGI"));
			picmemVO.setMem_latit(rs.getDouble("MEM_LATIT"));

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return picmemVO;
	}

	@Override
	public List<PicmemVO> getAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		List<PicmemVO> list = new ArrayList<PicmemVO>();
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PicmemVO picmemVO = new PicmemVO();
				picmemVO.setPicnic_no(rs.getString("PICNIC_NO"));
				picmemVO.setMem_no(rs.getString("MEM_NO"));
				picmemVO.setPicmem_iden(rs.getString("PICMEM_IDEN"));
				picmemVO.setPicmem_sta(rs.getString("PICMEM_STA"));
				picmemVO.setMem_longi(rs.getDouble("MEM_LONGI"));
				picmemVO.setMem_latit(rs.getDouble("MEM_LATIT"));
				list.add(picmemVO);
			}

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
}
