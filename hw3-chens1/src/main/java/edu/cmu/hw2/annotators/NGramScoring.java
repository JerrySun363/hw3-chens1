package edu.cmu.hw2.annotators;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.QuestionTokens;

public class NGramScoring extends JCasAnnotator_ImplBase {
  private HashSet<String> texts = new HashSet<String>();

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex ngramIndex = aJCas.getAnnotationIndex(NGram.type);
    // Get the limit of the question.
    FSIndex questionIndex = aJCas.getAnnotationIndex(Question.type);
    int questionEnd = ((Question) questionIndex.iterator().next()).getEnd();
    Iterator ngramIterator = ngramIndex.iterator();
    Iterator answerIterator = aJCas.getAnnotationIndex(Answer.type).iterator();
    Answer answer = (Answer) answerIterator.next();
    int count = 0;
    int total =0;
     while (ngramIterator.hasNext()) {
      NGram n = (NGram) ngramIterator.next();
      if (n.getEnd() <= questionEnd) {// this is an question token.
        texts.add(n.getCoveredText());
      } else if (n.getEnd() < answer.getEnd()) {
        total++;
        if (texts.contains(n.getCoveredText()))
          count++;
      } else {
        AnswerScore answerScore = new AnswerScore(aJCas);
        answerScore.setBegin(answer.getBegin());
        answerScore.setEnd(answer.getEnd());
        answerScore.setAnswer(answer);
        answerScore.setScore(count * 1.0 /total);
        answerScore.setConfidence(0.6);
        answerScore.setCasProcessorId(this.getClass().getName());
        answerScore.addToIndexes();
        // reset the scores and answers
        total=1;
        answer = (Answer) answerIterator.next();
        count = (texts.contains(n.getCoveredText()) ? 1 : 0);
      }
    }// if the iteration is over, again need to score the last question. The code is not elegant.
    AnswerScore answerScore = new AnswerScore(aJCas);
    answerScore.setBegin(answer.getBegin());
    answerScore.setEnd(answer.getEnd());
    answerScore.setAnswer(answer);
    answerScore.setScore(count * 1.0 / total);
    answerScore.setConfidence(0.6);
    answerScore.setCasProcessorId(this.getClass().getName());
    answerScore.addToIndexes();
    //process all the answers.

  }

}
