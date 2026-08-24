<script setup lang="ts">
  import { computed, ref, reactive } from "vue";
  import AnzeigeListeView from '@/views/AnzeigeListeView.vue'
  import LoginView from '@/views/LoginView.vue'
  import { useInfo } from '@/composables/useInfo.ts'
  import { useLogin } from '@/stores/loginstore.ts'
import router from "./router";
  const uselogin = useLogin()
  const {loginState, logout, login} = uselogin
  const {info, loescheInfo, setzeInfo} = useInfo()
  setzeInfo("Beachten Sie unsere aktuellen Anzeigen mit Sonderangeboten zur Ball-WM")
  function rufeAuf(ziel: string){
    router.push(ziel)
  }
</script>

<template>
    <header>
        <div class="kopf">
            <h1>Kleinstanzeigen</h1>
            <a v-if="loginState.loggedIn" v-on:click="rufeAuf('/anzeige')" class="benutzerListSeite">Anzeige</a>
            <div class="leere"></div>
            <div class="sprachumstellung">
                <a v-on:click="rufeAuf('/login')" class="benutzerListSeite">Login</a>
                <div class="leeredrei"></div>
            </div>
        </div>
        <div class="messageContainer" v-if="info != ''">
            <div class="message">
            <div class="messages">
                <span class="messagebold">Info</span>
                <p>{{info}}</p>
            </div>
            <button type="button" class="xbutton" v-on:click="loescheInfo()">x</button>
            </div>
        </div>
    </header>
    <RouterView></RouterView>
    <footer>
        <div class="fussangaben">
            <p v-if="loginState.loggedIn">{{loginState.username}}</p>
        </div>
    </footer>
</template>

<style scoped>
</style>
