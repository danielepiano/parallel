package com.dp.spring.parallel._startup;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SoldoSrlLoadingRunner implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(soldosrl().getName())) {
            return;
        }

        // Company and related structure definition
        var soldo = this.companyRepository.save(soldosrl());

        // User definition
        var psannino = this.userRepository.save(cm1(soldo));
        var dpiano = this.userRepository.save(em1(soldo));
    }


    Company soldosrl() {
        return new Company()
                .setName("Soldo Italia S.r.l.")
                .setCity("Milano")
                .setAddress("Via degli Olivetani 10/12")
                .setPhoneNumber("+39 50240")
                .setDescription("Soldo è un'azienda di servizi finanziari nonché produttrice della propria" +
                        " piattaforma software, che consente di automatizzare la gestione delle spese di dipendenti e" +
                        " aziende, dalla decisione ala riconciliazione.")
                .setWebsiteUrl("https://www.soldo.com");
    }


    CompanyManagerUser cm1(Company soldo) {
        return CompanyManagerUser.builder()
                .firstName("Pasquale")
                .lastName("Sannino")
                .birthDate(LocalDate.of(1980, 1, 1))
                .phoneNumber("+39 3451234567")
                .city("Roma")
                .address("Via Napoli, 10")
                .email("psannino@soldo.com")
                .password(this.passwordEncoder.encode("psannino"))
                .company(soldo)
                .jobPosition("Lead Integration Systems")
                .build();
    }

    EmployeeUser em1(Company soldo) {
        return EmployeeUser.builder()
                .firstName("Daniele")
                .lastName("Piano")
                .birthDate(LocalDate.of(2001, 5, 7))
                .phoneNumber("+39 3472121212")
                .city("San Giovanni Rotondo")
                .address("Via P., 42")
                .email("dpiano@soldo.com")
                .password(this.passwordEncoder.encode("dpiano"))
                .company(soldo)
                .jobPosition("Junior Software Developer")
                .build();
    }

}
