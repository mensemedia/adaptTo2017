package net.mensemedia.adaptto2017.myexport.storage;
import net.mensemedia.adaptto2017.myexport.domain.Dao;
import net.mensemedia.adaptto2017.myexport.domain.MyExportData;
import net.mensemedia.adaptto2017.myexport.domain.ProcessingContext;
import net.mensemedia.adaptto2017.commons.jcr.DaoBaseForJcrSession;
import net.mensemedia.adaptto2017.commons.jcr.JcrSessionTemplate;
import net.mensemedia.adaptto2017.commons.ValueHolder;

import javax.jcr.Node;
import java.util.Optional;
import java.util.function.BiConsumer;

public class DaoJcrSessionImpl extends DaoBaseForJcrSession implements Dao {
    private final JcrSessionTemplate jcrSessionTemplate;

    public DaoJcrSessionImpl(final JcrSessionTemplate jcrSessionTemplate,
                             final BiConsumer<String, Exception> errorConsumer) {
        super(errorConsumer);
        this.jcrSessionTemplate = jcrSessionTemplate;
    }

    @Override
    public Optional<MyExportData> getData(final ProcessingContext ctx) {
        ValueHolder<MyExportData> valueHolder = new ValueHolder<>();

        jcrSessionTemplate.execute(session ->
                ctx.getPath()
                        .map(path -> getNode(session, path))
                        .filter(Optional::isPresent)
                        .map(node -> toData(node.get()))
                        .ifPresent(valueHolder::set)
        );

        return valueHolder.get();
    }

    MyExportData toData(final Node node) {
        MyExportData data = new MyExportData();

        getNodePropertyAsString(node, "cq:template")
                .filter(s -> s.length() > 0)
                .ifPresent(data::setTemplate);
        getNodePropertyAsString(node, "group")
                .filter(s -> s.length() > 0)
                .ifPresent(data::setGroup);
        getNodePropertyAsString(node, "type")
                .filter(s -> s.length() > 0)
                .ifPresent(data::setRangeType);

        return data;
    }
}
