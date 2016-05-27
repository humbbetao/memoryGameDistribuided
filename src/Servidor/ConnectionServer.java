/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Cliente.MemoryGame;
import Mensagens.Mensagem;
import Mensagens.MensagemDeEnvio;
import Mensagens.MensagemDeFimDeJogo;
import Mensagens.MensagemDeInicioDeJogo;
import Mensagens.MensagemDoJogo;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author humberto ´É a Thread do lado do servidor
 */
public class ConnectionServer extends Thread {

    ArrayList<Socket> listaDeJogadoresDaPartida;

    ObjectInputStream inObject;
    ObjectOutputStream outObject;

    int numeroDeJogadores = 0;
    Mensagem m;
    MensagemDoJogo mJogo;
    MemoryGame memoryGame;

    int numButtons = 18;
    int tamanhoDasPecas = 9;
    static String files[] = {"Number6.png", "Number7.png", "Number9.png",
        "Number11.png", "Number17.png", "Number23.png", "Number30.png", "Number37.png",
        "Number39.png"};

    int numClicks = 0;
    int oddClickIndex = 0;
    int currentIndex = 0;
    int turno = 0;
    boolean f;
    int numeroDeClicks;
    ImageIcon closedIcon;
    ImageIcon icons[] = null;
    ArrayList<String> modoDeJogos;
    String modoDeJogoServidor;
    String inicioDeJogo = "InicioDeJogo";
    String fimDeJogo = "FimDeJogo";
    String meioDoJogo = "MeioDeJogo";
    String envio = "Envio";
    String recebimento = "Recebimento";
    int vencedor;
    int numeroDeCartasCertas;
    int numeroDoJogadorEmModoDeEnvio;
    ArrayList<Integer> numeroDeAcertosDeCadaJogador;
    MensagemDeEnvio mensagemDeEnvioAosJogadoresEmRecebimento = null;

    ConnectionServer() {
    }

    public void run() {
        System.out.println("O Jogo comecara");
        while (true) {
            turnar();
        }
    }

    void initServer(int numeroDeJogadores) {
        this.numeroDeJogadores = numeroDeJogadores;
        modoDeJogos = new ArrayList<>();
        this.turno = 0;
        this.numeroDeCartasCertas = 0;
        modoDeJogoServidor = inicioDeJogo;
        numeroDeAcertosDeCadaJogador = new ArrayList<>();
        numeroDoJogadorEmModoDeEnvio = 0;
        this.start();

    }

    private void turnar() {

        if (modoDeJogoServidor.equals(inicioDeJogo)) {
            int numeroDoJogador = 0;
            icons = new ImageIcon[numButtons];
            for (int i = 0; i < numButtons; i++) {
                icons[i] = new ImageIcon();
            }
            System.out.println("Selecionou as cartas");
            for (int j = 0; j < numeroDeJogadores; j++) {
                if (j == 0) {
                    modoDeJogos.add(envio);
                } else {
                    modoDeJogos.add(recebimento);
                }
            }//setando as configuracoes de jogo
            System.out.println("Decidiu quem e envio e quem eh recebimento");
            for (Socket e : listaDeJogadoresDaPartida) {

                try {
                    outObject = new ObjectOutputStream(e.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                MensagemDeInicioDeJogo mensagem = new MensagemDeInicioDeJogo();
                if (numeroDoJogador == 0) {
                    mensagem.setModoDeJogo(envio);
                    mensagem.setTelaTravada(false);
                } else {
                    mensagem.setModoDeJogo(recebimento);
                    mensagem.setTelaTravada(true);
                }
                mensagem.setTemp(icons);
                mensagem.setTurno(turno);
                mensagem.setNumeroDeButtonNatela(numButtons);
                try {
                    outObject.writeObject(mensagem);
                    System.out.println("Enviou a mensagem de inicio de Jogo");
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                numeroDeAcertosDeCadaJogador.add(0);//numerodeAcertosDeCadaum
                numeroDoJogador++;
            }
            numeroDoJogadorEmModoDeEnvio = 0;//eh o primeiro que comeca a jogar, depois eu coloco um sorteio
            modoDeJogoServidor = meioDoJogo;
        } else if (modoDeJogoServidor.equals(fimDeJogo)) {
            int numeroDoJogador = 0;

            turno++;
            int maior = 0;
            int numeroDoGanhador = 0;
            for (Integer e : numeroDeAcertosDeCadaJogador) {

                if (maior < e) {
                    maior = e;
                    numeroDoGanhador = numeroDoJogador;
                    vencedor = numeroDoGanhador;
                }
                numeroDoJogador++;
            }
            //enviando a mensagem De ganhador
            int i = 0;
            for (Socket e : listaDeJogadoresDaPartida) {
                try {
                    outObject = new ObjectOutputStream(e.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                MensagemDeFimDeJogo mensagem = new MensagemDeFimDeJogo();
                if (i == vencedor) {
                    mensagem.setVencedor(true);
                } else {
                    mensagem.setVencedor(false);
                }
                try {
                    outObject.writeObject(mensagem);
                    System.out.println("Enviou a mensagem de fim de Jogo");
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }

            //mas antes tem q mandar uma mensagem para todos para quem ganhou
            for (Socket e : listaDeJogadoresDaPartida) {

                try {
                    e.close();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                // fecho as conexoes
            }
        } else if (modoDeJogoServidor.equals(meioDoJogo)) {// se não os turnos continuam
            int numeroDoJogador = 0;

            if (numeroDeCartasCertas != numButtons) {//se não terminou o jogo;
                
                for (Socket e : listaDeJogadoresDaPartida) {
                    if (numeroDoJogador == numeroDoJogadorEmModoDeEnvio) {
                        try {
                            inObject = new ObjectInputStream(e.getInputStream());
                        } catch (IOException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            mensagemDeEnvioAosJogadoresEmRecebimento = (MensagemDeEnvio) inObject.readObject();
                            System.out.println("Leu mensagem de Recebimento");
                        } catch (IOException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    numeroDoJogador++;
                }                
                numeroDoJogador = 0;

                for (Socket e : listaDeJogadoresDaPartida) {
                    if (numeroDoJogador != numeroDoJogadorEmModoDeEnvio) {
                        try {
                            outObject = new ObjectOutputStream(e.getOutputStream());
                        } catch (IOException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            outObject.writeObject(mensagemDeEnvioAosJogadoresEmRecebimento);
                            outObject.flush();
                            System.out.println("Enviou mensagem de recebimento");
                        } catch (IOException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    numeroDoJogador++;
                }
            }
        }
    }
}
//            turno++;
//        } else {
////            tem q ver os dois clicks, tem que ver o click continuado e espera o jogador fazer a
////              a segunda jogada dele porque eu não sei se ele continou
//
//            int contadorDeJogador = 1;
//            int k = 1;
//            MensagemDoJogo mensagem = new MensagemDoJogo();
//            for (Socket s : listaDeJogadoresDaPartida) {
//                if (turno % contadorDeJogador == 0 && k == contadorDeJogador) {
//                    //se o turno eh odo usuario em questão
////                    eu leio a mensagem pq ele so manda pro servidor do cliente pro servidor
////                    pq aconteceu alguma coisa
//                    System.out.println("Comecou o turno" + turno + " do jogador " + contadorDeJogador);
//                    try {
//                        inObject = new ObjectInputStream(s.getInputStream());
//
//                    } catch (IOException ex) {
//                        Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    try {
//                        mensagem = (MensagemDoJogo) inObject.readObject();
//                    } catch (IOException ex) {
//                        Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                }
//                k++;
//                contadorDeJogador++;
//            }
//            contadorDeJogador = 1;
//            k = 1;
//            for (Socket e : listaDeJogadoresDaPartida) {
//                if (turno % contadorDeJogador != 0 && k == contadorDeJogador) {
////                    se o turno eh diferente
////                    eu escreve pros outros pra eles entenderem q aconteceu um evento na tela
//                    System.out.println("Comecou o turno" + turno + " do jogador " + contadorDeJogador);
//                    try {
//                        outObject = new ObjectOutputStream(e.getOutputStream());
//                    } catch (IOException ex) {
//                        Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//
//                    }
//                    try {
//                        outObject.writeObject(mensagem);
//                        outObject.flush();
//                    } catch (IOException ex) {
//                        Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                k++;
//                contadorDeJogador++;
//            }
//            turno++;
//        }
