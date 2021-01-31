/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.KategoriTransaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class KategoriTransaksiDAO {

    public static List<KategoriTransaksi> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori_transaksi ");
        ResultSet rs = ps.executeQuery();
        List<KategoriTransaksi> allKategoriTransaksi = new ArrayList<>();
        while(rs.next()){
            KategoriTransaksi k = new KategoriTransaksi();
            k.setKodeKategori(rs.getString(1));
            k.setJenisTransaksi(rs.getString(2));
            allKategoriTransaksi.add(k);
        }
        return allKategoriTransaksi;
    }
    public static void update(Connection con, KategoriTransaksi k)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_kategori_transaksi set jenis_transaksi=? where kode_kategori=?");
        ps.setString(1, k.getJenisTransaksi());
        ps.setString(2, k.getKodeKategori());
        ps.executeUpdate();
    }
    public static void insert(Connection con, KategoriTransaksi k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_kategori_transaksi values(?,?)");
        ps.setString(1, k.getKodeKategori());
        ps.setString(2, k.getJenisTransaksi());
        ps.executeUpdate();
    }
    public static void delete(Connection con, String kodeKategori)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_kategori_transaksi where kode_kategori=?");
        ps.setString(1, kodeKategori);
        ps.executeUpdate();
    }
}
