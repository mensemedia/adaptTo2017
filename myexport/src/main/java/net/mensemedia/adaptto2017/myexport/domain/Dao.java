package net.mensemedia.adaptto2017.myexport.domain;
import java.util.Optional;

public interface Dao {
    Optional<MyExportData> getData(ProcessingContext ctx);
}
