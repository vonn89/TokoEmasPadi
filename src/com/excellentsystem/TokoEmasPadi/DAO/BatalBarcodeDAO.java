/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.BatalBarcode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class BatalBarcodeDAO {
    
    
    public static List<BatalBarcode> getAllByTglBatal(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_batal_barcode "
                + " where left(tgl_batal,10) between ? and ? ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<BatalBarcode> allBatalBarcode = new ArrayList<>();
        while(rs.next()){
            BatalBarcode h = new BatalBarcode();
            h.setNoBatal(rs.getString(1));
            h.setTglBatal(rs.getString(2));
            h.setKodeUser(rs.getString(3));
            h.setKodeBarcode(rs.getString(4));
            h.setQty(rs.getInt(5));
            allBatalBarcode.add(h);
        }
        return allBatalBarcode;
    }
    public static void insert(Connection con, BatalBarcode h)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_batal_barcode values (?,?,?,?,?)");
        ps.setString(1, h.getNoBatal());
        ps.setString(2, h.getTglBatal());
        ps.setString(3, h.getKodeUser());
        ps.setString(4, h.getKodeBarcode());
        ps.setInt(5, h.getQty());
        ps.executeUpdate();
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps =  con.prepareStatement("select max(right(no_batal,4)) from tt_batal_barcode "
                + " where mid(no_batal,4,6)=? ");
        DecimalFormat df = new DecimalFormat("0000");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "BB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(rs.getInt(1)+1);
        else
            return "BB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(1);
    }
}
