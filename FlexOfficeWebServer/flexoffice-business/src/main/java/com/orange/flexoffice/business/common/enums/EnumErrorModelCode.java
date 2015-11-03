package com.orange.flexoffice.business.common.enums;

public enum EnumErrorModelCode {

    D001, // "impossible de charger le dashboard"
    D002, // "impossible de charger les statistiques sur les salles les plus utilisées"
    D003, // "impossible de charger les statistiques sur le taux d'occupation par type de salles"
    U001, // "impossible de charger la liste des utilisateurs"
    U002, // "impossible de créer cet utilisateur ({{email}})"
    U003, // "impossible de mettre à jour cet utilisateur ({{email}})"
    U004, // "impossible de supprimer cet utilisateur ({{email}})"
    U005, // "impossible de charger cet utilisateur ({{email}})"
    U100, // "cet email existe déjà"
    S001, // "impossible de charger la liste des capteurs"
    S002, // "impossible de créer ce capteur {{id}}",
    S003, // "impossible de mettre à jour ce capteur {{id}}",
    S004, // "impossible de supprimer ce capteur ({{id}})",
    S005, // "impossible de charger ce capteur ({{id}})",
    S100, // "ce capteur existe déjà",
    Z001, // "impossible de charger la liste des zones",
    Z002, // "impossible de créer cette zone ({{name}})",
    Z003, // "impossible de mettre à jour cette zone ({{name}})",
    Z004, // "impossible de supprimer cette zone ({{name}})",
    Z005, // "impossible de charger cette zone ({{name}})",
    G001, // "impossible de charger la liste des passerelles",
    G002, // "impossible de créer cette passerelle ({{name}})",
    G003, // "impossible de mettre à jour cette passerelle ({{name}})",
    G004, // "impossible de supprimer cette passerelle ({{name}})",
    G005, // "impossible de charger cette passerelle ({{name}})",
    G100, // "cette passerelle existe déjà",
    R001, // "impossible de charger la liste des salles",
    R002, // "impossible de créer cette salle ({{name}})",
    R003, // "impossible de mettre à jour cette salle ({{name}})",
    R004, // "impossible de supprimer cette salle ({{name}})",
    R005; // "impossible de charger cette salle ({{name}})"


    public String value() {
        return name();
    }

    public static EnumErrorModelCode fromValue(String v) {
        return valueOf(v);
    }

}
