package edu.cmu.hw2.annotators;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

public class ScorePrintAnnotator extends JCasAnnotator_ImplBase {

  public static double averagePrecision = 0.0;

  public static int totalDocument = 0;

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
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

    // TODO Auto-generated method stub

  }

  public void destroy() {
    System.out.printf("Average Precision: %2f\n\n", averagePrecision);
    super.destroy();
  }

}
