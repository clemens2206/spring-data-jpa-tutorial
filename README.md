# SPRING BOOT Aufgabe 4

Komplete Spring-Boot Application im Detail. Wir machen alle Themen von Spring durch. Darunter auch alle arten von Mapping. Was ist JPA und warum brauchen wir diese. Wir bekommen durch dieses Tutorial ein besseres Verständnis für Spring-Boot.

Java arbeitet mit Konzepte von Objekten und Datenbanken arbeiten mit Tabellen. Wir bekommen die Daten aus der Datenbank und ändern diese um zu Objekten, dafür gibt es verschieden Frameworks.

## What we will build?

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled.png)

## Connecting Springboot App with DB

→ Gehe auf [start.spring.io](http://start.spring.io) 

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%201.png)

→ **Dependencies**

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%202.png)

→ GENERATE

→ Extrahieren und in IntelliJ öffnen.

Wir benutzen die JPA mit Hibernet Implementierung.

→ öffne MySQL Workbench

→ creat neues Schema (schooldb)

→ Apply, Apply, Finish

→ application.properties:

```java
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/schooldb
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.show-sql=true
```

## Mapping entities with DB

Wir starten mit der Student Klasse und mappen diese als erstes.

→ neues Package entity und neue Klasse Student erzeugen.

Da wir diese Klasse zu unserer Datenbank hinzufügen möchten inizialisieren wir sie mit dem @Interface @Entity.

→ Datenfelder 

→ Getter und Setter generieren mit @Data vom package Lombok

→ @AllArgsConstructor für alle Argumente

→ @NoArgsConstructor für keine Argumente

→ @Builder für späteres Builder Pattern

Ein Datenbank Table hat immer einen Primary Key und diese müssen wir mit dem @Id markieren. In unserem Fall die studentId.

→ starte die Application mit SpringDataJpaTutiorialApplication 

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%203.png)

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%204.png)

Die Tabelle Student wurde in der Datenbank gebaut mit den Datenfeldern.

## Different JPA Annotations

mit @Table (name = "tbl_student") können wir eine Tabelle benennen und kennzeichnen.

mit @Column(name = "email_address") können wir die Spalte emailId in der Datenbank unter email_address ablegen und speichern

sequenceName ist die Sequenz in der Datenbank. So legen sie eine bereits im DB vorhandene Sequenz fest. Wenn Sie diesen Weg gehen, müssen sie allocationSize den Wert angeben, der derselbe Wert sein muss, den die DB-Sequenz als "Auto-Inkrement" verwendet.

```java
@SequenceGenerator(
            name ="student_sequence" ,
            sequenceName = "student_sequence",
            allocationSize = 1
    )
```

Wenn sie möchten, können sie eine Sequenz erstellen lassen. Dazu müssen sie jedoch SchemaGeneration verwenden, um es erstellen zu lassen.

```java
@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
```

Die emailId muss für jeden Studenten unique sein.

Das geben wir obne bei @Table an

```java
@Table(
        name = "tbl_student",
        uniqueConstraints = @UniqueConstraint(
                name = "emailid_uinque",
                columnNames = "email_address"
        )
)
```

Wir brauchen den Wert in der Spalte emai_address, somit geben wir ein nullable=false mit.

```java
@Column(
            name = "email_address",
            nullable = false
    )
    private String emailId;
```

→ starten der Application

→ eine neue Tabelle wurde erzeugt Namens tbl_student

## Understanding Repositories and Methods:

→ neues Package repository

→ neues Interface StudentRepository und diese inizialisieren als @Repository 

→ diese Interface erweitert das JpaRepository<Student, Long>

→ JpaRepository<T, ID> T = Entität, ID = Typ Long studentId

→ Gehe auf StudentRepository / rechts klick / Generate / Test und Generiere die StudentRepositoryTest Klasse.

→ Inizialisiere die Klasse mit @SpringBootTest 

→ Datenfeld vom Typ StudentRepository und fügen sie ein @Autowired hinzu.

→ eine Methode saveStudent() und markiere die Methode mit @Test, damit wir den Test auch ausführen können.

Wir benutzen Builder-Pattern um uns ein Objekt zu erzeugen in der Klasse StudentRepositoryTest

```java
Student student = Student.builder()
                .studentId(1L)
                .emailId("clekerber@gmail.com")
                .firstName("Clemens")
                .lastName("Kerber")
                .guardianName("Hansi")
                .guardianEmail("hansi@gmail.com")
                .guardianMobile("99999999999")
                .build();

studentRepository.save(student);
```

→ run die Methode saveStudent()

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%205.png)

→ neue Methode printAllStudent() (@Test)

```java
@Test
    public void printAllStudent(){
        List<Student> studentList = studentRepository.findAll();
        System.out.println("studentList = " + studentList);
    }
```

→ Run

## @Embeddable and @Embedded

Die guardian Datenfelder sollten nicht in der Klasse Student sein, da  es ein eigenes Objekt sein sollte .

→ neue Klasse Guardian in Package entity

→ Datenfelder von Student in Guardian

Wir möchten diese Felder aber nur für unsere Tabelle Student und nicht eine externe für Guardian.  Wir können dafür die Klasse mit @Embeddable markieren.

Wir benötigen aber ein Datenfeld in der Student Klasse

```java
@Embedded
    private Guardian guardian;
```

Für die Klasse Guardian benötigen wir @Data, @AllArgsConstructor, @NoArgsConstructor

→ Datenfelder zu name, email und mobile ändern.

Wir müssen die Atributte mappen zu unserer Datenbank

→ inizialisiere die Klasse Guardian mit @AttributeOverride

```java
@AttributeOverrides({
        @AttributeOverride(
                name = "name",
                column = @Column( name = "guardian_name")
        ),
        @AttributeOverride(
                name = "email",
                column = @Column( name = "guardian_email")
        ),
        @AttributeOverride(
                name = "mobile",
                column = @Column( name = "guardian_mobile")
        )
})
public class Guardian {
```

→ neue Methode saveStudentWithGuardian in der Klasse StudentRepositoryTest

Wir müssen ein neues Objekt von Guardian Pattern und können dies dann verwenden für den Student.

```java
@Test
    public void saveStudentWithGuardian(){

        Guardian guardian = Guardian.builder()
                .email("hansi@gmail.com")
                .mobile("99999994599")
                .name("Hansi")
                .build();

        Student student = Student.builder()
                .firstName("Johannes")
                .emailId("johannes@gmail.com")
                .lastName("Kerber")
                .guardian(guardian)
                .build();

        studentRepository.save(student);
    }
```

→ Run

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%206.png)

## JPA Repositories Methods

→ neue Vordefinierte Methode findByFirstName in StudentRepository

```java
public List<Student> findByFirstName(String firstName);
```

→ Neue Methode printStudentByFirstName() in StudentRepositoryTest.

```java
@Test
    public void printStudentByFirstName(){
        List<Student> students = studentRepository.findByFirstName("Clemens");
        System.out.println("students = " + students);
    }
```

Ausgabe:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%207.png)

→ neue Methode findByFirstNameContaining in StudentRepository

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%208.png)

Diese Methode findet einen firstName auch nur mit einem Bruchteil vom Namen in meinem Fall "cl".

→ neue Methode in StudentRepositoryTest

```java
@Test
    public void printStudentByFirstNameContaining(){
        List<Student> students = studentRepository.findByFirstNameContaining("Cl");
        System.out.println("students = " + students);
    }
```

→ neue Methode in StudentRepository findByLastNameNotNull:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%209.png)

→ neue Methode in StudentRepository

```java
public List<Student> findByGuardianName(String GuardianName);
```

→ implementierungs Methode printStudentBasedOnGuardianName in der Klasse StudentRepositoryTest:

```java
@Test
    public void printStudentBasedOnGuardianName(){
        List<Student> students = studentRepository.findByGuardianName("Hansi");
        System.out.println("students = " + students);
    }
```

[https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)

## JPA @Query Annonation

→ neue Methode in StudentRepository getStudentByEmailId.

Wir müssen die Methode mit @Query markieren, da wir eine Anfrage an die Datenbank benötigen. wir geben der @Query("select s from Student s `where s.emailId = ?1`") mit.

mit where s.emailId = ?1 geben wir die Email mit dem ersten Parameter mit.

JPQL = Bestandteile der Klasse nicht von der Tabelle

→ neue Test Methode printGetStudentByEmailAddress in StudentRepositoryTest

→ neue Methode getStudentFirstNameByEmailId in StudentRepository.

→ neue Test Methode in Stundent RepositoryTest namens printGetStudentFirstNameByEmailAddress.

→ run

Ausgabe:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2010.png)

## Native Queries Example

Das **Spring****Native**Projekt ermöglicht es Entwicklern, **Spring**Boot-Anwendungen mit der GraalVM-**Native**Image-Technologie in Executable Binaries zu kompilieren, ohne dass die nötigen Konfigurationsdateien manuell erstellt werden müssen oder die Anwendung speziell angepasst werden muss.

Neue Native Methode in StudentRepository:

```java
@Query(
            value = "select * from tbl_student s where s.email_address = ?1",
            nativeQuery = true)
    public Student getStudentByEmailIdNative(String email);
```

Test Methode in StudentRepositoryTest:

```java
@Test
    public void printGetStudentByEmailAddressNative(){
        Student student = studentRepository.getStudentByEmailIdNative("clekerber@gmail.com");
        System.out.println("student = " +student);
    }
```

## Query Named Params

→ Kopieren der Methode getStudentByEmailIdNative

→ Ändern des Mthodennamen in getStudentByEmailIdNativeNamdedParam

→ als Value geben wir mit  `SELECT * FROM tbl_student s where s.email_address :emailId`

```java
//Native Named Param
    @Query(
            value = "SELECT * FROM tbl_student s where s.email_address = :emailId ",
            nativeQuery = true)
    public Student getStudentByEmailIdNativeNamedParam(@Param("emailId") String  emailId);
```

```java
@Test
    public void printGetStudentByEmailIdNativeNamedParam(){
        Student student = studentRepository.getStudentByEmailIdNativeNamedParam("clekerber@gmail.com");
        System.out.println("student = " + student);
    }
```

## @Transactional & @Modifying Annotation

Neue Methode in StudentRepository updateStudentNameByEmailId, dies kennzeichnen wir mit @Query und geben als Value den Befehl "`update tbl_student set first_name = ?1 where email_address = ?2`" mit.

→ damit diese Methode modifizieren kann mit unserer Datenbank, müssen wir die Methode auch mit @Modifying kennzeichnen. Wir möchten auch eine Transaktion tätigen somit kennzeichnen wir sie auch mit @Transactional

```java
@Modifying
    @Transactional
    @Query(
            value = "update tbl_student set first_name = ?1 where email_address = ?2",
            nativeQuery = true)
    public int updateStudentNameByEmailId(String firstName, String emailId);
```

`updateStudentNameByEmailIdTest()`

```java
@Test
    public void updateStudentNameByEmailIdTest(){
        studentRepository.updateStudentNameByEmailId("Markus", "clekerber@gmail.com");
    }
```

Update in der Datenbank das Objekt mit der emailId clekerber@gmail.com bekommt einen neuen Namen Markus.

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2011.png)

## JPA One to One Relationship

One to One Beziehung zwischen Course und CourseMaterial. 

Wir benötigen einen Foreign Key für die jeweiligen Tabellen. 

→ neue Klasse Course und CourseMaterial im Package entity.

→ Beide Klassen markieren wir mit @Entity, @Data, @NoArgsConstructor, @AllArgsConstructor und @Builder.

→ Datenfelder erzeugen.

→ bei dem jeweiligen Primary Key in der Klasse geben wir folgendes mit.

Beispiel Course:

```java
@Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long courseId;
```

→ neues Datenfeld in CourseMaterial, da kein CourseMaterial existieren kann ohne Couse benötigen wir foglendes:

OneToOne mapping + JoinColumn

```java
@OneToOne
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "courseId"
    )
    private Course course;
```

→ run Main:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2012.png)

→ öffne Workbench:

Wir sehen vier neue Tabellen course_seqeunce, course_material_seqeunce, course_material und course. Wenn wir uns die Tabellecourse_material anschauen erkennen wir, dass die course_id hier vorhanden ist.

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2013.png)

→ zwei neue Interface CourseRepository und CourseMaterialRepository.

Beide erweitern die Klasse JpaRepository<> und werden gekennzeichnet mit @Repository

→ Generieren der Test Klasse für beide Repositorys.

→ beide Test klassen markieren wir mit @SpringBootTest

→ neues Datenfeld vom Typ CourseMaterialRepository in der Klasse CourseMaterialRepositoryTest und dieses inizialisieren wir mit @Autowired.

## Cascadina

→ in CourseMaterial müssen wir bei @OneToOne cascade hinzufügen.

```java
@OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "courseId"
    )
    private Course course;
```

Tabelle course_material

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2014.png)

Tabelle course

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2015.png)

## Fetch Types

→ in CourseMaterial müssen wir noch ein @interface hinzufügen @ToString(exclude = "course) der course wird somit excluded.

→ bei dem @OneToOne in CourseMaterial fügem wir den FetchType.LAZY hinzu.

Ausgabe:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2016.png)

## Uni & Bi directional relationship

→ öffne Test-Klasse CourseRepositoryTest

→ neues Datenfeld vom Typ CourseRepository @Autowired

→ neue Methode printCourses()

```java
@Test
    public void printCourses(){
        List<Course> courses = courseRepository.findAll();
        System.out.println("courses : " + courses);
    }
```

Ausgabe:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2017.png)

Damit wir auch die CourseMaterial bekommen müssen wir eine Bi directional relationship einrichten.

Wir benötigen eine Referenz auf das Datenfeld course in der Klasse CourseMaterial. 

→ markiere das Datenfeld courseMaterial in der Klasse Course mit @OneToOne und füge dort mappedBy = "course" hinzu.

Ausgabe (nun wird der Course mit dem CourseMaterial ausgegeben: 

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2018.png)

## JPA OneToMany relationship

Ein Teacher kann mehrere Courses haben, aber ein Course kann nur einen Teacher haben, daher OneToMany

→ erzeugen der Teacher-Klasse mit den 3 Datenfelder und den jeweiligen @Interfaces

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long teacherId;
    private String firstName;
    private String lastName;
}
```

Der Course Table hat ein extra Foreignkey für Teacher.

→ wir brauchen ein weiters Datenfeld vom Typ List<Course> die die Kurse haltet.

```java
@OneToMany(
            cascade = CascadeType.ALL
    )
@JoinColumn(
        name = "teacher_id",
        referencedColumnName = "teacherId"
)
    private List<Course> courses;
```

→ starte die Application

die Tabellen wurden erzeugt:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2019.png)

Bei der Tabelle course können wir nun die Spalte teacher_id sehen:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2020.png)

→ neues TeacherRepository erweitert JpaRepository<Teacher, Long> (@Repository)

→ Generieren der Test Klase TeacherRepositoryTest (@SpringBootTest)

→ Datenfeld in TeacherRepositoryTest vom Typ TeacherRepository (@Autowired)

→ neue Methode saveTeacher 

Wenn wir die Methode private machen kann SpringBoot die Test-Methode nicht finden, daher public.

Teacher-Tabelle:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2021.png)

Course-Tabelle:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2022.png)

→ gehe in die Klasse CourseMaterial und füge zu der @OneToOne optional = false hinzu.

Jetzt ist der Course verpflichtend und nicht mehr Optional

CourseMaterial-Tabelle:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2023.png)

Course-Tabelle:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2024.png)

## JPA ManyToOne relationship

→Methode saveCourseWithTeacher in CourseRepositoryTest

Neuer Teacher und neuer Course wird erzeugt.  Der Teacher wird dem Course als Teacher mitgegeben.

Tabelle-Teacher:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2025.png)

Tabelle-Course: (Wir sehen hier die teacher_id 2)

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2026.png)

## Paging and Sorting

Jedes Repository erweitert das JpaRepository Interface und dieses erweitert ein weiteres Interface PagingAndSortingRepository.

### Paging

Sobald wir unser Repository von PagingAndSortingRepository erweitern, müssen wir nur noch:

1. Erstellen oder erhalten Sie ein PageRequest - Objekt, das eine Implementierung der Pageable-Schnittstelle ist
2. Übergeben Sie das PageRequest - Objekt als Argument an die Repository-Methode, die wir verwenden möchten

Wir können ein PageRequest-Objekt erstellen, indem wir die angeforderte Seitennummer und die Seitengröße übergeben.

Sobald wir unser PageRequest-Objekt haben, können wir es übergeben, während wir die Methode unseres Repositorys aufrufen.

```java
@Test
    public void findAllPagination(){
        Pageable firstPageWithThreeRecords =
                PageRequest.of(0,3);
        Pageable secoundPageWithTwoRecords =
                PageRequest.of(1,2);
        List<Course> courses =
                courseRepository.findAll(firstPageWithThreeRecords).getContent();

        Long totalElements =
                courseRepository.findAll(firstPageWithThreeRecords).getTotalElements();

        long totalPages =
                courseRepository.findAll(firstPageWithThreeRecords).getTotalPages();

        System.out.println("totalPages = " + totalPages);
        System.out.println("totalElements = " + totalElements);
        System.out.println("courses = " + courses);
    }
```

Ausgabe:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2027.png)

secozndPageWithTwoRecords suchen:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2028.png)

### Sort

Wir möchten nach dem Titel sortieren

→ Methode findAllSorting

```java
@Test
    public void findAllSorting(){
        Pageable sortByTitle =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("titel")
                );

        Pageable sortByCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("credit").descending());

        Pageable sortByTitleAndCreditDesc =
                PageRequest.of(0,2, Sort.by("titel")
                        .descending().and(Sort.by("credit")));

        List<Course> courses = courseRepository.findAll(sortByTitle).getContent();

        System.out.println("courses = " + courses);
    }
```

→ Methode in CourseRepository

```java
Page<Course> findByTitelContaining(
String titel, Pageable pageable);
```

→ Methode Testen printfindByTitelContaining

```java
@Test
    public void printfindByTitelContaining(){
        Pageable firstPageTenRecords =
                PageRequest.of(0,10);

        List<Course> courses = courseRepository.findByTitelContaining(
                "D", firstPageTenRecords).getContent();

        System.out.println("courses = " + courses);
    }
```

## JPA ManyToMany Relationship

Wir machen eine neue Tabelle mit zwei Spalten einmal course_id und student_id. Die course_id Referenziert auf die courseId und die student_id Referenziert auf die studentId.

```java
@ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "student_course_map",
            joinColumns = @JoinColumn(
                    name = "course_id",
                    referencedColumnName = "courseId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "student_id",
                    referencedColumnName = "studentId"
            )
    )
    private List<Student> student;
```

→ neue Methode addStudents(Student student)

```java
public void addStudents(Student student){
        if(students == null){
            students = new ArrayList<>();
            students.add(student);
        }
    }
```

→ starten der Application

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2029.png)

neue Methode saveCourseWithStudentAndTeacher:

```java
Teacher teacher = Teacher.builder()
                .lastName("Scherl")
                .firstName("Hansi")
                .build();

        Student student = Student.builder()
                .firstName("Anna")
                .lastName("Kerber")
                .emailId("ankerber@tsn.at")
                .build();

        Course course = Course.builder()
                .titel("Ai")
                .credit(12)
                .teacher(teacher)
                .build();

        course.addStudents(student);

        courseRepository.save(course);
    }
```

Tabelle Teacher:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2030.png)

Tabelle Course:

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2031.png)

Tabelle tbl-student

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2032.png)

Tabelle student_course_map

![Untitled](SPRING%20BOOT%20Aufgabe%204%206baa86ffbd504b2781b3f600721d3bf7/Untitled%2033.png)
