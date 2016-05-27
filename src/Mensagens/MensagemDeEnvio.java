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

    public int getNumeroAberto() {
        return numeroAberto;
    }

    public void setNumeroAberto(int numeroAberto) {
        this.numeroAberto = numeroAberto;
    }
    
}
