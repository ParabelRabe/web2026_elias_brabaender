# web2026_elias_brabaender

Anwendung starten:
Im Terminal "./gradlew clean bootRun" ausführen. Die Seite kann dann unter  http://localhost:8080/admin erreicht werden.
Falls der Build Fehlschlagen sollte, kommentieren Sie in der build.gradle Datei ab Zeile 44 alles aus. Dies ist ein Windows-Problem.
Unter dem Ordner Frontend muss zuerst einmalig "npm install" und danach "npm run dev" im Terminal ausgeführt werden. Diese Seite ist dann unter http://localhost:5173 erreichbar.
Ersteres kann mit den Zugangsdaten Name:admin Passwort:admin zugegriffen werden (wäre in einer Realanwendung natürlich undenkbar). Dort kann man dann Nutzer erstellen, mit dessen Daten man sich dann im Frontend einloggen kann. Zusätzlich kann man Anzeigen erstellen und diese dann per http://localhost:8080/admin/anzeige/verlosung den Nutzern zuteilen. Das ist wichtig zu tun, da damit auch der Besitzer ausgelost wird. Und dieser muss vorhanden sein, damit man auf der Frontend-Website die Anzeige bestellen und stornieren kann.
