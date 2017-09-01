package net.mensemedia.adaptto2017.myexport.domain;
import java.util.Arrays;
import java.util.List;

public class ValidatorDefaultImpl implements Validator {

    private final List<Validator> validators;

    public ValidatorDefaultImpl() {
        validators = Arrays.asList(
                this::validateTemplate,
                this::validateGroup,
                this::validateModelRange
        );
    }

    @Override
    public void validate(final MyExportData data) throws ValidationException {
        if (data == null) {
            throw new ValidationException("Data is null");
        }
        for (Validator validator : validators) {
            validator.validate(data);
        }
    }


    void validateModelRange(final MyExportData data) throws ValidationException {
        if (!data.getRangeType().isPresent()) {
            throw new ValidationException("There no type of model range");
        }
    }

    void validateGroup(final MyExportData data) throws ValidationException {
        if (!data.getGroup().isPresent()) {
            throw new ValidationException("There is no group");
        }
    }

    void validateTemplate(final MyExportData data) throws ValidationException {
        if (!data.getTemplate()
                .filter("/apps/mercedesbenz-trucks-pit/templates/my-export"::equals)
                .isPresent()) {
            throw new ValidationException("Wrong template type");
        }
    }
}
