package com.mykey.log.mykey;

import java.io.IOException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseListener implements NativeMouseInputListener
{
	Writter writter;
	public MouseListener() {
		writter = Writter.getInstance();
		GlobalScreen.addNativeMouseListener(this);
		// TODO Auto-generated constructor stub
	}
	public void nativeMouseClicked(NativeMouseEvent event) {
		
		
	}

	public void nativeMousePressed(NativeMouseEvent event) {
		// TODO Auto-generated method stub
		try {
			if (NativeMouseEvent.BUTTON1 == event.getButton())
				writter.write("Mysz lewy");
			else 
				writter.write("Mysz prawy");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void nativeMouseReleased(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void nativeMouseDragged(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void nativeMouseMoved(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
