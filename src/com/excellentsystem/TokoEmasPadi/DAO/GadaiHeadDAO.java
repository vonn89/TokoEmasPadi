/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
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
public class GadaiHeadDAO {

    public static List<GadaiHead> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_head "
                + " where left(tgl_batal,10) between ? and ? and status_batal = 'true'");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<GadaiHead> allGadai = new ArrayList<>();
        while(rs.next()){
            GadaiHead h = new GadaiHead();
            h.setNoGadai(rs.getString(1));
            h.setTglGadai(rs.getString(2));
            h.setKodeSales(rs.getString(3));
            h.setNama(rs.getString(4));
            h.setAlamat(rs.getString(5));
            h.setKeterangan(rs.getString(6));
            h.setTotalQty(rs.getInt(7));
            h.setTotalBerat(rs.getDouble(8));
            h.setTotalPinjaman(rs.getDouble(9));
            h.setLamaPinjam(rs.getInt(10));
            h.setBungaPersen(rs.getDouble(11));
            h.setBungaKomp(rs.getDouble(12));
            h.setBungaRp(rs.getDouble(13));
            h.setJatuhTempo(rs.getString(14));
            h.setStatusLunas(rs.getString(15));
            h.setTglLunas(rs.getString(16));
            h.setSalesLunas(rs.getString(17));
            h.setStatusBatal(rs.getString(18));
            h.setTglBatal(rs.getString(19));
            h.setSalesBatal(rs.getString(20));
            allGadai.add(h);
        }
        return allGadai;
    }
    public static List<GadaiHead> getAllByTglLunas(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_head "
                + " where left(tgl_lunas,10) between ? and ? and status_lunas = 'true'");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<GadaiHead> allGadai = new ArrayList<>();
        while(rs.next()){
            GadaiHead h = new GadaiHead();
            h.setNoGadai(rs.getString(1));
            h.setTglGadai(rs.getString(2));
            h.setKodeSales(rs.getString(3));
            h.setNama(rs.getString(4));
            h.setAlamat(rs.getString(5));
            h.setKeterangan(rs.getString(6));
            h.setTotalQty(rs.getInt(7));
            h.setTotalBerat(rs.getDouble(8));
            h.setTotalPinjaman(rs.getDouble(9));
            h.setLamaPinjam(rs.getInt(10));
            h.setBungaPersen(rs.getDouble(11));
            h.setBungaKomp(rs.getDouble(12));
            h.setBungaRp(rs.getDouble(13));
            h.setJatuhTempo(rs.getString(14));
            h.setStatusLunas(rs.getString(15));
            h.setTglLunas(rs.getString(16));
            h.setSalesLunas(rs.getString(17));
            h.setStatusBatal(rs.getString(18));
            h.setTglBatal(rs.getString(19));
            h.setSalesBatal(rs.getString(20));
            allGadai.add(h);
        }
        return allGadai;
    }
    public static List<GadaiHead> getAllByTglGadaiAndStatusLunasAndStatusBatal(Connection con, 
            String tglMulai, String tglAkhir, String statusLunas, String statusBatal)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_head "
                + " where left(tgl_gadai,10) between ? and ? and status_lunas like ? and status_batal like ? ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, statusLunas);
        ps.setString(4, statusBatal);
        ResultSet rs = ps.executeQuery();
        List<GadaiHead> allGadai = new ArrayList<>();
        while(rs.next()){
            GadaiHead h = new GadaiHead();
            h.setNoGadai(rs.getString(1));
            h.setTglGadai(rs.getString(2));
            h.setKodeSales(rs.getString(3));
            h.setNama(rs.getString(4));
            h.setAlamat(rs.getString(5));
            h.setKeterangan(rs.getString(6));
            h.setTotalQty(rs.getInt(7));
            h.setTotalBerat(rs.getDouble(8));
            h.setTotalPinjaman(rs.getDouble(9));
            h.setLamaPinjam(rs.getInt(10));
            h.setBungaPersen(rs.getDouble(11));
            h.setBungaKomp(rs.getDouble(12));
            h.setBungaRp(rs.getDouble(13));
            h.setJatuhTempo(rs.getString(14));
            h.setStatusLunas(rs.getString(15));
            h.setTglLunas(rs.getString(16));
            h.setSalesLunas(rs.getString(17));
            h.setStatusBatal(rs.getString(18));
            h.setTglBatal(rs.getString(19));
            h.setSalesBatal(rs.getString(20));
            allGadai.add(h);
        }
        return allGadai;
    }
    public static GadaiHead get(Connection con, String noGadai)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_head where no_gadai=? ");
        ps.setString(1, noGadai);
        ResultSet rs = ps.executeQuery();
        GadaiHead h = null;
        while(rs.next()){
            h = new GadaiHead();
            h.setNoGadai(rs.getString(1));
            h.setTglGadai(rs.getString(2));
            h.setKodeSales(rs.getString(3));
            h.setNama(rs.getString(4));
            h.setAlamat(rs.getString(5));
            h.setKeterangan(rs.getString(6));
            h.setTotalQty(rs.getInt(7));
            h.setTotalBerat(rs.getDouble(8));
            h.setTotalPinjaman(rs.getDouble(9));
            h.setLamaPinjam(rs.getInt(10));
            h.setBungaPersen(rs.getDouble(11));
            h.setBungaKomp(rs.getDouble(12));
            h.setBungaRp(rs.getDouble(13));
            h.setJatuhTempo(rs.getString(14));
            h.setStatusLunas(rs.getString(15));
            h.setTglLunas(rs.getString(16));
            h.setSalesLunas(rs.getString(17));
            h.setStatusBatal(rs.getString(18));
            h.setTglBatal(rs.getString(19));
            h.setSalesBatal(rs.getString(20));
        }
        return h;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps =  con.prepareStatement("select max(right(no_gadai,4)) from tm_gadai_head "
                + " where mid(no_gadai,4,6)=? ");
        DecimalFormat df = new DecimalFormat("0000");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PT-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(rs.getInt(1)+1);
        else
            return "PT-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(1);
    }
    public static void insert(Connection con, GadaiHead g)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_gadai_head values "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, g.getNoGadai());
        ps.setString(2, g.getTglGadai());
        ps.setString(3, g.getKodeSales());
        ps.setString(4, g.getNama());
        ps.setString(5, g.getAlamat());
        ps.setString(6, g.getKeterangan());
        ps.setDouble(7, g.getTotalQty());
        ps.setDouble(8, g.getTotalBerat());
        ps.setDouble(9, g.getTotalPinjaman());
        ps.setDouble(10, g.getLamaPinjam());
        ps.setDouble(11, g.getBungaPersen());
        ps.setDouble(12, g.getBungaKomp());
        ps.setDouble(13, g.getBungaRp());
        ps.setString(14, g.getJatuhTempo());
        ps.setString(15, g.getStatusLunas());
        ps.setString(16, g.getTglLunas());
        ps.setString(17, g.getSalesLunas());
        ps.setString(18, g.getStatusBatal());
        ps.setString(19, g.getTglBatal());
        ps.setString(20, g.getSalesBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, GadaiHead g)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_gadai_head set "
            + " tgl_gadai=?, kode_sales=?, nama=?, alamat=?, keterangan=?, total_qty=?, total_berat=?, total_pinjaman=?, "
            + " lama_pinjam=?, bunga_persen=?, bunga_komp=?, bunga_rp=?, jatuh_tempo=?, "
            + " status_lunas=?, tgl_lunas=?, sales_lunas=?, status_batal=?, tgl_batal=?, sales_batal=? "
            + " where no_gadai=?");
        ps.setString(1, g.getTglGadai());
        ps.setString(2, g.getKodeSales());
        ps.setString(3, g.getNama());
        ps.setString(4, g.getAlamat());
        ps.setString(5, g.getKeterangan());
        ps.setDouble(6, g.getTotalQty());
        ps.setDouble(7, g.getTotalBerat());
        ps.setDouble(8, g.getTotalPinjaman());
        ps.setDouble(9, g.getLamaPinjam());
        ps.setDouble(10, g.getBungaPersen());
        ps.setDouble(11, g.getBungaKomp());
        ps.setDouble(12, g.getBungaRp());
        ps.setString(13, g.getJatuhTempo());
        ps.setString(14, g.getStatusLunas());
        ps.setString(15, g.getTglLunas());
        ps.setString(16, g.getSalesLunas());
        ps.setString(17, g.getStatusBatal());
        ps.setString(18, g.getTglBatal());
        ps.setString(19, g.getSalesBatal());
        ps.setString(20, g.getNoGadai());
        ps.executeUpdate();
    }
    public static void updateBungaGadai(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_gadai_head "
                + " set lama_pinjam=datediff(?,concat('20',substring(no_gadai,4,2),'-',substring(no_gadai,6,2),'-',substring(no_gadai,8,2))),"
                + " bunga_komp=ceil(total_pinjaman*bunga_persen/100/30*lama_pinjam/500)*500 , "
                + " bunga_rp=ceil(total_pinjaman*bunga_persen/100/30*lama_pinjam/500)*500 "
                + " where status_lunas='false' and status_batal='false' ");
        ps.setString(1, sistem.getTglSystem());
        ps.executeUpdate();
    }
}
