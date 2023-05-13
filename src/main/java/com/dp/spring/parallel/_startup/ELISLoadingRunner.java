package com.dp.spring.parallel._startup;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ELISLoadingRunner implements CommandLineRunner {
    private static final String COMPANY_NAME = "ELIS";
    private static final String COMPANY_CITY = "Roma";
    private static final String COMPANY_ADDRESS = "Via Sandro Sandri, 81";
    private static final String COMPANY_PHONE_NUMBER = "+39 0645924447";
    private static final String COMPANY_FE_DESCRIPTION = "L'associazione Centro ELIS è un ente non profit che promuove" +
            " sviluppo e innovazione nel mondo della formazione e del lavoro, supporta lo sviluppo di start-up in" +
            " un ecosistema di open-innovation che coinvolge grandi gruppi industriali, università, centri di ricerca" +
            " e altre istituzioni";
    private static final String COMPANY_WEBSITE_URL = "https://www.elis.org";

    private static final String HQ_CITY = "Roma";
    private static final String HQ_ADDRESS = "Via Sandro Sandri, 81";
    private static final String HQ_PHONE_NUMBER = "+39 0645924447";
    private static final String HQ_FE_DESCRIPTION = "Sede operativa di ELIS Innovation Hub, qui si realizzano progetti" +
            " d'innovazione e consulenza aziendale, creando sinergia tra grandi aziende, start-up, università," +
            " centri di ricerca e giovani.";

    private static final String CM_FIRST_NAME = "Giuseppe";
    private static final String CM_LAST_NAME = "Bianchi";
    private static final LocalDate CM_BIRTHDATE = LocalDate.of(1970, 1, 1);
    private static final String CM_PHONE_NUMBER = "+39 3341234567";
    private static final String CM_CITY = "Pomezia";
    private static final String CM_ADDRESS = "Via Giu di Lì, 73";
    private static final String CM_EMAIL = "g.bianchi@elis.org";
    private static final String CM_PASSWORD = "companyManager";

    private static final String HQ_REC_FIRST_NAME = "Mario";
    private static final String HQ_REC_LAST_NAME = "Rossi";
    private static final LocalDate HQ_REC_BIRTHDATE = LocalDate.of(1970, 1, 1);
    private static final String HQ_REC_PHONE_NUMBER = "+39 3331234567";
    private static final String HQ_REC_CITY = "Latina";
    private static final String HQ_REC_ADDRESS = "Via Da Qui, 75";
    private static final String HQ_REC_EMAIL = "m.rossi@elis.org";
    private static final String HQ_REC_PASSWORD = "receptionist";


    private final CompanyRepository companyRepository;
    private final HeadquartersRepository headquartersRepository;

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(COMPANY_NAME)) {
            return;
        }
        final Company elis = this.companyRepository.save(elisCompany());
        final CompanyManagerUser manager = this.userRepository.save(companyManager(elis));
        final Headquarters eih = this.headquartersRepository.save(eihHeadquarters(elis));
        final HeadquartersReceptionistUser receptionist = this.userRepository.save(receptionist(eih));
    }


    private Company elisCompany() {
        return new Company()
                .setName(COMPANY_NAME)
                .setCity(COMPANY_CITY)
                .setAddress(COMPANY_ADDRESS)
                .setPhoneNumber(COMPANY_PHONE_NUMBER)
                .setFeDescription(COMPANY_FE_DESCRIPTION)
                .setWebsiteUrl(COMPANY_WEBSITE_URL);
    }

    private CompanyManagerUser companyManager(final Company elis) {
        return CompanyManagerUser.builder()
                .firstName(CM_FIRST_NAME)
                .lastName(CM_LAST_NAME)
                .birthDate(CM_BIRTHDATE)
                .phoneNumber(CM_PHONE_NUMBER)
                .city(CM_CITY)
                .address(CM_ADDRESS)
                .email(CM_EMAIL)
                .password(this.passwordEncoder.encode(CM_PASSWORD))
                .company(elis)
                .build();
    }

    private Headquarters eihHeadquarters(final Company elis) {
        return new Headquarters()
                .setCity(HQ_CITY)
                .setAddress(HQ_ADDRESS)
                .setPhoneNumber(HQ_PHONE_NUMBER)
                .setFeDescription(HQ_FE_DESCRIPTION)
                .setCompany(elis);
    }

    private HeadquartersReceptionistUser receptionist(final Headquarters eih) {
        return HeadquartersReceptionistUser.builder()
                .firstName(HQ_REC_FIRST_NAME)
                .lastName(HQ_REC_LAST_NAME)
                .birthDate(HQ_REC_BIRTHDATE)
                .phoneNumber(HQ_REC_PHONE_NUMBER)
                .city(HQ_REC_CITY)
                .address(HQ_REC_ADDRESS)
                .email(HQ_REC_EMAIL)
                .password(this.passwordEncoder.encode(HQ_REC_PASSWORD))
                .headquarters(eih)
                .build();
    }
}
