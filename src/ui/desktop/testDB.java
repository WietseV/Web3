package ui.desktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//import domain.db.Secret;
import domain.model.Person;
import domain.model.Product;

public class testDB {

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

        Connection connection = DriverManager.getConnection(url,properties);
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
                System.out.println(e.getMessage());
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
