package com.rsi.udpsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UDPServer {
	private DatagramSocket socket = null;
	private byte[] arquivo = null;
	
	private byte[] retorno;
	
	public UDPServer() {
		
	}
	
	public void criarSocket(){
		
		System.out.println("Aguardando envio de pacotes...");
		
		try{
			
			socket = new DatagramSocket(12000);
			ArrayList<String> retornos = new ArrayList<>();
			
			String ack = "ack0";
			String pktEsperado = "pkt0";
			
			while(true){
				byte[] incomingData = new byte[1000];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, 0,incomingData.length);
				socket.receive(incomingPacket);
				
				Path arquivo = Paths.get("C:","Users", "Edvan Jr", "Desktop", incomingPacket.getAddress() + "-" + incomingPacket.getPort() + ".txt");

				String data = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
				
				String[] dados = data.split("--");
				
				InetAddress IPAdress = incomingPacket.getAddress();
				int port = incomingPacket.getPort();
				
				if(!dados[1].equalsIgnoreCase(pktEsperado)){
					byte[] replyBytea = ack.getBytes();
					DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAdress, port);
					socket.send(replyPacket);
				}else{
					
					retornos.add(dados[2]);
					
					if(dados[1].equalsIgnoreCase("pkt0")){
						ack = "ack0";
						pktEsperado = "pkt1";
						byte[] replyBytea = ack.getBytes();
						DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAdress, port);
						socket.send(replyPacket);
					} else if (dados[1].equalsIgnoreCase("pkt1")){
						ack = "ack1";
						pktEsperado = "pkt0";
						byte[] replyBytea = ack.getBytes();
						DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAdress, port);
						socket.send(replyPacket);
					}
				}
				
				if(Integer.parseInt(dados[0]) == retornos.size()){
					Files.write(arquivo, retornos, Charset.forName("UTF-8"));
					ack = "ack0";
					pktEsperado = "pkt0";
				}
				
				
			}
			
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UDPServer server = new UDPServer();
		server.criarSocket();
	}
}
