package edu.cmu_ucla.minuet.apiTest;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.IOException;

public class TestAppleScript {
    public static void main(String[] args) {
        String[] show1 = {"open","-a","Preview","/Users/runchangkang/Downloads/protocol.pdf"};
        String[] show2 ={"osascript","-e","tell application \"Preview\"",
                "-e","activate",
                "-e","tell application \"System Events\"",
                "-e","keystroke \"f\" using {control down, command down}",
                "-e","end tell",
                "-e","end tell"};

        String[] quit = {"osascript","-e","tell application \"Preview\" to quit"};
        String[] next = {"osascript","-e","tell application \"Preview\"",
                                    "-e", "activate",
                                    "-e","tell application \"System Events\"",
                                    "-e","key code 124",
                                    "-e","end tell",
                                    "-e","end tell"};
        String[] previous = {"osascript","-e","tell application \"Preview\"",
                "-e", "activate",
                "-e","tell application \"System Events\"",
                "-e","key code 123",
                "-e","end tell",
                "-e","end tell"};
        try {
//            Runtime.getRuntime().exec(show1);
//            Runtime.getRuntime().exec(show2);
            Runtime.getRuntime().exec(previous);
//            Runtime.getRuntime().exec(commands2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScriptEngineManager sem = new ScriptEngineManager();
            for (ScriptEngineFactory factory : sem.getEngineFactories()) {
                System.out.println(factory.getEngineName());
                System.out.println(factory.getNames());
            }
//            String script = "say \"Hello from Java\"";
//
//            ScriptEngineManager mgr = new ScriptEngineManager();
//            ScriptEngine engine = mgr.getEngineByName("AppleScriptEngine");
//            engine.eval(script);

    }
}
