<script setup lang="ts">
import { computed, ref, reactive } from "vue";
import type {IAnzeigeDTD} from '@/stores/IAnzeige.ts'
import { useLogin } from '@/stores/loginstore.ts'
import { useInfo } from '@/composables/useInfo.ts'
    const {info, loescheInfo, setzeInfo} = useInfo()
    const feldan = ref(false)
    const props = defineProps<{
        anzeige: IAnzeigeDTD
    }>()
    const loginStore = useLogin()
    function addOne(anzeige: IAnzeigeDTD){
        anzeige.anzahl = anzeige.anzahl + 1
    }
    function zeigeDetails(){
        if(feldan.value){
            feldan.value = false
        }
        else{
            feldan.value = true
        }
    }
    function openKarte(anzeige: IAnzeigeDTD){
        const uri = "https://nominatim.openstreetmap.org/ui/search.html?q="
        const encodeuri = encodeURI(anzeige.anbieterAdresse)
        window.open(uri + encodeuri, "_blank")
    }
    async function stornieren(anzeige: IAnzeigeDTD){
        const loginName = loginStore.loginState.username;

        if (!loginName) {
            setzeInfo("Bitte loggen Sie sich zuerst ein!")
            return;
        }

        try {
            const response = await fetch(`/api/anzeige/${anzeige.id}/stornieren/${loginName}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }); 

            if (!response.ok) {
                const errorText = await response.text();
                //setzeInfo('Stornierung fehlgeschlagen: ${errorText || response.statusText}');
                setzeInfo('Sie haben nichts zum Stornieren');
            }
            else{
                loescheInfo();
            }
        } catch (error) {
            console.error(error);
            setzeInfo("Stornierung fehlgeschlagen.")
        }
    }

    async function bestellen(anzeige: IAnzeigeDTD){
        const loginName = loginStore.loginState.username;

        if (!loginName) {
            setzeInfo("Bitte loggen Sie sich zuerst ein!")
            return;
        }

        try {
            const response = await fetch(`/api/anzeige/${anzeige.id}/bestellen`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ loginName: loginName })
            });

            if (!response.ok) {
                const errorText = await response.text();
                //setzeInfo('Bestellung fehlgeschlagen: ${errorText || response.statusText}');
                setzeInfo("Sie haben bereits bestellt oder versuchen Ihr eigenes Produkt zu bestellen oder sind kein registrierter Nutzer");
            }
            else{
                loescheInfo();
            }
        } catch (error) {
            console.error(error);
            setzeInfo("Bestellen fehlgeschlagen.")
        }
    }

</script>

<template>
    <tr>
        <td>{{anzeige.id}}</td>
        <td>{{anzeige.titel}}</td>
        <td>{{anzeige.preis}}</td>
        <td>{{anzeige.verfuegbar}} von {{anzeige.anzahl}}</td>
        <td>{{anzeige.ablaufdatum}}</td>
        <td class="listbuttons">
            <button type="button" @click="zeigeDetails()">+</button>
        </td>
    </tr>
    <tr v-if="feldan">
        <td></td>
        <td colspan="4" class="details">
            <div>
                <p>
                    von {{anzeige.anbieterName}},
                </p>
                <p>
                    aus {{anzeige.anbieterAdresse}}<a @click="openKarte(anzeige)">[Karte]</a> ,
                </p>
                <p>
                    {{anzeige.beschreibung}}
                </p>
            </div>
        </td>
    </tr>
    <tr v-if="feldan">
        <td></td>
        <td colspan="4">
            <div>
                <button v-if="anzeige.verfuegbar > 0" @click="bestellen(anzeige)">bestellen</button>
                <button v-if="anzeige.verfuegbar < anzeige.anzahl" @click="stornieren(anzeige)">stornieren</button>
            </div>
        </td>
    </tr>
</template>