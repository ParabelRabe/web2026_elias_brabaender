<template>
  <div>
    <div class="titel">
      <h1>Unsere aktuellen Anzeigen</h1>
    </div>
    <div class="titel">
      <input type="text" v-model="suche.inhalt" >
      <button type="button" v-on:click="resetSuche()">reset</button>
    </div>
    <div>
      <AnzeigeListe :anzeigen="gefilterteAnzeigen"></AnzeigeListe>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import AnzeigeListe from '@/components/anzeige/AnzeigeListe.vue'
import type {IAnzeigeDTD} from '@/stores/IAnzeige.ts'
import { useAnzeigeStore } from '@/stores/anzeigestore.ts'
import { storeToRefs } from 'pinia'
const anzeigeStore = useAnzeigeStore()
const {anzeigedata, updateAnzeigeListe} = anzeigeStore
if(!anzeigedata.ok){
  updateAnzeigeListe()
}
const suche = reactive<{inhalt: string}>({inhalt: ""})
function resetSuche(){
  suche.inhalt = ""
}
const gefilterteAnzeigen = computed(() => {
  if (suche.inhalt == '') {
    return anzeigedata.anzeigeliste
  }
  const sucheklein = suche.inhalt.toLowerCase()
  
  return anzeigedata.anzeigeliste.filter((anzeige: IAnzeigeDTD) => {
    return anzeige.titel.toLowerCase().includes(sucheklein)
  })
})
</script>

<style scoped></style>
