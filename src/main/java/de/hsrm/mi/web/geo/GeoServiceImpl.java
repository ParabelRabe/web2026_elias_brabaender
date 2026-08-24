package de.hsrm.mi.web.geo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeoServiceImpl implements GeoService{

    Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);
    RestClient restClient = RestClient.create("https://nominatim.openstreetmap.org");

    @Override
    public List<GeoAdresse> findeAdressen(String such) {
        if(such == null){
            logger.error("such ist null");
            return new ArrayList<>();
        }
        else{
            List<GeoAdresse> ergebnis = restClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/search")
                .queryParam("countrycodes", "de")
                .queryParam("q", such)
                .queryParam("format", "json")
                .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
            for(GeoAdresse g : ergebnis){
                if(g.addresstype().equals("country") || g.addresstype().equals("state") || g.addresstype().equals("region") || g.addresstype().equals("postcode")){
                    ergebnis.remove(g);
                }
            }
            return ergebnis;
        }
    }
    
}
