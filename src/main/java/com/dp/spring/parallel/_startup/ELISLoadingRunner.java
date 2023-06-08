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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.BOX, "BOX", 4));
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.DESK, "D", 10));
        this.workplaceRepository.saveAll(wp(agora, WorkplaceType.DESK, "SD", 1));

        var innovationhub = this.workspaceRepository.save(ws_innovationhub(eih));
        this.workplaceRepository.saveAll(wp(innovationhub, WorkplaceType.DESK, "D", 40));

        var ciscomeetingroom = this.workspaceRepository.save(ws_ciscomeetingroom(eih));
        this.workplaceRepository.saveAll(wp(ciscomeetingroom, WorkplaceType.DESK, "P", 8));

        var ufficioA = this.workspaceRepository.save(ws_ufficioA(eih));
        this.workplaceRepository.saveAll(wp(ufficioA, WorkplaceType.DESK, "P", 3));

        var ufficioB = this.workspaceRepository.save(ws_ufficioB(eih));
        this.workplaceRepository.saveAll(wp(ufficioB, WorkplaceType.DESK, "P", 6));


        // User definition
        var cm1 = this.userRepository.save(cm1(elis));
        var hqr1 = this.userRepository.save(hqr1(eih));
    }


    Company elis() {
        return new Company()
                .setName("ELIS")
                .setCity("Roma")
                .setAddress("Via Sandro Sandri, 81")
                .setPhoneNumber("+39 0645924447")
                .setFeDescription("L'associazione Centro ELIS è un ente non profit che promuove" +
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
                .setFeDescription("Sede operativa di ELIS Innovation Hub, qui si realizzano progetti" +
                        " d'innovazione e consulenza aziendale, creando sinergia tra grandi aziende, start-up, università," +
                        " centri di ricerca e giovani.")
                .setCompany(elis);
    }

    Workspace ws_agora(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Agorà")
                .setDescription("Ampio spazio di ritrovo, fornito di una serie di postazioni di lavoro.")
                .setType(WorkspaceType.OPEN_SPACE)
                .setMaxSeats(20)
                .setFloor("PT");
    }

    Workspace ws_innovationhub(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Innovation Hub")
                .setDescription("Ampio open space: qui ha luogo l'innovazione.")
                .setType(WorkspaceType.OPEN_SPACE)
                .setMaxSeats(40)
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
                .setFloor("P1");
    }

    Workspace ws_ufficioB(Headquarters eih) {
        return new Workspace()
                .setHeadquarters(eih)
                .setName("Ufficio B")
                .setType(WorkspaceType.PRIVATE_OFFICE)
                .setFloor("P1");
    }


    Set<Workplace> wp(Workspace ws, WorkplaceType type, String prefix, int n) {
        Set<Workplace> wp = new HashSet<>();

        Workplace base = new Workplace().setWorkspace(ws)
                .setType(type);

        for (int i = 0; i < n; i++) {
            wp.add(base.setName(prefix + i));
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

}
