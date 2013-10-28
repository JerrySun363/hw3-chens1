package edu.cmu.hw2.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

/**
 * Initialize with the Gold Pipeline method. This method is the ideal situation (in my opinion).
 * Because we were trying to use the given texts to find whether the answers are right rather than
 * using the internal isCorrect key to judge.
 */

public class GoldPipelineAnnotator extends JCasAnnotator_ImplBase {
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex answerIndex = aJCas.getAnnotationIndex(Answer.type);
    Iterator answerIterator = answerIndex.iterator();
    while (answerIterator.hasNext()) {
      Answer a = (Answer) answerIterator.next();
      AnswerScore as = new AnswerScore(aJCas);
      as.setAnswer(a);
      as.setScore(a.getIsCorrect()?1.0:0.0);
      as.setBegin(a.getBegin());
      as.setEnd(a.getEnd());
      as.addToIndexes();
    }

  }
}
