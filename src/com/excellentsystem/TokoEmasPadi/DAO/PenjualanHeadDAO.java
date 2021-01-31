/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class PenjualanHeadDAO {
    
    public static List<PenjualanHead> getAllByTglPenjualanAndStatus(Connection con, 
            String tglMulai, String tglAkhir, String status)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_head "
                + " where left(tgl_penjualan,10) between ? and ? and status = ?");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, status);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> allPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalBeratPembulatan(rs.getDouble(8));
            p.setGrandtotal(rs.getDouble(9));
            p.setDiskon(rs.getDouble(10));
            p.setCatatan(rs.getString(11));
            p.setStatus(rs.getString(12));
            p.setTglBatal(rs.getString(13));
            p.setUserBatal(rs.getString(14));
            allPenjualan.add(p);
        }
        return allPenjualan;
    }
    public static List<PenjualanHead> getAllByTglBatal(Connection con, String tglMulai,String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_head "
                + " where left(tgl_batal,10) between ? and ? and status='false'");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> allPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalBeratPembulatan(rs.getDouble(8));
            p.setGrandtotal(rs.getDouble(9));
            p.setDiskon(rs.getDouble(10));
            p.setCatatan(rs.getString(11));
            p.setStatus(rs.getString(12));
            p.setTglBatal(rs.getString(13));
            p.setUserBatal(rs.getString(14));
            allPenjualan.add(p);
        }
        return allPenjualan;
    }
    public static PenjualanHead get(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_head "
                + " where no_penjualan = ?");
        ps.setString(1, noPenjualan);
        ResultSet rs = ps.executeQuery();
        PenjualanHead p = null;
        while(rs.next()){
            p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalBeratPembulatan(rs.getDouble(8));
            p.setGrandtotal(rs.getDouble(9));
            p.setDiskon(rs.getDouble(10));
            p.setCatatan(rs.getString(11));
            p.setStatus(rs.getString(12));
            p.setTglBatal(rs.getString(13));
            p.setUserBatal(rs.getString(14));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps =  con.prepareStatement("select max(right(no_penjualan,4)) from tt_penjualan_head "
                + " where mid(no_penjualan,4,6)=? ");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(rs.getInt(1)+1);
        else
            return "PJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(1);
    }
    public static void insert(Connection con, PenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_head values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setString(2, p.getTglPenjualan());
        ps.setString(3, p.getKodeSales());
        ps.setString(4, p.getNama());
        ps.setString(5, p.getAlamat());
        ps.setDouble(6, p.getTotalQty());
        ps.setDouble(7, p.getTotalBerat());
        ps.setDouble(8, p.getTotalBeratPembulatan());
        ps.setDouble(9, p.getGrandtotal());
        ps.setDouble(10, p.getDiskon());
        ps.setString(11, p.getCatatan());
        ps.setString(12, p.getStatus());
        ps.setString(13, p.getTglBatal());
        ps.setString(14, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_penjualan_head set "
                + " tgl_penjualan=?, kode_sales=?, nama=?, alamat=?, total_qty=?, total_berat=?, "
                + " total_berat_pembulatan=?, grandtotal=?, diskon=?, catatan=?, status=?, tgl_batal=?, user_batal=? "
                + " where no_penjualan=? ");
        ps.setString(1, p.getTglPenjualan());
        ps.setString(2, p.getKodeSales());
        ps.setString(3, p.getNama());
        ps.setString(4, p.getAlamat());
        ps.setDouble(5, p.getTotalQty());
        ps.setDouble(6, p.getTotalBerat());
        ps.setDouble(7, p.getTotalBeratPembulatan());
        ps.setDouble(8, p.getGrandtotal());
        ps.setDouble(9, p.getDiskon());
        ps.setString(10, p.getCatatan());
        ps.setString(11, p.getStatus());
        ps.setString(12, p.getTglBatal());
        ps.setString(13, p.getUserBatal());
        ps.setString(14, p.getNoPenjualan());
        ps.executeUpdate();
    }
}
