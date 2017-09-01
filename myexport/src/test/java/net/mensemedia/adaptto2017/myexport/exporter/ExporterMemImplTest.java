package net.mensemedia.adaptto2017.myexport.exporter;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import net.mensemedia.adaptto2017.commons.testing.MyAssertsJUnit4;
import net.mensemedia.adaptto2017.myexport.domain.MyExportData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;

public class ExporterMemImplTest {
    List<Tuple<String, Exception>> errors;
    BiConsumer errorConsumer;
    private MyAssertsJUnit4 asserts;

    @Before
    public void setUp() throws Exception {
        errors = new ArrayList<>();
        errorConsumer = (s, e) -> errors.add(Tuple.of(s, e));
        asserts = new MyAssertsJUnit4(errors);
    }


    @Test
    public void test_export() throws Exception {
        ExporterMemImpl exporter = new ExporterMemImpl(errorConsumer);

        exporter.export(new MyExportData().setRangeType("m").setGroup("g"));
        asserts.assertNoErrors();

        assertEquals(Arrays.asList("Group", "Model Range"), exporter.getHeadings());
        assertEquals(Arrays.asList(Arrays.asList("g", "m")), exporter.getRows());
    }
}
