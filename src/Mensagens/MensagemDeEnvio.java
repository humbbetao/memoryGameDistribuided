/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import java.io.Serializable;

/**
 *
 * @author humbe
 */
public class MensagemDeEnvio implements Serializable {

    private int numeroAberto;
    private boolean acertada;


    public int getNumeroAberto() {
        return numeroAberto;
    }

    public MensagemDeEnvio() {
    }

    public void setNumeroAberto(int numeroAberto) {
        this.numeroAberto = numeroAberto;
    }

    public MensagemDeEnvio(int numeroAberto, boolean acertada) {
        this.numeroAberto = numeroAberto;
        this.acertada = acertada;
    }

    public boolean isAcertada() {
        return acertada;
    }

    public void setAcertada(boolean acertada) {
        this.acertada = acertada;
    }

    @Override
    public String toString() {
        return "MensagemDeEnvio{" + "numeroAberto=" + numeroAberto + ", acertada=" + acertada + '}';
    }

    
}
