package ui.desktop;

import domain.model.DomainException;
import domain.model.Person;
import domain.model.Product;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class addPersonToDB {
    //grand access to schema and tables local_r0632785
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        String url = "jdbc:postgresql://databanken.ucll.be:51819/2TX34?currentSchema=r0632785";
        properties.setProperty("user", "local_r0632785");
        properties.setProperty("password", "TDZ-RQhS0W&1&,sn");
        //Secret.setPass(properties);	// implements line 17 and 18
        properties.setProperty("ssl", "true");
        properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        properties.setProperty("sslmode","prefer");

        Person newperson = new Person();

        String newUserid = JOptionPane.showInputDialog("userid: ");
        String newFirstname = JOptionPane.showInputDialog("Firstname: ");
        String newLastname = JOptionPane.showInputDialog("Lastname: ");
        String newEmail = JOptionPane.showInputDialog("E-mail: ");
        String newPassword = JOptionPane.showInputDialog("Password: ");
        try {
             newperson = new Person(newUserid, newEmail, newPassword, newFirstname, newLastname);
        } catch (DomainException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }




        Connection connection = DriverManager.getConnection(url,properties);
        Statement addStatement = connection.createStatement();
        addStatement.executeUpdate("INSERT INTO Person (userid,firstname,lastname,email, password) " + "VALUES " + "('" + newUserid + "',' " + newFirstname + "',' " + newLastname + "',' " + newEmail + "',' " + newPassword + "')");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery( "SELECT * FROM Person" );


        while(result.next()){
            String userid = result.getString("userid");
            String firstname = result.getString("firstname");
            String lastname = result.getString("lastname");
            String email = result.getString("email");
            String password = result.getString("password");
            try {	// validation of data stored in database
                Person person = new Person(userid, email.trim(), password, firstname, lastname);
                System.out.println(person.toString());
            }
            catch (IllegalArgumentException e) {
                //System.out.println(e.getMessage());
            }
        }

        ResultSet result2 = statement.executeQuery("SELECT  * FROM Product");

        while(result2.next()){
            int productid = Integer.parseInt(result2.getString("productid"));
            String name = result2.getString("name");
            String description = result2.getString("description");
            Double price = Double.parseDouble(result2.getString("price"));
            try {	// validation of data stored in database
                Product product = new Product(productid, name, description, price);
                System.out.println(product.toString());
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        statement.close();
        connection.close();
    }
}
