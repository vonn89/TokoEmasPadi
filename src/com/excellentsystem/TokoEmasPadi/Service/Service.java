/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Service;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.BatalBarcodeDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.HancurDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.HancurHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KeuanganDAO;
import com.excellentsystem.TokoEmasPadi.DAO.LogHargaDAO;
import com.excellentsystem.TokoEmasPadi.DAO.OtoritasDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PembelianDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PembelianHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.UserDAO;
import com.excellentsystem.TokoEmasPadi.DAO.VerifikasiDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.BatalBarcode;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
import com.excellentsystem.TokoEmasPadi.Model.HancurHead;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.Keuangan;
import com.excellentsystem.TokoEmasPadi.Model.LogHarga;
import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Model.Verifikasi;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class Service {
    
    public static void insertKeuangan(Connection con, String kategori, String keterangan, 
            double jumlahRp, String user)throws Exception{
        Keuangan k = new Keuangan();
        k.setNoKeuangan(KeuanganDAO.getId(con));
        k.setTglKeuangan(Function.getSystemDate());
        k.setKategori(kategori);
        k.setKeterangan(keterangan);
        k.setJumlahRp(jumlahRp);
        k.setKodeUser(user);
        KeuanganDAO.insert(con, k);
    }
    public static String saveUpdateKategori(Connection con, Kategori kategori){
        try{
            con.setAutoCommit(false);
            
            KategoriDAO.update(con, kategori);
            BarangDAO.updateHarga(con, kategori);
            
            LogHarga l = new LogHarga();
            l.setTanggal(sistem.getTglSystem());
            l.setKodeKategori(kategori.getKodeKategori());
            l.setHargaBeli(kategori.getHargaBeli());
            l.setHargaJual(kategori.getHargaJual());
            LogHargaDAO.insert(con, l);
            
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String saveUpdateJenis(Connection con, Jenis jenis){
        try{
            con.setAutoCommit(false);
            
            JenisDAO.update(con, jenis);
            
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String saveNewUser(Connection con, User user){
        try{
            con.setAutoCommit(false);
            
            UserDAO.insert(con, user);
            for(Otoritas temp : user.getOtoritas()){
                OtoritasDAO.insert(con, temp);
            }
            for(Verifikasi temp : user.getVerifikasi()){
                VerifikasiDAO.insert(con, temp);
            }
            
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String saveUpdateUser(Connection con, User user){
        try{
            con.setAutoCommit(false);
            
            UserDAO.update(con, user);
            for(Otoritas temp : user.getOtoritas()){
                OtoritasDAO.update(con, temp);
            }
            for(Verifikasi temp : user.getVerifikasi()){
                VerifikasiDAO.update(con, temp);
            }
            
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String deleteUser(Connection con, User user){
        try{
            con.setAutoCommit(false);
            
            UserDAO.delete(con, user);
            OtoritasDAO.deleteAllByUsername(con, user.getUsername());
            VerifikasiDAO.deleteAllByUsername(con, user.getUsername());
            
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String saveNewBarang(Connection con, Barang b){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            b.setKodeBarcode(BarangDAO.getId(con));
            BarangDAO.insert(con, b);
            
            StokBarang stok = new StokBarang();
            stok.setTanggal(sistem.getTglSystem());
            stok.setKodeJenis(b.getKodeJenis());
            stok.setKodeBarcode(b.getKodeBarcode());
            stok.setStokAwal(0);
            stok.setBeratAwal(0);
            stok.setStokMasuk(b.getQty());
            stok.setBeratMasuk(b.getQty()*b.getBerat());
            stok.setStokKeluar(0);
            stok.setBeratKeluar(0);
            stok.setStokAkhir(b.getQty());
            stok.setBeratAkhir(b.getQty()*b.getBerat());
            StokBarangDAO.insert(con, stok);

            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String saveUpdateNilaiPokok(Connection con, List<Barang> listBarang, double nilaiPokokPerGram){
        try{
            con.setAutoCommit(false);
            
            for(Barang b : listBarang){
                b.setNilaiPokok(nilaiPokokPerGram*b.getBeratPembulatan());
                BarangDAO.update(con, b);
                
                List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByKodeBarcode(con, b.getKodeBarcode());
                for(PenjualanDetail d : listPenjualanDetail){
                    d.setNilaiPokok(nilaiPokokPerGram*d.getBeratPembulatan());
                    PenjualanDetailDAO.update(con, d);
                }
            }
                        
            con.commit();
            con.setAutoCommit(true);
            return "true";
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String saveBatalBarcode(Connection con, List<BatalBarcode> listBatalBarcode){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            for(BatalBarcode bb : listBatalBarcode){
                bb.setNoBatal(BatalBarcodeDAO.getId(con));
                BatalBarcodeDAO.insert(con, bb);
                
                Barang barang = BarangDAO.get(con, bb.getKodeBarcode());
                if(barang==null){
                    status = "Barang tidak ditemukan";
                }else{
                    if(barang.getQty()<1){
                        status = "Barang sudah tidak ada";
                    }else{
                        barang.setQty(barang.getQty()-bb.getQty());
                        BarangDAO.update(con, barang);
                    }
                    StokBarang stok = StokBarangDAO.getByTanggalAndBarcode(con, sistem.getTglSystem(), bb.getKodeBarcode());
                    if(stok==null){
                        status = "Stok barang tidak ditemukan";
                    }else{
                        if(stok.getStokAkhir()<1){
                            status = "Stok barang sudah tidak ada";
                        }else{
                            stok.setStokKeluar(stok.getStokKeluar()+bb.getQty());
                            stok.setBeratKeluar(stok.getBeratKeluar()+bb.getQty()*barang.getBerat());
                            stok.setStokAkhir(stok.getStokAkhir()-bb.getQty());
                            stok.setBeratAkhir(stok.getBeratAkhir()-bb.getQty()*barang.getBerat());
                            StokBarangDAO.update(con, stok);
                        }
                    }
                }
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String saveHancurBarang(Connection con, HancurHead h){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            h.setNoHancur(HancurHeadDAO.getId(con));
            HancurHeadDAO.insert(con, h);
            
            int i = 1;
            for(HancurDetail d : h.getListHancurDetail()){
                d.setNoHancur(h.getNoHancur());
                d.setNoUrut(i);
                HancurDetailDAO.insert(con, d);
                i++;
                
                Barang barang = BarangDAO.get(con, d.getKodeBarcode());
                if(barang==null){
                    status = "Barang tidak ditemukan";
                }else{
                    if(barang.getQty()<1){
                        status = "Barang sudah tidak ada";
                    }else{
                        barang.setQty(barang.getQty()-d.getQty());
                        BarangDAO.update(con, barang);
                    }
                    StokBarang stok = StokBarangDAO.getByTanggalAndBarcode(con, sistem.getTglSystem(), d.getKodeBarcode());
                    if(stok==null){
                        status = "Stok barang tidak ditemukan";
                    }else{
                        if(stok.getStokAkhir()<1){
                            status = "Stok barang sudah tidak ada";
                        }else{
                            stok.setStokKeluar(stok.getStokKeluar()+d.getQty());
                            stok.setBeratKeluar(stok.getBeratKeluar()+d.getQty()*barang.getBerat());
                            stok.setStokAkhir(stok.getStokAkhir()-d.getQty());
                            stok.setBeratAkhir(stok.getBeratAkhir()-d.getQty()*barang.getBerat());
                            StokBarangDAO.update(con, stok);
                        }
                    }
                }
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String savePenjualan(Connection con, PenjualanHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            p.setNoPenjualan(PenjualanHeadDAO.getId(con));
            PenjualanHeadDAO.insert(con, p);
            
            insertKeuangan(con, "Penjualan", p.getNoPenjualan(), p.getGrandtotal(), p.getKodeSales());
            if(p.getDiskon()!=0)
                insertKeuangan(con, "Diskon", p.getNoPenjualan(), -p.getDiskon(), p.getKodeSales());
            int i = 1;
            for(PenjualanDetail d : p.getListPenjualanDetail()){
                d.setNoPenjualan(p.getNoPenjualan());
                d.setNoUrut(i);
                PenjualanDetailDAO.insert(con, d);
                i++;
                
                Barang barang = BarangDAO.get(con, d.getKodeBarcode());
                if(barang==null){
                    status = "Barang tidak ditemukan";
                }else{
                    if(barang.getQty()<d.getQty()){
                        status = "Barang tidak mencukupi";
                    }else{
                        barang.setQty(barang.getQty()-d.getQty());
                        BarangDAO.update(con, barang);
                    }
                    StokBarang stok = StokBarangDAO.getByTanggalAndBarcode(con, sistem.getTglSystem(), d.getKodeBarcode());
                    if(stok==null){
                        status = "Stok tidak ditemukan";
                    }else{
                        if(stok.getStokAkhir()<d.getQty()){
                            status = "Stok barang tidak mencukupi";
                        }else{
                            stok.setStokKeluar(stok.getStokKeluar()+d.getQty());
                            stok.setBeratKeluar(stok.getBeratKeluar()+d.getQty()*barang.getBerat());
                            stok.setStokAkhir(stok.getStokAkhir()-d.getQty());
                            stok.setBeratAkhir(stok.getBeratAkhir()-d.getQty()*barang.getBerat());
                            StokBarangDAO.update(con, stok);
                        }
                    }
                }
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String batalPenjualan(Connection con, PenjualanHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            PenjualanHeadDAO.update(con, p);
            
            insertKeuangan(con, "Batal Penjualan", p.getNoPenjualan(), -p.getGrandtotal(), p.getKodeSales());
            if(p.getDiskon()!=0)
                insertKeuangan(con, "Diskon", p.getNoPenjualan(), p.getDiskon(), p.getKodeSales());
            for(PenjualanDetail d : p.getListPenjualanDetail()){
                Barang barang = BarangDAO.get(con, d.getKodeBarcode());
                if(barang==null){
                    status = "Barang tidak ditemukan";
                }else{
                    barang.setQty(barang.getQty()+d.getQty());
                    BarangDAO.update(con, barang);
                    StokBarang stok = StokBarangDAO.getByTanggalAndBarcode(con, sistem.getTglSystem(), d.getKodeBarcode());
                    if(stok==null){
                        status = "Stok tidak ditemukan";
                    }else{
                        stok.setStokMasuk(stok.getStokMasuk()+d.getQty());
                        stok.setBeratMasuk(stok.getBeratMasuk()+d.getQty()*barang.getBerat());
                        stok.setStokAkhir(stok.getStokAkhir()+d.getQty());
                        stok.setBeratAkhir(stok.getBeratAkhir()+d.getQty()*barang.getBerat());
                        StokBarangDAO.update(con, stok);
                    }
                }
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String savePembelian(Connection con, PembelianHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            p.setNoPembelian(PembelianHeadDAO.getId(con));
            PembelianHeadDAO.insert(con, p);
            
            insertKeuangan(con, "Pembelian", p.getNoPembelian(), -p.getTotalPembelian(), p.getKodeSales());
            
            int i = 1;
            for(PembelianDetail d : p.getListPembelianDetail()){
                d.setNoPembelian(p.getNoPembelian());
                d.setNoUrut(i);
                PembelianDetailDAO.insert(con, d);
                i++;
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String batalPembelian(Connection con, PembelianHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            PembelianHeadDAO.update(con, p);
            insertKeuangan(con, "Batal Pembelian", p.getNoPembelian(), p.getTotalPembelian(), p.getKodeSales());
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String saveTerimaGadai(Connection con, GadaiHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            p.setNoGadai(GadaiHeadDAO.getId(con));
            GadaiHeadDAO.insert(con, p);
            
            insertKeuangan(con, "Terima Gadai", p.getNoGadai(), -p.getTotalPinjaman(), p.getKodeSales());
            
            int i = 1;
            for(GadaiDetail d : p.getListGadaiDetail()){
                d.setNoGadai(p.getNoGadai());
                d.setNoUrut(i);
                GadaiDetailDAO.insert(con, d);
                i++;
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String batalTerimaGadai(Connection con, GadaiHead p){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            GadaiHeadDAO.update(con, p);
            
            insertKeuangan(con, "Batal Terima Gadai", p.getNoGadai(), p.getTotalPinjaman(), p.getKodeSales());
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    
    public static String savePelunasanGadai(Connection con, GadaiHead gadaiLama, GadaiHead gadaiBaru, double totalPembelian){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            GadaiHeadDAO.update(con, gadaiLama);
            
            insertKeuangan(con, "Pelunasan Gadai", gadaiLama.getNoGadai(), gadaiLama.getTotalPinjaman(), gadaiLama.getSalesLunas());
            insertKeuangan(con, "Bunga Gadai", gadaiLama.getNoGadai(), gadaiLama.getBungaRp(), gadaiLama.getSalesLunas());
            
            if(gadaiBaru!=null){
                gadaiBaru.setNoGadai(GadaiHeadDAO.getId(con));
                GadaiHeadDAO.insert(con, gadaiBaru);

                insertKeuangan(con, "Terima Gadai", gadaiBaru.getNoGadai(), -gadaiBaru.getTotalPinjaman(), gadaiBaru.getKodeSales());

                int i = 1;
                for(GadaiDetail d : gadaiBaru.getListGadaiDetail()){
                    d.setNoGadai(gadaiBaru.getNoGadai());
                    d.setNoUrut(i);
                    GadaiDetailDAO.insert(con, d);
                    i++;
                }
            }
            if(totalPembelian!=0){
                PembelianHead p = new PembelianHead();
                p.setNoPembelian(PembelianHeadDAO.getId(con));
                p.setTglPembelian(Function.getSystemDate());
                p.setKodeSales(gadaiLama.getSalesLunas());
                p.setNama(gadaiLama.getNama());
                p.setAlamat(gadaiLama.getAlamat());
                p.setTotalQty(gadaiLama.getTotalQty());
                p.setTotalBerat(gadaiLama.getTotalBerat());
                p.setTotalPembelian(totalPembelian);
                p.setCatatan("Pembelian Gadai - "+gadaiLama.getNoGadai());
                p.setStatus("true");
                p.setTglBatal("2000-01-01 00:00:00");
                p.setUserBatal("");
                PembelianHeadDAO.insert(con, p);
                
                int noUrut = 1;
                for(GadaiDetail d : gadaiLama.getListGadaiDetail()){
                    d.getPembelianDetail().setNoPembelian(p.getNoPembelian());
                    d.getPembelianDetail().setNoUrut(noUrut);
                    PembelianDetailDAO.insert(con, d.getPembelianDetail());
                    
                    noUrut = noUrut + 1;
                }

                insertKeuangan(con, "Pembelian", p.getNoPembelian(), -p.getTotalPembelian(), p.getKodeSales());
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            e.printStackTrace();
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
    public static String batalPelunasanGadai(Connection con, GadaiHead gadai, String userBatal){
        try{
            con.setAutoCommit(false);
            String status = "true";
            
            insertKeuangan(con, "Batal Pelunasan Gadai", gadai.getNoGadai(), -gadai.getTotalPinjaman(), gadai.getSalesLunas());
            insertKeuangan(con, "Batal Bunga Gadai", gadai.getNoGadai(), -gadai.getBungaRp(), gadai.getSalesLunas());
            
            gadai.setTglLunas("2000-01-01 00:00:00");
            gadai.setSalesLunas("");
            gadai.setStatusLunas("false");
            gadai.setBungaRp(gadai.getBungaKomp());
            GadaiHeadDAO.update(con, gadai);
            
            List<GadaiHead> listGadai = GadaiHeadDAO.getAllByTglGadaiAndStatusLunasAndStatusBatal(con, 
                    sistem.getTglSystem(), sistem.getTglSystem(), "false", "false");
            GadaiHead gadaiBaru = null;
            for(GadaiHead g : listGadai){
                if(g.getKeterangan().contains(gadai.getNoGadai()))
                    gadaiBaru = g;
            }
            if(gadaiBaru!=null){
                gadaiBaru.setStatusBatal("true");
                gadaiBaru.setTglBatal(Function.getSystemDate());
                gadaiBaru.setSalesBatal(userBatal);
                GadaiHeadDAO.update(con, gadaiBaru);
                
                insertKeuangan(con, "Batal Terima Gadai", gadaiBaru.getNoGadai(), gadaiBaru.getTotalPinjaman(), gadaiBaru.getKodeSales());
            }
            
            if(status.equals("true"))
                con.commit();
            else
                con.rollback();
            con.setAutoCommit(true);
            return status;
        }catch(Exception e){
            try{
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            }catch(SQLException ex){
                return ex.toString();
            }
        }
    }
}
