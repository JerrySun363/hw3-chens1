package edu.cmu.hw2.annotators;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

public class ScorePrinter extends CasConsumer_ImplBase {

  public static double averagePrecision = 0.0;

  public static int totalDocument = 0;

  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas aJCas=null; 
    try{
      aJCas= aCAS.getJCas();
     } catch (CASException e){
       throw new ResourceProcessException(e); 
     }
    
    FSIndex answers = aJCas.getAnnotationIndex(AnswerScore.type);
    FSIndex question = aJCas.getAnnotationIndex(Question.type);
    Question q = (Question) question.iterator().next();
    LinkedList<AnswerScore> as = new LinkedList<AnswerScore>();
    // insert the answers to the list
    Iterator answerIterator = answers.iterator();
    int N = 0;
    while (answerIterator.hasNext()) {
      AnswerScore answerScore = (AnswerScore) answerIterator.next();
      int i = 0;
      if(as.size()==i){
        as.add(answerScore);
      }
      else{
        while (i<as.size()&&as.get(i) != null && answerScore.getScore() < as.get(i).getScore()) {
          i++;
        }
        as.add(i, answerScore);
      }
      
      N = N + (answerScore.getAnswer().getIsCorrect() ? 1 : 0);
    }
    Iterator asIterator = as.iterator();

    System.out.println("Question: " + q.getCoveredText());

    int rightAtN = 0;
    int i = 0;
    while (asIterator.hasNext()) {
      AnswerScore a = (AnswerScore) asIterator.next();
      System.out.printf("%s %.2f %s\n", a.getAnswer().getIsCorrect() ? "+" : "-", a.getScore(), a
              .getAnswer().getCoveredText());
      if (i < N) {
        rightAtN += (a.getAnswer().getIsCorrect() ? 1 : 0);
      }
      i++;
    }
    System.out.printf("Precision at %d: %.2f\n\n", N, rightAtN * 1.0 / N);

    averagePrecision = ((rightAtN * 1.0 / N) + totalDocument * averagePrecision)
            / (totalDocument + 1);
    totalDocument++;

  }

  public void collectionProcessComplete(ProcessTrace arg0)throws ResourceProcessException, IOException {
    System.out.printf("Average Precision: %2f\n\n", averagePrecision);
  }
  
  public void batchProcessComplete(ProcessTrace arg0) throws ResourceProcessException, IOException{
    System.out.printf("Average Precision: %2f\n\n", averagePrecision);
  }
  
}
