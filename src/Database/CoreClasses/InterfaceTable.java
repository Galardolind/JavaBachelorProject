package Database.CoreClasses;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface d'une table avec ses differentes fonctionnalités
 *
 * 
 */
public interface InterfaceTable {

    /**
     * charge une table complete
     *
     * @param header l'entete de la table
     * @throws IOException, ExceptionDataBase
     */
    public void load(Header header) throws ExceptionDataBase;

    /**
     * ecrit une table dans l'integralité
     *
     * @throws IOException, ExceptionDataBase
     */
    public void save() throws ExceptionDataBase;

    /**
     * recupere une ligne souhaité a partir d'un champs connu, la colone doit
     * etre indexé
     *
     *
     * @param index le champs connu
     * @return renvoie la ligne souhaité ou null si echec
     */
    public Row getRowByIndex(String index);

    /**
     * recupere toutes les lignes a partir d'un champs connu
     *
     * @param key le nom de la colone dans lequel chercher
     * @param value la valeur du champs
     * @return renvoie toutes les lignes qui match la valeur
     * @throws ExceptionDataBase
     */
    public ArrayList<Row> getRows(String key, String value) throws ExceptionDataBase;

    /**
     * Ajoute dans la table une ligne, cette methode fait aussi de l'ajout sans
     * doublon dans le cas d'une colonne indexé
     *
     * @param line la ligne a ajouté
     * @throws ExceptionDataBase
     */
    public void addRow(Row line) throws ExceptionDataBase;

    /**
     * supprime toutes les lignes a partir d'une colonne et d'une valeur
     *
     * @param key la colonne dans laquelle rechercher
     * @param value la valeur a supprimer
     * @throws ExceptionDataBase
     */
    public void delRow(String key, String value) throws ExceptionDataBase;

    /**
     * Ajoute a la table une entete contenant toutes les colonnes ainsi que
     * leurs proprietés, ne peut etre effectué qu'une fois
     *
     * @param header l'entete de la table
     * @throws ExceptionDataBase
     */
    public void setHeader(Header header) throws ExceptionDataBase;

    /**
     * renvoie le nom de la colonne qui est indexe
     *
     * @return String
     */
    public String getIndex();

    /**
     * renvoie l'entete de la table
     *
     * @return Header
     */
    public Header getHeader();

    /**
     * renvoie le nom de la table
     *
     * @return String
     */
    public String getName();

    /**
     * renvoie le type de table (JSON,CSV,DIRECTORY)
     *
     * @return String
     */
    public String getType();

    /**
     * purge la table sans faire de sauvegarde
     *
     * @throws ExceptionDataBase
     */
    public void dropTable() throws ExceptionDataBase;

    /**
     * renvoie la totalité de la table
     *
     * @return ArrayList<Row>
     */
    public ArrayList<Row> getAll();

    /**
     * classe anonyme contenant l'ensemble des donnee de la table
     */
    interface Rows {

        /**
         * ajoute une ligne dans la table
         *
         * @param r une ligne a ajouter
         * @return precedente Row
         */
        public Row addRow(Row r);
    };
}
