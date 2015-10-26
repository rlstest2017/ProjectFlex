package com.orange.flexoffice.business.gatewayapi.core.hdModels;

import java.util.Comparator;

/**
 * 
 * RecoComparator : implements Comparator<Recommendation>
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * Comparator of recommendations
 * 
 * Orders the recommendations
 *  1) in descending order of their predicted ratings
 *  2) in descending order of their trust measures
 * 	3) in ascending order of their identifiers
 * 
 * Note: this comparator imposes orderings that are inconsistent with equals.
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Comparateur de recommandations
 * 
 * Classe les recommandations
 *  1) par ordre décroissant de note prédite
 *  2) par ordre décroissant de mesure de confiance
 * 	3) par ordre croissant des identifiants
 *
 */

public class RecoComparator implements Comparator<Recommendation> {

	public int compare(Recommendation r1, Recommendation r2) {
		
		if (r1.getRating() > r2.getRating()) {
			return -1;
		}
		else if (r1.getRating() < r2.getRating()) {
			return 1;
		}
		else if (r1.getTrustMeasure() > r2.getTrustMeasure()) {
			return -1;
		}
		else if (r1.getTrustMeasure() < r2.getTrustMeasure()) {
			return 1;
		}
		else if (r1.getID() > r2.getID()) {
			return 1;
		}
		else if (r1.getID() < r2.getID()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
