import converter.CurrencyConverter;
import db.DatabaseConnection;

import nbp.NbpApi;
import db.CurrencyRepository;
import nbp.NbpApiRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;


public class MainTest {
    public static void main(String[] args) throws IOException, InterruptedException {

        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        CurrencyRepository currencyRepository = new CurrencyRepository(databaseConnection);

        NbpApi nbpApi = new NbpApi();
        NbpApiRepository nbpRepository = new NbpApiRepository(nbpApi);

        CurrencyConverter currencyConverter = new CurrencyConverter(currencyRepository, nbpRepository);

        System.out.println(currencyRepository.findCurrency("EUR"));
        System.out.println(currencyConverter.convertToPln("EUR", new BigDecimal("10")));
        System.out.println(currencyConverter.convertFromPln("EUR", new BigDecimal("90")));
        System.out.println(currencyConverter.convertToPln("USD", new BigDecimal("20")));
        System.out.println(currencyConverter.convertFromPln("BAM", new BigDecimal("20")));
        System.out.println(currencyConverter.convert("EUR","USD", new BigDecimal("20")));

        System.out.println("By date");

        System.out.println(LocalDate.now());

        System.out.println(currencyConverter.convertToPlnByDate("EUR", new BigDecimal("10"), LocalDate.now()));
        //currencyConverter.convertFromPln("ABC", new BigDecimal(10));
    }
}
