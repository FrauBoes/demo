# Demo-Project für socoto #

## Quickstart
Der Service kann mit der folgenden Eingabe gestartet werden:
```commandline
mvn clean spring-boot:run
```
Der Service kann dann auf Port 8080 erreicht werden, z.B. mit den folgenden Requests:
```text
GET localhost:8080/demo/persons
```
```text
POST localhost:8080/demo/persons
```
mit Request Body:
```json
{
    "firstName": "Jane",
    "lastName": "Doe",
    "birthDate": "2020-12-30"
}
```
```text
GET localhost:8080/demo/persons/1
```
```text
PUT localhost:8080/demo/persons/1
```
mit Request Body:
```json
{
    "firstName": "Jane",
    "lastName": "Doesy",
    "birthDate": "2020-12-30"
}
```
```text
DELETE localhost:8080/demo/persons/1
```

## Beschreibung
### Funktion
Ein REST-Service `PersonenService`, gebaut mit Spring Boot, mit den folgenden Methoden:
1. `Person create(Person)`
2. `Person update(Person, Long)`
3. `void delete(Long)`
4. `Person get(Long)`
5. `Iterable<Person> getAll()` 

`4.` und `5.` wurden ergänzt, um alle CRUD-Methoden zur Verfügung zu stellen.

Die folgenden Validierungen werden ausgeführt:
1. Der Vorname einer Person kann leer sein und muss <= 20 Zeichen lang sein
2. Der Nachname einer Person muss >= 3 Zeichen und <= 20 Zeichen lang sein
3. Das Geburtsdatum einer Person darf nicht in der Zukunft liegen

### Komponenten
Der Service läuft im embedded Tomcat servlet container, den Spring Boot mitliefert. 

Den Service kann man auf dem Applikationsserver der Wahl laufen lassen. Eine `.jar` Datei zum Deployen auf einem Applikationsserver kann mit folgender Eingabe erstellt werden:
```commandline
mvn clean package
```

Mit Wildfly hatte ich leider keinen Erfolg, trotz einiger Versuche z.B. mit diesem [Guide](https://www.mastertheboss.com/jboss-frameworks/spring/spring-boot-hello-world-on-wildfly/). Ich vermute das Problem lag in der Konfiguration.

Der Service nutzt eine in-memory [H2](https://h2database.com/html/main.html) Datenbank. Dies kann nach Bedarf angepasst werden in `application.properties`.

### Tests

Unittests mit JUnit für die `create` Methode finden sich in `DemoApplicationTests`.

Integrationstests in `DemoApplicationIntegrationTests` mit Arquillian habe ich leider nicht funktionstüchtig erstellen können. U.a. mit diesen [Guides](http://arquillian.org/guides/) habe ich gearbeitet, aber ich habe herausfinden können, wie im Test auf Instanzen der Klassen zugegriffen werden kann.

Die Tests können mit der folgenden Eingabe ausgeführt werden:
```commandline
mvn clean test
```

## Fragen

### 1. Der Service unterstützt aktuell keine Autorisation. Wie würden Sie diese einbauen?

Spring Boot bietet dafür [Spring Authorization Server](https://spring.io/projects/spring-authorization-server) mit Implementationen von `OAuth 2.1` für Autorisation und `OpenID Connect 1.0` für Authentifizierung. Das Framework kann im Projekt genutzt werden.

### 2. Wie würden Sie den Service um Protokollierung und Performancemessungen erweitern?

Für Protokolliering nutzt Spring Boot die `Commons Logging` Library als Standard. Stattdessen kann aber auch eine der vielzähligen Alternativen genutzt werden, z.B. `Log4J2` oder `Java Util Logging`.

Für Performancemessungen bietet Spring Boot [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.5.6/reference/html/actuator.html). Mithilfe dessen können andere Frameworks für Monitoring und Performancemessungen integriert werden, z.B `JMX`. Alternativ könnte [JMeter](https://jmeter.apache.org/) als eigenständiges Tool für Performancemessungen genutzt werden.  

### 3. Wenn ein Client den Service zur Anlage einer neuen Person aufruft und der Service (aus welchen Gründen auch immer) nicht antwortet, wie können Sie sicherstellen, dass die Person nicht mehrfach angelegt wird?

REST gibt vor, dass das Client-Server Setup stateless ist. Ein POST Request ist nicht idempotent, das heißt identische POST Requests können sich unterschiedlich auswirken. Eine Mehrfachanlegung einer Person ist daher grundsätzlich möglich.

Es gibt mehrere Möglichkeit, dies zu vermeiden: 

Eine Möglichkeit ist, die Datenbank mit einer `UNIQUE` Einschränkung zu versehen. In diesem Fall muss die Kombination aus `firstName`, `lastName` und `birthDate` einzigartig sein ("composite key"), andernfalls wird die neue Person nicht gespeichert.

Eine andere Möglichkeit ist, dass der Client jedes neue Object mit einer `GUID` versieht. Der Server kann den Request dann wie einen PUT Request behandeln (PUT ist immer idempotent): wenn eine Person mit der `GUID` bereits gespeichert ist, wird sie mit der neuen Person überschrieben, andernfalls wird die neue Person gespeichert.
