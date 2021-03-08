import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;
import javax.usb.UsbPort;
import javax.usb.UsbServices;
import org.apache.commons.lang3.CharSet;

import java.awt.BorderLayout;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static volatile BlockingQueue<Byte> q= new LinkedBlockingQueue<>();
	public static volatile StatusWord sw= new StatusWord();
	public static UsbDevice findDevice(UsbHub hub, short vendorId, short productId)
	{
		System.out.println("here3");
	    for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices())
	    {
	    	System.out.println("here");
	        UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
	        if (desc.idVendor() == vendorId && desc.idProduct() == productId) return device;
	        if (device.isUsbHub())
	        {
	            device = findDevice((UsbHub) device, vendorId, productId);
	            if (device != null) return device;
	        }
	    }
	    return null;
	}

	public static void executePreCommand() {
		File f=null;
		
    	try{
    		f = File.createTempFile("script", null);

		    Writer streamWriter = new OutputStreamWriter(new FileOutputStream(f));
		    PrintWriter printWriter = new PrintWriter(streamWriter);

		    printWriter.println("#!/bin/bash");
		    printWriter.println("echo -n -e '\\x55\\x55\\x55\\x01\\x0D\\x0D'>/dev/cu.usbserial-AI040VE1");

		    printWriter.close();
    		ProcessBuilder builder=new ProcessBuilder();
    		
    		builder.command("bash",f.toString());
    		
    		
    		Process p=builder.start();
    		int e=p.waitFor();
    		System.out.println(e);
    	}
    	catch(Exception ex) {
    		System.out.print(ex.getMessage());
    	}
    	finally {
    		if(f!=null)f.delete();
    	}
    	
	}
	
    public static void main(final String[] args) throws UsbException, UsbDisconnectedException, InterruptedException, IOException
    {
    	
    	StatusWord.setup();
    	executePreCommand();
    	/*
    	JFrame jf = new JFrame();
    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	JProgressBar jp= new JProgressBar(0,40);
    	JLabel jl = new JLabel("0 W ");
    	JButton jb= new JButton("OP");
    	jb.addActionListener(e->{
    		Main.q.add((byte)0x0D);
    	});
    	jf.getContentPane().setLayout(new BorderLayout());
    	jf.getContentPane().add(jp,BorderLayout.NORTH);
    	jf.getContentPane().add(jl,BorderLayout.EAST);
    	jf.getContentPane().add(jb,BorderLayout.SOUTH);
    	jf.setSize(300,300);
    	jf.setLocationRelativeTo(null);
    	jf.show();
    	*/
    	

        // Get the USB services and dump information about them
    	short vendorId=0x0403;
    	short prodId=0x6001;
        UsbDevice targ=findDevice(UsbHostManager.getUsbServices().getRootUsbHub(),vendorId,prodId);
        
        
        Fetcher fet= new Fetcher(targ);
        Thread thread= new Thread(fet);
        
        MainWindow w=new MainWindow();
        while(targ==null)Thread.sleep(1000);
        thread.start();
        while(true) {
        	StatusWord ss;
        	
        	synchronized (Main.sw) {
        		ss=new StatusWord(Main.sw);
        	}
        	
        	//System.out.println(ss);
        	//System.out.println(System.currentTimeMillis());
        	w.updateValues(ss);
        	synchronized(Main.sw) {
        		Main.sw.wait();
        	}
        	
        	
        }
        
        
        
    }
}