package lipid;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class AdductDetectionTest {
    // DONE > !!TODO For the adduct detection both regular algorithms or drools can be used as far the tests are passed.

    @Before
    public void setup() {
        // DONE > !! TODO Empty by now,you can create common objects for all tests.
        System.out.println("Let's initialize the test");

    }

    @Test
    public void shouldDetectAdductBasedOnMzDifference() {
        // Given two peaks with ~21.98 Da difference (e.g., [M+H]+ and [M+Na]+)
        // Assume real M = (700.500 - 1.0073) = 699.492724
        // So [M+Na]+ = (M + 22.989218) = 722.482
        Peak mH = new Peak(700.500, 100000.0); // [M+H]+
        Peak mNa = new Peak(722.482, 80000.0);  // [M+Na]+
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);

        double annotationMZ = 700.499999d;
        double annotationIntensity = 80000.0;
        double annotationRT = 6.5d;
        Annotation annotation = new Annotation(lipid, annotationMZ, annotationIntensity, annotationRT, IoniationMode.POSITIVE, Set.of(mH, mNa));

        annotation.detectAdductByPairComparison(0.001);//We have given the initial tolerance 0.001

        // Then we should call the algorithmic/knowledge system rules fired to detect the adduct and Set it!
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
        System.out.println("Inferred adduct: "+annotation.getAdduct());
    }


    @Test
    public void shouldDetectLossOfWaterAdduct() {
        Peak mh = new Peak(700.500, 90000.0);// [M+H]+
        Peak mhH2O = new Peak(682.4894, 70000.0);// [M+H–H2O]+, ~18.0106 Da less

        Lipid lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", LipidType.PE, 36, 2);
        Annotation annotation = new Annotation(lipid, mh.getMz(), mh.getIntensity(), 7.5d, IoniationMode.POSITIVE, Set.of(mh, mhH2O));

        annotation.detectAdductByPairComparison(0.001);
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
        System.out.println("Inferred adduct: "+annotation.getAdduct());
    }

    @Test
    public void shouldDetectDoublyChargedAdduct() {
        // Assume real M = (700.500 - 1.0073) = 699.4927
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536
        // So [M+2H]2+ = (M / 2) + 2.0146 = 351.760914 With the original formula
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.754, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3);
        Annotation annotation = new Annotation(lipid, singlyCharged.getMz(), singlyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));

        annotation.detectAdductByPairComparison(0.001);
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());

        System.out.println("Inferred adduct: "+annotation.getAdduct());
    }


}
