package server.collection_methods;

import server.CollectionAdministrator;

public class RemoveLowerKey extends SimpleMethod{
    public RemoveLowerKey(CollectionAdministrator administrator){
        super(administrator);
    }

    @Override
    public String run(String str) {
        getAdministrator().remove_lower_key(str);
        getAdministrator().save();
        return "Elements with lower keys were removed.";
    }
}
