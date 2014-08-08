package registofatura.ws.domain;

import pt.ist.fenixframework.pstm.VBox;
import pt.ist.fenixframework.pstm.RelationList;
import pt.ist.fenixframework.pstm.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializationGenerator.*;
public abstract class RegistoFatura_Base extends pt.ist.fenixframework.pstm.OneBoxDomainObject {
    public final static dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Cliente> role$$cliente = new dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Cliente>() {
        public dml.runtime.RelationBaseSet<registofatura.ws.domain.Cliente> getSet(registofatura.ws.domain.RegistoFatura o1) {
            return ((RegistoFatura_Base)o1).get$rl$cliente();
        }
        public dml.runtime.Role<registofatura.ws.domain.Cliente,registofatura.ws.domain.RegistoFatura> getInverseRole() {
            return registofatura.ws.domain.Cliente.role$$registoFatura;
        }
        
    };
    public final static dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Emissor> role$$emissor = new dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Emissor>() {
        public dml.runtime.RelationBaseSet<registofatura.ws.domain.Emissor> getSet(registofatura.ws.domain.RegistoFatura o1) {
            return ((RegistoFatura_Base)o1).get$rl$emissor();
        }
        public dml.runtime.Role<registofatura.ws.domain.Emissor,registofatura.ws.domain.RegistoFatura> getInverseRole() {
            return registofatura.ws.domain.Emissor.role$$registoFatura;
        }
        
    };
    public final static dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Serie> role$$serie = new dml.runtime.RoleMany<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Serie>() {
        public dml.runtime.RelationBaseSet<registofatura.ws.domain.Serie> getSet(registofatura.ws.domain.RegistoFatura o1) {
            return ((RegistoFatura_Base)o1).get$rl$serie();
        }
        public dml.runtime.Role<registofatura.ws.domain.Serie,registofatura.ws.domain.RegistoFatura> getInverseRole() {
            return registofatura.ws.domain.Serie.role$$registoFatura;
        }
        
    };
    public static dml.runtime.Relation<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Cliente> RegistoFaturaContainsCliente;
    public static dml.runtime.Relation<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Emissor> RegistoFaturaContainsEmissor;
    public static dml.runtime.Relation<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Serie> RegistoFaturaContainsSerie;
    
    
    private RelationList<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Cliente> get$rl$cliente() {
        return get$$relationList("cliente", RegistoFaturaContainsCliente);
        
    }
    private RelationList<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Emissor> get$rl$emissor() {
        return get$$relationList("emissor", RegistoFaturaContainsEmissor);
        
    }
    private RelationList<registofatura.ws.domain.RegistoFatura,registofatura.ws.domain.Serie> get$rl$serie() {
        return get$$relationList("serie", RegistoFaturaContainsSerie);
        
    }
    
    
    private void initInstance() {
        initInstance(true);
    }
    
    private void initInstance(boolean allocateOnly) {
        
    }
    
    {
        initInstance(false);
    }
    
    protected  RegistoFatura_Base() {
        super();
    }
    
    public int getNextNumSerie() {
        pt.ist.fenixframework.pstm.DataAccessPatterns.noteGetAccess(this, "nextNumSerie");
        return ((DO_State)this.get$obj$state(false)).nextNumSerie;
    }
    
    public void setNextNumSerie(int nextNumSerie) {
        ((DO_State)this.get$obj$state(true)).nextNumSerie = nextNumSerie;
    }
    
    private int get$nextNumSerie() {
        int value = ((DO_State)this.get$obj$state(false)).nextNumSerie;
        return pt.ist.fenixframework.pstm.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$nextNumSerie(int arg0, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).nextNumSerie = (int)(arg0);
    }
    
    public int getClienteCount() {
        return get$rl$cliente().size();
    }
    
    public boolean hasAnyCliente() {
        return (! get$rl$cliente().isEmpty());
    }
    
    public boolean hasCliente(registofatura.ws.domain.Cliente cliente) {
        return get$rl$cliente().contains(cliente);
    }
    
    public java.util.Set<registofatura.ws.domain.Cliente> getClienteSet() {
        return get$rl$cliente();
    }
    
    public void addCliente(registofatura.ws.domain.Cliente cliente) {
        RegistoFaturaContainsCliente.add((registofatura.ws.domain.RegistoFatura)this, cliente);
    }
    
    public void removeCliente(registofatura.ws.domain.Cliente cliente) {
        RegistoFaturaContainsCliente.remove((registofatura.ws.domain.RegistoFatura)this, cliente);
    }
    
    public java.util.List<registofatura.ws.domain.Cliente> getCliente() {
        return get$rl$cliente();
    }
    
    public void set$cliente(OJBFunctionalSetWrapper cliente) {
        get$rl$cliente().setFromOJB(this, "cliente", cliente);
    }
    
    public java.util.Iterator<registofatura.ws.domain.Cliente> getClienteIterator() {
        return get$rl$cliente().iterator();
    }
    
    public int getEmissorCount() {
        return get$rl$emissor().size();
    }
    
    public boolean hasAnyEmissor() {
        return (! get$rl$emissor().isEmpty());
    }
    
    public boolean hasEmissor(registofatura.ws.domain.Emissor emissor) {
        return get$rl$emissor().contains(emissor);
    }
    
    public java.util.Set<registofatura.ws.domain.Emissor> getEmissorSet() {
        return get$rl$emissor();
    }
    
    public void addEmissor(registofatura.ws.domain.Emissor emissor) {
        RegistoFaturaContainsEmissor.add((registofatura.ws.domain.RegistoFatura)this, emissor);
    }
    
    public void removeEmissor(registofatura.ws.domain.Emissor emissor) {
        RegistoFaturaContainsEmissor.remove((registofatura.ws.domain.RegistoFatura)this, emissor);
    }
    
    public java.util.List<registofatura.ws.domain.Emissor> getEmissor() {
        return get$rl$emissor();
    }
    
    public void set$emissor(OJBFunctionalSetWrapper emissor) {
        get$rl$emissor().setFromOJB(this, "emissor", emissor);
    }
    
    public java.util.Iterator<registofatura.ws.domain.Emissor> getEmissorIterator() {
        return get$rl$emissor().iterator();
    }
    
    public int getSerieCount() {
        return get$rl$serie().size();
    }
    
    public boolean hasAnySerie() {
        return (! get$rl$serie().isEmpty());
    }
    
    public boolean hasSerie(registofatura.ws.domain.Serie serie) {
        return get$rl$serie().contains(serie);
    }
    
    public java.util.Set<registofatura.ws.domain.Serie> getSerieSet() {
        return get$rl$serie();
    }
    
    public void addSerie(registofatura.ws.domain.Serie serie) {
        RegistoFaturaContainsSerie.add((registofatura.ws.domain.RegistoFatura)this, serie);
    }
    
    public void removeSerie(registofatura.ws.domain.Serie serie) {
        RegistoFaturaContainsSerie.remove((registofatura.ws.domain.RegistoFatura)this, serie);
    }
    
    public java.util.List<registofatura.ws.domain.Serie> getSerie() {
        return get$rl$serie();
    }
    
    public void set$serie(OJBFunctionalSetWrapper serie) {
        get$rl$serie().setFromOJB(this, "serie", serie);
    }
    
    public java.util.Iterator<registofatura.ws.domain.Serie> getSerieIterator() {
        return get$rl$serie().iterator();
    }
    
    protected void checkDisconnected() {
        if (hasAnyCliente()) handleAttemptToDeleteConnectedObject();
        if (hasAnyEmissor()) handleAttemptToDeleteConnectedObject();
        if (hasAnySerie()) handleAttemptToDeleteConnectedObject();
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$nextNumSerie(pt.ist.fenixframework.pstm.ResultSetReader.readint(rs, "NEXT_NUM_SERIE"), state);
    }
    protected dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("cliente")) return RegistoFaturaContainsCliente;
        if (attrName.equals("emissor")) return RegistoFaturaContainsEmissor;
        if (attrName.equals("serie")) return RegistoFaturaContainsSerie;
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("cliente", RegistoFaturaContainsCliente);
        get$$relationList("emissor", RegistoFaturaContainsEmissor);
        get$$relationList("serie", RegistoFaturaContainsSerie);
        
    }
    protected static class DO_State extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State {
        private int nextNumSerie;
        protected void copyTo(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.nextNumSerie = this.nextNumSerie;
            
        }
        
        // serialization code
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return new SerializedForm(this);
        }
        
        protected static class SerializedForm extends pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State.SerializedForm {
            private static final long serialVersionUID = 1L;
            
            private int nextNumSerie;
            
            protected  SerializedForm(DO_State obj) {
                super(obj);
                this.nextNumSerie = obj.nextNumSerie;
                
            }
            
             Object readResolve() throws java.io.ObjectStreamException {
                DO_State newState = new DO_State();
                fillInState(newState);
                return newState;
            }
            
            protected void fillInState(pt.ist.fenixframework.pstm.OneBoxDomainObject.DO_State obj) {
                super.fillInState(obj);
                DO_State state = (DO_State)obj;
                state.nextNumSerie = this.nextNumSerie;
                
            }
            
        }
        
    }
    
}
