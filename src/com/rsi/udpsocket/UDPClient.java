package com.rsi.udpsocket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UDPClient {
    private SocketError socket = null;
    private Arquivo arquivo = null;
    private String arquivoOrigem = "C:/Users/Edvan Jr/Documents/texto.txt";
    private String arquivoDestino = "C:/Users/Edvan Jr/Desktop/";
    private String hostName = "localHost";

    public UDPClient() {

    }

    public void conexao() {
        try {
        	boolean ok = false;
        	
        	ArrayList<String> dados = readFile();
        	int count = dados.size();
        	
        	while(count >= 0){
        		
	        	socket = new SocketError();
	        	socket.setErrorProb(0.8);
	        	
	            InetAddress IPAddress = InetAddress.getByName(hostName);
	            
	            // Criando arquivo a ser enviado
	            byte[] incomingData = new byte[1024];
	            arquivo = getArquivo();
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            ObjectOutputStream os = new ObjectOutputStream(outputStream);
	            os.writeObject(arquivo);
	            byte[] data = outputStream.toByteArray();
	            
	            System.out.println(data.length);
	            
	            
	            try{
	            	
	            	// Enviando pacote
	            	DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 12000);
	            	
	            	socket.sendWithError(sendPacket);
	            	System.out.println("Arquivo enviado...");
	            	
	            	//Recebendo resposta
            		DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
    	            socket.recvWithError(incomingPacket);
    	            String resposta = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
   	                System.out.println("Resposta recebida: " + resposta);
   	                
   	                ok = true;
	            	
	            	
	            }catch(SocketTimeoutException e) {
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
    
    
    public byte[] readFile(){
    	Path file = Paths.get("C:", "Users", "Edvan Jr", "Documents", "texto.txt");
    	
    	if(Files.exists(file)){
    		try{ 
    			
    			byte[] b = Files.readAllBytes(file);
    		    return b;
    		    
        	} catch (IOException e) {
    		    System.err.println(e);
    		    return null;
    		}
    	}else{
    		System.out.println("Caminho do arquivo não encontrado.");
    		return null;
    	}
    	
	}
    
    
    // Método responsável por criar o objeto que vai com o arquivo
    public Arquivo getArquivo() {
    	
        Arquivo fileEvent = new Arquivo();
        String fileName = arquivoOrigem.substring(arquivoOrigem.lastIndexOf("/") + 1, arquivoOrigem.length());
        String path = arquivoOrigem.substring(0, arquivoOrigem.lastIndexOf("/") + 1);
        fileEvent.setDiretorioDestino(arquivoDestino);
        fileEvent.setNomeArquivo(fileName);
        fileEvent.setDiretorioOrigem(arquivoOrigem);
        File file = new File(arquivoOrigem);
        if (file.isFile()) {
            try {
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setTamanhoArquivo(len);
                fileEvent.setArquivo(fileBytes);
                fileEvent.setStatus("Success");
                
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("Caminho do arquivo não encontrado.");
            fileEvent.setStatus("Error");
        }
        return fileEvent;
    }
    
    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.conexao();
    }
}
