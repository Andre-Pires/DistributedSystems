package registofatura.ws.domain;

import pt.ist.fenixframework.pstm.VBox;
import pt.ist.fenixframework.pstm.RelationList;
import pt.ist.fenixframework.pstm.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializationGenerator.*;
public abstract class Emissor_Base extends pt.ist.fenixframework.pstm.OneBoxDomainObject {
    public final static pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Emissor,registofatura.ws.domain.RegistoFatura> role$$registoFatura = new pt.ist.fenixframework.pstm.dml.RoleOne<registofatura.ws.domain.Emissor,registofatura.ws.domain.RegistoFatura>() {
        public registofatura.ws.domain.RegistoFatura getValue(registofatura.ws.domain.Emissor o1) {
            return ((Emissor_Base.DO_State)o1.get$obj$state(false)).registoFatura;
        }
        public void setValue(registofatura.ws.domain.Emissor o1, registofatura.ws.domain.RegistoFatura o2) {
            ((Emissor_Base.DO_State)o1.get$obj$state(true)).registoFatura = o2;
        }
        public dml.runtime.Role<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Emissor> getInverseRole() {
            return registofatura.ws.domain.RegistoFatura.role$$emissor;
        }
        
    };
    public final static pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Emissor,registofatura.ws.domain.RegistoFatura> RegistoFaturaContainsEmissor = new pt.ist.fenixframework.pstm.LoggingRelation<registofatura.ws.domain.Emissor,registofatura.ws.domain.RegistoFatura>(role$$registoFatura);
    static {
        registofatura.ws.domain.RegistoFatura.RegistoFaturaContainsEmissor = RegistoFaturaContainsEmissor.getInverseRelation();
    }
    
    static {
        RegistoFaturaContainsEmissor.setRelationName("registofatura.ws.domain.Emissor.RegistoFaturaContainsEmissor");
    }
    
    
    
    
    private void initInstance() {
        initInstance(true);
    }
    
    private void initInstance(boolean allocateOnly) {
        
    }
    
    {
        initInstance(false);
    }
    
    protected  Emissor_Base() {
        super();
    }
    
    public java.lang.String getNome() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nome");
        return ((DO_State)this.get$obj$state(false)).nome;
    }
    
    public void setNome(java.lang.String nome) {
        ((DO_State)this.get$obj$state(true)).nome = nome;
    }
    
    private java.lang.String get$nome() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).nome;
        return (value == null) ? null : pt.ist.fenixframework.pstm.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$nome(java.lang.String arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nome = (java.lang.String)((arg0 == null) ? null : arg0);
    }
    
    public int getNif() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nif");
        return ((DO_State)this.get$obj$state(false)).nif;
    }
    
    public void setNif(int nif) {
        ((DO_State)this.get$obj$state(true)).nif = nif;
    }
    
    private int get$nif() {
        int value = ((DO_State)this.get$obj$state(false)).nif;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$nif(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nif = (int)(arg0);
    }
    
    public registofatura.ws.domain.RegistoFatura getRegistoFatura() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "registoFatura");
        return ((DO_State)this.get$obj$state(false)).registoFatura;
    }
    
    public void setRegistoFatura(registofatura.ws.domain.RegistoFatura registoFatura) {
        RegistoFaturaContainsEmissor.add((registofatura.ws.domain.Emissor)this, registoFatura);
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
    
    protected void checkDisconnected() {
        if (hasRegistoFatura()) handleAttemptToDeleteConnectedObject();
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$nome(pt.ist.fenixframework.pstm.ResultSetReader.readString(rs, "NOME"), state);
        set$nif(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NIF"), state);
        castedState.registoFatura = pt.ist.fenixframework.pstm.ResultSetReader.readDomainObject(rs, "OID_REGISTO_FATURA");
    }
    protected dml.runtime.Relation get$$relationFor(String attrName) {
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        
    }
    protected static class DO_State extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String nome;
        private int nif;
        private registofatura.ws.domain.RegistoFatura registoFatura;
        protected void copyTo(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.nome = this.nome;
            newCasted.nif = this.nif;
            newCasted.registoFatura = this.registoFatura;
            
        }
        
        // serialization code
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return new SerializedForm(this);
        }
        
        protected static class SerializedForm extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State.SerializedForm {
            private static final long serialVersionUID = 1L;
            
            private java.lang.String nome;
            private int nif;
            private registofatura.ws.domain.RegistoFatura registoFatura;
            
            protected  SerializedForm(DO_State obj) {
                super(obj);
                this.nome = obj.nome;
                this.nif = obj.nif;
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
                state.nome = this.nome;
                state.nif = this.nif;
                state.registoFatura = this.registoFatura;
                
            }
            
        }
        
    }
    
}
