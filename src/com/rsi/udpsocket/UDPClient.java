package com.rsi.udpsocket;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class UDPClient {
    private SocketError socket = null;
    private String arquivoOrigem = "C:/Users/Edvan Jr/Documents/texto.txt";
    private String hostName = "localHost";

    public UDPClient() {

    }

	public void conexao() {
        try {
        	ArrayList<byte[]> dados = readFile();
        	int qtdPck = dados.size();
        	int count = 0;
        	
        	socket = new SocketError();
        	socket.setErrorProb(0.4);
        	socket.setSoTimeout(2000);
            InetAddress IPAddress = InetAddress.getByName(hostName);
            byte[] incomingData = new byte[1024];
            
            String pkt = "pkt0";
            String ack = "ack0";

            	
        	while(count < qtdPck){
        		
        		String temp = qtdPck + "--" + pkt + "--" + new String(dados.get(count), 0, dados.get(count).length);
        		byte[] data = temp.getBytes();
        		
            	// Pacote a enviar
            	DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 12000);
            	
            	//Resposta a receber
        		DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
        		
        		//Enviando pacote
				socket.sendWithError(sendPacket);
				System.out.println("Enviando " + pkt);
				
				if(pkt.equalsIgnoreCase("pkt0")){
					ack = "ack0";
				}else{
					ack = "ack1";
				}
        		
    			try{
    				
    				//Recebendo resposta
    	            socket.recvWithError(incomingPacket);
    	            
    	            String resposta = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
    	            
    	            if(resposta.equalsIgnoreCase(ack)){
    	            	if(ack.equalsIgnoreCase("ack0")){
    	            		pkt = "pkt1";
    	            	} else if (ack.equalsIgnoreCase("ack1")){
    	            		pkt = "pkt0";
    	            	}
    	            } else {
    	            	continue;
    	            }
    	            
                    System.out.println("Resposta recebida: " + resposta);
    	            count = count + 1;
    			} catch (SocketTimeoutException e){
    				System.out.println("SocketTimeoutException: Reenviando o pacote...");
    				continue;
    			}
            	
        	}
        	
        	Thread.sleep(2000);
            System.exit(0);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    public ArrayList<byte[]> readFile(){
    	
    	double pctSize = 1000.0;
    	
    	File file = new File(arquivoOrigem);
    	if(file.isFile()){
    		try{
    			ArrayList<byte[]> dados = new ArrayList<byte[]>();
    			DataInputStream diStream = new DataInputStream(new FileInputStream(file));
    			
    			long len = (int) file.length();
    			byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                
    			int qtdPacotes = (int) Math.ceil(len / pctSize);
    			
    			int start = 0;
    			for(int i = 1; i <= qtdPacotes; i++){
    				int end = (int) (i * pctSize);
    				dados.add(Arrays.copyOfRange(fileBytes, start, end));
    				start = end;
    			}
    			
    		    return dados;
    		    
        	} catch (IOException e) {
    		    System.err.println(e);
    		    return null;
    		}
    	}else{
    		System.out.println("Caminho do arquivo não encontrado.");
    		return null;
    	}
    	
	}
    
    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.conexao();
    }
}
