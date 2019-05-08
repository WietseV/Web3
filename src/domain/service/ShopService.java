package domain.service;

import domain.db.*;
import domain.model.*;

import java.util.List;

public class ShopService {
    private PersonDb personDb = new PersonDbSQL();

    public ShopService() {
    }

    public Person getPerson(String personId) {
        return getPersonDb().get(personId);
    }

    public List<Person> getPersons() {
        return getPersonDb().getAll();
    }

    public void addPerson(Person person) {
        getPersonDb().add(person);
    }

    public void updatePersons(Person person) {
        getPersonDb().update(person);
    }

    public void deletePerson(String id) {
        getPersonDb().delete(id);
    }

    public PersonDb getPersonDb() {
        return personDb;
    }
}
