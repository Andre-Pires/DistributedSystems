package registofatura.ws.domain;

import pt.ist.fenixframework.pstm.VBox;
import pt.ist.fenixframework.pstm.RelationList;
import pt.ist.fenixframework.pstm.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializationGenerator.*;
public abstract class Serie_Base extends pt.ist.fenixframework.pstm.OneBoxDomainObject {
    public final static pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Serie,registofatura.ws.domain.RegistoFatura> role$$registoFatura = new pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Serie,registofatura.ws.domain.RegistoFatura>() {
        public registofatura.ws.domain.RegistoFatura getValue(registofatura.ws.domain.Serie o1) {
            return ((Serie_Base.DO_State)o1.get$obj$state(false)).registoFatura;
        }
        public void setValue(registofatura.ws.domain.Serie o1, registofatura.ws.domain.RegistoFatura o2) {
            ((Serie_Base.DO_State)o1.get$obj$state(true)).registoFatura = o2;
        }
        public dml.runtime.Role<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Serie> getInverseRole() {
            return registofatura.ws.domain.RegistoFatura.role$$serie;
        }
        
    };
    public final static dml.runtime.RoleMany<registofatura.ws.domain.Serie,registofatura.ws.domain.Fatura> role$$fatura = new dml.runtime.RoleMany<registofatura.ws.domain.Serie,registofatura.ws.domain.Fatura>() {
        public dml.runtime.RelationBaseSet<registofatura.ws.domain.Fatura> getSet(registofatura.ws.domain.Serie o1) {
            return ((Serie_Base)o1).get$rl$fatura();
        }
        public dml.runtime.Role<registofatura.ws.domain.Fatura,registofatura.ws.domain.Serie> getInverseRole() {
            return registofatura.ws.domain.Fatura.role$$serie;
        }
        
    };
    public final static pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Serie,registofatura.ws.domain.RegistoFatura> RegistoFaturaContainsSerie = new pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Serie,registofatura.ws.domain.RegistoFatura>(role$$registoFatura);
    static {
        registofatura.ws.domain.RegistoFatura.RegistoFaturaContainsSerie = RegistoFaturaContainsSerie.getInverseRelation();
    }
    
    static {
        RegistoFaturaContainsSerie.setRelationName("registofatura.ws.domain.Serie.RegistoFaturaContainsSerie");
    }
    public static dml.runtime.Relation<registofatura.ws.domain.Serie,registofatura.ws.domain.Fatura> SerieContainsFatura;
    
    
    private RelationList<registofatura.ws.domain.Serie,registofatura.ws.domain.Fatura> get$rl$fatura() {
        return get$$relationList("fatura", SerieContainsFatura);
        
    }
    
    
    private void initInstance() {
        initInstance(true);
    }
    
    private void initInstance(boolean allocateOnly) {
        
    }
    
    {
        initInstance(false);
    }
    
    protected  Serie_Base() {
        super();
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
    
    public org.joda.time.DateTime getValidoAte() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "validoAte");
        return ((DO_State)this.get$obj$state(false)).validoAte;
    }
    
    public void setValidoAte(org.joda.time.DateTime validoAte) {
        ((DO_State)this.get$obj$state(true)).validoAte = validoAte;
    }
    
    private java.sql.Timestamp get$validoAte() {
        org.joda.time.DateTime value = ((DO_State)this.get$obj$state(false)).validoAte;
        return (value == null) ? null : pt.ist.fenixframework.pstm.ToSqlConverter.getValueForDateTime(value);
    }
    
    private final void set$validoAte(org.joda.time.DateTime arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).validoAte = (org.joda.time.DateTime)((arg0 == null) ? null : arg0);
    }
    
    public registofatura.ws.domain.RegistoFatura getRegistoFatura() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "registoFatura");
        return ((DO_State)this.get$obj$state(false)).registoFatura;
    }
    
    public void setRegistoFatura(registofatura.ws.domain.RegistoFatura registoFatura) {
        RegistoFaturaContainsSerie.add((registofatura.ws.domain.Serie)this, registoFatura);
    }
    
    public boolean hasRegistoFatura() {
        return (getRegistoFatura() != null);
    }
    
    public void removeRegistoFatura() {
        setRegistoFatura(null);
    }
    
    private java.lang.Long get$oidRegistoFatura() {
        pt.ist.fenixframework.pstm.AbstractDomainObject value = ((DO_State)this.get$obj$state(false)).registoFatura;
        return (value == null) ? null : value.getOid();
    }
    
    public int getFaturaCount() {
        return get$rl$fatura().size();
    }
    
    public boolean hasAnyFatura() {
        return (! get$rl$fatura().isEmpty());
    }
    
    public boolean hasFatura(registofatura.ws.domain.Fatura fatura) {
        return get$rl$fatura().contains(fatura);
    }
    
    public java.util.Set<registofatura.ws.domain.Fatura> getFaturaSet() {
        return get$rl$fatura();
    }
    
    public void addFatura(registofatura.ws.domain.Fatura fatura) {
        SerieContainsFatura.add((registofatura.ws.domain.Serie)this, fatura);
    }
    
    public void removeFatura(registofatura.ws.domain.Fatura fatura) {
        SerieContainsFatura.remove((registofatura.ws.domain.Serie)this, fatura);
    }
    
    public java.util.List<registofatura.ws.domain.Fatura> getFatura() {
        return get$rl$fatura();
    }
    
    public void set$fatura(OJBFunctionalSetWrapper fatura) {
        get$rl$fatura().setFromOJB(this, "fatura", fatura);
    }
    
    public java.util.Iterator<registofatura.ws.domain.Fatura> getFaturaIterator() {
        return get$rl$fatura().iterator();
    }
    
    @jvstm.cps.ConsistencyPredicate
    public final boolean checkMultiplicityOfFatura() {
        if (get$rl$fatura().size() > 4) return false;
        return true;
    }
    
    protected void checkDisconnected() {
        if (hasRegistoFatura()) handleAttemptToDeleteConnectedObject();
        if (hasAnyFatura()) handleAttemptToDeleteConnectedObject();
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$nifEmissor(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NIF_EMISSOR"), state);
        set$numSerie(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NUM_SERIE"), state);
        set$validoAte(pt.ist.fenixframework.pstm.ResultSetReader.readDateTime(rs, "VALIDO_ATE"), state);
        castedState.registoFatura = pt.ist.fenixframework.pstm.ResultSetReader.readDomainObject(rs, "OID_REGISTO_FATURA");
    }
    protected dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("fatura")) return SerieContainsFatura;
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("fatura", SerieContainsFatura);
        
    }
    protected static class DO_State extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State {
        private int nifEmissor;
        private int numSerie;
        private org.joda.time.DateTime validoAte;
        private registofatura.ws.domain.RegistoFatura registoFatura;
        protected void copyTo(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.nifEmissor = this.nifEmissor;
            newCasted.numSerie = this.numSerie;
            newCasted.validoAte = this.validoAte;
            newCasted.registoFatura = this.registoFatura;
            
        }
        
        // serialization code
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return new SerializedForm(this);
        }
        
        protected static class SerializedForm extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State.SerializedForm {
            private static final long serialVersionUID = 1L;
            
            private int nifEmissor;
            private int numSerie;
            private org.joda.time.DateTime validoAte;
            private registofatura.ws.domain.RegistoFatura registoFatura;
            
            protected  SerializedForm(DO_State obj) {
                super(obj);
                this.nifEmissor = obj.nifEmissor;
                this.numSerie = obj.numSerie;
                this.validoAte = obj.validoAte;
                this.registoFatura = obj.registoFatura;
                
            }
            
             Object readResolve() throws java.io.ObjectStreamException {
                DO_State newState = new DO_State();
                fillInState(newState);
                return newState;
            }
            
            protected void fillInState(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State obj) {
                super.fillInState(obj);
                DO_State state = (DO_State)obj;
                state.nifEmissor = this.nifEmissor;
                state.numSerie = this.numSerie;
                state.validoAte = this.validoAte;
                state.registoFatura = this.registoFatura;
                
            }
            
        }
        
    }
    
}
