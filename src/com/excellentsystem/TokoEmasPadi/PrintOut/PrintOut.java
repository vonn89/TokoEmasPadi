/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.PrintOut;

import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.printerBarcode;
import static com.excellentsystem.TokoEmasPadi.Main.printerGadai;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Yunaz
 */
public class PrintOut {
    
    public String angka(int satuan){ 
          String[] huruf ={"","Satu","Dua","Tiga","Empat","Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"}; 
          String hasil=""; 
          if(satuan<12) 
           hasil=hasil+huruf[satuan]; 
          else if(satuan<20) 
           hasil=hasil+angka(satuan-10)+" Belas"; 
          else if(satuan<100) 
           hasil=hasil+angka(satuan/10)+" Puluh "+angka(satuan%10); 
          else if(satuan<200) 
           hasil=hasil+"Seratus "+angka(satuan-100); 
          else if(satuan<1000) 
           hasil=hasil+angka(satuan/100)+" Ratus "+angka(satuan%100); 
          else if(satuan<2000) 
           hasil=hasil+"Seribu "+angka(satuan-1000); 
          else if(satuan<1000000) 
           hasil=hasil+angka(satuan/1000)+" Ribu "+angka(satuan%1000); 
          else if(satuan<1000000000) 
           hasil=hasil+angka(satuan/1000000)+" Juta "+angka(satuan%1000000); 
          else if(satuan>=1000000000) 
           hasil="Angka terlalu besar, harus kurang dari 1 milyar!"; 
          return hasil; 
    }
//    public void printSuratPenjualan(PenjualanHead p) throws Exception{
//        DecimalFormat rp = new DecimalFormat("###,##0");
//        DecimalFormat gr = new DecimalFormat("#,##0.00");
//        DateFormat jam = new SimpleDateFormat("HH:mm");
//        Map<String, Object> map = new HashMap<>();
//        map.put("noPenjualan", p.getNoPenjualan());        
//        map.put("tglPenjualan", tglNormal.format(tglSql.parse(p.getTglPenjualan())));     
//        map.put("alamatToko", sistem.getAlamat()+", "+sistem.getKota());   
//        map.put("noTelpToko", sistem.getNoTelp());  
//        map.put("npwpToko", sistem.getWebsite());     
//        map.put("jam", jam.format(tglSql.parse(p.getTglPenjualan())));       
//        map.put("nama", p.getNama());     
//        map.put("alamat", p.getAlamat());     
//        map.put("noTelp", "");     
//        map.put("sales", p.getKodeSales());    
//        int total = Integer.parseInt(new DecimalFormat("##0").format(p.getGrandtotal()));
//        map.put("terbilang", angka(total));      
//        map.put("total", rp.format(p.getGrandtotal()));                       
//        List<Map<String, Object>> tables = new ArrayList<>();
//        for (PenjualanDetail d : p.getListPenjualanDetail()) {
////            d.getBarang().getNamaBarcode();
//            Map<String, Object> line = new HashMap<>();
//            line.put("qty", d.getQty());
//            line.put("namaBarang", d.getNamaBarang());
//            line.put("berat", gr.format(d.getBerat())+" Gr");
//            line.put("kadar", d.getBarang().getMerk());
//            line.put("harga", rp.format(d.getHargaJual()));
//            tables.add(line);
//            Map<String, Object> line2 = new HashMap<>();
//            line2.put("qty", "");
//            line2.put("namaBarang", "");
//            line2.put("berat", "");
//            line2.put("kadar", "");
//            line2.put("harga", "");
//            tables.add(line2);
//        }
//        while(tables.size()<6){
//            Map<String, Object> line = new HashMap<>();
//            line.put("qty", "");
//            line.put("namaBarang", "");
//            line.put("berat", "");
//            line.put("kadar", "");
//            line.put("harga", "");
//            tables.add(line);
//            Map<String, Object> line2 = new HashMap<>();
//            line2.put("qty", "");
//            line2.put("namaBarang", "");
//            line2.put("berat", "");
//            line2.put("kadar", "");
//            line2.put("harga", "");
//            tables.add(line2);
//        }
//        map.put("table", tables);   
//        SimpleEscp simpleEscp = new SimpleEscp();      
//        JsonTemplate template = new JsonTemplate(getClass().getResourceAsStream("template.json"));
//        simpleEscp.print(template, map);
//    }
    public void printSuratPenjualan(List<PenjualanDetail> allDetail) throws Exception{
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("SuratPenjualan.jrxml"));
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(allDetail);
        HashMap hash = new HashMap();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        int selectedService = 0;
        for(int i = 0; i < services.length;i++){
            if(services[i].getName().toUpperCase().contains(printerGadai))
                selectedService = i;
        }
        job.setPrintService(services[selectedService]);
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
        printRequestAttributeSet.add(mediaSizeName);
        printRequestAttributeSet.add(new Copies(1));
        JRPrintServiceExporter exporter;
        exporter = new JRPrintServiceExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        exporter.exportReport();
    }
    public void printBuktiPembelian(List<PembelianDetail> allDetail) throws Exception{
//        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("BuktiPembelianUmum.jrxml"));
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(allDetail);
//        HashMap hash = new HashMap();
//        hash.put("namaToko", Main.sistem.getNama());
//        hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
//        hash.put("noTelpToko", Main.sistem.getNoTelp()); 
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        int selectedService = 0;
//        for(int i = 0; i < services.length;i++){
//            if(services[i].getName().toUpperCase().contains(printerGadai))
//                selectedService = i;
//        }
//        job.setPrintService(services[selectedService]);
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
//        printRequestAttributeSet.add(mediaSizeName);
//        printRequestAttributeSet.add(new Copies(1));
//        JRPrintServiceExporter exporter;
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
    }
    public void printSuratHutang(List<GadaiDetail> gadai) throws Exception{
//        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("SuratHutang.jrxml"));
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(gadai);
//        HashMap hash = new HashMap();
//        hash.put("namaToko", Main.sistem.getNama());
//        hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
//        hash.put("noTelpToko", Main.sistem.getNoTelp());  
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        int selectedService = 0;
//        for(int i = 0; i < services.length;i++){
//            if(services[i].getName().toUpperCase().contains(printerGadai))
//                selectedService = i;
//        }
//        job.setPrintService(services[selectedService]);
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
//        printRequestAttributeSet.add(mediaSizeName);
//        printRequestAttributeSet.add(new Copies(1));
//        JRPrintServiceExporter exporter;
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
    }
    public void printSuratHutangInternal(List<GadaiDetail> gadai) throws Exception{
//        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("SuratHutangIntern.jrxml"));
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(gadai);
//        HashMap hash = new HashMap();
//        hash.put("namaToko", Main.sistem.getNama());
//        hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
//        hash.put("noTelpToko", Main.sistem.getNoTelp());  
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        int selectedService = 0;
//        for(int i = 0; i < services.length;i++){
//            if(services[i].getName().toUpperCase().contains(printerGadai))
//                selectedService = i;
//        }
//        job.setPrintService(services[selectedService]);
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
//        printRequestAttributeSet.add(mediaSizeName);
//        printRequestAttributeSet.add(new Copies(1));
//        JRPrintServiceExporter exporter;
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
    }
    public void printBuktiPelunasanGadai(List<GadaiDetail> gadai) throws Exception{
//        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("BuktiPelunasanGadai.jrxml"));
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(gadai);
//        HashMap hash = new HashMap();
//        hash.put("namaToko", Main.sistem.getNama());
//        hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
//        hash.put("noTelpToko", Main.sistem.getNoTelp());    
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        int selectedService = 0;
//        for(int i = 0; i < services.length;i++){
//            if(services[i].getName().toUpperCase().contains(printerGadai))
//                selectedService = i;
//        }
//        job.setPrintService(services[selectedService]);
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
//        printRequestAttributeSet.add(mediaSizeName);
//        printRequestAttributeSet.add(new Copies(1));
//        JRPrintServiceExporter exporter;
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
    }
    public void printKeuanganHarian(String tanggal, double saldoAwal) throws Exception{
        try(Connection con = Koneksi.getConnection()){
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("LaporanKeuanganHarianSummary.jrxml"));
            String sql = "select kategori,tipe_transaksi,sum(jumlah_rp) from tt_keuangan "
                    + " where left(tgl_keuangan,10) like '"+tanggal+"'  group by kategori order by no_keuangan";
            HashMap hash = new HashMap();
            hash.put("tanggal", tglNormal.format(tglBarang.parse(tanggal)));
            hash.put("saldoAwal", saldoAwal);
            hash.put("namaToko", Main.sistem.getNama());
            hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
            hash.put("noTelpToko", Main.sistem.getNoTelp()); 

            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasperDesign.setQuery(newQuery);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hash, con);
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            int selectedService = 0;
            for(int i = 0; i < services.length;i++){
                if(services[i].getName().toUpperCase().contains(printerGadai))
                    selectedService = i;
            }
            job.setPrintService(services[selectedService]);
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
            printRequestAttributeSet.add(mediaSizeName);
            printRequestAttributeSet.add(new Copies(1));
            JRPrintServiceExporter exporter;
            exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        }
    }
    public void printPembelian(String tglMulai, String tglAkhir) throws Exception{
        try(Connection con = Koneksi.getConnection()){
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("PembelianUmum.jrxml"));
            String sql ="select * from tt_pembelian_head a , tt_pembelian_detail b "
                    + " where a.no_pembelian=b.no_pembelian and "
                    + " left(a.tgl_pembelian,10) between '"+tglMulai+"' and '"+tglAkhir+"' and a.status ='true'"
                    + " order by a.kode_sales,b.kode_kategori,b.kode_jenis";

            HashMap hash = new HashMap();
            hash.put("namaToko", Main.sistem.getNama());
            hash.put("alamatToko", Main.sistem.getAlamat()+", "+Main.sistem.getKota());   
            hash.put("noTelpToko", Main.sistem.getNoTelp()); 
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasperDesign.setQuery(newQuery);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hash, con);
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            int selectedService = 0;
            for(int i = 0; i < services.length;i++){
                if(services[i].getName().toUpperCase().contains(printerGadai))
                    selectedService = i;
            }
            job.setPrintService(services[selectedService]);
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            MediaSizeName mediaSizeName = MediaSize.findMedia(1000,1000,MediaPrintableArea.MM);
            printRequestAttributeSet.add(mediaSizeName);
            printRequestAttributeSet.add(new Copies(1));
            JRPrintServiceExporter exporter;
            exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        }
    }
    public static void printBarcode1(List<Barang> barang)throws Exception{
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        int selectedService = 0;
        for(int i = 0; i < services.length;i++){
            if(services[i].getName().toUpperCase().contains(printerBarcode))
                selectedService = i;
        }
        DecimalFormat gr = new DecimalFormat("##0.00");
        for(int i=0; i<barang.size();i++){
            Barang b = barang.get(i);
            if(i==0||i%2==0){
                String commands = "^XA^MNN^LL216^XZ^XA^JUS^XZ";
                commands=commands+"^XA^LH10,00";
                commands=commands+
                    "^FO20,00^ADN,18,10^FD"+b.getKodeBarcode()+"^FS" +
                    "^FO05,20^BY1^BCN,50,N,N,N,N^FD"+b.getKodeBarcode()+"^FS" +
                    "^FO00,90^ADN,18,10^FD"+b.getNamaBarcode()+"^FS" +
                    "^FO00,115^ADN,30,15^FD"+gr.format(b.getBeratPembulatan())+"^FS" +
                    "^FO00,155^ADN,18,10^FD"+b.getKodeJenis()+"^FS" +
                    "^FO100,155^ADN,18,10^FD"+b.getMerk()+"^FS";
                if(barang.size()==i+1){

                }else{
                    Barang b2 = barang.get(i+1);
                    commands=commands+
                        "^FO480,00^ADN,18,10^FD"+b2.getKodeBarcode()+"^FS" +
                        "^FO465,20^BY1^BCN,50,N,N,N,N^FD"+b2.getKodeBarcode()+"^FS" +
                        "^FO460,90^ADN,18,10^FD"+b2.getNamaBarcode()+"^FS" +
                        "^FO460,115^ADN,30,15^FD"+gr.format(b2.getBeratPembulatan())+"^FS" +
                        "^FO460,155^ADN,18,10^FD"+b2.getKodeJenis()+"^FS" +
                        "^FO560,155^ADN,18,10^FD"+b2.getMerk()+"^FS";
                }
                commands=commands+"^XZ";
                PrintService pservice = services[selectedService];
                DocPrintJob job = pservice.createPrintJob();  
                DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
                job.print(doc, null);
            }
        }  
    }
    public static void printBarcode(List<Barang> listBarang)throws Exception{
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        int selectedService = 0;
        for(int i = 0; i < services.length;i++){
            if(services[i].getName().toUpperCase().contains(printerBarcode))
                selectedService = i;
        }
        DecimalFormat gr = new DecimalFormat("###,##0.00");
        for(int i=0; i<listBarang.size();i++){
            Barang b = listBarang.get(i);
            if(i==0||i%2==0){
                String commands = "<ESC>A<ESC>A3H0130V0000";
                commands=commands+"<ESC>H0000<ESC>V0007<ESC>M"+b.getKodeBarcode()+
                    "<ESC>H0000<ESC>V0030<ESC>BG01050"+b.getKodeBarcode()+
                    "<ESC>H0000<ESC>V0100<ESC>S"+b.getNamaBarcode()+
                    "<ESC>H0020<ESC>V0125<ESC>L0102<ESC>M"+gr.format(b.getBeratPembulatan())+"<ESC>L0102<ESC>S gr" +
                    "<ESC>H0000<ESC>V0165<ESC>L0101<ESC>S"+b.getKodeJenis()+"<ESC>H0100<ESC>V0165<ESC>S"+b.getMerk();
                if(listBarang.size()==i+1){}else{
                    Barang b2 = listBarang.get(i+1);
                    commands=commands+"<ESC>H0455<ESC>V0007<ESC>M"+b2.getKodeBarcode()+
                        "<ESC>H0455<ESC>V0030<ESC>BG01050"+b2.getKodeBarcode()+
                        "<ESC>H0455<ESC>V0100<ESC>S" +b2.getNamaBarcode()+
                        "<ESC>H0475<ESC>V0125<ESC>L0102<ESC>M"+gr.format(b2.getBeratPembulatan())+"<ESC>L0102<ESC>S gr" +
                        "<ESC>H0455<ESC>V0165<ESC>L0101<ESC>S"+b2.getKodeJenis()+"<ESC>H0555<ESC>V0165<ESC>S"+b2.getMerk();
                }
                commands=commands+"<ESC>Q1<ESC>Z";
                char ch = 27;
                commands = commands.replace("<ESC>", String.valueOf(ch));
                PrintService pservice = services[selectedService];
                DocPrintJob job = pservice.createPrintJob();  
                DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
                job.print(doc, null);
            }
        } 
    }
}
