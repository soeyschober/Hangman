# Hangman

Author: Soey Schober

LBS Eibiswald | 2aAPC

In diesem Projekt habe ich das klassische Hangman-Spiel in Java umgesetzt.
Der Schwerpunkt lag auf einer sauberen Benutzeroberfläche, einfacher Bedienung und robuster Fehlerbehandlung.
Ziel war es, ein spielbares, übersichtliches und optisch ansprechendes Programm zu entwickeln, das die grundlegenden Java-Konzepte (GUI, Events, Logiktrennung) demonstriert.

---

## Verwendung

### Technologien im Einsatz:

* Java
* IntelliJ IDEA
* Swing GUI Builder

---

### Start der Anwendung

Nach dem Start öffnet sich das Hangman-Fenster.
Der Benutzer kann ein Wort erraten, indem er Buchstaben über die Eingabe auswählt.
Jeder falsche Buchstabe lässt den Galgen ein Stück weiter „entstehen“.
Das Spiel endet, wenn entweder das Wort vollständig erraten oder der Galgen vollständig ist.

**Funktionen im Überblick:**

* Anzeige der bereits geratenen Buchstaben
* Visuelle Darstellung des Spielfortschritts
* Neustart-Funktion nach Spielende
* Einstellungsmenü über `settingsForm`, um Anpassungen vorzunehmen (z. B. Schwierigkeitsgrad, Wortliste etc.)
* Fehlerprüfung bei der Eingabe (keine Zahlen, Sonderzeichen, leere Eingaben)

---

### Klassenstruktur

| Datei               | Beschreibung                                                  |
| ------------------- | ------------------------------------------------------------- |
| `Main.java`         | Einstiegspunkt des Programms; startet die Benutzeroberfläche. |
| `HangmanUI.java`    | Hauptfenster mit Spiellogik, UI-Elementen und Event-Handling. |
| `HangmanUI.form`    | GUI-Layout-Datei, die das visuelle Design beschreibt.         |
| `settingsForm.java` | Fenster zur Anpassung von Spielparametern.                    |
| `settingsForm.form` | GUI-Layout-Datei für die Einstellungen.                       |

---

### Beispielablauf

1. Anwendung starten
2. Neues Spiel beginnen
3. Buchstaben raten
4. Fortschritt beobachten
5. Nach Spielende – Sieg oder Niederlage – Neues Spiel starten

### Output

**0 Errors und 0 Eingegeben Buchstaben**

<img width="644" height="853" alt="image" src="https://github.com/user-attachments/assets/c395cb4c-6fed-4fb8-a020-a627d54b3e7c" />

**Einstellungen**

<img width="650" height="859" alt="image" src="https://github.com/user-attachments/assets/1597ef52-6a00-4562-9e55-be5faaacf81b" />

**Mit Fehleranzeige und anderer Wörterlänge + 2 Errors auf default**

<img width="647" height="858" alt="image" src="https://github.com/user-attachments/assets/2c750061-3537-4c6d-a606-540308ab81de" />
