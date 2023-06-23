package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;

public class WorkspaceFixture {

    public static Workspace get() {
        return new Workspace()
                .setHeadquarters(HeadquartersFixture.get())
                .setName("name-test")
                .setDescription("description-test")
                .setType(WorkspaceType.OPEN_SPACE)
                .setFloor("floor-test");
    }

}
