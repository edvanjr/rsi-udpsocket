package com.rsi.udpsocket;

import java.io.*;
import java.net.*;
import java.util.Random;

public class UDPClient {
    private DatagramSocket socket = null;
    private Arquivo arquivo = null;
    private String arquivoOrigem = "C:/Users/Edvan Jr/Pictures/edvan.jpg";
    private String arquivoDestino = "C:/Users/Edvan Jr/Desktop/";
    private String hostName = "localHost";

    public UDPClient() {

    }

    public void conexao() {
        try {
        	boolean ok = false;
        	
        	while(!ok){
	        	socket = new DatagramSocket();
	            InetAddress IPAddress = InetAddress.getByName(hostName);
	            byte[] incomingData = new byte[1024];
	            arquivo = getArquivo();
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            ObjectOutputStream os = new ObjectOutputStream(outputStream);
	            os.writeObject(arquivo);
	            byte[] data = outputStream.toByteArray();
	            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 12000);
	            socket.send(sendPacket);
	            
	            System.out.println("Pacote enviado!");
	            System.out.println("Aguardando resposta...");
	            
	            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
	            socket.receive(incomingPacket);
	            String resposta = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
	            
	            System.out.println("Resposta recebida: " + resposta);
	            
	            if(resposta.toString().equalsIgnoreCase("Success")){
	            	ok = true;
	            	System.out.println("OK!");
	            }else{
	            	System.out.println("Reenviando pacote.");
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
                
                Random random = new Random();
                
                // Simulador de erro. Define se o pacote vai com erro ou não
                if(random.nextInt(2) == 0){
                	fileEvent.setStatus("Error");
                } else {
                	fileEvent.setStatus("Success");
                }
                
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
