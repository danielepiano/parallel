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
public class HPCDSItaliaLoadingRunner implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (this.companyRepository.existsByName(cds().getName())) {
            return;
        }

        // Company and related structure definition
        var cds = this.companyRepository.save(cds());

        // User definition
        var managercds = this.userRepository.save(cm1(cds));
        var dciuffreda = this.userRepository.save(em1(cds));
    }


    Company cds() {
        return new Company()
                .setName("HP CDS Italia S.r.l.")
                .setCity("Roma")
                .setAddress("Via Zoe Fontana, 220")
                .setPhoneNumber("+39 656")
                .setDescription("CDS Italia è una Azienda di proprietà del Gruppo Hewlett-Packard Enterprise che" +
                        " fornisce servizi di Hardware Maintenance, Application Support, Infrasctructures" +
                        " Administration, servizi in ambito CGT - Comunication Technology Group" +
                        " (ex -CMS Communication & Media Service) e GMS - Greenlake Management Services all’interno" +
                        " del gruppo Hewlett Packard Enterprise e si rivolge ad aziende leader operanti in vari" +
                        " settori produttivi.")
                .setWebsiteUrl("https://www.hpecds.com/it/");
    }


    CompanyManagerUser cm1(Company cds) {
        return CompanyManagerUser.builder()
                .firstName("Alessandro")
                .lastName("Merlini")
                .birthDate(LocalDate.of(1980, 1, 1))
                .phoneNumber("+39 3451234568")
                .city("Monza")
                .address("Via A.M., 12")
                .email("a.merlini@hpecds.com")
                .password(this.passwordEncoder.encode("amerlini"))
                .company(cds)
                .jobPosition("Lead Integration Systems")
                .build();
    }

    EmployeeUser em1(Company cds) {
        return EmployeeUser.builder()
                .firstName("Davide")
                .lastName("Ciuffreda")
                .birthDate(LocalDate.of(2001, 6, 3))
                .phoneNumber("+39 3473131313")
                .city("Monte Sant'Angelo")
                .address("Via C., 3")
                .email("d.ciuffreda@hpecds.com")
                .password(this.passwordEncoder.encode("dciuffreda"))
                .company(cds)
                .jobPosition("Junior Software Developer")
                .build();
    }

}
