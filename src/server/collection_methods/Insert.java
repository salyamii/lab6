package server.collection_methods;

import com.sun.org.apache.xpath.internal.SourceTree;
import server.CollectionAdministrator;
import server.data.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class Insert extends SimpleMethod{
    public Insert(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run(String str) {
        try{
            XmlMapper xmlMapper = new XmlMapper();
            City city = xmlMapper.readValue(str, City.class);
            city.setId(getAdministrator().receiveID());
            city.setCreationDate(getAdministrator().receiveCreationDate());
            getAdministrator().getCities().put(city.getId(), city);
            getAdministrator().save();
            return "A new city was inserted successfully.";
        }
        catch (Exception e){
            System.out.println("Incorrect deserializing.");
        }
        return null;
    }
}
