import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Link implements Runnable{

	private String IP = "192.168.42.1";
	 Socket client = null;
   String ipAddress; //접속을 요청할 Server의 IP 주소를 저장할 변수
   static final int port = 3000; //접속을 요청할 Server의 port 번호와 동일하게 지정
 
   BufferedReader read;
 
   //입력용 Stream
   InputStream is;
   DataInputStream ois;

   //출력용 Stream
   OutputStream os;
   ObjectOutputStream oos;
   final GpioController gpio,gpio2;
// provision gpio pin #01 as an output pin and turn on
   final GpioPinDigitalOutput pin ,pin2;
   String sendData;
   String receiveData;
    public Link(){
    	gpio = GpioFactory.getInstance();
    	gpio2 = GpioFactory.getInstance();
    	pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED", PinState.LOW);
    	pin2 = gpio2.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED2", PinState.LOW);
    	run();
    }
	
	public void dataStreaming(String json) throws IOException{
			
	}
	@Override
	public void run() {
		while(true){
		try {
			InetAddress tagetIp = InetAddress.getByName(IP);
			boolean reachable =  tagetIp.isReachable(2000);
 
			if (reachable) {
				if(client == null){
					client = new Socket(IP,port);
					System.out.println("Connection");
					is = client.getInputStream();
			        ois = new DataInputStream(is);
				}

				while(true){
					System.out.println("Ready to receive data");
					String receiveData = ois.readUTF();
					System.out.println("Data from GW : " + receiveData);
					if(receiveData.equals("on")){
						pin.high();					// fan on
						pin2.low();					// window close
					}
					else if(receiveData.equals("off"))
						pin.low();
					else if(receiveData.equals("open"))
						pin2.high();
					else if(receiveData.equals("close"))
						pin2.low();
				}
			} else {
				System.out.println("disConnection");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		}
	}
}
