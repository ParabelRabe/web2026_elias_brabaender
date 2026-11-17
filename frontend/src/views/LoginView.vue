<template>
  <div>
    <div class="titel">
        <h1>Login</h1>
    </div>
    <div class="titel">
        <p>Username</p>
        <input type="text" v-model="userdata.username" >
    </div>
    <div class="titel">
        <p>Passwort</p>
        <input type="password" v-model="userdata.passwort" >
    </div>
    <div class="titel">
        <button type="button" v-on:click="clickLogin()">Login</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import AnzeigeListe from '@/components/anzeige/AnzeigeListe.vue'
import { storeToRefs } from 'pinia'
import { useLogin } from '@/stores/loginstore.ts'
import { useInfo } from '@/composables/useInfo.ts'
import router from '@/router'
const uselogin = useLogin()
const {loginState, logout, login} = uselogin
const userdata = reactive<{username: string, passwort: string}>({username: "", passwort: ""})
const useinfo = useInfo()
const {info, loescheInfo, setzeInfo} = useinfo
logout()
async function clickLogin(){
    try{
        const response = await fetch(`/api/anzeige/getUser/${userdata.username}`, {
            method: 'GET',
        });
        const userExists: boolean = await response.json();
        if (userExists) {
            if(!login(userdata.username, userdata.passwort)){
            setzeInfo("Jammer - der Login-Versuch war zwar nicht erfolgreich, aber erfolglos")
            userdata.passwort = ""
            }
            else{
                loescheInfo()
                userdata.username = ""
                userdata.passwort = ""
                router.push("/anzeige")
            }
        } 
        else {
            setzeInfo("Jammer - der Nutzer ist nicht vergeben")
            userdata.passwort = ""
        }
    } catch (error) {
        console.error(error);
        setzeInfo("Stornierung fehlgeschlagen.")
    }
}
</script>

<style scoped></style>
