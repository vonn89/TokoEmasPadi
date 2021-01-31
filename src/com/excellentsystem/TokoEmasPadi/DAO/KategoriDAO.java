/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class KategoriDAO {

    public static List<Kategori> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori ");
        ResultSet rs = ps.executeQuery();
        List<Kategori> allKategori = new ArrayList<>();
        while(rs.next()){
            Kategori k = new Kategori();
            k.setKodeKategori(rs.getString(1));
            k.setNamaKategori(rs.getString(2));
            k.setHargaBeli(rs.getDouble(3));
            k.setHargaJual(rs.getDouble(4));
            allKategori.add(k);
        }
        return allKategori;
    }
    public static Kategori get(Connection con, String kodeKategori)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori where kode_kategori = ?");
        ps.setString(1, kodeKategori);
        ResultSet rs = ps.executeQuery();
        Kategori k = null;
        while(rs.next()){
            k = new Kategori();
            k.setKodeKategori(rs.getString(1));
            k.setNamaKategori(rs.getString(2));
            k.setHargaBeli(rs.getDouble(3));
            k.setHargaJual(rs.getDouble(4));
        }
        return k;
    }
    public static void update(Connection con, Kategori k)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_kategori set nama_kategori=?, harga_beli=?, harga_jual=? where kode_kategori=?");
        ps.setString(1, k.getNamaKategori());
        ps.setDouble(2, k.getHargaBeli());
        ps.setDouble(3, k.getHargaJual());
        ps.setString(4, k.getKodeKategori());
        ps.executeUpdate();
    }
    public static void insert(Connection con, Kategori k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_kategori values(?,?,?,?)");
        ps.setString(1, k.getKodeKategori());
        ps.setString(2, k.getNamaKategori());
        ps.setDouble(3, k.getHargaBeli());
        ps.setDouble(4, k.getHargaJual());
        ps.executeUpdate();
    }
    public static void delete(Connection con, String kodeKategori)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_kategori where kode_kategori=?");
        ps.setString(1, kodeKategori);
        ps.executeUpdate();
    }
}
