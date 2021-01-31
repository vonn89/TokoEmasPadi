/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class StokBarangDAO {

    public static List<StokBarang> getAllByTanggal(Connection con, String tanggal)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_stok_barang where tanggal = ?");
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        List<StokBarang> allStokBarang = new ArrayList<>();
        while(rs.next()){
            StokBarang s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeJenis(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setStokAwal(rs.getInt(4));
            s.setBeratAwal(rs.getDouble(5));
            s.setStokMasuk(rs.getInt(6));
            s.setBeratMasuk(rs.getDouble(7));
            s.setStokKeluar(rs.getInt(8));
            s.setBeratKeluar(rs.getDouble(9));
            s.setStokAkhir(rs.getInt(10));
            s.setBeratAkhir(rs.getDouble(11));
            allStokBarang.add(s);
        }
        return allStokBarang;
    }
    public static List<StokBarang> getAllByTanggalMulaiAndAkhir(Connection con, String tglMulai,String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select tanggal,kode_jenis,kode_barcode,stok_awal,berat_awal,"
                + " sum(stok_masuk),sum(berat_masuk),sum(stok_keluar),sum(berat_keluar),stok_awal+sum(stok_masuk)-sum(stok_keluar),berat_awal+sum(berat_masuk)-sum(berat_keluar) "
                + " from tt_stok_barang where tanggal between ? and ? group by kode_jenis");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<StokBarang> allStokBarang = new ArrayList<>();
        while(rs.next()){
            StokBarang s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeJenis(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setStokAwal(rs.getInt(4));
            s.setBeratAwal(rs.getDouble(5));
            s.setStokMasuk(rs.getInt(6));
            s.setBeratMasuk(rs.getDouble(7));
            s.setStokKeluar(rs.getInt(8));
            s.setBeratKeluar(rs.getDouble(9));
            s.setStokAkhir(rs.getInt(10));
            s.setBeratAkhir(rs.getDouble(11));
            allStokBarang.add(s);
        }
        return allStokBarang;
    }
    public static List<StokBarang> getAllByDateAndJenisAndBarcode(Connection con, 
            String tglAwal, String tglAkhir, String jenis, String barcode)throws Exception{
        String sql = "select * from tt_stok_barang where tanggal between ? and ? ";
        if(!jenis.equals("%"))
            sql = sql + " and kode_jenis  = '"+jenis+"' ";
        if(!barcode.equals("%"))
            sql = sql + " and kode_barcode  = '"+barcode+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglAwal);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<StokBarang> allStokBarang = new ArrayList<>();
        while(rs.next()){
            StokBarang s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeJenis(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setStokAwal(rs.getInt(4));
            s.setBeratAwal(rs.getDouble(5));
            s.setStokMasuk(rs.getInt(6));
            s.setBeratMasuk(rs.getDouble(7));
            s.setStokKeluar(rs.getInt(8));
            s.setBeratKeluar(rs.getDouble(9));
            s.setStokAkhir(rs.getInt(10));
            s.setBeratAkhir(rs.getDouble(11));
            allStokBarang.add(s);
        }
        return allStokBarang;
    }
    public static StokBarang getByTanggalAndBarcode(Connection con, String tanggal, String kodeBarcode)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_stok_barang "
                + " where tanggal=? and kode_barcode=? ");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ps.setString(2, kodeBarcode);
        StokBarang s = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeJenis(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setStokAwal(rs.getInt(4));
            s.setBeratAwal(rs.getDouble(5));
            s.setStokMasuk(rs.getInt(6));
            s.setBeratMasuk(rs.getDouble(7));
            s.setStokKeluar(rs.getInt(8));
            s.setBeratKeluar(rs.getDouble(9));
            s.setStokAkhir(rs.getInt(10));
            s.setBeratAkhir(rs.getDouble(11));
        }
        return s;
    }
    public static void update(Connection con, StokBarang stok)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_stok_barang set "
                + " stok_awal=?, berat_awal=?, "
                + " stok_masuk=?, berat_masuk=?, "
                + " stok_keluar=?, berat_keluar=?, "
                + " stok_akhir=?, berat_akhir=? "
                + " where tanggal=? and kode_barcode=? and kode_jenis=? ");
        ps.setInt(1, stok.getStokAwal());
        ps.setDouble(2, stok.getBeratAwal());
        ps.setInt(3, stok.getStokMasuk());
        ps.setDouble(4, stok.getBeratMasuk());
        ps.setInt(5, stok.getStokKeluar());
        ps.setDouble(6, stok.getBeratKeluar());
        ps.setInt(7, stok.getStokAkhir());
        ps.setDouble(8, stok.getBeratAkhir());
        ps.setString(9, stok.getTanggal());
        ps.setString(10, stok.getKodeBarcode());
        ps.setString(11, stok.getKodeJenis());
        ps.executeUpdate();
    }
    public static void insert(Connection con, StokBarang stok)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_stok_barang values (?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, stok.getTanggal());
        ps.setString(2, stok.getKodeJenis());
        ps.setString(3, stok.getKodeBarcode());
        ps.setInt(4, stok.getStokAwal());
        ps.setDouble(5, stok.getBeratAwal());
        ps.setInt(6, stok.getStokMasuk());
        ps.setDouble(7, stok.getBeratMasuk());
        ps.setInt(8, stok.getStokKeluar());
        ps.setDouble(9, stok.getBeratKeluar());
        ps.setInt(10, stok.getStokAkhir());
        ps.setDouble(11, stok.getBeratAkhir());
        ps.executeUpdate();
    }
    public static void insertNewStokBarang(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_stok_barang "
            + " select ?,kode_jenis,kode_barcode,stok_akhir,berat_akhir,0,0,0,0,stok_akhir,berat_akhir "
            + " from tt_stok_barang "
            + " where tanggal=? and stok_akhir!=0");
        LocalDate yesterday = LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE);
        LocalDate today = yesterday.plusDays(1);
        ps.setString(1, today.toString());
        ps.setString(2, yesterday.toString());
        ps.executeUpdate();
    }
}
