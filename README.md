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
1. Wenn man sich einloggt, die App beendet, sie wieder öffnet und in den "Offline Modus" geht, kann man die Applications refreshen solange das JWT noch gültig ist.
2. Auf dem Handy werden manche Elemente hochkant und andere Elemente im Landscape nicht abgebildet (Sollte auf dem Tablet nicht der Fall sein).
3. Zwei der Tests schlagen fehl.
