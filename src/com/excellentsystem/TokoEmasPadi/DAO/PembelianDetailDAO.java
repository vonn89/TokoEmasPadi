/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class PembelianDetailDAO {

    public static List<PembelianDetail> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_detail where no_pembelian in ( "
                + " select no_pembelian from tt_pembelian_head where left(tgl_batal,10) between ? and ? and status = 'false' )");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PembelianDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PembelianDetail d = new PembelianDetail();
            d.setNoPembelian(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setHargaKomp(rs.getDouble(9));
            d.setHargaBeli(rs.getDouble(10));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<PembelianDetail> getAllByTglBeliAndStatus(Connection con, 
            String tglMulai, String tglAkhir, String status)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_detail where no_pembelian in ( "
                + " select no_pembelian from tt_pembelian_head where left(tgl_pembelian,10) between ? and ? and status = ? )");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, status);
        ResultSet rs = ps.executeQuery();
        List<PembelianDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PembelianDetail d = new PembelianDetail();
            d.setNoPembelian(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setHargaKomp(rs.getDouble(9));
            d.setHargaBeli(rs.getDouble(10));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<PembelianDetail> getAll(Connection con, String noPembelian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_detail where no_pembelian = ? ");
        ps.setString(1, noPembelian);
        ResultSet rs = ps.executeQuery();
        List<PembelianDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PembelianDetail d = new PembelianDetail();
            d.setNoPembelian(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeKategori(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getInt(5));
            d.setBerat(rs.getDouble(6));
            d.setKondisi(rs.getString(7));
            d.setStatusSurat(rs.getString(8));
            d.setHargaKomp(rs.getDouble(9));
            d.setHargaBeli(rs.getDouble(10));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static void insert(Connection con, PembelianDetail d)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pembelian_detail values (?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, d.getNoPembelian());
        ps.setInt(2, d.getNoUrut());
        ps.setString(3, d.getKodeKategori());
        ps.setString(4, d.getNamaBarang());
        ps.setInt(5, d.getQty());
        ps.setDouble(6, d.getBerat());
        ps.setString(7, d.getKondisi());
        ps.setString(8, d.getStatusSurat());
        ps.setDouble(9, d.getHargaKomp());
        ps.setDouble(10, d.getHargaBeli());
        ps.executeUpdate();
    }
}
