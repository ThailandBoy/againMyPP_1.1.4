package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection con;
    public UserDaoJDBCImpl() {
        con = Util.createDBConnection();
    }

    @Override
    public void createUsersTable() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS `userdb`.`user` (\n" +
                "  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NULL,\n" +
                "  `last_name` VARCHAR(45) NULL,\n" +
                "  `age` TINYINT(3) NULL,\n" +
                "  PRIMARY KEY (id));";
        try {
            PreparedStatement pstm = con.prepareStatement(createUsersTable);
            pstm.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTable = "DROP TABLE IF EXISTS userdb.user";
        try {
            PreparedStatement pstm = con.prepareStatement(dropUsersTable);
            pstm.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUser = "INSERT INTO userdb.user (name, last_name, age) VALUES(?,?,?)";
        try {
            PreparedStatement pstm = con.prepareStatement(saveUser);
            pstm.setString(1, name);
            pstm.setString(2, lastName);
            pstm.setByte(3, age);
            pstm.executeUpdate();
            System.out.printf(" User c именем - %s добавлен в базу данных \n", name);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String deleteUserById = "DELETE FROM userdb.user WHERE id = ?";
        try {
            PreparedStatement pstm = con.prepareStatement(deleteUserById);
            pstm.setLong(1, id);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsers = "SELECT * FROM userdb.user";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement pstm = con.prepareStatement(getAllUsers);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getByte(4)));
            }
            users.forEach(System.out::println);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String cleanUsersTable = "TRUNCATE TABLE userdb.user";
        try {
            Statement stm = con.createStatement();
            stm.execute(cleanUsersTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
