/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author humbe
 * MEnsagemDOJogo é uma mensagem que deve ser pasada entre cliente e servidor
 */
public class MensagemDoJogo extends Mensagem implements Serializable{

    private int numeroDeButtons;
    private ImageIcon[] temp;
    private int rand;
    private boolean telaTravada;
    private int turno;
    private int numeroDoJogador;
    private int botaoClicado1;
    private int botaoClicado2;
    private boolean botao;
    

    public MensagemDoJogo() {
    }

    public MensagemDoJogo(int numButtons, ImageIcon[] icon, boolean telaTravada) {
        this.numeroDeButtons = numButtons;
        this.temp = icon;
        this.telaTravada = telaTravada;
    }
    public MensagemDoJogo(int numButtons, ImageIcon[] icon) {
        this.numeroDeButtons = numButtons;
        this.temp = icon;
    }

    @Override
    public boolean isTelaTravada() {
        return telaTravada;
    }

    @Override
    public void setTelaTravada(boolean telaTravada) {
        this.telaTravada = telaTravada;
    }
    

    public int getNumeroDeButtons() {
        return numeroDeButtons;
    }

    public void setNumeroDeButtons(int numeroDeButtons) {
        this.numeroDeButtons = numeroDeButtons;
    }

    public ImageIcon[] getTemp() {
        return temp;
    }

    public void setTemp(ImageIcon[] temp) {
        this.temp = temp;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getNumeroDoJogador() {
        return numeroDoJogador;
    }

    public void setNumeroDoJogador(int numeroDoJogador) {
        this.numeroDoJogador = numeroDoJogador;
    }

    public int getBotaoClicado1() {
        return botaoClicado1;
    }

    public void setBotaoClicado1(int botaoClicado1) {
        this.botaoClicado1 = botaoClicado1;
    }

    public int getBotaoClicado2() {
        return botaoClicado2;
    }

    public void setBotaoClicado2(int botaoClicado2) {
        this.botaoClicado2 = botaoClicado2;
    }

    public boolean isBotao() {
        return botao;
    }

    public void setBotao(boolean botao) {
        this.botao = botao;
    }

    
}
