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

    @Override
    public void createUsersTable() {
        String create_user_table = "CREATE TABLE IF NOT EXISTS `userdb`.`user` (\n" +
                "  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NULL,\n" +
                "  `last_name` VARCHAR(45) NULL,\n" +
                "  `age` TINYINT(3) NULL,\n" +
                "  PRIMARY KEY (id));";
        try (Connection con = Util.createDBConnection()) {
            PreparedStatement pstm = con.prepareStatement(create_user_table);
            pstm.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String drop_table_user = "DROP TABLE IF EXISTS userdb.user";
        try (Connection con = Util.createDBConnection()) {
            PreparedStatement pstm = con.prepareStatement(drop_table_user);
            pstm.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String save_user = "INSERT INTO userdb.user (name, last_name, age) VALUES(?,?,?)";
        try (Connection con = Util.createDBConnection()) {
            PreparedStatement pstm = con.prepareStatement(save_user);
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
        String delete_user_byid = "DELETE FROM userdb.user WHERE id = ?";
        try (Connection con = Util.createDBConnection()) {
            PreparedStatement pstm = con.prepareStatement(delete_user_byid);
            pstm.setLong(1, id);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String get_all_users = "SELECT * FROM userdb.user";
        List<User> users = new ArrayList<>();
        try (Connection con = Util.createDBConnection()) {
            PreparedStatement pstm = con.prepareStatement(get_all_users);
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
        String clean_user_table = "TRUNCATE TABLE userdb.user";
        try (Connection con = Util.createDBConnection()) {
            Statement stm = con.createStatement();
            stm.execute(clean_user_table);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
