package service;

import model.Results;
import model.User;
import org.postgresql.Driver;

import javax.management.Query;
import java.sql.*;
import java.util.Objects;

public class DbService {

    String url = "jdbc:postgresql://localhost:5432/app-auth";
    String username = "postgres";
    String password = "oub1999";


    public Results registerUser(User user) {

        try {
//          Driverni ro`yxatdan o`tkazish
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String query = "select phone_number from users where phone_number = '" + user.getPhoneNumber() + "'";

            String findPhoneNumber = "";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                findPhoneNumber = resultSet.getString(1);
            }
          if (Objects.equals(findPhoneNumber, user.getPhoneNumber())){
              return new Results("Bunday telefon raqam mavjud", false);
          }


//            String checkPhoneNumberQuery = "select count(*) from users where phone_number = '" + user.getPhoneNumber() + "'";
//            ResultSet resultSet = statement.executeQuery(checkPhoneNumberQuery);
//            int countUserByPhoneNumber = 0;
//            while (resultSet.next()) {
//                countUserByPhoneNumber = resultSet.getInt(1);
//            }
//            if (countUserByPhoneNumber > 0) {
//                return new Results("Bunday telefon raqam mavjud", false);
//            }
            String countUserByUsernameQuery = "select count(*) from users where username = '" + user.getUsername() + "'";
            ResultSet resultSetUsername = statement.executeQuery(countUserByUsernameQuery);
            int countUserByUsername = 0;
            while (resultSetUsername.next()) {
                countUserByUsername = resultSetUsername.getInt(1);
            }
            if (countUserByUsername > 0) {
                return new Results("Bunday username mavjud", false);
            }
            String saveUserQuery = "insert into users(first_name, last_name, phone_number, username, password)\n " +
                    "values('" +user.getFirstname()+ "','" +user.getLastname()+ "','"
                    +user.getPhoneNumber()+"','" + user.getUsername()+"','" + user.getPassword() + "');";
            boolean execute = statement.execute(saveUserQuery);
            System.out.println(execute);
            return new Results("User saqlandi",true);

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public User login(String username,String password){
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,this.username,this.password);
            String query = "select * from users where users.username=? and users.password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){

                int id = resultSet.getInt(1);
                String firstname = resultSet.getString(2);
                String lastname = resultSet.getString(3);
                String phoneNumber = resultSet.getString(4);
                User user = new User(id,firstname,lastname,phoneNumber);
                return user;
            }
            return null;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
