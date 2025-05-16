package main;

import lipid.*;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Let's initialize the program");

        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();
        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        try {

            //DONE > TODO INTRODUCE THE CODE IF DESIRED TO INSERT FACTS AND TRIGGER RULES
            // DONE > TODO INTRODUCE THE QUERIES IF DESIRED

            Peak mH = new Peak(700.500, 100000.0); // [M+H]+
            Peak mNa = new Peak(722.482, 80000.0);  // [M+Na]+
            Peak mhH2O = new Peak(682.4894, 70000.0); // [M+H–H2O]+

            Lipid lipid1 = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);
            Annotation annotation1 = new Annotation(lipid1, mNa.getMz(), mNa.getIntensity(), 6.5, IoniationMode.POSITIVE, Set.of(mH, mNa));
            annotation1.detectAdductByPairComparison(0.001);
            System.out.println("Inferred adduct of annotation 1: "+annotation1.getAdduct());

            Lipid lipid2 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PI, 54, 0);
            Annotation annotation2 = new Annotation(lipid2, mH.getMz(), mH.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(mH, mhH2O));
            annotation2.detectAdductByPairComparison(0.001);
            System.out.println("Inferred adduct of annotation 2: "+annotation2.getAdduct());

            LOG.info("Insert data");
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);

            LOG.info("Run query. Rules are also fired");
            instance.fire();


            System.out.println("Annotation 1 score: "+annotation1.getScore()+" Annotation 1 normalized score: "+annotation1.getNormalizedScore());
            System.out.println("Annotation 2 score: "+annotation2.getScore()+" Annotation 1 normalized score: "+annotation2.getNormalizedScore());

        } finally {
            instance.close();
        }
    }
}
