/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import javax.swing.ImageIcon;

/**
 *
 * @author humbe
 */
public class MensagemDeInicioDeJogo {

    private String modoDeJogo;
    private boolean telaTravada;
    private int turno;
    private ImageIcon[] temp;
    private int numeroDeButtonNatela;

    public MensagemDeInicioDeJogo() {
    }

    public MensagemDeInicioDeJogo(String modoDeJogo, boolean telaTravada, int turno, ImageIcon[] temp) {
        this.modoDeJogo = modoDeJogo;
        this.telaTravada = telaTravada;
        this.turno = turno;
        this.temp = temp;
    }

    public int getNumeroDeButtonNatela() {
        return numeroDeButtonNatela;
    }

    public void setNumeroDeButtonNatela(int numeroDeButtonNatela) {
        this.numeroDeButtonNatela = numeroDeButtonNatela;
    }
    

    public String getModoDeJogo() {
        return modoDeJogo;
    }

    public void setModoDeJogo(String modoDeJogo) {
        this.modoDeJogo = modoDeJogo;
    }

    public boolean isTelaTravada() {
        return telaTravada;
    }

    public void setTelaTravada(boolean telaTravada) {
        this.telaTravada = telaTravada;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public ImageIcon[] getTemp() {
        return temp;
    }

    public void setTemp(ImageIcon[] temp) {
        this.temp = temp;
    }
    
    
}
