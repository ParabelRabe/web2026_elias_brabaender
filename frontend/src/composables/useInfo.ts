import { computed, reactive, readonly } from 'vue'
const state = reactive({info: "String"})
export function useInfo(){

    function loescheInfo(){
        state.info = ""
    }

    function setzeInfo(msg: string){
        state.info = msg
    }
    
    return {
        info: computed(()=> readonly(state).info),
        loescheInfo,
        setzeInfo
    }
}