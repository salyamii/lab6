package server.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "governor")
@XmlAccessorType(XmlAccessType.NONE)
public class HumanForParsing {
    @XmlElement
    private String birthday;

    /** Constructor for Human
     * @param birthday - birthday of a human
     */
    public HumanForParsing(String birthday){
        this.birthday = birthday;
    }

    public HumanForParsing(){}

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return birthday;
    }
}
