package net.mensemedia.adaptto2017.myexport.domain;
public interface Validator {
    void validate(MyExportData data) throws ValidationException;
}
