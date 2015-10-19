package Database.CoreClasses;

/**
 * L'enum specifie le type d'une colonne. Pour le moment les colonnes peuvent
 * contenir des entiers, des chaine de caractere ou des textes complet.
 *
 * 
 */
public enum Type {

    INTEGER("INTEGER"),
    STRING("STRING"),
    TEXT("TEXT");
    /**
     * type de l'enumeration
     */
    private String type;

    /**
     * choix de l'enumeration
     *
     * @param type le type de l'enum
     */
    private Type(String type) {
        this.type = type;
    }

    /**
     * renvoie si la valeur respecte les contrainte impose
     *
     * @param value la valeur a tester
     * @return vrai si la valeur est correct, faux sinon
     */
    public boolean validConstraint(String value) {

        switch (type) {
            case "INTEGER":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            case "STRING":
                return true;
            case "TEXT":
                return true;
            default:
                return false;
        }

    }
}
