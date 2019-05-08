package domain.db;

import domain.model.Person;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PersonDbSQL implements PersonDb {
    Properties properties = new Properties();
    String url = "jdbc:postgresql://databanken.ucll.be:51819/2TX34?currentSchema=r0632785";

    public PersonDbSQL(){
        properties.setProperty("user", "local_r0632785");
        properties.setProperty("password", "TDZ-RQhS0W&1&,sn");
        //Secret.setPass(properties);	// implements line 17 and 18
        properties.setProperty("ssl", "true");
        properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        properties.setProperty("sslmode","prefer");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    @Override
    public Person get(String personId) {
        if(personId == null){
            throw new DbException("No id given");
        }
        for (Person person : getAll()){
            if (person.getUserid() == personId) return person;
        }

        return null;
    }

    @Override
    public List<Person> getAll(){
        List<Person> persons = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url,properties);
            Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM Person");
            while (result.next()) {
                String userid = result.getString("userid");
                String firstname = result.getString("firstname");
                String lastname = result.getString("lastname");
                String email = result.getString("email");
                String password = result.getString("password");
                try {    // validation of data stored in database
                    Person person = new Person(userid, email.trim(), password, firstname, lastname);
                    persons.add(person);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage(), e);
        }
        return persons;
    }

    @Override
    public void add(Person person){
        if(person == null){
            throw new DbException("No person given");
        }
        if (getAll().contains(person.getUserid())) {
            throw new DbException("User already exists");
        }
        String sql = "INSERT INTO Person (userid,firstname,lastname,email, password) " + "VALUES " + "('" + person.getUserid() + "',' " + person.getFirstName() + "',' " + person.getLastName() + "',' " + person.getEmail() + "',' " + person.getPassword() + "')";

        try (Connection connection = DriverManager.getConnection(url, properties);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DbException(e);
        }
        //TO-DO
    }

    @Override
    public void update(Person person){
        if(person == null){
            throw new DbException("No person given");
        }
        if(!getAll().contains(person.getUserid())){
            throw new DbException("No person found");
        }
    }

    @Override
    public void delete(String personId){
        if(personId == null){
            throw new DbException("No id given");
        }
        //TO-DO
    }

    @Override
    public int getNumberOfPersons() {
        return getAll().size();
    }
}
