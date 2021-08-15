package com.reservation.web.mapper;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.application.service.CreatePropertyServiceTest;
import com.reservation.web.controller.PropertyResourceTest;
import com.reservation.web.model.PropertyApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PropertyApiMapperImpl.class)
public class PropertyApiMapperTest {

    @Autowired
    private PropertyApiMapper propertyApiMapper;

    @Test
    public void toCreateCommand_testMapping() {

        PropertyApi propertyApi = PropertyResourceTest.createPropertyApi();
        CreatePropertyUseCase.Command command = propertyApiMapper.toCreatePropertyCommand(propertyApi);

        assertThat(command)
                .hasFieldOrPropertyWithValue("name", propertyApi.getName())
                .hasFieldOrPropertyWithValue("starRatings", propertyApi.getStarRatings())
                .hasFieldOrPropertyWithValue("propertyTypeId", propertyApi.getPropertyTypeId())
                .hasFieldOrPropertyWithValue("description", propertyApi.getDescription())
                .hasFieldOrPropertyWithValue("country", propertyApi.getCountry())
                .hasFieldOrPropertyWithValue("region", propertyApi.getRegion())
                .hasFieldOrPropertyWithValue("street", propertyApi.getStreet())
                .hasFieldOrPropertyWithValue("houseNumber", propertyApi.getHouseNumber())
                .hasFieldOrPropertyWithValue("zip", propertyApi.getZip())
                .hasFieldOrPropertyWithValue("contactName", propertyApi.getContactName())
                .hasFieldOrPropertyWithValue("contactNumber", propertyApi.getContactNumber())
                .hasFieldOrPropertyWithValue("contactEmail", propertyApi.getContactEmail());
    }

    @Test
    public void toApiModel_testMapping() {

        Property property = CreatePropertyServiceTest.createProperty();
        property.setId(13L);
        property.getType().setId(14123L);

        PropertyApi propertyApi = propertyApiMapper.toApiModel(property);

        assertThat(propertyApi)
                .hasFieldOrPropertyWithValue("name", property.getName())
                .hasFieldOrPropertyWithValue("starRatings", property.getStarRatings())
                .hasFieldOrPropertyWithValue("propertyTypeId", property.getType().getId())
                .hasFieldOrPropertyWithValue("description", property.getDescription())
                .hasFieldOrPropertyWithValue("country", property.getCountry())
                .hasFieldOrPropertyWithValue("region", property.getRegion())
                .hasFieldOrPropertyWithValue("street", property.getStreet())
                .hasFieldOrPropertyWithValue("houseNumber", property.getHouseNumber())
                .hasFieldOrPropertyWithValue("zip", property.getZip())
                .hasFieldOrPropertyWithValue("contactName", property.getContactName())
                .hasFieldOrPropertyWithValue("contactNumber", property.getContactNumber())
                .hasFieldOrPropertyWithValue("contactEmail", property.getContactEmail());
    }
}
