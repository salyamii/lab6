package server.collection_methods;

import server.CollectionAdministrator;

public class Exit extends SimpleMethod{
    public Exit(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run() {
        getAdministrator().save();
        return "Exiting the program.";
    }
}
