package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {

  private long count;

  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    
    //т.к конвертим начиная со второй первую просто пропускаем
    //нельзя сделать remove неизменяемых коллекций, а skip можно этим он лучше
    return persons.stream().skip(1).map(Person::getFirstName).collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    //в distinct нет необходимости, set уникальные значения
    return new HashSet<>(getNames(persons));
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
    
    return Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName()).
        filter(Objects::nonNull).collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().
        collect(Collectors.toMap(id -> id.getId(), name -> convertPersonToString(name), (a, b) -> a));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  //Иходный работает за O(n^2)
  //мой работает за O(n)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Map<Person, String> personIdName = persons1.stream().collect(Collectors.toMap(person -> person, Person::getFirstName));
    for (Person person : persons2) {
      if (personIdName.containsKey(person))
        return true;
    }
    return false;
  }

  //...
  public long countEven(Stream<Integer> numbers) {
    
    return numbers.filter(num -> num % 2 == 0).count();
  }

  @Override
  public boolean check() {
    System.out.println("Слабо дойти до сюда и исправить Fail этой таски?");
    Person ivan =  new Person(6, "Ivan", Instant.now());
    List<Person> persons = List.of(
        new Person(1, "Oleg", Instant.now()),
        new Person(2, "Vasya", Instant.now()),
        new Person(3, "Oleg", Instant.now()),
        new Person(4, "Vasya", Instant.now()),
        new Person(5, "Oleg", Instant.now()),
        ivan
    );
    //getNames
    boolean names = List.of("Vasya", "Oleg", "Vasya", "Oleg", "Ivan")
        .equals(getNames(persons));
    //getDistinct
    boolean distinct = Set.of("Ivan", "Vasya", "Oleg")
        .equals(getDifferentNames(persons));
    //convertPersonToString
    boolean toString = "Ivan".equals(convertPersonToString(ivan));
    //getPersonNames
    boolean personNames = Map.of(1,"Oleg", 2, "Vasya", 3, "Oleg", 4, "Vasya",
        5,  "Oleg", 6, "Ivan").equals(getPersonNames(persons));
    //hasSamePersons
    List<Person> persons2 = List.of(
        new Person(1, "Oleg", Instant.now()),
        new Person(2, "Vasya", Instant.now()),
        ivan
    );
    boolean has = hasSamePersons(persons, persons2);
  
    boolean codeSmellsGood = has && personNames && toString && distinct && names;
    boolean reviewerDrunk = false;
    return codeSmellsGood || reviewerDrunk;
  }
}
