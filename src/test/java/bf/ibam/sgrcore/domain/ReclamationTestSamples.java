package bf.ibam.sgrcore.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReclamationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reclamation getReclamationSample1() {
        return new Reclamation()
            .id(1L)
            .nomReclamant("nomReclamant1")
            .prenomReclamant("prenomReclamant1")
            .niveauEtude("niveauEtude1")
            .objetReclamation("objetReclamation1")
            .motifReclamation("motifReclamation1")
            .justificatif("justificatif1")
            .etatReclamation("etatReclamation1");
    }

    public static Reclamation getReclamationSample2() {
        return new Reclamation()
            .id(2L)
            .nomReclamant("nomReclamant2")
            .prenomReclamant("prenomReclamant2")
            .niveauEtude("niveauEtude2")
            .objetReclamation("objetReclamation2")
            .motifReclamation("motifReclamation2")
            .justificatif("justificatif2")
            .etatReclamation("etatReclamation2");
    }

    public static Reclamation getReclamationRandomSampleGenerator() {
        return new Reclamation()
            .id(longCount.incrementAndGet())
            .nomReclamant(UUID.randomUUID().toString())
            .prenomReclamant(UUID.randomUUID().toString())
            .niveauEtude(UUID.randomUUID().toString())
            .objetReclamation(UUID.randomUUID().toString())
            .motifReclamation(UUID.randomUUID().toString())
            .justificatif(UUID.randomUUID().toString())
            .etatReclamation(UUID.randomUUID().toString());
    }
}
