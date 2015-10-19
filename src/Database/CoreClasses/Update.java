package Database.CoreClasses;

/**
 * Cette commande mets a jour un champs dans une table.
 *
 * 
 */
public class Update extends AbstractCommand {

    public Update(Request request, DataBase db) {
        super(request, db);
    }

    @Override
    public InterfaceCommand startCommand(Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InterfaceCommand endCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InterfaceCommand startField(Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InterfaceCommand endField() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InterfaceCommand name(Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InterfaceCommand value(Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean initCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
