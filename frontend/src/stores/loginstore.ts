import { reactive, readonly, computed, ref } from "vue";
import { defineStore } from "pinia";

export const useLogin = defineStore("loginstore", () => {
    const loginState = reactive<{username: string, loggedIn: boolean}>({username: "", loggedIn: false})

    function logout(){
        loginState.username = ""
        loginState.loggedIn = false
    }
    function login(username: string, passwort: string){
        if(username != "" && passwort != ""){
            loginState.username = username
            loginState.loggedIn = true
            return true
        }
        else{
            logout()
            return false
        }
    }
    return {
        loginState,
        logout,
        login
    }
});