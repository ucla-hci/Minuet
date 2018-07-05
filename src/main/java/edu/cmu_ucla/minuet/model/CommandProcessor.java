package edu.cmu_ucla.minuet.model;

import java.util.List;

public class CommandProcessor {

    public CommandProcessor() {
    }

    public ProcessorReturn inspectTheString(List<String> s){
        ProcessorReturn processorReturn = null;
        if (s.contains("turn")&&s.contains("on") ){processorReturn = new ProcessorReturn("POWER",1); }
        if (s.contains("turn")&&s.contains("off")){processorReturn = new ProcessorReturn("POWER",0);}
        if (s.contains("volume")){processorReturn = new ProcessorReturn("VOLUME",-1);}




        return processorReturn;
    }
}
