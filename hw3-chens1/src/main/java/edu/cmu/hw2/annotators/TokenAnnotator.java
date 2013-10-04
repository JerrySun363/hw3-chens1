package edu.cmu.hw2.annotators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.internal.util.TextTokenizer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerTokens;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.QuestionTokens;
import edu.cmu.deiis.types.Token;

public class TokenAnnotator extends JCasAnnotator_ImplBase {
  private JCas aJCas = null;

  private int globalPosition = 0;

  private QuestionTokens qs = null;

  private AnswerTokens as = null;

  private int ANSWER = 0;

  private int QUESTION = 1;

  private LinkedList<Token> tokenList = new LinkedList<Token>();

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // reset the documents and the globalPosition
    this.aJCas = aJCas;
    this.globalPosition = 0;
    FSIndex questionIndex = aJCas.getAnnotationIndex(Question.type);
    FSIndex answerIndex = aJCas.getAnnotationIndex(Answer.type);

    Iterator questionIterator = questionIndex.iterator();
    Iterator answerIterator = answerIndex.iterator();
    // parse the tokens in the texts

    // System.out.println(questionIndex.size());
    // System.out.println(answerIndex.size());

    while (questionIterator.hasNext()) {
      Question question = ((Question) questionIterator.next());
      String text = question.getCoveredText();
      this.globalPosition = question.getBegin();
      qs = new QuestionTokens(aJCas, question.getBegin(), question.getEnd());

      if (qs.getTokens() == null)
        qs.setTokens(new FSArray(aJCas, questionIndex.size()));

      parseTokens(text, this.QUESTION);
      qs.addToIndexes(aJCas);
    }

    while (answerIterator.hasNext()) {

      Answer answer = ((Answer) answerIterator.next());
      String text = answer.getCoveredText();
      this.globalPosition = answer.getBegin();
      as = new AnswerTokens(aJCas, answer.getBegin(), answer.getEnd());
      as.setAnswer(answer);

      parseTokens(text, this.ANSWER);
      as.addToIndexes(aJCas);
    }
  }

  public void parseTokens(String text, int type) {
    TextTokenizer t = new TextTokenizer(text);
    t.setSeparators(".,!?");
    t.setShowWhitespace(false);
    t.setShowSeparators(false);
    int offset = 0;
    int i = 0;
    
    tokenList.clear();
    while (t.hasNext()) {
      int begin = this.globalPosition + offset;
      offset += t.nextToken().length();
      int end = this.globalPosition + offset;
      Token token = new Token(aJCas, begin, end);
      token.setConfidence(1.0);
      token.setCasProcessorId(this.getClass().getName());
      token.addToIndexes(aJCas);
      tokenList.add(token);
      i++;
      offset += 1;
    }
    
    Iterator iterator = tokenList.iterator();
    
    int j = 0;

    if (type == this.QUESTION) {
      qs.setTokens(new FSArray(aJCas, i));
      while (iterator.hasNext()){
         qs.setTokens(j,(Token)iterator.next());
         j++;
      }

    }else{
      as.setTokens(new FSArray(aJCas, tokenList.size()));
      while (iterator.hasNext()){
         as.setTokens(j,(Token)iterator.next());
         j++;
      }
    }
      

  }

}
