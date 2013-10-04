package edu.cmu.hw2.annotators;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;

public class TextElementAnnotator extends  JCasAnnotator_ImplBase
 {
   //This integer is used to recorded the global position of the file. 
   private int globalPosition = 0;

  @Override
  public void process(JCas aJCas) {
    //System.out.println("new: "+aJCas.getDocumentText());
    // TODO Auto-generated method stub
    String text = aJCas.getDocumentText();
    //reset GlobalPosition if multiple files are analyzed.
    //Only one Class instance will be created although each document is
    //treated as an individual JCas object. 
    globalPosition=0;
    
    String[] lines=text.split("\n");
    for(int i=0; i <lines.length;i++){
     if(lines[i].startsWith("Q"))
        parseQuestion(aJCas, lines[i]);
     else
       parseAnswer(aJCas,lines[i]);
    }
    
  }
  
  private void parseQuestion(JCas aJCas, String line){
    int start = globalPosition+2;
    int end = globalPosition+line.length();
    globalPosition+=line.length()+1;
    Question question = new Question(aJCas, start, end);
    question.setConfidence(1.0);
    question.setCasProcessorId(this.getClass().getName());
    question.addToIndexes(aJCas);
  }
  
  private void parseAnswer(JCas aJCas, String line){
    int offset = line.indexOf(" ", 2)+1;
    int start = globalPosition+offset;
    int end = globalPosition +line.length();
    globalPosition+=line.length()+1;
    Answer answer = new Answer(aJCas,start,end);
    String tokens[] = line.split(" ");
    answer.setIsCorrect(tokens[1].equals("1"));
    answer.setConfidence(1.0);
    answer.setCasProcessorId(this.getClass().getName());
    answer.addToIndexes(aJCas);
    
  }

    
}
