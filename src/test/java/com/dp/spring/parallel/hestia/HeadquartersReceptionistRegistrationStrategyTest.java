package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.services.registration_strategies.HeadquartersReceptionistRegistrationStrategy;
import com.dp.spring.springcore.observer.ObserverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class HeadquartersReceptionistRegistrationStrategyTest {

    @Spy
    HeadquartersReceptionistRegistrationStrategy headquartersReceptionistRegistrationStrategy;

    @Spy
    HeadquartersService headquartersService = new HeadquartersService() {
        @Override
        public Headquarters add(Integer companyId, CreateHeadquartersRequestDTO toAddData) {
            return null;
        }

        @Override
        public Headquarters headquarters(Integer headquartersId) {
            return null;
        }

        @Override
        public List<Headquarters> headquarters() {
            return null;
        }

        @Override
        public List<Headquarters> favoriteHeadquarters() {
            return null;
        }

        @Override
        public Set<Headquarters> companyHeadquarters(Integer companyId) {
            return null;
        }

        @Override
        public Headquarters update(Integer companyId, Integer headquartersId, UpdateHeadquartersRequestDTO updatedData) {
            return null;
        }

        @Override
        public void remove(Integer companyId, Integer headquartersId) {

        }

        @Override
        public void removeAll(Company company) {

        }

        @Override
        public void toggleFavouriteHeadquarters(Integer headquartersId) {

        }

        @Override
        public void checkExistence(Integer companyId, Integer headquartersId) {

        }

        @Override
        public void addObserver(User o, Headquarters headquarters) {

        }

        @Override
        public void removeObserver(User o, Headquarters headquarters) {

        }

        @Override
        public <C, OS extends ObserverService<User, Headquarters, C>> void notifyObservers(Headquarters headquarters, OS os, C c) {

        }
    };


    @BeforeEach
    public void setUp() throws Exception {
        var constructor = HeadquartersReceptionistRegistrationStrategy.class
                .getDeclaredConstructor();
        constructor.setAccessible(true);

        headquartersReceptionistRegistrationStrategy = constructor.newInstance();

        var companyService = HeadquartersReceptionistRegistrationStrategy.class.getDeclaredField("headquartersService");
        companyService.setAccessible(true);
        companyService.set(headquartersReceptionistRegistrationStrategy, this.headquartersService);
    }

    @Test
    void buildUser_shouldMap() throws Exception {
        String encodedPassword = "encoded-password";
        Integer scopeId = 2;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        Headquarters headquarters = HeadquartersFixture.get();
        doReturn(headquarters).when(headquartersService).headquarters(scopeId);

        var buildUser = HeadquartersReceptionistRegistrationStrategy.class
                .getDeclaredMethod("buildUser", String.class, Integer.class, RegistrationRequestDTO.class);
        buildUser.setAccessible(true);
        HeadquartersReceptionistUser result = (HeadquartersReceptionistUser) buildUser.invoke(headquartersReceptionistRegistrationStrategy, encodedPassword, scopeId, request);

        assertEquals(request.getFirstName(), result.getFirstName(), "wrong mapping");
        assertEquals(request.getLastName(), result.getLastName(), "wrong mapping");
        assertEquals(request.getBirthDate(), result.getBirthDate(), "wrong mapping");
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber(), "wrong mapping");
        assertEquals(request.getCity(), result.getCity(), "wrong mapping");
        assertEquals(request.getAddress(), result.getAddress(), "wrong mapping");
        assertEquals(request.getEmail(), result.getEmail(), "wrong mapping");
        assertEquals(encodedPassword, result.getPassword(), "wrong mapping");
        assertEquals(headquarters, result.getHeadquarters(), "wrong mapping");
    }
}
