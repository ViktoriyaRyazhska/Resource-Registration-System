package org.registrator.community.enumeration;

public enum UIMessages {
    WRONG_INPUT("msg.batchops.wrongInput"),
    IS_ADMIN("msg.batchops.cantChangeAdmins"),
    CANT_CHANGE_SELF("msg.batchops.cantChangeOwnState"),
    DIFFIRENT_TCS("msg.batchops.moreThenOneTC"),
    CHANGES_ACCEPTED("msg.batchops.changesaccepted");

    private UIMessages(final String message){
        value = message;
    }
    private final String value;
    
    public String getMessage(){
        return value;
    }
    @Override
    public String toString(){
        return value;
    }
}
