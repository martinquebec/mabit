package mabit.data.marketdata;

import mabit.data.instruments.IInstrument;

/**
 * Created by martin on 9/9/2016.
 */
public interface IMarketDataService {
    Quote getLastQuote(IInstrument instrument);
}
