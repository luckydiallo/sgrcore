package bf.ibam.sgrcore.domain;

import static bf.ibam.sgrcore.domain.FiliereTestSamples.*;
import static bf.ibam.sgrcore.domain.MatiereTestSamples.*;
import static bf.ibam.sgrcore.domain.ReclamationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bf.ibam.sgrcore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReclamationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reclamation.class);
        Reclamation reclamation1 = getReclamationSample1();
        Reclamation reclamation2 = new Reclamation();
        assertThat(reclamation1).isNotEqualTo(reclamation2);

        reclamation2.setId(reclamation1.getId());
        assertThat(reclamation1).isEqualTo(reclamation2);

        reclamation2 = getReclamationSample2();
        assertThat(reclamation1).isNotEqualTo(reclamation2);
    }

    @Test
    void filiereTest() {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        Filiere filiereBack = getFiliereRandomSampleGenerator();

        reclamation.setFiliere(filiereBack);
        assertThat(reclamation.getFiliere()).isEqualTo(filiereBack);

        reclamation.filiere(null);
        assertThat(reclamation.getFiliere()).isNull();
    }

    @Test
    void matiereTest() {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        reclamation.setMatiere(matiereBack);
        assertThat(reclamation.getMatiere()).isEqualTo(matiereBack);

        reclamation.matiere(null);
        assertThat(reclamation.getMatiere()).isNull();
    }
}
