package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    /*if (persons.size() == 0) {
      return Collections.emptyList();
    }*/
    //т.к конвертим начиная со второй первую просто пропускаем
    return persons.stream().skip(1).map(Person::getFirstName).collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    //в distinct нет необходимости, set уникальные значения
    return getNames(persons).stream().collect(Collectors.toSet());
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
    String result = "";
    if (person.getSecondName() != null) {
      result += person.getSecondName() + " ";
    }
  
    if (person.getFirstName() != null) {
      result += person.getFirstName();
    }
    //Изменили на getMiddleName
    if (person.getMiddleName() != null) {
      result += " " + person.getMiddleName();
    }
    return result;
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toMap(i -> i.getId(), n -> convertPersonToString(n)));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().anyMatch(persons2::contains);
  }

  //...
  public long countEven(Stream<Integer> numbers) {
    count = 0;
    numbers.filter(num -> num % 2 == 0).forEach(num -> count++);
    return count;
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