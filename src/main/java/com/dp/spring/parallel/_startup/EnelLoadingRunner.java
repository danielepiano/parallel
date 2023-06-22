package com.dp.spring.parallel._startup;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EnelLoadingRunner implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final HeadquartersRepository headquartersRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkplaceRepository workplaceRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(enel().getName())) {
            return;
        }

        // Company and related structure definition
        var enel = this.companyRepository.save(enel());
        var enelmilano = this.headquartersRepository.save(enelmilano(enel));
        var meetingroommilano1 = this.workspaceRepository.save(ws_meetingroom(1, enelmilano));
        this.workplaceRepository.saveAll(wp(meetingroommilano1, WorkplaceType.DESK, "D", 5));
        var meetingroommilano2 = this.workspaceRepository.save(ws_meetingroom(2, enelmilano));
        this.workplaceRepository.saveAll(wp(meetingroommilano2, WorkplaceType.DESK, "D", 5));
        var openspacemilano = this.workspaceRepository.save(ws_openspace(enelmilano));
        this.workplaceRepository.saveAll(wp(openspacemilano, WorkplaceType.DESK, "D", 20));

        var enelnapoli = this.headquartersRepository.save(enelnapoli(enel));
        var meetingroomnapoli1 = this.workspaceRepository.save(ws_meetingroom(1, enelnapoli));
        this.workplaceRepository.saveAll(wp(meetingroomnapoli1, WorkplaceType.DESK, "D", 5));
        var meetingroomnapoli2 = this.workspaceRepository.save(ws_meetingroom(2, enelnapoli));
        this.workplaceRepository.saveAll(wp(meetingroomnapoli2, WorkplaceType.DESK, "D", 5));
        var openspacenapoli = this.workspaceRepository.save(ws_openspace(enelnapoli));
        this.workplaceRepository.saveAll(wp(openspacenapoli, WorkplaceType.DESK, "D", 20));

        // User definition
        var cm1 = this.userRepository.save(cm1(enel));
        var hqrmilano = this.userRepository.save(hqrmilano(enelmilano));
        var hqrnapoli = this.userRepository.save(hqrnapoli(enelmilano));

    }


    Company enel() {
        return new Company()
                .setName("Enel")
                .setCity("Milano")
                .setAddress("Viale Regina Margherita, 125")
                .setPhoneNumber("+39 0664511012")
                .setDescription("Enel è una multinazionale dell'energia nata nel 1962. Oggi è la più grande azienda" +
                        " elettrica d'Italia e serve oltre 69 milioni di clienti in tutto il mondo.")
                .setWebsiteUrl("https://www.enel.it");
    }

    Headquarters enelmilano(Company enel) {
        return new Headquarters()
                .setCity("Milano")
                .setAddress("Via Giosué Carducci, 1")
                .setPhoneNumber("+39 0664511012")
                .setDescription("Enel Milano Carducci è uno spazio accogliente, moderno, informale." +
                        " Un posto in cui sentirti libero di esporre idee e creatività, far conoscere il tuo lavoro e" +
                        " stringere rapporti con altri professionisti.")
                .setCompany(enel);
    }

    Headquarters enelnapoli(Company enel) {
        return new Headquarters()
                .setCity("Napoli")
                .setAddress("Via Galileo Ferraris, 59")
                .setPhoneNumber("+39 0664511012")
                .setDescription("Enel Napoli Ferraris è uno spazio accogliente, moderno, informale." +
                        " Un posto in cui sentirti libero di esporre idee e creatività, far conoscere il tuo lavoro" +
                        " e stringere rapporti con altri professionisti.")
                .setCompany(enel);
    }

    Workspace ws_openspace(Headquarters enelmilano) {
        return new Workspace()
                .setHeadquarters(enelmilano)
                .setName("Open space")
                .setDescription("Un posto in cui sentirti libero di esporre idee e creatività, far conoscere il tuo" +
                        " lavoro e stringere rapporti con altri professionisti")
                .setType(WorkspaceType.OPEN_SPACE)
                .setFloor("P5");
    }

    Workspace ws_meetingroom(int offset, Headquarters hq) {
        return new Workspace()
                .setHeadquarters(hq)
                .setName("Meeting room " + offset)
                .setType(WorkspaceType.MEETING_ROOM)
                .setFloor("P5");
    }

    List<Workplace> wp(Workspace ws, WorkplaceType type, String prefix, int n) {
        List<Workplace> wp = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            wp.add(new Workplace()
                    .setWorkspace(ws)
                    .setName(prefix + "-" + i)
                    .setType(type)
            );
        }
        return wp;
    }


    CompanyManagerUser cm1(Company enel) {
        return CompanyManagerUser.builder()
                .firstName("Giuseppe")
                .lastName("Bianchi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3341234567")
                .city("Pomezia")
                .address("Via Giu di Lì, 73")
                .email("g.bianchi@enel.org")
                .password(this.passwordEncoder.encode("companyManager"))
                .company(enel)
                .build();
    }

    HeadquartersReceptionistUser hqrmilano(Headquarters enelmilano) {
        return HeadquartersReceptionistUser.builder()
                .firstName("Mario")
                .lastName("Rossi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3331234567")
                .city("Lodi")
                .address("Via Da Qui, 75")
                .email("m.rossi@enel.org")
                .password(this.passwordEncoder.encode("receptionist"))
                .headquarters(enelmilano)
                .build();
    }

    HeadquartersReceptionistUser hqrnapoli(Headquarters enelnapoli) {
        return HeadquartersReceptionistUser.builder()
                .firstName("Maria")
                .lastName("Rossina")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3331234567")
                .city("Caserta")
                .address("Via Da Qui, 10")
                .email("m.rossina@enel.org")
                .password(this.passwordEncoder.encode("receptionist"))
                .headquarters(enelnapoli)
                .build();
    }

}
