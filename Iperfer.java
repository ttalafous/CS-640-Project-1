import java.net.*;
import java.io.*;
class Iperfer{
    public static void main(String[] args) throws IOException{
	int counter = 0, i = 0, port = 0, time = 0;
	byte[] b = new byte[1000]; 
	OutputStream out = null;
	boolean cflag = false, sflag = false;
    	String hostName = null;
	//parsing commandline
	while (i < args.length && args[i].startsWith("-")){
	    if(args[i].equals("-c")) cflag = true;
	    if(args[i].equals("-h") && i < (args.length -1)){
		i++;
		hostName = args[i];
	    }
	    if(args[i].equals("-p") && i < (args.length -1)){
		try{
		    i++;
		    port = Integer.parseInt(args[i]);
		}catch (NumberFormatException e){
		    System.err.println("Error: port must be integer");
		    System.exit(0);
		}
	    }
	    if(args[i].equals("-t") && i < (args.length -1)){
		try{
		    i++;
		    time = Integer.parseInt(args[i]);
		}catch (NumberFormatException e){
		    System.err.println("Error: time must be integer");
		    System.exit(0);
		}
	    }
	    if(args[i].equals("-s")) sflag = true;
	    i++;
	}  
	if(port < 1024 || port > 65535){
	    System.err.println("Error: port number must be in the range 1024 to 65535");
	    System.exit(0);
	}
	if(cflag){
	    //arg checking
	    if(args.length != 7){
		System.err.println("Error: missing or additional arguments");
		System.exit(0);
	    }
	    if(time <= 0){
		System.err.println("Error: time must be a positve integer");
		System.exit(0);
	    }
		//converting time in seconds to nanoseconds
	    double nanTime = time * 100000000;
	    //create new socket object for the client
	    Socket clien = null;
	    try{
		clien = new Socket(hostName, port);
		out = clien.getOutputStream();
	    }catch (IOException e){
		System.err.println("Invalid hostname or ip address");
		System.exit(0);
	    }
	    long startTime = System.nanoTime();
	    //while still time remaining send 1000 byte array and increment
	    //counter
	    while (System.nanoTime() - startTime < nanTime){
		try{
		    out.write(b);
		}catch (IOException e){
		    System.err.println("Write failed");
		    System.exit(0);
		}
		counter++;
	    }
	    clien.close();
	    out.close();
	    //number of kilobytes sent will be i
	    double rate = counter/time;
	    System.out.println("sent=" + counter +" KB rate =" + rate/1000 + " Mbps");
	}
	if(sflag){
	    if(args.length != 3){
		System.err.println("Error: missing or additional arguments");
		System.exit(0);
	    }
	    int stdin = 0;
	    Socket server = null;
	    ServerSocket serv = null;
	    try{
		serv = new ServerSocket(port);
		server = serv.accept();
	    }catch (IOException e){
		System.err.println("Server socket error");
		System.exit(0);
	    }
	    
	    long startTime = System.nanoTime();
	    while (stdin > -1) {
		try{
		    byte[] bytes = new byte[1000];
		    stdin = server.getInputStream().read(bytes);
		    counter = counter +  stdin;
		}catch (IOException e){
		    System.err.println("Read failed");
		    System.exit(0);
		}
	    }
	    server.close();
	    serv.close();
	    counter ++;
	    counter = counter/1000;
	    double  elapsed = System.nanoTime() - startTime;
	    elapsed = elapsed * (.00000001);
	    double rate = counter/elapsed;
	    System.out.println("received=" + counter +" KB rate =" + rate/1000 + " Mbps");
	}
    }
}
