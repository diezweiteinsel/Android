# Startdokumentation – Android App

## 1. Allgemeine Informationen
- **App-Name:** Civitas  
- **Version:** 1.0.0  
- **Plattform:** Android (minSdkVersion: 24, targetSdkVersion: 36)  
- **Programmiersprache:** Kotlin  
- **Entwicklungsumgebung:** Android Studio [Narwhal 3 2025.1.3]    

---

## 2. Ziel der App
- **Kurze Beschreibung:**  
  Die Android App unserer Anwendung soll die Funktionen des Applicants beinhalten. Die Kernfunktion besteht dabei darin Antragsformulare aus dem 
  Backend ausfüllen und abschicken zu können 
- **Zielgruppe:**  
  Bürger die einen Meldeantrag stellen möchten
- **Hauptfunktionen:**  
  - Meldeantrag erstellen & abschicken
  - Registrieren und Login
  - Öffentliche und eigene Anträge einsehen können 

---

## 3. Architektur & Aufbau
- **Architekturmuster:** MVS 
- **Module & Schichten:**  
  - **UI-Layer:** Component, Screens, Views 
  - **Daten-Layer:** Model(Datenklassen),Repository, Datenbank, API, Autentication Manager, Appcontainer(Http-Client)
  - **Business-Logik:** ViewModels

**Technologien & Bibliotheken:**  
- Jetpack Compose 
- Retrofit & OkHttp für Netzwerk  
- Room / SQLite für Datenbank  
- Hilt / Dagger für Dependency Injection  

---

## 4. Einrichtung & Installation
### Voraussetzungen
#### app-debug.apk
-Android Gerät mit mindestens Android 11
-App öffnen
-Standardmäßig ist die Url auf der das Backend läuft ausgewählt
-Url: http://134.245.1.240:1203/

#### In Android Studio
1. Repository klonen:  
2. Run App

## 5. Tests
-Geringe Testabdeckung -> Aufruf über gradle test
-Bei dem Rest: Manuelle Tests

---

## 6. Bekannte Probleme
1. Die Applications werden nur refresht, wenn man sich erfolgreich einloggt oder durch ein pull down to refresh. Allerdings scheint dieses an die Applications gebunden zu sein und so kann man nicht aktualisieren, wenn die Liste der Applications leer ist. Man muss sich also einmal neu einloggen, damit die Liste der Applications geladen wird.
2. Öffentliche Anträge sind nicht einsehbar, da "GET http://134.245.1.240:1203/api/v1/applications?is_public=true" nur die Applications der eingeloggten User*in zurückgibt.
3. Aus irgendeinem Grund bleibt z.B. bei dem "Hundeantrag" der Name null, auch wenn man einen angibt. Unsere "DynamicAttributeView" bräuchte also noch diese Zeilen Code am Anfang damit die App beim Aufklappen der Application nicht abstürzt:
val valueAsString = if (attribute.value == null || attribute.value.isJsonNull) {
        "N/A"
    } else
4. Dass ein value null wird, wenn man den Antrag in der App ausfüllt ist natürlich auch nicht gewollt.
5. Wenn man sich einloggt, die App beendet, sie wieder öffnet und in den "Offline Modus" geht, kann man die Applications refreshen solange das JWT noch gültig ist.
6. Auf dem Handy werden manche Elemente hochkant und andere Elemente im Landscape nicht abgebildet (Sollte auf dem Tablet nicht der Fall sein).
7. Zwei der Tests schlagen fehl.