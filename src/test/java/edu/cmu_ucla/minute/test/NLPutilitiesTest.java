package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;
import org.junit.Test;

import java.io.IOException;

public class NLPutilitiesTest {
    @Test
    public void build() {
        String command = "turn this light off,come on ";
        TokenNode turnOnCommand = new TokenNode("turn");
        turnOnCommand.addSon(new TokenNode("off"),1);

        try {
            TokenNode root = NLPHandler.parse(command);
            System.out.println(NLPHandler.isExecutable(turnOnCommand,root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
