/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.Sistem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Xtreme
 */
public class SistemDAO {
    public static Sistem get(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_system");
        Sistem s = null ;
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            s = new Sistem();
            s.setNama(rs.getString(1));
            s.setAlamat(rs.getString(2));
            s.setKota(rs.getString(3));
            s.setNoTelp(rs.getString(4));
            s.setWebsite(rs.getString(5));
            s.setTglSystem(rs.getString(6));
            s.setPersentasePinjaman(rs.getDouble(7));
            s.setJatuhTempoGadai(rs.getInt(8));
            s.setCode(rs.getString(9));
        }
        return s;
    }
    public static void update(Connection con, Sistem s)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_system set "
                + " nama=?, alamat=?, kota=?, no_telp=?, website=?, "
                + " tgl_system=?, persentase_pinjaman=?, jatuh_tempo=?, code=? ");
        ps.setString(1, s.getNama());
        ps.setString(2, s.getAlamat());
        ps.setString(3, s.getKota());
        ps.setString(4, s.getNoTelp());
        ps.setString(5, s.getWebsite());
        ps.setString(6, s.getTglSystem());
        ps.setDouble(7, s.getPersentasePinjaman());
        ps.setInt(8, s.getJatuhTempoGadai());
        ps.setString(9, s.getCode());
        ps.executeUpdate();
    }
}
