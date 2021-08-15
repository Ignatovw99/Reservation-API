package com.reservation.web.mapper;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.application.service.CreatePropertyTypeServiceTest;
import com.reservation.web.controller.PropertyTypeResourceTest;
import com.reservation.web.model.PropertyTypeApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PropertyTypeApiMapperImpl.class)
public class PropertyTypeApiMapperTest {

    @Autowired
    private PropertyTypeApiMapper propertyTypeApiMapper;

    @Test
    public void toCreateCommand_testMapping() {
        PropertyTypeApi propertyTypeApi = PropertyTypeResourceTest.createPropertyTypeApi();

        CreatePropertyTypeUseCase.Command command = propertyTypeApiMapper.toCreatePropertyTypeCommand(propertyTypeApi);

        assertThat(command)
                .hasFieldOrPropertyWithValue("name", propertyTypeApi.getName())
                .hasFieldOrPropertyWithValue("requiresPrivate", propertyTypeApi.getRequiresPrivate())
                .hasFieldOrPropertyWithValue("allowsMultipleRooms", propertyTypeApi.getAllowsMultipleRooms())
                .hasFieldOrPropertyWithValue("requiresAlternative", propertyTypeApi.getRequiresAlternative());
    }

    @Test
    public void toApiModel_testMapping() {
        PropertyType propertyType = CreatePropertyTypeServiceTest.createPropertyType();

        PropertyTypeApi propertyTypeApi = propertyTypeApiMapper.toApiModel(propertyType);

        assertThat(propertyTypeApi)
                .hasFieldOrPropertyWithValue("id", propertyType.getId())
                .hasFieldOrPropertyWithValue("name", propertyType.getName())
                .hasFieldOrPropertyWithValue("requiresPrivate", propertyType.getRequiresPrivate())
                .hasFieldOrPropertyWithValue("allowsMultipleRooms", propertyType.getAllowsMultipleRooms())
                .hasFieldOrPropertyWithValue("requiresAlternative", propertyType.getRequiresAlternative());
    }
}
