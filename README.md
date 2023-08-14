# MineQuest Data ORM Framework

Das MineQuest Data ORM Framework ist eine leistungsstarke und flexible Lösung für die Verwaltung von Datenbankentitäten
in Java-Anwendungen. Es ermöglicht eine nahtlose Integration von Datenbankzugriffen in deine Anwendung und bietet eine
benutzerfreundliche API für das Speichern, Abrufen und Aktualisieren von Entitätsdaten.

## Features

- Einfache und intuitive API zur Verwaltung von Datenbankentitäten.
- Unterstützung für verschiedene Datenbanktypen (derzeit PostgreSQL und MySQL).
- Automatische Generierung von Datenbanktabellen basierend auf den Entity-Klassen.
- Flexibles Datenbank-Schema-Management (Erstellen, Aktualisieren, Löschen von Tabellen).
- Asynchrone Datenbankzugriffe für verbesserte Performance.

## Installation

Folge diesen Schritten, um das MineQuest Data ORM Framework in deinem Projekt zu integrieren:

1. Füge die Abhängigkeit zu deinem Gradle-Projekt hinzu:

```groovy
dependencies {
    implementation 'com.example:minequest-dataorm:1.0.0'
    // Füge hier weitere Abhängigkeiten hinzu, z. B. Datenbanktreiber
}
```

2. Erstelle eine Konfigurationsdatei für die Datenbankverbindung (`database.json`) in deinem Ressourcenverzeichnis:

```json
{
  "serverAddresses": [
    {
      "host": "localhost",
      "port": 5432
    }
  ],
  "db": "mydatabase",
  "user": "username",
  "password": "password",
  "debug": true,
  "timings": false,
  "dataSourceType": "POSTGRES",
  "dataSourceSchemaRule": "CREATE",
  "numThreads": 4
}
```

3. Erstelle deine Entity-Klassen und registriere sie im `DataServiceRegistry`:

```java
// Erstelle eine Instanz von DataServiceRegistry
DataServiceRegistry registry=new DataServiceRegistry(new DefaultDataCompoundFactory());

// Registriere eine DataService-Instanz für deine Entity-Klasse
        DataService<MyEntity, Long> myEntityDataService=registry.registerDataService(MyEntity.class);
```

4. Verwende die DataService-Instanz, um auf die Datenbank zuzugreifen:

```java
// Speichere eine neue Entität
MyEntity entity=new MyEntity();
        entity.setName("John Doe");
        myEntityDataService.getProvider().save(entity);

// Lese alle Entitäten aus der Datenbank
        List<MyEntity> entities=myEntityDataService.getProvider().findAll();
        for(MyEntity e:entities){
        System.out.println("ID: "+e.getId()+", Name: "+e.getName());
        }
```

## Benutzerdefinierte Konfiguration

Das MineQuest Data ORM Framework ermöglicht es dir, benutzerdefinierte Konfigurationen für die Datenbankverbindung zu
erstellen. Dazu kannst du die `DataSourceSettingsProvider`-Schnittstelle implementieren und deine
eigenen `DataSourceSettings`-Objekte bereitstellen. Anschließend registrierst du deine benutzerdefinierten Provider
im `DataServiceRegistry`.

```java
// Erstelle eine benutzerdefinierte DataSourceSettingsProvider
public class CustomDataSourceSettingsProvider implements DataSourceSettingsProvider {
    @Override
    public DataSourceSettings getDataSourceSettings() {
        // Hier kannst du benutzerdefinierte DataSourceSettings zurückgeben
        return new DataSourceSettings(/* ... */);
    }
}

// Registriere den benutzerdefinierten Provider im DataServiceRegistry
registry.registerDataSourceSettingsProvider("custom",new CustomDataSourceSettingsProvider());
```

## Beispielprojekt

Ein vollständiges Beispielprojekt findest du
in [diesem Repository](https://github.com/example/minequest-dataorm-example), das zeigt, wie du das MineQuest Data ORM
Framework in einer Java-Anwendung verwenden kannst.

## Unterstützte Datenbanken

Das MineQuest Data ORM Framework unterstützt derzeit die folgenden Datenbanktypen:

- PostgreSQL
- MySQL (in Kürze verfügbar)

## Feedback und Mitwirkung

Wir freuen uns über dein Feedback und deine Mitwirkung zur Weiterentwicklung des MineQuest Data ORM Frameworks. Wenn du
Fragen hast oder Probleme auftreten, erstelle bitte ein Issue in
unserem [GitHub-Repository](https://github.com/example/minequest-dataorm).

## Lizenz

Das MineQuest Data ORM Framework ist unter der MIT-Lizenz lizenziert. Weitere Informationen findest du in
der [Lizenzdatei](LICENSE).

---