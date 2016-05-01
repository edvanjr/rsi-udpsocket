package com.rsi.udpsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Random;

public class SocketError extends DatagramSocket{
	
	private Double errorProb = 0.0;
	
	public SocketError() throws SocketException {
		super();
	}
	
	public void setErrorProb(Double errorProb){
		this.errorProb = errorProb;
	}
	
	public Double getErrorProb(){
		return this.errorProb;
	}
	
	public void sendWithError(DatagramPacket dp) throws IOException, SocketTimeoutException{
		if(gerarProbabilidade() > errorProb){
			send(dp);
		} else {
			throw new SocketTimeoutException();
		}
	}
	
	public byte[] recvWithError(DatagramPacket dp) throws IOException{
			
		receive(dp);
		
		if(gerarProbabilidade() > errorProb){
			return dp.getData();
		} else {
			throw new SocketTimeoutException();
		}
	}
	
	
	public Double gerarProbabilidade(){
		Random random = new Random();
		return random.nextDouble();
	}
}
