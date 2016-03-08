package com.orange.flexoffice.business.common.enums;

/**
 * EnumErrorModel enum 
 * the mapping between code and error message
 * @author oab
 */
public enum EnumErrorModel {
	ERROR_1("D001", "impossible de charger le dashboard"),
	ERROR_2("D002", "impossible de charger les statistiques sur les salles les plus utilisées"),
	ERROR_3("D003", "impossible de charger les statistiques sur le taux d'occupation par type de salles"),
	ERROR_4("U001", "impossible de charger la liste des utilisateurs"),
	ERROR_5("U002", "impossible de créer cet utilisateur"),
	ERROR_6("U003", "impossible de mettre à jour cet utilisateur"),
	ERROR_7("U004", "impossible de supprimer cet utilisateur"),
	ERROR_8("U005", "impossible de charger cet utilisateur"),
	ERROR_9("U100", "cet email existe déjà"),
	ERROR_91("U101", "email non fourni"),
	ERROR_10("S001","impossible de charger la liste des capteurs"),
	ERROR_11("S002","impossible de créer ce capteur"),
	ERROR_12("S003","impossible de mettre à jour ce capteur"),
	ERROR_13("S004","impossible de supprimer ce capteur"),
	ERROR_14("S005","impossible de charger ce capteur"),
	ERROR_15("S100","ce capteur existe déjà"),
	ERROR_16("Z001","impossible de charger la liste des zones"),
	ERROR_17("Z002","impossible de créer cette zone"),
	ERROR_18("Z003","impossible de mettre à jour cette zone"),
	ERROR_19("Z004","impossible de supprimer cette zone"),
	ERROR_20("Z005","impossible de charger cette zone"),
	ERROR_21("G001","impossible de charger la liste des passerelles"),
	ERROR_22("G002","impossible de créer cette passerelle"),
	ERROR_23("G003","impossible de mettre à jour cette passerelle"),
	ERROR_24("G004","impossible de supprimer cette passerelle"),
	ERROR_25("G005","impossible de charger cette passerelle"),
	ERROR_26("G100","cette passerelle existe déjà"),
	ERROR_27("R001","impossible de charger la liste des salles"),
	ERROR_28("R002","impossible de créer cette salle"),
	ERROR_29("R003","impossible de mettre à jour cette salle"),
	ERROR_30("R004","impossible de supprimer cette salle"),
	ERROR_31("R005","impossible de charger cette salle"),
	ERROR_32("500","une erreur métier s'est produite "),
	ERROR_33("U101","erreur dans le paramètre authorization"),
	ERROR_34("L001", "authentification impossible"),
	ERROR_35("T001", "impossible de démarrer une détection de capteurs"),
	ERROR_36("T002", "une détection de capteurs est déjà en cours"),
	ERROR_37("T003", "pas de détection de capteurs en cours"),
	ERROR_38("T004", "Le profile est incorrect !!!"),
	ERROR_39("C001","impossible de charger la liste des pays"),
	ERROR_40("C002","impossible de créer ce pays"),
	ERROR_41("C003","impossible de mettre à jour ce pays"),
	ERROR_42("C004","impossible de supprimer ce pays"),
	ERROR_43("C005","impossible de charger ce pays"),
	ERROR_44("RG001","impossible de charger la liste des régions"),
	ERROR_45("RG002","impossible de créer cette région"),
	ERROR_46("RG003","impossible de mettre à jour cette région"),
	ERROR_47("RG004","impossible de supprimer cette région"),
	ERROR_48("RG005","impossible de charger cette région"),
	ERROR_49("CT001","impossible de charger la liste des villes"),
	ERROR_50("CT002","impossible de créer cette ville"),
	ERROR_51("CT003","impossible de mettre à jour cette ville"),
	ERROR_52("CT004","impossible de supprimer cette ville"),
	ERROR_53("CT005","impossible de charger cette ville"),
	ERROR_54("B001","impossible de charger la liste des bâtiments"),
	ERROR_55("B002","impossible de créer ce bâtiment"),
	ERROR_56("B003","impossible de mettre à jour ce bâtiment"),
	ERROR_57("B004","impossible de supprimer ce bâtiment"),
	ERROR_58("B005","impossible de charger ce bâtiment");
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
