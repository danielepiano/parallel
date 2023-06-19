package com.dp.spring.parallel._startup;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.database.repositories.EventRepository;
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
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.parallel.mnemosyne.database.repositories.EventBookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PiccolaAziendaLoadingRunner implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final HeadquartersRepository headquartersRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkplaceRepository workplaceRepository;
    private final EventRepository eventRepository;
    private final EventBookingRepository eventBookingRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(piccolaazienda().getName())) {
            return;
        }

        // Company and related structure definition
        var piccolaazienda = this.companyRepository.save(piccolaazienda());
        var piccolasede = this.headquartersRepository.save(piccolasede(piccolaazienda));

        var openspace = this.workspaceRepository.save(ws_openspace(piccolasede));
        this.workplaceRepository.saveAll(wp(openspace, WorkplaceType.DESK, "D", 4));

        // User definition
        var cm1 = this.userRepository.save(cm1(piccolaazienda));
        var hqr1 = this.userRepository.save(hqr1(piccolasede));

        // Events definition
        var piccoloevento = this.eventRepository.save(piccoloevento(piccolasede));

        // Event bookings definition
        var eb1 = this.eventBookingRepository.save(eb(piccoloevento, cm1));
    }


    Company piccolaazienda() {
        return new Company()
                .setName("Piccola Azienda")
                .setCity("Piccola Città")
                .setAddress("Vialetto Piccolo, 1")
                .setPhoneNumber("+39 1111111111")
                .setDescription("Veramente una piccola azienda.");
    }

    Headquarters piccolasede(Company piccolaazienda) {
        return new Headquarters()
                .setCity("Piccola Città")
                .setAddress("Vialetto Piccolo, 1")
                .setPhoneNumber("+39 1111111111")
                .setDescription("Veramente una piccola sede per una piccola azienda.")
                .setCompany(piccolaazienda);
    }

    Workspace ws_openspace(Headquarters piccolasede) {
        return new Workspace()
                .setHeadquarters(piccolasede)
                .setName("Piccolo open-space")
                .setDescription("Un piccolo posto in cui sentirti piccolo.")
                .setType(WorkspaceType.OPEN_SPACE)
                .setFloor("P-1");
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


    CompanyManagerUser cm1(Company piccolaazienda) {
        return CompanyManagerUser.builder()
                .firstName("Giuseppe")
                .lastName("Bianchi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3341111111")
                .city("Pomezia")
                .address("Via Giu di Lì, 1")
                .email("g.bianchi@piccolaazienda.pa")
                .password(this.passwordEncoder.encode("companyManager"))
                .company(piccolaazienda)
                .build();
    }

    HeadquartersReceptionistUser hqr1(Headquarters piccolasede) {
        return HeadquartersReceptionistUser.builder()
                .firstName("Mario")
                .lastName("Rossi")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("+39 3331111111")
                .city("Latina")
                .address("Via Da Qui, 1")
                .email("m.rossi@piccolaazienda.pa")
                .password(this.passwordEncoder.encode("receptionist"))
                .headquarters(piccolasede)
                .build();
    }


    Event piccoloevento(Headquarters piccolasede) {
        return new Event()
                .setHeadquarters(piccolasede)
                .setName("Piccolo evento")
                .setOnDate(LocalDate.of(2023, 7, 5))
                .setStartTime(LocalTime.of(14, 30))
                .setEndTime(LocalTime.of(15, 0))
                .setMaxPlaces(15);
    }

    EventBooking eb(Event event, User worker) {
        return new EventBooking()
                .setEvent(event)
                .setWorker(worker);
    }


}
