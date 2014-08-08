package registofatura.ws.domain;


public class ItemFatura extends
        ItemFatura_Base {

    private final pt.registofatura.ws.ItemFatura stubItemFatura;

    public ItemFatura(String descricao, int quantidade, int precoTotal) {
        super();
        this.stubItemFatura = new pt.registofatura.ws.ItemFatura();
        this.setDescricao(descricao);
        this.setQuantidade(quantidade);
        this.setPreco(precoTotal);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.ItemFatura_Base#setDescricao(java.lang.String)
     */
    @Override
    public void setDescricao(String descricao) {

        stubItemFatura.setDescricao(descricao);
        super.setDescricao(descricao);
    }

    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.ItemFatura_Base#setQuantidade(int)
     */
    @Override
    public void setQuantidade(int quantidade) {

        stubItemFatura.setQuantidade(quantidade);
        super.setQuantidade(quantidade);
    }


    /* (non-Javadoc)
     * @see ff.pt.registofatura.ws.ItemFatura_Base#setPreco(int)
     */
    @Override
    public void setPreco(int precoTotal) {

        stubItemFatura.setPreco(precoTotal);
        super.setPreco(precoTotal);
    }

    public pt.registofatura.ws.ItemFatura getRemoteItemFatura() {
        return this.stubItemFatura;
    }
}
