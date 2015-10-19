package Database.CoreClasses;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Le systeme de commande a été concu pour etre combiné a un parseur, dans notre
 * cas Jflex + BYACC/J.
 *
 * Exemple d'utilisation :
 * <pre>{@code
 * InterfaceCommand c = new Command(); /* Create(), Update(), Search() ou Add() c.initCommand() *\/
 * c.initCommand();
 * c.startCommand(arg);
 *      c.startField(arg);
 *          c.name(nom1);
 *          c.value(valeur1);
 *      c.endField();
 *      c.startField(arg);
 *          c.name(nom2);
 *          c.value(valeur2);
 *      c.endField();
 *      c.startField(arg);
 *          c.name(nom3);
 *          c.value(valeur3);
 *      c.endField();
 * c.endCommand();
 * }</pre>
 *
 * Ou :
 * <pre>{@code
 * InterfaceCommand c = new Command(); /* Create(), Update(), Search() ou Add() c.initCommand() *\/
 * c.initCommand();
 * c.startCommand(arg)
 *      .startField(arg)
 *          .name(nom1)
 *          .value(valeur1)
 *      .endField()
 *      .startField(arg)
 *          .name(nom2)
 *          .value(valeur2)
 *      .endField()
 *      .startField(arg)
 *          .name(nom3)
 *          .value(valeur3)
 *      .endField()
 * .endCommand();
 * }</pre>
 *
 * 
 */
public interface InterfaceCommand {

    /**
     * Fonction permetant de reinitialiser la procedure des commandes
     *
     * @return vrai si reussi, faux sinon
     * @throws ExceptionDataBase
     */
    public boolean initCommand() throws ExceptionDataBase;

    /**
     * Demarre la commande
     *
     * @param arg le paramettre de la commande
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand startCommand(Object arg) throws ExceptionDataBase;

    /**
     * Met fin a la commande et l'execute;
     *
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand endCommand() throws ExceptionDataBase;

    /**
     * Demarre un champs, ajout, suppression, mise a jour, recherche.
     *
     * @param arg Le paramettre du champs
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand startField(Object arg) throws ExceptionDataBase;

    /**
     * Termine un champs
     *
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand endField() throws ExceptionDataBase;

    /**
     * Le nom du champs
     *
     * @param arg Le nom du champs
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand name(Object arg) throws ExceptionDataBase;

    /**
     * La valeur du champs
     *
     * @param arg La valeur du champs
     * @return renvoie this si reussi, null sinon
     * @throws ExceptionDataBase
     */
    public InterfaceCommand value(Object arg) throws ExceptionDataBase;
}
