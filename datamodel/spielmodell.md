## Spiel
 - 2 Spieler
 - Weltzustand
 - Runden
  - Welcher Spieler ist dran?
  - Was hat der andere Spieler gerade gespielt?
  - Wann wird der Simulationsschritt ausgeführt?

## Maßnahmen/Karten
 - Funktion, die auf Weltzustand angewendet wird => haben Effekt auf Zustands(übergangs-)eigenschaften
 - Vorraussetzungen (andere Maßnahmen)

## Weltzustand
 - Optional: Zustand kann Liste an Zuständen von enthaltenen Gebieten enthaltenen (vgl. Entwurfsmuster Kompositum)
 - Zustandseigenschaften
  - Absolute Zahlen wie gesunde/infizierte/resistente/tote Personen
  - Gesundheitssystem-Kapazität
  - Stimmung der Gesellschaft
  - Vorhandene Geldmittel
 - Zustandsübergangseigenschaften
  - Laufende Kosten (oder andere sich wiederholende Effekte)
  - Mortalitätsrate
  - Ausbreitungsrate
 - Verlauf der bisherigen Maßnahmen

## Statistik
  - Beobachtet Weltzustand
  - Erstellt auf dieser Basis Statistiken/Auswertung
  - Auswirkung/Log von Maßnahmen

## Funktionen
  - Simulationsfunktion
    Wendet Simulationsschritt an -> neue Leute infiziert, Bewegung von Infizierten in andere Regionen
    Bestimmt durch Weltzustand
  - Maßnahmenfunktion
    Ausspielen von Karten passt Zustands(übergangs-)eigenschaften an
