import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbClaimException;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;

public class Fetcher implements Runnable {
	private UsbDevice device;

	Fetcher(UsbDevice device) {
		this.device = device;
	}
	public void sendCommand(byte b) throws UsbClaimException, UsbNotActiveException, UsbDisconnectedException, UsbException {
		UsbConfiguration configuration = null;
		UsbInterface iface = null;
		UsbPipe pipe = null;
		
		try {
			configuration = device.getActiveUsbConfiguration();
			iface = configuration.getUsbInterface((byte) 0);
			
			iface.claim();
			
			pipe = iface.getUsbEndpoint((byte) 0x02).getUsbPipe();
			
			pipe.open();
			
			pipe.abortAllSubmissions();

			int sent = pipe.syncSubmit(new byte[] { 0x55, 0x55, 0x55, 0x01, b, b });
			

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(pipe!=null) pipe.close();
			if(iface!=null)iface.release();
			
		}
	}
	public void fetchData() throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException{
		
		UsbConfiguration configuration = null;
		UsbInterface iface = null;
		UsbPipe pipe = null;
		
		try {
			
			configuration = device.getActiveUsbConfiguration();
			iface = configuration.getUsbInterface((byte) 0);
			
			iface.claim();
			
			pipe = iface.getUsbEndpoint((byte) 0x81).getUsbPipe();
			
			pipe.open();
			
			List<Byte> b = new ArrayList<Byte>();
			byte[] data = new byte[64];
			int cycles = 0;
			while (true) {
				pipe.syncSubmit(data);

				for (int i = 2; i < 64; ++i) {
					 //System.out.format("0x%02X ", data[i]);
					if (b.size() == 0 && data[i] != (byte) 0xAA) continue;
					if (data[i] == 0) break;
					
					b.add(data[i]);

				}
				//System.out.println("");
				if (b.size() >= 75) break;
					
				cycles++;
				if (cycles > 10) return;
			}
			
			byte[] d2 = new byte[90];
			for (int i = 5; i < Math.min(b.size(), 73); ++i)  d2[i] = b.get(i);
			

			for(int i=0;i<b.size();++i) {
				//System.out.format("0x%02X ", b.get(i));
			}
			//System.out.println("");
			StatusWord sw=null;
			
			try{
				sw= new StatusWord(new String(d2));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			if(sw!=null) {
				synchronized(Main.sw){
					Main.sw.updateData(sw);
					Main.sw.notify();
				}
			}
			


			//System.out.println(ele[9]);
			

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(pipe!=null) pipe.close();
			if(iface!=null)iface.release();
			
		}
	}
	@Override
	public void run() {

		while (true) {
			try {
				
				sendCommand((byte)0x90);
				fetchData();
				
			} catch (Exception e) {

			}
			while(!Main.q.isEmpty()) {
				Byte b=Main.q.poll();
				try {
					sendCommand(b.byteValue());
					//Thread.sleep(100);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
