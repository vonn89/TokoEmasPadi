/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
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
public class PembelianHeadDAO {

    public static List<PembelianHead> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_head "
                + " where left(tgl_batal,10) between ? and ? and status = 'false'");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PembelianHead> allPembelian = new ArrayList<>();
        while(rs.next()){
            PembelianHead p = new PembelianHead();
            p.setNoPembelian(rs.getString(1));
            p.setTglPembelian(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalPembelian(rs.getDouble(8));
            p.setCatatan(rs.getString(9));
            p.setStatus(rs.getString(10));
            p.setTglBatal(rs.getString(11));
            p.setUserBatal(rs.getString(12));
            allPembelian.add(p);
        }
        return allPembelian;
    }
    public static List<PembelianHead> getAllByTglBeliAndStatus(Connection con, 
            String tglMulai, String tglAkhir, String status)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_head "
                + " where left(tgl_pembelian,10) between ? and ? and status = ?");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, status);
        ResultSet rs = ps.executeQuery();
        List<PembelianHead> allPembelian = new ArrayList<>();
        while(rs.next()){
            PembelianHead p = new PembelianHead();
            p.setNoPembelian(rs.getString(1));
            p.setTglPembelian(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalPembelian(rs.getDouble(8));
            p.setCatatan(rs.getString(9));
            p.setStatus(rs.getString(10));
            p.setTglBatal(rs.getString(11));
            p.setUserBatal(rs.getString(12));
            allPembelian.add(p);
        }
        return allPembelian;
    }
    public static PembelianHead get(Connection con, String noPembelian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_head "
                + " where no_pembelian = ?");
        ps.setString(1, noPembelian);
        ResultSet rs = ps.executeQuery();
        PembelianHead p = null;
        while(rs.next()){
            p = new PembelianHead();
            p.setNoPembelian(rs.getString(1));
            p.setTglPembelian(rs.getString(2));
            p.setKodeSales(rs.getString(3));
            p.setNama(rs.getString(4));
            p.setAlamat(rs.getString(5));
            p.setTotalQty(rs.getInt(6));
            p.setTotalBerat(rs.getDouble(7));
            p.setTotalPembelian(rs.getDouble(8));
            p.setCatatan(rs.getString(9));
            p.setStatus(rs.getString(10));
            p.setTglBatal(rs.getString(11));
            p.setUserBatal(rs.getString(12));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps =  con.prepareStatement("select max(right(no_pembelian,4)) from tt_pembelian_head "
                + " where mid(no_pembelian,4,6)=? ");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(rs.getInt(1)+1);
        else
            return "PB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(1);
    }
    public static void insert(Connection con, PembelianHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pembelian_head values (?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPembelian());
        ps.setString(2, p.getTglPembelian());
        ps.setString(3, p.getKodeSales());
        ps.setString(4, p.getNama());
        ps.setString(5, p.getAlamat());
        ps.setDouble(6, p.getTotalQty());
        ps.setDouble(7, p.getTotalBerat());
        ps.setDouble(8, p.getTotalPembelian());
        ps.setString(9, p.getCatatan());
        ps.setString(10, p.getStatus());
        ps.setString(11, p.getTglBatal());
        ps.setString(12, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PembelianHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_pembelian_head set "
                + " tgl_pembelian = ?, kode_sales = ?, nama = ?, alamat = ?, "
                + " total_qty = ?, total_berat = ?, total_pembelian = ?, catatan = ?, status=?, tgl_batal=?, user_batal=? "
                + " where no_pembelian=? ");
        ps.setString(1, p.getTglPembelian());
        ps.setString(2, p.getKodeSales());
        ps.setString(3, p.getNama());
        ps.setString(4, p.getAlamat());
        ps.setDouble(5, p.getTotalQty());
        ps.setDouble(6, p.getTotalBerat());
        ps.setDouble(7, p.getTotalPembelian());
        ps.setString(8, p.getCatatan());
        ps.setString(9, p.getStatus());
        ps.setString(10, p.getTglBatal());
        ps.setString(11, p.getUserBatal());
        ps.setString(12, p.getNoPembelian());
        ps.executeUpdate();
    }
}
