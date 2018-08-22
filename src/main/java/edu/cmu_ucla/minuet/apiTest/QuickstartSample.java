package edu.cmu_ucla.minuet.apiTest;

import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;
import edu.cmu_ucla.minuet.NLP.TokenNode;
import edu.stanford.nlp.util.ArraySet;

import java.util.List;
import java.util.Set;

public class QuickstartSample {
    public static void main(String... args) throws Exception {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            Document doc = Document.newBuilder()
                    .setContent("play next music")
                    .setType(Type.PLAIN_TEXT)
                    .build();
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();
            // analyze the syntax in the given text
//


            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
            List<Token> tmpTokenList = response.getTokensList();
            long startTime   = System.nanoTime();
            Token root = tmpTokenList
                    .stream()
                    .filter(s->s.getDependencyEdge().getLabel().equals(DependencyEdge.Label.ROOT))
                    .findFirst()
                    .get();
            TokenNode rootNode = new TokenNode(root);
            int rootIndex = tmpTokenList.indexOf(root);

            Set<Integer> countedIndex = new ArraySet<>();
            countedIndex.add(rootIndex);
            for(int i = 0; i< tmpTokenList.size();i++){
                if(i != rootIndex && tmpTokenList.get(i).getDependencyEdge().getHeadTokenIndex()==rootIndex){
                    TokenNode newNode = new TokenNode(tmpTokenList.get(i));
                    rootNode.addSon(newNode,i);
                    countedIndex.add(i);
                }
            }
            for(int j = 0; j< tmpTokenList.size();j++){
                if(!countedIndex.contains(j)&&rootNode.getSons().containsKey(tmpTokenList.get(j).getDependencyEdge().getHeadTokenIndex())){
                    TokenNode newNode = new TokenNode(tmpTokenList.get(j));
                    rootNode.getSons().get(tmpTokenList.get(j).getDependencyEdge().getHeadTokenIndex()).addSon(newNode,j);
                }
            }

            System.out.println("ROOT:"+rootNode.getText());
            System.out.println("ROOT'S SONS:");
            for (TokenNode node : rootNode.getSons().values()){
                System.out.println(node.getText());
                if(node.hasSons()){
                    System.out.println("    sons: ");
                    for(TokenNode sonNode: node.getSons().values()){
                        System.out.println("    "+sonNode.getText());
                    }
                }
            }

            long endTime   = System.nanoTime();
            long totalTime = (endTime - startTime);
            System.out.println("total time is: "+totalTime);

//            for (Token token : response.getTokensList()) {
//
////                token.getDependencyEdge()
////                if(token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.ROOT)){ System.out.println(token.getLemma()); }
//                System.out.printf("\tText: %s\n", token.getText().getContent());
//                System.out.printf("\tBeginOffset: %d\n", token.getText().getBeginOffset());
//                System.out.printf("Lemma: %s\n", token.getLemma());
//                System.out.printf("PartOfSpeechTag: %s\n", token.getPartOfSpeech().getTag());
//                System.out.printf("\tAspect: %s\n", token.getPartOfSpeech().getAspect());
//                System.out.printf("\tCase: %s\n", token.getPartOfSpeech().getCase());
//                System.out.printf("\tForm: %s\n", token.getPartOfSpeech().getForm());
//                System.out.printf("\tGender: %s\n", token.getPartOfSpeech().getGender());
//                System.out.printf("\tMood: %s\n", token.getPartOfSpeech().getMood());
//                System.out.printf("\tNumber: %s\n", token.getPartOfSpeech().getNumber());
//                System.out.printf("\tPerson: %s\n", token.getPartOfSpeech().getPerson());
//                System.out.printf("\tProper: %s\n", token.getPartOfSpeech().getProper());
//                System.out.printf("\tReciprocity: %s\n", token.getPartOfSpeech().getReciprocity());
//                System.out.printf("\tTense: %s\n", token.getPartOfSpeech().getTense());
//                System.out.printf("\tVoice: %s\n", token.getPartOfSpeech().getVoice());
//                System.out.println("DependencyEdge");
//
//                System.out.printf("\tHeadTokenIndex: %d\n", token.getDependencyEdge().getHeadTokenIndex());
//                System.out.printf("\tLabel: %s\n\n", token.getDependencyEdge().getLabel());
//            }
//            return response.getTokensList();
        }
    }
}