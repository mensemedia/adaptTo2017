package net.mensemedia.adaptto2017.myexport.domain;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(JUnitPlatform.class)
public class MyServiceImplTest {

    List<Tuple> errors;
    List<String> warnings;
    List<MyExportData> exportedItems;

    @BeforeEach
    public void setUp() throws Exception {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
        exportedItems = new ArrayList<>();
    }


    @Test
    public void test_init() throws Exception {
        assertNotNull(
                createService(ctx -> Optional.empty())
        );
    }


    @Test
    public void test_empty() throws Exception {
        final MyService service = createService(ctx -> Optional.empty());
        service.process(new ProcessingContext(Optional.of("/a/b/c"), s -> true));
        assertEquals(0, exportedItems.size(),
                "It should not export any data when DAO provides not data");
    }


    @Test
    public void test_process_OK() throws Exception {
        final MyExportData data = createValidData();
        final MyService service = createService(ctx -> Optional.of(data));

        // when
        service.process(new ProcessingContext(Optional.of("/a/b/c/jcr:content"), s -> true));

        assertEquals(1, exportedItems.size());
        assertEquals(0, errors.size());
        assertEquals(0, warnings.size());
    }


    @Test
    public void test_process_wrongPath() throws Exception {
        final MyExportData data = createValidData();
        final MyService service = createService(ctx -> Optional.of(data));

        // when
        service.process(new ProcessingContext(Optional.of("/a/b/c"), s -> s.endsWith("d")));

        assertEquals(0, exportedItems.size(),
                "It should not export any data when path does not end with the required suffix");
        assertEquals(0, errors.size());
        assertEquals(0, warnings.size());
    }

    private static MyExportData createValidData() {
        return new MyExportData()
                .setRangeType("mr")
                .setGroup("g")
                .setTemplate("/apps/mercedesbenz-trucks-pit/templates/my-export");
    }


    @Test
    public void test_validation_fail() throws Exception {
        final MyExportData data = new MyExportData(); // template is mising -> should fail
        final MyService service = createService(ctx -> Optional.of(data));
        service.process(new ProcessingContext(Optional.of("/a/b/c/jcr:content"), s -> true));
        assertEquals(0, exportedItems.size(),
                "It should not export any data when data is invalid");
        assertEquals(Arrays.asList("Wrong template type. Stop export."), warnings);
    }

    private MyService createService(final Dao dao) {
        return new MyServiceImpl(
                dao,
                exportedItems::add,
                (s1, e) -> errors.add(Tuple.of(s1, e)),
                warnings::add
        );
    }
}
