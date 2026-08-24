package de.hsrm.mi.web.projekt.messaging;

public class FrontendNachrichtEvent{
    private Event typ;
    private long id;
    private Operation art;
    public FrontendNachrichtEvent(Event typ, long id, Operation art){
        this.typ=  typ;
        this.id = id;
        this.art = art;

    }
    public Event getTyp() {
        return typ;
    }
    public void setTyp(Event typ) {
        this.typ = typ;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Operation getArt() {
        return art;
    }
    public void setArt(Operation art) {
        this.art = art;
    }
    
}
