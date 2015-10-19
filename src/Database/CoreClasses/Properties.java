package Database.CoreClasses;

/**
 * Proprieté d'une colonnes, dont peut etre specifié si la colonne doit etre
 * indexé si les information dedans provienne d'une autre table, cle etrangere.
 *
 * 
 */
public class Properties {

    /**
     * Type de la colonne
     */
    private Type type;
    /**
     * specifie si la colonne est indexé
     */
    private boolean index;
    /**
     * specifie si la colonne est une cle etrangere
     */
    private String foreignKey;

    /**
     * creer des proprietés
     *
     * @param type le type de la BD
     * @param index booleen qui represente la "clef primaire"
     * @param foreignKey le nom de la table vers laquelle point la clef
     * etrangere
     */
    public Properties(Type type, boolean index, String foreignKey) {
        this.type = type;
        this.index = index;
        if (foreignKey == null || foreignKey.isEmpty()) {
            this.foreignKey = "null";
        } else {
            this.foreignKey = foreignKey;
        }
    }

    /**
     * creer des proprietés avec foreingKey a null
     *
     * @param type le type de la BD
     * @param index booleen qui represente la "clef primaire"
     */
    public Properties(Type type, boolean index) {
        this(type, index, null);
    }

    /**
     * creer des proprietés avec foreingKey et index a null
     *
     * @param type le type de la BD
     */
    public Properties(Type type) {
        this(type, false, null);
    }

    private Properties() {
    }

    /**
     * Valide les contraite de type sur une valeur
     *
     * @param value la valeur a valider
     * @return renvoie vrai pour le moment
     */
    public boolean validConstraint(String value) {
        return type.validConstraint(value);
    }

    /**
     * renvoie si la colonnes est un index
     *
     * @return vrai si la colonne est indexé
     */
    public boolean isIndex() {
        return index;
    }

    /**
     * renvoie le type de la colonne
     *
     * @return Type
     */
    public Type getType() {
        return type;
    }

    /**
     * renvoie le nom de la table etrangere
     *
     * @return String
     */
    public String getForeignKey() {
        return foreignKey;
    }
}
