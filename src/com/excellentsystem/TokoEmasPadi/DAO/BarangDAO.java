/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
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
public class BarangDAO {

    public static List<Barang> getAllByTglBarcode(Connection con, String mulaiDate, String akhirDate, 
            String kodeKategori, String kodeJenis, String keterangan, String persentase, String user)throws Exception{
        String sql = "select * from tm_barang where left(barcode_date,10) between ? and ? ";
        if(!"%".equals(kodeKategori))
            sql = sql + " and kode_kategori = '"+kodeKategori+"' ";
        if(!"%".equals(kodeJenis))
            sql = sql + " and kode_jenis = '"+kodeJenis+"' ";
        if(!"%".equals(keterangan))
            sql = sql + " and keterangan = '"+keterangan+"' ";
        if(!"%".equals(persentase))
            sql = sql + " and persentase = '"+persentase+"' ";
        if(!"%".equals(user))
            sql = sql + " and barcode_by = '"+user+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, mulaiDate);
        ps.setString(2, akhirDate);
        ResultSet rs = ps.executeQuery();
        List<Barang> allBarang = new ArrayList<>();
        while(rs.next()){
            Barang b = new Barang();
            b.setKodeBarcode(rs.getString(1));
            b.setNamaBarang(rs.getString(2));
            b.setNamaBarcode(rs.getString(3));
            b.setKodeKategori(rs.getString(4));
            b.setKodeJenis(rs.getString(5));
            b.setBerat(rs.getDouble(6));
            b.setBeratPembulatan(rs.getDouble(7));
            b.setNilaiPokok(rs.getDouble(8));
            b.setHargaJual(rs.getDouble(9));
            b.setKeterangan(rs.getString(10));
            b.setMerk(rs.getString(11));
            b.setPersentase(rs.getDouble(12));
            b.setBarcodeDate(rs.getString(13));
            b.setBarcodeBy(rs.getString(14));
            b.setQty(rs.getInt(15));
            allBarang.add(b);
        }
        return allBarang;
    }
    public static List<Barang> getAllByKategoriAndJenisAndKeteranganAndMerkAndStokAda(Connection con, 
            String kodeKategori, String kodeJenis, String keterangan, String merk)throws Exception{
        String sql = "select * from tm_barang where qty>=1 ";
        if(!"%".equals(kodeKategori))
            sql = sql + " and kode_kategori = '"+kodeKategori+"' ";
        if(!"%".equals(kodeJenis))
            sql = sql + " and kode_jenis = '"+kodeJenis+"' ";
        if(!"%".equals(keterangan))
            sql = sql + " and keterangan = '"+keterangan+"' ";
        if(!"%".equals(merk))
            sql = sql + " and merk = '"+merk+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        List<Barang> allBarang = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Barang b = new Barang();
            b.setKodeBarcode(rs.getString(1));
            b.setNamaBarang(rs.getString(2));
            b.setNamaBarcode(rs.getString(3));
            b.setKodeKategori(rs.getString(4));
            b.setKodeJenis(rs.getString(5));
            b.setBerat(rs.getDouble(6));
            b.setBeratPembulatan(rs.getDouble(7));
            b.setNilaiPokok(rs.getDouble(8));
            b.setHargaJual(rs.getDouble(9));
            b.setKeterangan(rs.getString(10));
            b.setMerk(rs.getString(11));
            b.setPersentase(rs.getDouble(12));
            b.setBarcodeDate(rs.getString(13));
            b.setBarcodeBy(rs.getString(14));
            b.setQty(rs.getInt(15));
            allBarang.add(b);
        }
        return allBarang;
    }
    public static Barang get(Connection con, String kodeBarcode)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_barang where kode_barcode = ?");
        ps.setString(1, kodeBarcode);
        Barang b = null;
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            b = new Barang();
            b.setKodeBarcode(rs.getString(1));
            b.setNamaBarang(rs.getString(2));
            b.setNamaBarcode(rs.getString(3));
            b.setKodeKategori(rs.getString(4));
            b.setKodeJenis(rs.getString(5));
            b.setBerat(rs.getDouble(6));
            b.setBeratPembulatan(rs.getDouble(7));
            b.setNilaiPokok(rs.getDouble(8));
            b.setHargaJual(rs.getDouble(9));
            b.setKeterangan(rs.getString(10));
            b.setMerk(rs.getString(11));
            b.setPersentase(rs.getDouble(12));
            b.setBarcodeDate(rs.getString(13));
            b.setBarcodeBy(rs.getString(14));
            b.setQty(rs.getInt(15));
        }
        return b;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(kode_barcode,7)) "
                + " from tm_barang where left(kode_barcode,1) like ? ");
        ps.setString(1, sistem.getCode());
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return sistem.getCode() + new DecimalFormat("0000000").format(rs.getInt(1)+1);
        else 
            return sistem.getCode()+"0000000";    
    }
    public static void updateHarga(Connection con, Kategori k)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_barang set harga_jual=ceil(?*berat_pembulatan/500)*500 "
                + " where kode_kategori=? and qty>=1");
        ps.setDouble(1, k.getHargaJual());
        ps.setString(2, k.getKodeKategori());
        ps.executeUpdate();
    }
    public static void update(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_barang set "
                + " nama_barang=?, nama_barcode=?, kode_kategori=?, kode_jenis=?, "
                + " berat=?, berat_pembulatan=?, nilai_pokok=?, harga_jual=?, keterangan=?, merk=?, "
                + " persentase=?, barcode_date=?, barcode_by=?, qty=? where kode_barcode=? ");
        ps.setString(1, b.getNamaBarang());
        ps.setString(2, b.getNamaBarcode());
        ps.setString(3, b.getKodeKategori());
        ps.setString(4, b.getKodeJenis());
        ps.setDouble(5, b.getBerat());
        ps.setDouble(6, b.getBeratPembulatan());
        ps.setDouble(7, b.getNilaiPokok());
        ps.setDouble(8, b.getHargaJual());
        ps.setString(9, b.getKeterangan());
        ps.setString(10, b.getMerk());
        ps.setDouble(11, b.getPersentase());
        ps.setString(12, b.getBarcodeDate());
        ps.setString(13, b.getBarcodeBy());
        ps.setInt(14, b.getQty());
        ps.setString(15, b.getKodeBarcode());
        ps.executeUpdate();
    }
    public static void insert(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_barang values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, b.getKodeBarcode());
        ps.setString(2, b.getNamaBarang());
        ps.setString(3, b.getNamaBarcode());
        ps.setString(4, b.getKodeKategori());
        ps.setString(5, b.getKodeJenis());
        ps.setDouble(6, b.getBerat());
        ps.setDouble(7, b.getBeratPembulatan());
        ps.setDouble(8, b.getNilaiPokok());
        ps.setDouble(9, b.getHargaJual());
        ps.setString(10, b.getKeterangan());
        ps.setString(11, b.getMerk());
        ps.setDouble(12, b.getPersentase());
        ps.setString(13, b.getBarcodeDate());
        ps.setString(14, b.getBarcodeBy());
        ps.setInt(15, b.getQty());
        ps.executeUpdate();
    }
}
