import java.net.*;
import java.io.*;
class Iperfer{
    public static void main(String[] args) throws IOException{
    int counter = 0;
    byte[] b = new byte[1000]; 
    OutputStream out = null;
    String flag = args[0];
    
    if(flag.equals("-c")){
        String hostName = args[1];
	int port = Integer.parseInt(args[2]);
	int time = Integer.parseInt(args[3]);
	//converting time in seconds to nanoseconds
	double nanTime = time * 100000000;
	//create new socket object for the client
	Socket clien = null;
	try{
	    clien = new Socket(hostName, port);
	    out = clien.getOutputStream();
	}catch (IOException e){
	    System.err.println("Caught IOException: " + e.getMessage());
	}
	long startTime = System.nanoTime();
	//while still time remaining send 1000 byte array and increment
	//counter
	while (System.nanoTime() - startTime < nanTime){
	    try{
		out.write(b);
	    }catch (IOException e){
		System.err.println("Caught IOException: " + e.getMessage());
	    }
	    counter++;
	}
	clien.close();
	out.close();
	//number of kilobytes sent will be i
	double rate = counter/time;
	System.out.println("sent=" + counter +" KB rate =" + rate/1000 + " Mbps");
    }
    if(flag.equals("-s")){
	int sPort = Integer.parseInt(args[1]);
	int stdin = 0;
	Socket server = null;
	try{
	    ServerSocket serv = new ServerSocket(sPort);
	    server = serv.accept();
	}catch (IOException e){
	    System.err.println("Caught IOException: " + e.getMessage());
	}
	
	long startTime = System.nanoTime();
	while (stdin > -1) {
	    try{
		byte[] bytes = new byte[1000];
		stdin = server.getInputStream().read(bytes);
		counter = counter +  stdin;
	    }catch (IOException e){
		System.err.println("Caught IOException: " + e.getMessage());
	    }
	}
	counter ++;
	counter = counter/1000;
	double  elapsed = System.nanoTime() - startTime;
	elapsed = elapsed * (.00000001);
	//System.out.println(elapsed);
	double rate = counter/elapsed;
	System.out.println("recieved=" + counter +" KB rate =" + rate/1000 + " Mbps");
    }
    }
}
