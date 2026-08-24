import { reactive, readonly, computed, ref } from "vue";
import { defineStore } from "pinia";
import type {IAnzeigeDTD} from '@/stores/IAnzeige.ts'
import { useInfo } from '@/composables/useInfo.ts'
import { Client } from "@stomp/stompjs";
import type { IFrontendNachrichtEvent } from "@/services/IFrontendNachrichtEvent";
const {info, loescheInfo, setzeInfo} = useInfo()
export const useAnzeigeStore = defineStore("anzeigestore", () => {
    const anzeigedata = reactive<{ok: boolean, anzeigeliste: IAnzeigeDTD[]}>({ok: false, anzeigeliste: []})

    function updateAnzeigeListe(){
        fetch('/api/anzeige')
        .then( (response) => {
          if (!response.ok) {
            anzeigedata.ok = false
            setzeInfo(response.statusText)
            throw new Error('schade');
          }
          return response.json();
        })
        .then((jsondata: IAnzeigeDTD[]) => {
          console.log("Anzeigen geladen.");
          anzeigedata.anzeigeliste = jsondata
          anzeigedata.ok = true;
          console.log("Starte STOMP-Live-Updates");
          startAnzeigeLiveUpdate();
        })
        .catch((error) => {
          console.error("REST-Fetch abgebrochen", error);
        });
    }

    const stompclient = ref<Client | null>(null)

    function startAnzeigeLiveUpdate(){
      if (stompclient.value) {
          return;
      }
      const wsurl = `ws://${window.location.host}/stompbroker`;
      const DEST = "/topic/anzeige";
      stompclient.value = new Client({ brokerURL: wsurl });
      stompclient.value.onWebSocketError = (event) => {
          setzeInfo('Fehler bei der WebSocket-Verbindung zum Server.');
      };
      stompclient.value.onStompError = (frame) => {
          setzeInfo(`STOMP-Fehler aufgetreten: ${frame.headers['message']}`);
      };
      stompclient.value.onConnect = (frame) => {
          console.log('STOMP-Verbindung erfolgreich etabliert');
          stompclient.value?.subscribe(DEST, (message) => {
              try {
                  const eventobjekt: IFrontendNachrichtEvent = JSON.parse(message.body);
                  console.log(JSON.stringify(eventobjekt));
                  if (eventobjekt.typ === 'ANZEIGE') {
                      updateAnzeigeListe();
                  }
              } catch (error) {
                  setzeInfo('Fehler beim Parsen der Live-Nachricht.');
              }
          });
      };
      stompclient.value.onDisconnect = () => {
          setzeInfo('Die Live-Verbindung wurde abgebaut.');
      };
      stompclient.value.activate();
    }
    return {
        anzeigedata,
        updateAnzeigeListe,
        startAnzeigeLiveUpdate
    }
});

const liste = ref<IAnzeigeDTD[]>(
  JSON.parse(`
[
  {
    "id": 1,
    "titel": "Orangenhammer",
    "beschreibung": "Ein Hammer, der Orangen zerschmettert, und das gründlich.",
    "preis": 10,
    "anzahl": 20,
    "verfuegbar": 17,
    "ablaufdatum": "2026-08-17",
    "anbieterName": "Joghurta Biffel",
    "anbieterAdresse": "An der Ecke, Vollradisroda"
  },
  {
    "id": 2,
    "titel": "Bananenbrecher",
    "beschreibung": "Ein Gerät, das Bananen in Scheiben schneidet, ideal für Obstsalate.",
    "preis": 15,
    "anzahl": 30,
    "verfuegbar": 25,
    "ablaufdatum": "2026-09-01",
    "anbieterName": "Trubert vom Senkel",
    "anbieterAdresse": "Obstweg 5, Fruchtstadt"
  },
  {
    "id": 3,
    "titel": "Apfelzertrümmerer",
    "beschreibung": "Ein robustes Werkzeug, das Äpfel in Stücke zerschmettert, perfekt für Apfelmus.",
    "preis": 20,
    "anzahl": 10,
    "verfuegbar": 8,
    "ablaufdatum": "2026-10-15",
    "anbieterName": "Jöndhard Biffel",
    "anbieterAdresse": "Kernstraße 12, Apfelhausen"
  },
  {
    "id": 4,
    "titel": "Traubenzerquetscher",
    "beschreibung": "Ein praktisches Gerät, das Trauben zerquetscht, ideal für die Weinherstellung.",
    "preis": 25,
    "anzahl": 15,
    "verfuegbar": 12,
    "ablaufdatum": "2026-11-30",
    "anbieterName": "Kees van Bommelding",
    "anbieterAdresse": "Weinstraße 8, Traubendorf"
  }
]
`),
)