/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi;

import static com.excellentsystem.TokoEmasPadi.Main.allUser;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Model.Verifikasi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.Annotation;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.util.StringConverter;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author yunaz
 */
public class Function {
    public static double pembulatan(double angka){
        return (double) Math.round(angka*100)/100;
    }
    public static String getSystemDate(){
        return sistem.getTglSystem()+" "+new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    public static Comparator<String> sortDate(DateFormat df){
        return (String t, String t1) -> {
            try{
                return Long.compare(df.parse(t).getTime(),df.parse(t1).getTime());
            }catch(ParseException e){
                return -1;
            }
        };
    }
    public static TableCell getTableCell(DecimalFormat df, String tipe){ 
        TableCell cell = new TableCell<Annotation, Number>(){
            @Override
            public void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty)
                    setText(null);
                else{
                    if(tipe.equals("Rp"))
                        setText("Rp "+df.format(value.doubleValue()));
                    else if(tipe.equals("gr"))
                        setText(df.format(value.doubleValue())+" gr");
                    else
                        setText(df.format(value.doubleValue()));
                }
            }
        };
        return cell;
    }
    public static TreeTableCell getTreeTableCell(DecimalFormat df, boolean kosong, String tipe){
        return new TreeTableCell<Annotation, Number>() {
            @Override 
            public void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) 
                    setText(null);
                else{
                    if(kosong && value.doubleValue()==0.0)
                        setText(null);
                    else{
                        if(tipe.equals("Rp"))
                            setText("Rp "+df.format(value.doubleValue()));
                        else if(tipe.equals("gr"))
                            setText(df.format(value.doubleValue())+" gr");
                        else
                            setText(df.format(value.doubleValue()));
                    }
                }
            }
        };
    }
    public static DateCell getDateCellDisableBefore(LocalDate date){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: derive(RED, 150%);");
                if (item.isBefore(date)) 
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellDisableAfter(LocalDate date){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: derive(RED, 150%);");
                if (item.isAfter(date)) 
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellMulai(DatePicker tglAkhir, LocalDate tglSystem){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: derive(RED, 150%);");
                if(item.isAfter(tglSystem))
                    this.setDisable(true);
                if(item.isAfter(tglAkhir.getValue()))
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellAkhir(DatePicker tglMulai, LocalDate tglSystem){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: derive(RED, 150%);");
                if(item.isAfter(tglSystem))
                    this.setDisable(true);
                if(item.isBefore(tglMulai.getValue()))
                    this.setDisable(true);
            }
        };
    }
    public static StringConverter<LocalDate> getTglConverter(){
        return new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            @Override 
            public String toString(LocalDate date) {
                if (date != null) 
                    return dateFormatter.format(date);
                else 
                    return "";
            }
            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) 
                    return LocalDate.parse(string, dateFormatter);
                else 
                    return null;
            }
        };
    }
    public static void setNumberField(TextField field, DecimalFormat df){
        field.setOnKeyReleased((event) -> {
            try{
                String string = field.getText();
                if(string.contains("-"))
                    field.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            field.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            field.setText(df.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        field.setText(df.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                field.end();
            }catch(Exception e){
                field.undo();
            }
        });
    }
    public static User verifikasi(String password, String jenis){
        User userVerifikasi = null;
        for(User u : allUser){
            if(u.getPassword().equalsIgnoreCase(password)){
                for(Verifikasi v : u.getVerifikasi()){
                    if(v.getJenis().equalsIgnoreCase(jenis)){
                        if(v.isStatus())
                            userVerifikasi = u; 
                    }
                }
            }
        }
        return userVerifikasi;
    }
    public static String cekSales(String password){
        String namaSales = null ;
        for(User u : allUser){
            if(u.getPassword().equalsIgnoreCase(password))
                namaSales = u.getUsername();
        }
        return namaSales;
    }
    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        
        if("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }else if("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }else if(operatingSystem.matches(".*Windows.*")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }else{
            throw new RuntimeException("Unsupported operating system.");
        }
        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }
    public static String downloadUpdate(String ftpServer, String user, String password, String filename)throws Exception{
        FTPClient client = new FTPClient();
        String status;
        client.connect(ftpServer, 21);
        boolean login = client.login(user, password);
        if (login) {
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE); 
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            
            System.out.println("create backup");
            Path sourceFile = Paths.get(filename);
            Path targetFile = Paths.get(filename+" backup");
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("start download");
            FileOutputStream fos = new FileOutputStream(filename);
            boolean file = client.retrieveFile("/" + filename, fos);
            if(file){
                status = "Update Success - please restart application";
            }else{
                System.out.println("rollback file");
                File fileasli = new File(filename);
                File filebackup = new File(filename+" backup");
                if (!fileasli.exists()) {
                    fileasli.createNewFile();
                }
                FileChannel sourceChannel = new FileInputStream(filebackup).getChannel();
                FileChannel destChannel = new FileOutputStream(fileasli).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
                if (sourceChannel != null) 
                    sourceChannel.close();
                if (destChannel != null) 
                    destChannel.close();
                    
                status = "Update Failed - please try again";
            }
            
            System.out.println("delete backup");
            Files.deleteIfExists(Paths.get(filename+" backup")); 
            
            client.logout();
            if(fos!= null) 
                fos.close();
        }else{
            status = "Update Failed - couldn't login to FTP server";
        }
        client.disconnect();
        return status;
    }
    public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    public static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}
