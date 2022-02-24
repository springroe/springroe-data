package io.github.springroe.data.core.util;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test for {@link EntityUtils}
 *
 * @author kongsiyu
 */
class EntityUtilsTest {
    static final String PERSON_V_2 = "PersonV2";
    static final String PERSON_V_3 = "PersonV3";

    @Test
    void testGetEntityComment() {
        assertEquals(PERSON_V_2, EntityUtils.getEntityComment(PersonV2.class));
        assertEquals(PERSON_V_3, EntityUtils.getEntityComment(PersonV3.class));
    }

    @ApiModel(PERSON_V_2)
    static class PersonV2 {
    }

    @Schema(description = PERSON_V_3)
    static class PersonV3 {
    }
}