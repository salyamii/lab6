package server.collection_methods;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.CollectionAdministrator;
import server.data.City;

import java.io.IOException;

public class UpdateID extends SimpleMethod{
    public UpdateID(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run(String str){
        try{
            XmlMapper xmlMapper = new XmlMapper();
            City city = xmlMapper.readValue(str, City.class);
            if(getAdministrator().getCities().containsKey(city.getId())) {
                city.setCreationDate(getAdministrator().receiveCreationDate());
                getAdministrator().getCities().put(city.getId(), city);
                getAdministrator().save();
                return "City was updated.";
            }
            else{
                return "You can't update nonexistent city.";
            }
        }
        catch (Exception exception){
            return "Incorrect deserializing.";
        }

    }
}
