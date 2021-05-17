package server.collection_methods;

import server.CollectionAdministrator;
import server.data.City;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class CountLessThanEstablishmentDate extends SimpleMethod{
    public CountLessThanEstablishmentDate(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run(String str) {
        LocalDate establishmentDate;
        String date = str;
        try{
            establishmentDate = LocalDate.parse(date, CollectionAdministrator.formatter);
            int counter = 0;
            for(Map.Entry<Long, City> city : getAdministrator().getCities().entrySet()){
                if(city.getValue().getEstablishmentDate().isBefore(establishmentDate)){
                    counter++;
                }
            }
            return String.valueOf(counter) + "cities.";
        }
        catch (DateTimeParseException dateTimeParseException){
            return "Data is invalid.";
        }
    }
}
