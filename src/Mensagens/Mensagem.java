package Mensagens;

import java.io.Serializable;

/**
 *
 * @author humbe
 * Mensagem é uma mensagem que deve ser serializada e passa
 */
public class Mensagem implements Serializable {

    private String mensagem;
    private boolean telaTravada;
    private int turno;

    public Mensagem() {
    }

    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Mensagem(String mensagem, boolean telaTravada, int turno) {
        this.mensagem = mensagem;
        this.telaTravada = telaTravada;
        this.turno = turno;
    }

    public Mensagem(String novoJogo, int turno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isTelaTravada() {
        return telaTravada;
    }

    public void setTelaTravada(boolean telaTravada) {
        this.telaTravada = telaTravada;
    }

    @Override
    public String toString() {
        return "Mensagem{" + "mensagem=" + mensagem + ", telaTravada=" + telaTravada + '}';
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }
    

}
