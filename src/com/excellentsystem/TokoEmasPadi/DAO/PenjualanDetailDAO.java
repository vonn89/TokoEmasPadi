/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class PenjualanDetailDAO {

    public static List<PenjualanDetail> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail where no_penjualan in ( "
                + " select no_penjualan from tt_penjualan_head where left(tgl_penjualan,10) between ? and ?  and status='false' )");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail d = new PenjualanDetail();
            d.setNoPenjualan(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeBarcode(rs.getString(3));
            d.setKodeKategori(rs.getString(4));
            d.setKodeJenis(rs.getString(5));
            d.setNamaBarang(rs.getString(6));
            d.setQty(rs.getInt(7));
            d.setBerat(rs.getDouble(8));
            d.setBeratPembulatan(rs.getDouble(9));
            d.setNilaiPokok(rs.getDouble(10));
            d.setHargaKomp(rs.getDouble(11));
            d.setHargaJual(rs.getDouble(12));
            d.setTotal(rs.getDouble(13));
            d.setNoPembelian(rs.getString(14));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<PenjualanDetail> getAllByTglPenjualanAndStatus(Connection con, 
            String tglMulai, String tglAkhir, String status)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail where no_penjualan in ( "
                + " select no_penjualan from tt_penjualan_head where left(tgl_penjualan,10) between ? and ?  and status=? )");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, status);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail d = new PenjualanDetail();
            d.setNoPenjualan(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeBarcode(rs.getString(3));
            d.setKodeKategori(rs.getString(4));
            d.setKodeJenis(rs.getString(5));
            d.setNamaBarang(rs.getString(6));
            d.setQty(rs.getInt(7));
            d.setBerat(rs.getDouble(8));
            d.setBeratPembulatan(rs.getDouble(9));
            d.setNilaiPokok(rs.getDouble(10));
            d.setHargaKomp(rs.getDouble(11));
            d.setHargaJual(rs.getDouble(12));
            d.setTotal(rs.getDouble(13));
            d.setNoPembelian(rs.getString(14));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<PenjualanDetail> getAllByNoPenjualan(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail where no_penjualan = ? ");
        ps.setString(1, noPenjualan);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail d = new PenjualanDetail();
            d.setNoPenjualan(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeBarcode(rs.getString(3));
            d.setKodeKategori(rs.getString(4));
            d.setKodeJenis(rs.getString(5));
            d.setNamaBarang(rs.getString(6));
            d.setQty(rs.getInt(7));
            d.setBerat(rs.getDouble(8));
            d.setBeratPembulatan(rs.getDouble(9));
            d.setNilaiPokok(rs.getDouble(10));
            d.setHargaKomp(rs.getDouble(11));
            d.setHargaJual(rs.getDouble(12));
            d.setTotal(rs.getDouble(13));
            d.setNoPembelian(rs.getString(14));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static List<PenjualanDetail> getAllByKodeBarcode(Connection con, String kodeBarcode)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail where kode_barcode = ? ");
        ps.setString(1, kodeBarcode);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> allDetail = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail d = new PenjualanDetail();
            d.setNoPenjualan(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeBarcode(rs.getString(3));
            d.setKodeKategori(rs.getString(4));
            d.setKodeJenis(rs.getString(5));
            d.setNamaBarang(rs.getString(6));
            d.setQty(rs.getInt(7));
            d.setBerat(rs.getDouble(8));
            d.setBeratPembulatan(rs.getDouble(9));
            d.setNilaiPokok(rs.getDouble(10));
            d.setHargaKomp(rs.getDouble(11));
            d.setHargaJual(rs.getDouble(12));
            d.setTotal(rs.getDouble(13));
            d.setNoPembelian(rs.getString(14));
            allDetail.add(d);
        }
        return allDetail;
    }
    public static void insert(Connection con, PenjualanDetail d)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_detail values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, d.getNoPenjualan());
        ps.setInt(2, d.getNoUrut());
        ps.setString(3, d.getKodeBarcode());
        ps.setString(4, d.getKodeKategori());
        ps.setString(5, d.getKodeJenis());
        ps.setString(6, d.getNamaBarang());
        ps.setDouble(7, d.getQty());
        ps.setDouble(8, d.getBerat());
        ps.setDouble(9, d.getBeratPembulatan());
        ps.setDouble(10, d.getNilaiPokok());
        ps.setDouble(11, d.getHargaKomp());
        ps.setDouble(12, d.getHargaJual());
        ps.setDouble(13, d.getTotal());
        ps.setString(14, d.getNoPembelian());
        ps.executeUpdate();
    }
    public static void update(Connection con, PenjualanDetail d)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_penjualan_detail set "
                + " kode_barcode = ?, kode_kategori = ?, kode_jenis = ?, nama_barang=?, qty = ?, berat=?, berat_pembulatan=?, "
                + " nilai_pokok=?, harga_komp=?, harga_jual=?, total=?, no_pembelian=? "
                + " where no_penjualan = ? and no_urut=? ");
        ps.setString(1, d.getKodeBarcode());
        ps.setString(2, d.getKodeKategori());
        ps.setString(3, d.getKodeJenis());
        ps.setString(4, d.getNamaBarang());
        ps.setDouble(5, d.getQty());
        ps.setDouble(6, d.getBerat());
        ps.setDouble(7, d.getBeratPembulatan());
        ps.setDouble(8, d.getNilaiPokok());
        ps.setDouble(9, d.getHargaKomp());
        ps.setDouble(10, d.getHargaJual());
        ps.setDouble(11, d.getTotal());
        ps.setString(12, d.getNoPembelian());
        ps.setString(13, d.getNoPenjualan());
        ps.setInt(14, d.getNoUrut());
        ps.executeUpdate();
    }
}
