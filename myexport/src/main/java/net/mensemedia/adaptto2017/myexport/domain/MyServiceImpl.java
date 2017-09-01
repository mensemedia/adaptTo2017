package net.mensemedia.adaptto2017.myexport.domain;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyServiceImpl implements MyService {
    private final Dao dao;
    private final Exporter exporter;
    private final BiConsumer<String, Exception> errorConsumer;
    private final Consumer<String> warningConsumer;

    private Validator validator = new ValidatorDefaultImpl();

    public MyServiceImpl(final Dao dao,
                         final Exporter exporter,
                         final BiConsumer<String, Exception> errorConsumer,
                         final Consumer<String> warningConsumer) {
        this.dao = dao;
        this.exporter = exporter;
        this.errorConsumer = errorConsumer;
        this.warningConsumer = warningConsumer;
    }

    @Override
    public void process(final ProcessingContext ctx) {
        ctx.getPath().filter(ctx.getPathFilter()).ifPresent(p -> {
            final Optional<MyExportData> data = dao.getData(ctx);
            data.ifPresent(d -> {
                try {
                    validator.validate(d);
                    exporter.export(d);
                } catch (ValidationException e) {
                    warningConsumer.accept(
                            String.format("%s. Stop export.", e.getMessage())
                    );
                }
            });
        });
    }
}
