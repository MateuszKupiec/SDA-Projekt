package nbp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.Currency;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class NbpApiRepository {

    private final NbpApi nbpApi;

    public NbpApiRepository(NbpApi nbpApi) {
        this.nbpApi = nbpApi;
    }

    public Optional<Currency> findCurrency(String codeOfCurrency) {
        return getCurrency(codeOfCurrency, getCurrencyExchangeRateTable(nbpApi.callApiA()))
                .or(() -> getCurrency(codeOfCurrency, getCurrencyExchangeRateTable(nbpApi.callApiB())));
    }

    private Optional<Currency> getCurrency(String codeOfCurrency, List<CurrencyExchangeRateList> currencyExchangeRateLists){
        return currencyExchangeRateLists.stream().flatMap(x -> {
           String date = x.getEffectiveDate();
          return x.getRates().stream().map(y -> convertCurrencyExchangeRateToCurrency(y, date));
        }).filter(x -> x.getCode().equalsIgnoreCase(codeOfCurrency)).findFirst();
    }

    private List<CurrencyExchangeRateList> getCurrencyExchangeRateTable(String jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonFile, new TypeReference<List<CurrencyExchangeRateList>>(){});
        } catch (Exception e) {
            throw new NbpApiException("Processing JSON file failed", e.getCause());
        }
    }

    private Currency convertCurrencyExchangeRateToCurrency(CurrencyExchangeRate rate, String date) {
        LocalDate dateToAdd = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        return new Currency(null, rate.getCode(), rate.getCurrency(), rate.getMid(), dateToAdd);
    }

}
