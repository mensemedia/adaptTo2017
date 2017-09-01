package net.mensemedia.adaptto2017.myexport.domain;
import java.util.Optional;

public class MyExportData {
    private Optional<String> template = Optional.empty();

    public Optional<String> getTemplate() {
        return template;
    }

    public MyExportData setTemplate(final String template) {
        this.template = Optional.ofNullable(template);
        return this;
    }


    private Optional<String> group = Optional.empty();

    public Optional<String> getGroup() {
        return group;
    }

    public MyExportData setGroup(final String group) {
        this.group = Optional.ofNullable(group);
        return this;
    }


    private Optional<String> rangeType = Optional.empty();

    public Optional<String> getRangeType() {
        return rangeType;
    }

    public MyExportData setRangeType(final String value) {
        this.rangeType = Optional.ofNullable(value);
        return this;
    }
}
