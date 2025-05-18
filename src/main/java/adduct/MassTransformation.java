package adduct;

import lipid.IoniationMode;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MassTransformation {

// DONE > TODO create functions to transform the mass of the mzs to monoisotopic masses and vice versa.
// DONE > !! TODO METHOD

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param mz mz of the adduct
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc...)
     * @return the monoisotopic mass of the experimental mass mz with the adduct @param adduct
     */

    /*
      if Adduct is single charge the formula is M = m/z +- adductMass. Charge is 1 so it does not affect
      if Adduct is double or triple charged the formula is M =( mz - adductMass ) * charge
      if adduct is a dimer the formula is M =  (mz - adductMass) / numberOfMultimer
     */

    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct, IoniationMode ioniationMode) {
        Double massToSearch;
        int A = getMultimerFromAdduct(adduct); // "A" corresponde a el multimer. Si es un 1, no es un multimer. Si es 2 es un dimer, si es 3 es un trimer.
        int Q = getChargeFromAdduct(adduct); // "Q" corresponde a la carga.

        Map<String, Double> adductMap;
        if(ioniationMode == IoniationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        }
        else {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        }

        Double adductMass = adductMap.get(adduct);
        if (adductMass == null) {
            throw new IllegalArgumentException("Adduct not found in the list: " + adduct);
        }

        //massToSearch = (mz + adductMass) * Q / A;
        massToSearch = (mz * Q) + adductMass / A;;
        return massToSearch;
    }


    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct
     *
     * @param monoisotopicMass of the adduct
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc...)
     * @return massToSearch of the final mz
     */


    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct, IoniationMode ioniationMode) {
        Double massToSearch;
        int A = getMultimerFromAdduct(adduct); // "A" corresponde a el multimer. Si es un 1, no es un multimer. Si es 2 es un dimer, si es 3 es un trimer.
        int Q = getChargeFromAdduct(adduct); // "Q" corresponde a la carga.

        Map<String, Double> adductMap;
        if(ioniationMode == IoniationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        }
        else {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        }

        Double adductMass = adductMap.get(adduct);
        if (adductMass == null) {
            throw new IllegalArgumentException("Adduct not found in the list: " + adduct);
        }

        massToSearch = ((monoisotopicMass* A) / Q) - adductMass;
        return massToSearch;

    }

    // DONE > !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

    /**
     *
     * @param adduct we are looking for a specific adduct pattern, we want to get the number of multimers
     * @return result of the multimer number
     */

    public static int getMultimerFromAdduct(String adduct) {
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-][^\\]]+\\](\\d*)?([+-])");
        Matcher matcher = pattern.matcher(adduct);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de aducto inválido: " + adduct);
        }

        String multimer = matcher.group(1);
        int result;
        if (multimer.isEmpty()) {
            result = 1;
        } else {
            result = Integer.parseInt(multimer);
        }

        return result;
    }

    /**
     *
     * @param adduct we are looking for a specific adduct pattern, we want to get the charge of the adduct
     * @return charge of the adduct
     */

    public static int getChargeFromAdduct(String adduct) {
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-][^\\]]+\\](\\d*)?([+-])");
        Matcher matcher = pattern.matcher(adduct);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de aducto inválido: " + adduct);
        }

        String chargeStr = matcher.group(2);
        int charge;
        if (chargeStr.isEmpty()) {
            charge = 1;
        } else {
            charge = Integer.parseInt(chargeStr);
        }

        return charge;
    }

}
