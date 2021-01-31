/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.Keuangan;
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
public class KeuanganDAO {

    public static Double getSaldoAwal(Connection con, String tanggal)throws Exception{
        PreparedStatement ps = con.prepareStatement("select sum(jumlah_rp) " +
            " from tt_keuangan where left(tgl_keuangan,10)  < ? ");
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        double saldoAwal = 0;
        if(rs.next())
            saldoAwal = rs.getDouble(1);
        return saldoAwal;
    }
    public static Double getSaldoAkhir(Connection con, String tanggal)throws Exception{
        PreparedStatement ps = con.prepareStatement("select sum(jumlah_rp) " +
            " from tt_keuangan where left(tgl_keuangan,10)  <= ?  ");
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        double saldoAkhir = 0;
        if(rs.next())
            saldoAkhir = rs.getDouble(1);
        return saldoAkhir;
    }
    public static List<Keuangan> getAllByDateAndKategori(Connection con, 
            String tglMulai, String tglAkhir, String kategori)throws Exception{
        String sql = "select * from tt_keuangan  where left(tgl_keuangan,10) between ? and ? ";
        if(!kategori.equals("%"))
            sql = sql + " and kategori = '"+kategori+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<Keuangan> allKeuangan = new ArrayList<>();
        while(rs.next()){
            Keuangan k = new Keuangan();
            k.setNoKeuangan(rs.getString(1));
            k.setTglKeuangan(rs.getString(2));
            k.setKategori(rs.getString(3));
            k.setKeterangan(rs.getString(4));
            k.setJumlahRp(rs.getDouble(5));
            k.setKodeUser(rs.getString(6));
            allKeuangan.add(k);
        }
        return allKeuangan;
    }
    public static List<Keuangan> getAllByNoKeuangan(Connection con, String noKeuangan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_keuangan where no_keuangan = ? ");
        ps.setString(1, noKeuangan);
        ResultSet rs = ps.executeQuery();
        List<Keuangan> allKeuangan = new ArrayList<>();
        while(rs.next()){
            Keuangan k = new Keuangan();
            k.setNoKeuangan(rs.getString(1));
            k.setTglKeuangan(rs.getString(2));
            k.setKategori(rs.getString(3));
            k.setKeterangan(rs.getString(4));
            k.setJumlahRp(rs.getDouble(5));
            k.setKodeUser(rs.getString(6));
            allKeuangan.add(k);
        }
        return allKeuangan;
    }
    public static Keuangan get(Connection con, String kategori, String keterangan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_keuangan where kategori = ? and keterangan = ? ");
        ps.setString(1, kategori);
        ps.setString(2, keterangan);
        ResultSet rs = ps.executeQuery();
        Keuangan k = null;
        while(rs.next()){
            k = new Keuangan();
            k.setNoKeuangan(rs.getString(1));
            k.setTglKeuangan(rs.getString(2));
            k.setKategori(rs.getString(3));
            k.setKeterangan(rs.getString(4));
            k.setJumlahRp(rs.getDouble(5));
            k.setKodeUser(rs.getString(6));
        }
        return k;
    }
    public static void insert(Connection con, Keuangan k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_keuangan values (?,?,?,?,?,?)");
        ps.setString(1, k.getNoKeuangan());
        ps.setString(2, k.getTglKeuangan());
        ps.setString(3, k.getKategori());
        ps.setString(4, k.getKeterangan());
        ps.setDouble(5, k.getJumlahRp());
        ps.setString(6, k.getKodeUser());
        ps.executeUpdate();
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(no_keuangan,4)) from tt_keuangan where mid(no_keuangan,4,6)=?");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "KK-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(rs.getInt(1)+1);
        else 
            return "KK-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+new DecimalFormat("0000").format(1);
    }
    public static void deleteAllByNoKeuangan(Connection con, String noKeuangan)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tt_keuangan where no_keuangan=?");
        ps.setString(1, noKeuangan);
        ps.executeUpdate();
    }
}
