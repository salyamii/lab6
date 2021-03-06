package server.collection_methods;

import server.CollectionAdministrator;

public class Clear extends SimpleMethod{
    public Clear(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run() {
        getAdministrator().clear();
        getAdministrator().save();
        return "Collection is cleared.";
    }
}
