package registofatura.ws.domain;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

public class Fatura extends
        Fatura_Base {

    private final pt.registofatura.ws.Fatura stubFatura;

    public Fatura(DateTime data,
                  int numSeqFatura,
                  int numSerie,
                  int nifEmissor,
                  String nomeEmissor,
                  Integer nifCliente,
                  int iva,
                  int total) {
        super();
        this.stubFatura = new pt.registofatura.ws.Fatura();
        this.setData(data);
        this.setNumSeqFatura(numSeqFatura);
        this.setNumSerie(numSerie);
        this.setNifEmissor(nifEmissor);
        this.setNomeEmissor(nomeEmissor);
        this.setIva(iva);
        this.setTotal(total);
        this.setNifCliente(nifCliente);
    }


    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setData(org.joda.time.DateTime)
     */
    @Override
    public void setData(DateTime data) {

        stubFatura.setData(XMLCalendarToDate.toXMLGregorianCalendar(data.toDate()));
        super.setData(data);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setNumSeqFatura(int)
     */
    @Override
    public void setNumSeqFatura(int numSeqFatura) {

        stubFatura.setNumSeqFatura(numSeqFatura);
        super.setNumSeqFatura(numSeqFatura);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setNumSerie(int)
     */
    @Override
    public void setNumSerie(int numSerie) {

        stubFatura.setNumSerie(numSerie);
        super.setNumSerie(numSerie);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setNifEmissor(int)
     */
    @Override
    public void setNifEmissor(int nifEmissor) {

        stubFatura.setNifEmissor(nifEmissor);
        super.setNifEmissor(nifEmissor);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setNomeEmissor(java.lang.String)
     */
    @Override
    public void setNomeEmissor(String nomeEmissor) {

        stubFatura.setNomeEmissor(nomeEmissor);
        super.setNomeEmissor(nomeEmissor);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setNifCliente(int)
     */
    @Override
    public void setNifCliente(Integer nifCliente) {

        stubFatura.setNifCliente(nifCliente);
        super.setNifCliente(nifCliente);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setIva(int)
     */
    @Override
    public void setIva(int iva) {

        stubFatura.setIva(iva);
        super.setIva(iva);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.Fatura_Base#setTotal(int)
     */
    @Override
    public void setTotal(int total) {

        stubFatura.setTotal(total);
        super.setTotal(total);
    }

    public XMLGregorianCalendar getGregorianData() {
        return XMLCalendarToDate.toXMLGregorianCalendar(this.getData().toDate());
    }

    public pt.registofatura.ws.Fatura getRemoteFatura() {
        return this.stubFatura;
    }

}
