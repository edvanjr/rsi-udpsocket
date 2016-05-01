package com.rsi.udpsocket;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer {
	private DatagramSocket socket = null;
	private Arquivo arquivo = null;
	
	public UDPServer() {
		
	}
	
	public void criarSocket(){
		try{
			socket = new DatagramSocket(12000);
			
			byte[] incomingData = new byte[1024 * 1000 * 50];
			
			System.out.println("Aguardando envio de pacotes...");
			
			while(true){
				
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				byte[] data = incomingPacket.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				arquivo = (Arquivo) is.readObject();
				
				if(arquivo.getStatus().equalsIgnoreCase("Error")){
					System.out.println("Ocorreu um erro no pacote");
					System.out.println("Aguardando reenvio...");
				} else {
					criarArquivo();
					
					// Enviando a resposta para o cliente
					InetAddress IPAdress = incomingPacket.getAddress();
					int port = incomingPacket.getPort();
					String reply = "Success";
					
					byte[] replyBytea = reply.getBytes();
					DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAdress, port);
					socket.send(replyPacket);
					Thread.sleep(3000);
					System.exit(0);
				}
				
			}
			
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	// Responsável por ler e escrever o arquivo 
	public void criarArquivo() {
		String outputFile = arquivo.getDiretorioDestino() + arquivo.getNomeArquivo();
		
		if(!new File(arquivo.getDiretorioDestino()).exists()){
			new File(arquivo.getDiretorioDestino()).mkdirs();
		}
		
		File dstFile = new File(outputFile);
		FileOutputStream fileOutputStream = null;
		
		try{
			fileOutputStream = new FileOutputStream(dstFile);
			fileOutputStream.write(arquivo.getArquivo());
			fileOutputStream.flush();
			fileOutputStream.close();
			
			System.out.println("Arquivo salvo com sucesso.");
		} catch (FileNotFoundException e){
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
