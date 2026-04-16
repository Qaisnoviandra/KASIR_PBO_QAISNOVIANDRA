/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasirtoko;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static Connection conn;
    
    public static Connection getKoneksi() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/kasir_toko";
                String user = "root";
                String pass = "";
                conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
/**
 *
 * @author a c e r
 */

