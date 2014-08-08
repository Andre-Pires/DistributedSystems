package registofatura.ws.domain;

import pt.ist.fenixframework.pstm.VBox;
import pt.ist.fenixframework.pstm.RelationList;
import pt.ist.fenixframework.pstm.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializationGenerator.*;
public abstract class Fatura_Base extends pt.ist.fenixframework.pstm.OneBoxDomainObject {
    public final static pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Fatura,registofatura.ws.domain.Serie> role$$serie = new pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Fatura,registofatura.ws.domain.Serie>() {
        public registofatura.ws.domain.Serie getValue(registofatura.ws.domain.Fatura o1) {
            return ((Fatura_Base.DO_State)o1.get$obj$state(false)).serie;
        }
        public void setValue(registofatura.ws.domain.Fatura o1, registofatura.ws.domain.Serie o2) {
            ((Fatura_Base.DO_State)o1.get$obj$state(true)).serie = o2;
        }
        public dml.runtime.Role<registofatura.ws.domain.Serie,registofatura.ws.domain.Fatura> getInverseRole() {
            return registofatura.ws.domain.Serie.role$$fatura;
        }
        
    };
    public final static dml.runtime.RoleMany<registofatura.ws.domain.Fatura,registofatura.ws.domain.ItemFatura> role$$itemFatura = new dml.runtime.RoleMany<registofatura.ws.domain.Fatura,registofatura.ws.domain.ItemFatura>() {
        public dml.runtime.RelationBaseSet<registofatura.ws.domain.ItemFatura> getSet(registofatura.ws.domain.Fatura o1) {
            return ((Fatura_Base)o1).get$rl$itemFatura();
        }
        public dml.runtime.Role<registofatura.ws.domain.ItemFatura,registofatura.ws.domain.Fatura> getInverseRole() {
            return registofatura.ws.domain.ItemFatura.role$$fatura;
        }
        
    };
    public final static pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Fatura,registofatura.ws.domain.Serie> SerieContainsFatura = new pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Fatura,registofatura.ws.domain.Serie>(role$$serie);
    static {
        registofatura.ws.domain.Serie.SerieContainsFatura = SerieContainsFatura.getInverseRelation();
    }
    
    static {
        SerieContainsFatura.setRelationName("registofatura.ws.domain.Fatura.SerieContainsFatura");
    }
    public static dml.runtime.Relation<registofatura.ws.domain.Fatura,registofatura.ws.domain.ItemFatura> FaturaContainsItemFatura;
    
    
    private RelationList<registofatura.ws.domain.Fatura,registofatura.ws.domain.ItemFatura> get$rl$itemFatura() {
        return get$$relationList("itemFatura", FaturaContainsItemFatura);
        
    }
    
    
    private void initInstance() {
        initInstance(true);
    }
    
    private void initInstance(boolean allocateOnly) {
        
    }
    
    {
        initInstance(false);
    }
    
    protected  Fatura_Base() {
        super();
    }
    
    public org.joda.time.DateTime getData() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "data");
        return ((DO_State)this.get$obj$state(false)).data;
    }
    
    public void setData(org.joda.time.DateTime data) {
        ((DO_State)this.get$obj$state(true)).data = data;
    }
    
    private java.sql.Timestamp get$data() {
        org.joda.time.DateTime value = ((DO_State)this.get$obj$state(false)).data;
        return (value == null) ? null : pt.ist.fenixframework.pstm.ToSqlConverter.getValueForDateTime(value);
    }
    
    private final void set$data(org.joda.time.DateTime arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).data = (org.joda.time.DateTime)((arg0 == null) ? null : arg0);
    }
    
    public int getNumSeqFatura() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "numSeqFatura");
        return ((DO_State)this.get$obj$state(false)).numSeqFatura;
    }
    
    public void setNumSeqFatura(int numSeqFatura) {
        ((DO_State)this.get$obj$state(true)).numSeqFatura = numSeqFatura;
    }
    
    private int get$numSeqFatura() {
        int value = ((DO_State)this.get$obj$state(false)).numSeqFatura;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$numSeqFatura(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).numSeqFatura = (int)(arg0);
    }
    
    public int getNumSerie() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "numSerie");
        return ((DO_State)this.get$obj$state(false)).numSerie;
    }
    
    public void setNumSerie(int numSerie) {
        ((DO_State)this.get$obj$state(true)).numSerie = numSerie;
    }
    
    private int get$numSerie() {
        int value = ((DO_State)this.get$obj$state(false)).numSerie;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$numSerie(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).numSerie = (int)(arg0);
    }
    
    public int getNifEmissor() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nifEmissor");
        return ((DO_State)this.get$obj$state(false)).nifEmissor;
    }
    
    public void setNifEmissor(int nifEmissor) {
        ((DO_State)this.get$obj$state(true)).nifEmissor = nifEmissor;
    }
    
    private int get$nifEmissor() {
        int value = ((DO_State)this.get$obj$state(false)).nifEmissor;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$nifEmissor(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nifEmissor = (int)(arg0);
    }
    
    public java.lang.String getNomeEmissor() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nomeEmissor");
        return ((DO_State)this.get$obj$state(false)).nomeEmissor;
    }
    
    public void setNomeEmissor(java.lang.String nomeEmissor) {
        ((DO_State)this.get$obj$state(true)).nomeEmissor = nomeEmissor;
    }
    
    private java.lang.String get$nomeEmissor() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).nomeEmissor;
        return (value == null) ? null : pt.ist.fenixframework.pstm.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$nomeEmissor(java.lang.String arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nomeEmissor = (java.lang.String)((arg0 == null) ? null : arg0);
    }
    
    public java.lang.Integer getNifCliente() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nifCliente");
        return ((DO_State)this.get$obj$state(false)).nifCliente;
    }
    
    public void setNifCliente(java.lang.Integer nifCliente) {
        ((DO_State)this.get$obj$state(true)).nifCliente = nifCliente;
    }
    
    private java.lang.Integer get$nifCliente() {
        java.lang.Integer value = ((DO_State)this.get$obj$state(false)).nifCliente;
        return (value == null) ? null : pt.ist.fenixframework.pstm.ToSqlConverter.getValueForInteger(value);
    }
    
    private final void set$nifCliente(java.lang.Integer arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nifCliente = (java.lang.Integer)((arg0 == null) ? null : arg0);
    }
    
    public int getIva() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "iva");
        return ((DO_State)this.get$obj$state(false)).iva;
    }
    
    public void setIva(int iva) {
        ((DO_State)this.get$obj$state(true)).iva = iva;
    }
    
    private int get$iva() {
        int value = ((DO_State)this.get$obj$state(false)).iva;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$iva(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).iva = (int)(arg0);
    }
    
    public int getTotal() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "total");
        return ((DO_State)this.get$obj$state(false)).total;
    }
    
    public void setTotal(int total) {
        ((DO_State)this.get$obj$state(true)).total = total;
    }
    
    private int get$total() {
        int value = ((DO_State)this.get$obj$state(false)).total;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$total(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).total = (int)(arg0);
    }
    
    public registofatura.ws.domain.Serie getSerie() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "serie");
        return ((DO_State)this.get$obj$state(false)).serie;
    }
    
    public void setSerie(registofatura.ws.domain.Serie serie) {
        SerieContainsFatura.add((registofatura.ws.domain.Fatura)this, serie);
    }
    
    public boolean hasSerie() {
        return (getSerie() != null);
    }
    
    public void removeSerie() {
        setSerie(null);
    }
    
    private java.lang.Long get$oidSerie() {
        pt.ist.fenixframework.pstm.AbstractDomainObject value = ((DO_State)this.get$obj$state(false)).serie;
        return (value == null) ? null : value.getOid();
    }
    
    public int getItemFaturaCount() {
        return get$rl$itemFatura().size();
    }
    
    public boolean hasAnyItemFatura() {
        return (! get$rl$itemFatura().isEmpty());
    }
    
    public boolean hasItemFatura(registofatura.ws.domain.ItemFatura itemFatura) {
        return get$rl$itemFatura().contains(itemFatura);
    }
    
    public java.util.Set<registofatura.ws.domain.ItemFatura> getItemFaturaSet() {
        return get$rl$itemFatura();
    }
    
    public void addItemFatura(registofatura.ws.domain.ItemFatura itemFatura) {
        FaturaContainsItemFatura.add((registofatura.ws.domain.Fatura)this, itemFatura);
    }
    
    public void removeItemFatura(registofatura.ws.domain.ItemFatura itemFatura) {
        FaturaContainsItemFatura.remove((registofatura.ws.domain.Fatura)this, itemFatura);
    }
    
    public java.util.List<registofatura.ws.domain.ItemFatura> getItemFatura() {
        return get$rl$itemFatura();
    }
    
    public void set$itemFatura(OJBFunctionalSetWrapper itemFatura) {
        get$rl$itemFatura().setFromOJB(this, "itemFatura", itemFatura);
    }
    
    public java.util.Iterator<registofatura.ws.domain.ItemFatura> getItemFaturaIterator() {
        return get$rl$itemFatura().iterator();
    }
    
    protected void checkDisconnected() {
        if (hasSerie()) handleAttemptToDeleteConnectedObject();
        if (hasAnyItemFatura()) handleAttemptToDeleteConnectedObject();
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$data(pt.ist.fenixframework.pstm.ResultSetReader.readDateTime(rs, "DATA"), state);
        set$numSeqFatura(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NUM_SEQ_FATURA"), state);
        set$numSerie(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NUM_SERIE"), state);
        set$nifEmissor(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NIF_EMISSOR"), state);
        set$nomeEmissor(pt.ist.fenixframework.pstm.ResultSetReader.readString(rs, "NOME_EMISSOR"), state);
        set$nifCliente(pt.ist.fenixframework.pstm.ResultSetReader.readInteger(rs, "NIF_CLIENTE"), state);
        set$iva(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "IVA"), state);
        set$total(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "TOTAL"), state);
        castedState.serie = pt.ist.fenixframework.pstm.ResultSetReader.readDomainObject(rs, "OID_SERIE");
    }
    protected dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("itemFatura")) return FaturaContainsItemFatura;
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("itemFatura", FaturaContainsItemFatura);
        
    }
    protected static class DO_State extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State {
        private org.joda.time.DateTime data;
        private int numSeqFatura;
        private int numSerie;
        private int nifEmissor;
        private java.lang.String nomeEmissor;
        private java.lang.Integer nifCliente;
        private int iva;
        private int total;
        private registofatura.ws.domain.Serie serie;
        protected void copyTo(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.data = this.data;
            newCasted.numSeqFatura = this.numSeqFatura;
            newCasted.numSerie = this.numSerie;
            newCasted.nifEmissor = this.nifEmissor;
            newCasted.nomeEmissor = this.nomeEmissor;
            newCasted.nifCliente = this.nifCliente;
            newCasted.iva = this.iva;
            newCasted.total = this.total;
            newCasted.serie = this.serie;
            
        }
        
        // serialization code
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return new SerializedForm(this);
        }
        
        protected static class SerializedForm extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State.SerializedForm {
            private static final long serialVersionUID = 1L;
            
            private org.joda.time.DateTime data;
            private int numSeqFatura;
            private int numSerie;
            private int nifEmissor;
            private java.lang.String nomeEmissor;
            private java.lang.Integer nifCliente;
            private int iva;
            private int total;
            private registofatura.ws.domain.Serie serie;
            
            protected  SerializedForm(DO_State obj) {
                super(obj);
                this.data = obj.data;
                this.numSeqFatura = obj.numSeqFatura;
                this.numSerie = obj.numSerie;
                this.nifEmissor = obj.nifEmissor;
                this.nomeEmissor = obj.nomeEmissor;
                this.nifCliente = obj.nifCliente;
                this.iva = obj.iva;
                this.total = obj.total;
                this.serie = obj.serie;
                
            }
            
             Object readResolve() throws java.io.ObjectStreamException {
                DO_State newState = new DO_State();
                fillInState(newState);
                return newState;
            }
            
            protected void fillInState(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State obj) {
                super.fillInState(obj);
                DO_State state = (DO_State)obj;
                state.data = this.data;
                state.numSeqFatura = this.numSeqFatura;
                state.numSerie = this.numSerie;
                state.nifEmissor = this.nifEmissor;
                state.nomeEmissor = this.nomeEmissor;
                state.nifCliente = this.nifCliente;
                state.iva = this.iva;
                state.total = this.total;
                state.serie = this.serie;
                
            }
            
        }
        
    }
    
}
