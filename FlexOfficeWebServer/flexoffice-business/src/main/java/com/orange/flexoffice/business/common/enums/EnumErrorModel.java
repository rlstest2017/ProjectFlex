package com.orange.flexoffice.business.common.enums;

/**
 * EnumErrorModel enum 
 * the mapping between code and error message
 * @author oab
 */
public enum EnumErrorModel {
    /**
     *  
     */
  
	ERROR_1("D001", "impossible de charger le dashboard"),
	ERROR_2("D002", "impossible de charger les statistiques sur les salles les plus utilisées"),
	ERROR_3("D003", "impossible de charger les statistiques sur le taux d'occupation par type de salles"),
	ERROR_4("U001", "impossible de charger la liste des utilisateurs"),
	ERROR_5("U002", "impossible de créer cet utilisateur" /*({{email}})*/),
	ERROR_6("U003", "impossible de mettre à jour cet utilisateur" /*({{email}})*/),
	ERROR_7("U004", "impossible de supprimer cet utilisateur" /*({{email}})*/),
	ERROR_8("U005", "impossible de charger cet utilisateur" /*({{email}})*/),
	ERROR_9("U100", "cet email existe déjà"),
	ERROR_10("S001","impossible de charger la liste des capteurs"),
	ERROR_11("S002","impossible de créer ce capteur" /* {{id}}*/),
	ERROR_12("S003","impossible de mettre à jour ce capteur" /* {{id}}*/),
	ERROR_13("S004","impossible de supprimer ce capteur" /* {{id}}*/),
	ERROR_14("S005","impossible de charger ce capteur" /* {{id}}*/),
	ERROR_15("S100","ce capteur existe déjà"),
	ERROR_16("Z001","impossible de charger la liste des zones"),
	ERROR_17("Z002","impossible de créer cette zone" /*({{name}})*/),
	ERROR_18("Z003","impossible de mettre à jour cette zone" /*({{name}})*/),
	ERROR_19("Z004","impossible de supprimer cette zone" /*({{name}})*/),
	ERROR_20("Z005","impossible de charger cette zone" /*({{name}})*/),
	ERROR_21("G001","impossible de charger la liste des passerelles"),
	ERROR_22("G002","impossible de créer cette passerelle" /*({{name}})*/),
	ERROR_23("G003","impossible de mettre à jour cette passerelle" /*({{name}})*/),
	ERROR_24("G004","impossible de supprimer cette passerelle" /*({{name}})*/),
	ERROR_25("G005","impossible de charger cette passerelle" /*({{name}})*/),
	ERROR_26("G100","cette passerelle existe déjà"),
	ERROR_27("R001","impossible de charger la liste des salles"),
	ERROR_28("R002","impossible de créer cette salle" /*({{name}})*/),
	ERROR_29("R003","impossible de mettre à jour cette salle" /*({{name}})*/),
	ERROR_30("R004","impossible de supprimer cette salle" /*({{name}})*/),
	ERROR_31("R005","impossible de charger cette salle" /*({{name}})*/),
	ERROR_32("500","");
        
     /**
     * status code
     */
    private final String code;
    
    /**
     * status value
     */
    private final String value;
        
    /**  
     * @param c code
     * @param v value
     */
    EnumErrorModel(final String c, final String v) {
        code = c;
        value = v;
    }

        /**
     * @return the code
     */
    public String code() {
        return code;
    }
        
    /**
     * @return the value
     */
    public String value() {
        return value;
    }

    /**
     * @param v the value
     * @return the corresponding enum by value
     */
    public static EnumErrorModel fromValue(String v) {
        for (EnumErrorModel enumeration : EnumErrorModel.values()) {
            if (enumeration.value.equals(v)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
