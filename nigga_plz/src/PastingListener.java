import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Date;

public class PastingListener {
    public int i;
    public Double lastPasteTime;
    public double minPastingInterval;
    TextStorage textStorage;
    boolean isPausing = false;

    public PastingListener(){
        i = 0;
        this.lastPasteTime = (double) new Date().getTime();
        this.minPastingInterval = 500d; //default interval 500ms
    }

    public void clip(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
    }

    public void init(TextStorage currentTextStorage, double interval) {
        textStorage = currentTextStorage;
        clip(textStorage.getFirstLine());
        this.minPastingInterval = interval >= 1 ? interval : 1;
        try
        {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener()
            {
                private boolean ctrl = false, v = false, slash = false;
                @Override
                public void nativeKeyPressed(NativeKeyEvent e) {
                    if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
                        ctrl = true;
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_V) {
                        v = true;
                        if (ctrl && !isPausing) {
                            System.out.println("Ctrl+V");
                            if (lastPasteTime != null && lastPasteTime + minPastingInterval < new Date().getTime()) {
                                clip(textStorage.getNextLine());
                                lastPasteTime = (double) new Date().getTime();
                            }
                        } else {//remove this else only for testing
                            System.out.println("Only V");
                        }
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_SLASH) {
                        slash = true;
                        if (ctrl) {
                            isPausing = !isPausing;
                            System.out.println("Ctrl+/");
                        } else {//remove this else only for testing
                            System.out.println("Only /");
                        }
                    }
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent e) {
                    if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
                        ctrl = false;
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_V) {
                        v = false;
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_SLASH) {
                        slash = false;
                    }
                }

                @Override
                public void nativeKeyTyped(NativeKeyEvent e) {
                }
            });
        }
        catch (NativeHookException e)
        {
            e.printStackTrace();
        }
    }

    public void stop() throws NativeHookException {
        textStorage = null;
        GlobalScreen.unregisterNativeHook();
    }

}
