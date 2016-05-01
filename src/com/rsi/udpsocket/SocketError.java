package com.rsi.udpsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class SocketError {
	
	private DatagramSocket socket;
	private Double errorProb;
	
	public SocketError(DatagramSocket socket){
		this.socket = socket;
		this.errorProb = 0.0;
	}
	
	public void setErrorProb(Double errorProb){
		this.errorProb = errorProb;
	}
	
	public Double getErrorProb(){
		return this.errorProb;
	}
	
	public void sendWithError(DatagramPacket dp){
		if(gerarProbabilidade() > errorProb){
			try {
				socket.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] recvWithError(DatagramPacket dp){
		try {
			
			socket.receive(dp);
			
			if(gerarProbabilidade() > errorProb){
				return dp.getData();
			} else {
				throw new TimeoutException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Double gerarProbabilidade(){
		Random random = new Random();
		Double prob = random.nextInt(101) / 100.0;
		return prob;
	}
	

}
