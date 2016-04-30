package com.rsi.udpsocket;

import java.io.Serializable;

public class Arquivo implements Serializable {
	
    public Arquivo() {

    }

    private static final long serialVersionUID = 1L;

    private String diretorioDestino;
    private String diretorioOrigem;
    private String nomeArquivo;
    private long tamanhoArquivo;
    private byte[] arquivo;
    private String status;

    public String getDiretorioDestino() {
        return diretorioDestino;
    }

 
    public void setDiretorioDestino(String diretorioDestino) {
        this.diretorioDestino = diretorioDestino;
    }

    public String getDiretorioOrigem() {
        return diretorioOrigem;
    }

    public void setDiretorioOrigem(String diretorioOrigem) {
    	this.diretorioOrigem = diretorioOrigem;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
 
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public long getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(long tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }
}
