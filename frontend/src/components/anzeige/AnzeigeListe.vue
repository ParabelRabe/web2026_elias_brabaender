<script setup lang="ts">
import AnzeigeListeZeile from '@/components/anzeige/AnzeigeListeZeile.vue'
import type {IAnzeigeDTD} from '@/stores/IAnzeige.ts'
    const props = defineProps<{
        anzeigen: Array<IAnzeigeDTD>
    }>()
</script>

<template>
    <div class="tabelleVue">
        <table>
            <thead>
                <tr>
                    <th>
                        <p class="messagebold">ID</p>
                    </th>
                    <th>
                        <p class="messagebold">Titel</p>
                    </th>
                    <th>
                        <p class="messagebold">Preis</p>
                    </th>
                    <th>
                        <p class="messagebold">verfügbar</p>
                    </th>
                    <th>
                        <p class="messagebold">Ablaufdatum</p>
                    </th>
                    <th>
                    </th>
                </tr>
            </thead>
            <tbody v-for="anzeige in anzeigen" :key="anzeige.id">
                <AnzeigeListeZeile :anzeige="anzeige"></AnzeigeListeZeile>
            </tbody>
        </table>
    </div>
</template>