/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class GadaiDetailDAO {

    public static List<GadaiDetail> getAllByTglGadaiAndStatusLunasAndStatusBatal(Connection con, 
            String tglMulai, String tglAkhir, String statusLunas, String statusBatal)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_detail where no_gadai in ( "
                + " select no_gadai from tm_gadai_head where left(tgl_gadai,10) between ? and ? "
                + " and status_lunas = ? and status_batal = ? ) ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, statusLunas);
        ps.setString(4, statusBatal);
        ResultSet rs = ps.executeQuery();
        List<GadaiDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            GadaiDetail d = new GadaiDetail();
            d.setNoGadai(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setNilaiJual(rs.getDouble(9));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<GadaiDetail> getAllByTglLunas(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_detail where no_gadai in ( "
                + " select no_gadai from tm_gadai_head where left(tgl_lunas,10) between ? and ? and status_lunas = 'true' ) ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<GadaiDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            GadaiDetail d = new GadaiDetail();
            d.setNoGadai(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setNilaiJual(rs.getDouble(9));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<GadaiDetail> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_detail where no_gadai in ( "
                + " select no_gadai from tm_gadai_head where left(tgl_batal,10) between ? and ? and status_batal = 'true' ) ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<GadaiDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            GadaiDetail d = new GadaiDetail();
            d.setNoGadai(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setNilaiJual(rs.getDouble(9));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<GadaiDetail> getAll(Connection con, String noGadai)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_gadai_detail where no_gadai = ?");
        ps.setString(1, noGadai);
        ResultSet rs = ps.executeQuery();
        List<GadaiDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            GadaiDetail d = new GadaiDetail();
            d.setNoGadai(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setNilaiJual(rs.getDouble(9));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static void insert(Connection con, GadaiDetail d)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_gadai_detail values (?,?,?,?,?,?,?,?,?)");
        ps.setString(1, d.getNoGadai());
        ps.setInt(2, d.getNoUrut());
        ps.setString(3, d.getKodeKategori());
        ps.setString(4, d.getNamaBarang());
        ps.setDouble(5, d.getQty());
        ps.setDouble(6, d.getBerat());
        ps.setString(7, d.getKondisi());
        ps.setString(8, d.getStatusSurat());
        ps.setDouble(9, d.getNilaiJual());
        ps.executeUpdate();
    }
}
