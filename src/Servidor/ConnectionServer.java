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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Collections.max;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author humberto ´É a Thread do lado do servidor
 */
public class ConnectionServer extends Thread {

    ArrayList<Socket> listaDeJogadoresDaPartida;
    ArrayList<ObjectInputStream> inPutStreams;
    ArrayList<ObjectOutputStream> outPutStreams;

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
    int cartaAberta;
    private int currentIndexAberta;
    private int oddIndexAberta;
    boolean leu = false;
    boolean escreveu = false;
    int id;

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
            iniciarOJogo();
        } else if (modoDeJogoServidor.equals(fimDeJogo) && numeroDeCartasCertas == numButtons) {
            finalizarOJogo();
        } else if (modoDeJogoServidor.equals(meioDoJogo)) {// se não os turnos continuam
            turnoDoJogo();
        }
    }

    private void atualizarCartasAbertas() {
        currentIndexAberta = mensagemDeEnvioAosJogadoresEmRecebimento.getNumeroAberto();
        cartaAberta++;
        if (cartaAberta == 2) {
            if (currentIndexAberta == oddIndexAberta) {
                int num = numeroDeAcertosDeCadaJogador.get(numeroDoJogadorEmModoDeEnvio);
                numeroDeAcertosDeCadaJogador.set(numeroDoJogadorEmModoDeEnvio, (num++));
            }
        }
    }

    private void iniciarOJogo() {
        cartaAberta = 0;
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
        System.out.println("Elementos de jogadores" + listaDeJogadoresDaPartida.size());
//        outPutStreams = new ArrayList<>();
//        inPutStreams = new ArrayList<>();
        for (Socket e : listaDeJogadoresDaPartida) {
            try {

                outObject = new ObjectOutputStream(e.getOutputStream());
//                outPutStreams.add(outObject);
//                inPutStreams.add(new ObjectInputStream(e.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
//            try {
//
//            } catch (IOException ex) {
//                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
//            }
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
            } catch (IOException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Enviou a mensagem de inicio de Jogo");
            numeroDeAcertosDeCadaJogador.add(0);//numerodeAcertosDeCadaum
            numeroDoJogador++;
        }
        numeroDoJogadorEmModoDeEnvio = 0;//eh o primeiro que comeca a jogar, depois eu coloco um sorteio
        modoDeJogoServidor = meioDoJogo;

    }

    private void finalizarOJogo() {
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

    }

    private void turnoDoJogo() {
//        int numeroDoJogador = 0;
        int numeroDacartaAberta;
        if (numeroDeCartasCertas != numButtons) {//se não terminou o jogo;
            System.out.println("Eh diferente");

            MensagemDeEnvio deEnvio = recebimentoDaMensagem();//recebe a mensagem do usuario
            currentIndexAberta = deEnvio.getNumeroAberto();
            cartaAberta++;
            if (cartaAberta == 2) {
                if (currentIndexAberta == oddIndexAberta) {
                    int num = numeroDeAcertosDeCadaJogador.get(numeroDoJogadorEmModoDeEnvio);
                    num++;
                    numeroDeAcertosDeCadaJogador.set(numeroDoJogadorEmModoDeEnvio, num);
                }
            }
            envioDaMensagem(deEnvio); //envia a mensagem
            oddIndexAberta = currentIndexAberta;

            if (numeroDeCartasCertas == numButtons) {
                int j = max(numeroDeAcertosDeCadaJogador);
                for (int i : numeroDeAcertosDeCadaJogador) {
                    MensagemDeFimDeJogo mensagemDeFimDeJogo;
                    if (i != j) {
                        mensagemDeFimDeJogo = new MensagemDeFimDeJogo(false, "FimDeJogo");
                    } else {
                        mensagemDeFimDeJogo = new MensagemDeFimDeJogo(true, "FimDeJogo");
                    }
                }
            }
        }
    }

    private MensagemDeEnvio recebimentoDaMensagem() {
        int numeroDoJogador = 0;
        for (Socket e : listaDeJogadoresDaPartida) {
            System.out.println("Numero do jogador " + numeroDoJogador);
            System.out.println("Passou aqui para receber a mensagem");

            if (numeroDoJogador == numeroDoJogadorEmModoDeEnvio && leu == false) {

                try {
                    System.out.println("ta aqui");
                    inObject = new ObjectInputStream(e.getInputStream());
                    System.out.println("recebeu");
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    mensagemDeEnvioAosJogadoresEmRecebimento = (MensagemDeEnvio) inObject.readObject();
                    System.out.println("Leu mensagem de Recebimento");
                    id++;
                    leu = true;
                    System.out.println(mensagemDeEnvioAosJogadoresEmRecebimento.toString());
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            numeroDoJogador++;
        }

        return mensagemDeEnvioAosJogadoresEmRecebimento;
    }

    private void envioDaMensagem(MensagemDeEnvio mEnvio) {
        int numeroDoJogador = 0;
        for (Socket e : listaDeJogadoresDaPartida) {
//            if (numeroDoJogador != numeroDoJogadorEmModoDeEnvio) {
            if (numeroDoJogador != numeroDoJogadorEmModoDeEnvio && escreveu == false) {
                try {
                    outObject = new ObjectOutputStream(e.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    System.out.println("Abrir a carta " + mEnvio.getNumeroAberto());
                    outObject.writeObject(mEnvio);
                    escreveu = true;
//                    outObject.flush();

                    System.out.println("Enviou mensagem de recebimento");
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            numeroDoJogador++;
//        mEnvio=null;
        }

    }

}
