package de.rockethome.demo.dao;

import de.rockethome.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private  static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = selectPersonById(id);
        if (!personMaybe.isPresent()) {
            return 0;
        }
        DB.remove(personMaybe.get());
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person update) {
        return selectPersonById(id)
                .map(p -> {
                    int indexOfPersonToUpdate = DB.indexOf(p);
                    if (indexOfPersonToUpdate >= 0) {
                        DB.set(indexOfPersonToUpdate, new Person(id, update.getName()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

}
/* For those of you who are getting Error for IsEmpty() use

        ![variable name].isPresent() instead

        and those who are getting error for List.of...,
        you are probably using earlier version of java (same for me i'm using java 8),
        it wont be supported so you would have to change that method a bit.
        What I have done is

@Override
 public List<Person> selectAllPeople() {
  //List<Person> p = (List<Person>) new Person(UUID.randomUUID(), "FROM POSTGRES DB");
  //use this instead
  List<Person> p = new ArrayList<>();
  p.add(new Person(UUID.randomUUID(), "FROM POSTGRES DB"));
  return p;
 } */