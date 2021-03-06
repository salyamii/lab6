package server.collection_methods;

import server.CollectionAdministrator;

public class RemoveGreater extends SimpleMethod{
    public RemoveGreater(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run(String str) {
        getAdministrator().remove_greater(str);
        getAdministrator().save();
        return "Cities with greater population were removed.";
    }
}
