package lipid;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AditionalTest
{
    Peak mH;
    Peak mNa;
    Peak mhH2O;
    Peak singlyCharged;
    Peak doublyCharged;
    Annotation annotation;
    Lipid lipid;

    @Before
    public void setup() {
        // DONE > !! TODO Empty by now,you can create common objects for all tests.
        System.out.println("These test were conducted in order to guarantee the proper functionality of the method " +
                "responsible for the identification of the adducts");
        mH = new Peak(700.500, 100000.0); //[M+H]+
        mNa =new Peak(722.482, 80000.0); //[M+Na]+
        mhH2O = new Peak(682.4894, 70000.0);//[M+H–H2O]+
        singlyCharged = new Peak(700.500, 100000.0); //[M+H]+
        doublyCharged = new Peak(350.754, 85000.0);   //[M+2H]2+

    }
        @Test
        public void shouldDetectAdductBasedOnMzNa() {

        lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);
        double annotationRT = 6.5d;
        annotation = new Annotation(lipid, mNa.getMz(), mNa.getIntensity(), annotationRT, IoniationMode.POSITIVE, Set.of(mH, mNa));

        annotation.detectAdductByPairComparison(0.001);
        assertNotNull("[M+Na]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+Na]+", annotation.getAdduct());
        System.out.println("Inferred adduct: "+annotation.getAdduct());

        }

        @Test
        public void shouldDetectLossOfWater() {

        lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", LipidType.PE, 36, 2);
        annotation = new Annotation(lipid, mhH2O.getMz(), mhH2O.getIntensity(), 7.5d, IoniationMode.POSITIVE, Set.of(mH, mhH2O));

        annotation.detectAdductByPairComparison(0.001);
        assertNotNull("[M+H-H2O]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H-H2O]+", annotation.getAdduct());
        System.out.println("Inferred adduct: "+annotation.getAdduct());
        }

        @Test
        public void shouldDetectDoubly() {
        // Assume real M = (700.500 - 1.0073) = 699.4927
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536

        lipid = new Lipid(3, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3);
        annotation = new Annotation(lipid, doublyCharged.getMz(), doublyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));

        annotation.detectAdductByPairComparison(0.001);
        assertNotNull("[M+2H]2+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+2H]2+", annotation.getAdduct());
        System.out.println("Inferred adduct: "+annotation.getAdduct());
        }

}
