/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class UserDAO {
    
    public static List<User> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_user ");
        ResultSet rs = ps.executeQuery();
        List<User> allUser = new ArrayList<>();
        while(rs.next()){
            User u = new User();
            u.setUsername(rs.getString(1));
            u.setPassword(rs.getString(2));
            u.setLevel(rs.getString(3));
            allUser.add(u);
        }
        return allUser;
    }
    public static User get(Connection con, String username)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_user where username=? ");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        User u = null;
        while(rs.next()){
            u = new User();
            u.setUsername(rs.getString(1));
            u.setPassword(rs.getString(2));
            u.setLevel(rs.getString(3));
        }
        return u;
    }
    public static void update(Connection con, User u)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_user set password=?, level=? where username=? ");
        ps.setString(1, u.getPassword());
        ps.setString(2, u.getLevel());
        ps.setString(3, u.getUsername());
        ps.executeUpdate();
    }
    public static void insert(Connection con, User u)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_user values(?,?,?)");
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getLevel());
        ps.executeUpdate();
    }
    public static void delete(Connection con, User u)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_user where username=? ");
        ps.setString(1, u.getUsername());
        ps.executeUpdate();
                
    }
}
