package net.mensemedia.adaptto2017.myexport.exporter;
import net.mensemedia.adaptto2017.myexport.domain.MyExportData;
import net.mensemedia.adaptto2017.myexport.domain.Exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class ExporterMemImpl implements Exporter {

    private final BiConsumer<String, Exception> errorConsumer;
    private final List<String> headings;
    private final List<List<String>> rows;

    public ExporterMemImpl(final BiConsumer<String, Exception> errorConsumer) {
        this.errorConsumer = errorConsumer;
        headings = new ArrayList<>();
        rows = new ArrayList<>();
    }

    @Override
    public void export(final MyExportData data) {
        headings.add("Group");
        headings.add("Model Range");

        rows.add(
                Arrays.asList(
                        data.getGroup().orElse(""),
                        data.getRangeType().orElse("")
                )
        );
    }

    public List<String> getHeadings() {
        return headings;
    }

    public List<List<String>> getRows() {
        return rows;
    }
}
