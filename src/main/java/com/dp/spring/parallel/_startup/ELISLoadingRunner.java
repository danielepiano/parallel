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
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ELISLoadingRunner implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final HeadquartersRepository headquartersRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkplaceRepository workplaceRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(elis().getName())) {
            return;
        }

        // Company and related structure definition
        var elis = this.companyRepository.save(elis());
        var eih = this.headquartersRepository.save(eih(elis));

        var agora = this.workspaceRepository.save(ws_agora(eih));
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.BOX, "BOX", 3));
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.DESK, "D", 4));
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.DESK, "SD", 1));

        var palestra = this.workspaceRepository.save(ws_palestra(eih));
        this.workplaceRepository.saveAll(wp(palestra, WorkplaceType.DESK, "D", 20));

        var ciscomeetingroom = this.workspaceRepository.save(ws_ciscomeetingroom(eih));
        this.workplaceRepository.saveAll(wp(ciscomeetingroom, WorkplaceType.DESK, "P", 4));

        var ufficioA = this.workspaceRepository.save(ws_ufficioA(eih));
        this.workplaceRepository.saveAll(wp(ufficioA, WorkplaceType.DESK, "P", 2));

        // User definition
        var cm1 = this.userRepository.save(cm1(elis));
        var hqr1 = this.userRepository.save(hqr1(eih));
        var dev = this.userRepository.save(dev(elis));
    }


    Company elis() {
        return new Company()
                .setName("ELIS")
                .setCity("Roma")
                .setAddress("Via Sandro Sandri, 81")
                .setPhoneNumber("+39 0645924447")
                .setDescription("L'associazione Centro ELIS è un ente non profit che promuove" +
                        " sviluppo e innovazione nel mondo della formazione e del lavoro, supporta lo sviluppo di start-up in" +
                        " un ecosistema di open-innovation che coinvolge grandi gruppi industriali, università, centri di ricerca" +
                        " e altre istituzioni")
                .setWebsiteUrl("https://www.elis.org");
    }

    Headquarters eih(Company elis) {
        return new Headquarters()
                .setCity("Roma")
                .setAddress("Via Sandro Sandri, 81")
                .setPhoneNumber("+39 0645924447")
                .setDescription("ELIS è l’agorà che mette insieme persone ispirate da ideali quali" +
                        " Innovazione Sociale, Benessere e Sostenibilità. Concepiamo il lavoro come emancipazione" +
                        " personale, progetto di vita e opportunità per mettersi al servizio degli altri." +
                        "Ci rivolgiamo ai giovani, professionisti e imprese con l’obiettivo di costruire insieme" +
                        " progetti di innovazione e attività di sviluppo sostenibile.")
                .setCompany(elis);
    }

    Workspace ws_agora(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Agorà")
                .setDescription("Ampio spazio di ritrovo, fornito di una serie di postazioni di lavoro.")
                .setType(WorkspaceType.OPEN_SPACE)
                .setFloor("PT");
    }

    Workspace ws_palestra(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Innovation Hub")
                .setDescription("Ampio open space: qui ha luogo l'innovazione.")
                .setType(WorkspaceType.OPEN_SPACE)
                .setFloor("PT");
    }

    Workspace ws_ciscomeetingroom(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Cisco Meeting-Room")
                .setDescription("Stanza dotata di apparecchiatura Cisco, utilissima per meeting e riunioni.")
                .setType(WorkspaceType.MEETING_ROOM)
                .setFloor("PT");
    }

    Workspace ws_ufficioA(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Ufficio A")
                .setType(WorkspaceType.PRIVATE_OFFICE)
                .setFloor("PT");
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


    CompanyManagerUser cm1(Company elis) {
        return CompanyManagerUser.builder()
                .firstName("Giuseppe")
                .lastName("Bianchi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3341234567")
                .city("Pomezia")
                .address("Via Giu di Lì, 73")
                .email("g.bianchi@elis.org")
                .password(this.passwordEncoder.encode("companyManager"))
                .company(elis)
                .build();
    }

    HeadquartersReceptionistUser hqr1(Headquarters eih) {
        return HeadquartersReceptionistUser.builder()
                .firstName("Mario")
                .lastName("Rossi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3331234567")
                .city("Latina")
                .address("Via Da Qui, 75")
                .email("m.rossi@elis.org")
                .password(this.passwordEncoder.encode("receptionist"))
                .headquarters(eih)
                .build();
    }

    EmployeeUser dev(Company elis) {
        return EmployeeUser.builder()
                .firstName("Developer")
                .lastName("Dev")
                .birthDate(LocalDate.of(2001, 1, 1))
                .phoneNumber("+39 6969696969")
                .city("Roma")
                .address("Via Sandro Sandri, 71")
                .email("dev")
                .password(this.passwordEncoder.encode("dev"))
                .company(elis)
                .jobPosition("Laurendo in Ing. Informatica")
                .build();
    }

}
