package edu.cmu_ucla.minuet.model;

import java.util.List;

public class CommandProcessor {

    public CommandProcessor() {
    }

    public ProcessorReturn inspectTheString(List<String> s){
        ProcessorReturn processorReturn = null;
        if (s.contains("turn")&&s.contains("on") ){processorReturn = new ProcessorReturn("POWER",1); }
        else if (s.contains("turn")&&s.contains("off")){processorReturn = new ProcessorReturn("POWER",0);}





        return processorReturn;
    }
}
