package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;

public class WorkplaceFixture {

    public static Workplace get() {
        return new Workplace()
                .setWorkspace(WorkspaceFixture.get())
                .setName("name-test")
                .setDescription("description-test")
                .setType(WorkplaceType.DESK);
    }

}
