package server.collection_methods;

import client.CityForParsing;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.CollectionAdministrator;
import server.data.City;
import server.data.Human;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateID extends SimpleMethod{
    public UpdateID(CollectionAdministrator administrator){
        super(administrator);
    }

    public static final String DATE_FORMATTER = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    @Override
    public String run(String str){
        try{
            XmlMapper xmlMapper = new XmlMapper();
            CityForParsing cityForParsing = xmlMapper.readValue(str, CityForParsing.class);
            if(getAdministrator().getCities().containsKey(cityForParsing.getId())) {
                City oldCity = getAdministrator().getCities().get(cityForParsing.getId());
                oldCity.setName(cityForParsing.getName());
                oldCity.setCoordinates(cityForParsing.getCoordinates());
                oldCity.setArea(cityForParsing.getArea());
                oldCity.setPopulation(cityForParsing.getPopulation());
                oldCity.setMetersAboveSeaLevel(cityForParsing.getMetersAboveSeaLevel());
                oldCity.setEstablishmentDate(LocalDate.parse(cityForParsing.getEstablishmentDate(), formatter));
                oldCity.setTelephoneCode(cityForParsing.getTelephoneCode());
                oldCity.setClimate(cityForParsing.getClimate());
                oldCity.setGovernor(new Human(LocalDateTime.parse(cityForParsing.getGovernor().getBirthday(), dateTimeFormatter)));
                getAdministrator().getCities().put(oldCity.getId(), oldCity);
                getAdministrator().save();
                return "City was updated.";
            }
            else{
                return "You can't update nonexistent city.";
            }
        }
        catch (Exception exception){
            //exception.printStackTrace();
            return "Incorrect deserializing.";
        }

    }
}
