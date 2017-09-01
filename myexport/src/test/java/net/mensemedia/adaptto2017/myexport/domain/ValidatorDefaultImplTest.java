package net.mensemedia.adaptto2017.myexport.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ObjectArrayArguments;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
public class ValidatorDefaultImplTest {

    ValidatorDefaultImpl validator;

    @BeforeEach
    public void setUp() throws Exception {
        validator = new ValidatorDefaultImpl();
    }

    private static MyExportData createValidData() {
        return new MyExportData()
                        .setRangeType("mr")
                        .setGroup("g")
                        .setTemplate("/apps/mercedesbenz-trucks-pit/templates/my-export");
    }

    @Test
    public void test_validate_ok() throws Exception {
        validator.validate(createValidData());
    }

    @ParameterizedTest(name = "input \"{1}\" should yield error. ")
    @MethodSource(names = "validate_errorProvider")
    void test_validate_error(final String msg, final MyExportData data) {
        try {
            validator.validate(data);
            fail("It should fail for the given input");
        } catch (ValidationException e) {
            assertTrue(true, msg);
        }
    }

    static Stream<Arguments> validate_errorProvider() {
        return Stream.of(
                ObjectArrayArguments.create(
                        "Rule: input null -> error", null),
                ObjectArrayArguments.create(
                        "Rule: input wrong template -> error", createValidData().setTemplate("/")),
                ObjectArrayArguments.create(
                        "Rule: input wrong group -> error", createValidData().setGroup(null)),
                ObjectArrayArguments.create(
                        "Rule: input wrong model range -> error", createValidData().setRangeType(null))
        );
    }


    @Test
    public void test_validateGroup_ok() throws Exception {
        validator.validateGroup(new MyExportData().setGroup("x"));
        assertTrue(true, "It should pass without exception");
    }

    @ParameterizedTest(name = "input \"{1}\" should yield error. ")
    @MethodSource(names = "validateGroup_errorProvider")
    void test_validateGroup_error(final String msg, final MyExportData data) {
        test_validate_error(msg, "There is no group", data,
                validator::validateGroup);
    }

    static Stream<Arguments> validateGroup_errorProvider() {
        return Stream.of(
                ObjectArrayArguments.create(
                        "Rule: group default -> error", new MyExportData()),
                ObjectArrayArguments.create(
                        "Rule: group null -> error", new MyExportData().setGroup(null))
        );
    }


    @Test
    public void test_validateModelRange_ok() throws Exception {
        validator.validateModelRange(new MyExportData().setRangeType("x"));
        assertTrue(true, "It should pass without exception");
    }

    @ParameterizedTest(name = "input \"{1}\" should yield error. ")
    @MethodSource(names = "validateModelRange_errorProvider")
    void test_validateModelRange_error(final String msg, final MyExportData data) {
        test_validate_error(msg, "There no type of model range", data,
                validator::validateModelRange);
    }

    static Stream<Arguments> validateModelRange_errorProvider() {
        return Stream.of(
                ObjectArrayArguments.create(
                        "Rule: modelRange default -> error", new MyExportData()),
                ObjectArrayArguments.create(
                        "Rule: modelRange null -> error", new MyExportData().setRangeType(null))
        );
    }


    @Test
    public void test_validateTemplate_ok() throws Exception {
        validator.validateTemplate(new MyExportData()
                .setTemplate("/apps/mercedesbenz-trucks-pit/templates/my-export"));
        assertTrue(true, "It should pass without exception");
    }

    @ParameterizedTest(name = "input \"{1}\" should yield error. ")
    @MethodSource(names = "validateTemplate_errorProvider")
    void test_validateTemplate_error(final String msg, final MyExportData data) {
        test_validate_error(msg, "Wrong template type", data,
                validator::validateTemplate);
    }

    static Stream<Arguments> validateTemplate_errorProvider() {
        return Stream.of(
                ObjectArrayArguments.create(
                        "Rule: template default -> error", new MyExportData()),
                ObjectArrayArguments.create(
                        "Rule: template null -> error", new MyExportData().setTemplate(null)),
                ObjectArrayArguments.create(
                        "Rule: template not as expected -> error",
                        new MyExportData().setTemplate("/apps/mercedesbenz-trucks-pit/templates/xyz"))
        );
    }



    void test_validate_error(final String msg, final String expectedMessage, final MyExportData data,
                             final ThrowingConsumer<MyExportData> consumer) {
        try {
            consumer.accept(data);
            fail("It should fail for the given input");
        } catch (Throwable e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(ValidationException.class, e.getClass());
        }
    }
}
