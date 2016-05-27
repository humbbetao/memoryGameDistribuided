/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

/**
 *
 * @author humbe
 */
public class MensagemDeFimDeJogo {

    boolean vencedor;

    public MensagemDeFimDeJogo() {
    }
    public MensagemDeFimDeJogo(boolean vencedor) {
        this.vencedor = vencedor;
    }

    public boolean isVencedor() {
        return vencedor;
    }

    public void setVencedor(boolean vencedor) {
        this.vencedor = vencedor;
    }
}
