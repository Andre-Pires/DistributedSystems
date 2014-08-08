package registofatura.ws.domain;

import org.joda.time.DateTime;

public class Serie extends
        Serie_Base {

    private final pt.registofatura.ws.Serie stubSerie;

    public Serie(int nifEmissor, int numSerie, DateTime validoAte) {
        super();
        this.stubSerie = new pt.registofatura.ws.Serie();
        this.setNumSerie(numSerie);
        this.setValidoAte(validoAte);
        this.setNifEmissor(nifEmissor);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Serie_Base#setNumSerie(int)
     */
    @Override
    public void setNumSerie(int numSerie) {

        stubSerie.setNumSerie(numSerie);
        super.setNumSerie(numSerie);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Serie_Base#setValidoAte(org.joda.time.DateTime)
     */
    @Override
    public void setValidoAte(DateTime validoAte) {

        stubSerie.setValidoAte(XMLCalendarToDate.toXMLGregorianCalendar(validoAte.toDate()));
        super.setValidoAte(validoAte);
    }


    public pt.registofatura.ws.Serie getRemoteSerie() {
        return this.stubSerie;
    }
}
