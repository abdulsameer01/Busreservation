// UserDAO.java
package dao;
import models.User;
import java.sql.*;

public class UserDAO {
    private Connection conn;
    public UserDAO(Connection conn) { this.conn = conn; }

    public boolean registerUser(String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        return ps.executeUpdate() > 0;
    }

    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;
        }
        return null;
    }
}
