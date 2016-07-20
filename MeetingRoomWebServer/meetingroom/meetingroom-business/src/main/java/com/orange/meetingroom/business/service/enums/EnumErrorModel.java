package com.orange.meetingroom.business.service.enums;

/**
 * EnumErrorModel enum 
 * the mapping between code and error message
 * @author oab
 */
public enum EnumErrorModel {
	ERROR_1("500","une erreur métier s'est produite"),
	ERROR_2("A001","impossible de charger cet agent"),
	ERROR_3("A002","cet agent n'est pas associé à une salle"),
	ERROR_4("D001","impossible de charger cet écran"),
	ERROR_5("M001","la réservation a échoué "/* le créneau horaire est refusé ou erreur serveur*/),
	ERROR_6("M002","l'annulation de la réservation a échoué"),
	ERROR_7("M003","la confirmation de la réservation a échoué"),
	ERROR_8("M004","impossible de charger cette salle"/*meetingRoomExternalId not found, It doesn't exist or connexion problem to Exchange Server*/),
	ERROR_9("M005","une erreur serveur distant s'est produite"/*errorFlag=true in PHP server return*/),
	ERROR_10("M006","une erreur serveur s'est produite"/*no rooms or at least one roomId is not found in exchange server or the RoomGroupID (config xml file referenced) is wrong*/),
	ERROR_11("M007","les dates sont en dehors de l'intervalle autorisé");
	
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
