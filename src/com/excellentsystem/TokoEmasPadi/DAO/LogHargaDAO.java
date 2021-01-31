/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.LogHarga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class LogHargaDAO {

    public static void insert(Connection con, LogHarga l)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_log_harga values(?,?,?,?)");
        ps.setString(1, l.getTanggal());
        ps.setString(2, l.getKodeKategori());
        ps.setDouble(3, l.getHargaBeli());
        ps.setDouble(4, l.getHargaJual());
        ps.executeUpdate();
    }
    public static void update(Connection con, LogHarga l)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_log_harga set harga_beli = ?, harga_jual =? "
                + " where tanggal = ? and kode_kategori = ?");
        ps.setDouble(1, l.getHargaBeli());
        ps.setDouble(2, l.getHargaJual());
        ps.setString(3, l.getTanggal());
        ps.setString(4, l.getKodeKategori());
        ps.executeUpdate();
    }
    public static List<LogHarga> getAllByDate(Connection con, String tglMulai,String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_log_harga where tanggal between ? and ? ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<LogHarga> allLogHarga = new ArrayList<>();
        while(rs.next()){
            LogHarga l = new LogHarga();
            l.setTanggal(rs.getString(1));
            l.setKodeKategori(rs.getString(2));
            l.setHargaBeli(rs.getDouble(3));
            l.setHargaJual(rs.getDouble(4));
            allLogHarga.add(l);
        }
        return allLogHarga;
    }
}
