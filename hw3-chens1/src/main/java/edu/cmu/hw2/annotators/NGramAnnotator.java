package edu.cmu.hw2.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import edu.cmu.deiis.types.AnswerTokens;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.QuestionTokens;
import edu.cmu.deiis.types.Token;

/**
 * This processor tries its best to implement the N-Gram method. However, due to the lack of model
 * and training data, it is extremely imprecise and lacks the ability to apply to all the programs.
 * Thus, it is of little use to test other performances.
 * 
 * I wish I could improve it with proper tools or models in the future.
 * 
 * Also, as for Eric's slides in class, it seems that he merely combines the words regardless of
 * whether the word is a noun or a verb. That's what I did here.
 */

public class NGramAnnotator extends JCasAnnotator_ImplBase {
   /**
    * The method processes the JCas types.
    */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    @SuppressWarnings("rawtypes")
    FSIndex questionTokensIndex = aJCas.getAnnotationIndex(QuestionTokens.type);
    @SuppressWarnings("rawtypes")
    FSIndex answerTokensIndex = aJCas.getAnnotationIndex(AnswerTokens.type);
    @SuppressWarnings("rawtypes")
    Iterator questionTokensIterator = questionTokensIndex.iterator();
    @SuppressWarnings("rawtypes")
    Iterator answerTokensIterator = answerTokensIndex.iterator();

    /* Generate the Question NGrams*/
    while (questionTokensIterator.hasNext()) {
      QuestionTokens t = (QuestionTokens) questionTokensIterator.next();

      for (int i = 0; i < t.getTokens().size(); i++) {
        /* generate 1-Gram */
        NGram n = new NGram(aJCas);
        n.setBegin(t.getTokens(i).getBegin());
        n.setEnd(t.getTokens(i).getEnd());
        n.setCasProcessorId(this.getClass().getName());
        n.setConfidence(1.0);
        n.setElementType(t.getTokens(i).getClass().getName());
        n.setElements(new FSArray(aJCas,1));
        n.setElements(0, t.getTokens(i));
        n.addToIndexes();
        /* generate 2-Gram */
        if (i < t.getTokens().size() - 1) {
          NGram n2 = new NGram(aJCas);
          n2.setBegin(t.getTokens(i).getBegin());
          n2.setEnd(t.getTokens(i+1).getEnd());
          n2.setCasProcessorId(this.getClass().getName());
          n2.setConfidence(1.0);
          n2.setElementType(t.getTokens(i).getClass().getName());
          n2.setElements(new FSArray(aJCas,2));
          n2.setElements(0, t.getTokens(i));
          n2.setElements(1, t.getTokens(i+1));
          n2.addToIndexes();
        }
        
        /* generate 2-Gram */
        if (i < t.getTokens().size() - 2) {
          NGram n3 = new NGram(aJCas);
          n3.setBegin(t.getTokens(i).getBegin());
          n3.setEnd(t.getTokens(i+2).getEnd());
          n3.setCasProcessorId(this.getClass().getName());
          n3.setConfidence(1.0);
          n3.setElementType(t.getTokens(i).getClass().getName());
          n3.setElements(new FSArray(aJCas,3));
          n3.setElements(0, t.getTokens(i));
          n3.setElements(1, t.getTokens(i+1));
          n3.setElements(2, t.getTokens(i+2));
          n3.addToIndexes();
        }
      }

    }

    /* Generate the Answer NGrams*/
    while (answerTokensIterator.hasNext()) {
      AnswerTokens t = (AnswerTokens) answerTokensIterator.next();

      for (int i = 0; i < t.getTokens().size(); i++) {
        /* generate 1-Gram */
        NGram n = new NGram(aJCas);
        n.setBegin(t.getTokens(i).getBegin());
        n.setEnd(t.getTokens(i).getEnd());
        n.setCasProcessorId(this.getClass().getName());
        n.setConfidence(1.0);
        n.setElementType(t.getTokens(i).getClass().getName());
        n.setElements(new FSArray(aJCas,1));
        n.setElements(0, t.getTokens(i));
        n.addToIndexes();
        /* generate 2-Gram */
        if (i < t.getTokens().size() - 1) {
          NGram n2 = new NGram(aJCas);
          n2.setBegin(t.getTokens(i).getBegin());
          n2.setEnd(t.getTokens(i+1).getEnd());
          n2.setCasProcessorId(this.getClass().getName());
          n2.setConfidence(1.0);
          n2.setElementType(t.getTokens(i).getClass().getName());
          n2.setElements(new FSArray(aJCas,2));
          n2.setElements(0, t.getTokens(i));
          n2.setElements(1, t.getTokens(i+1));
          n2.addToIndexes();
        }
        
        /* generate 2-Gram */
        if (i < t.getTokens().size() - 2) {
          NGram n3 = new NGram(aJCas);
          n3.setBegin(t.getTokens(i).getBegin());
          n3.setEnd(t.getTokens(i+2).getEnd());
          n3.setCasProcessorId(this.getClass().getName());
          n3.setConfidence(1.0);
          n3.setElementType(t.getTokens(i).getClass().getName());
          n3.setElements(new FSArray(aJCas,3));
          n3.setElements(0, t.getTokens(i));
          n3.setElements(1, t.getTokens(i+1));
          n3.setElements(2, t.getTokens(i+2));
          n3.addToIndexes();
        }
      }

    }

    
    
  }

}
