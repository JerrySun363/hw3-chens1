package edu.cmu.hw2.annotators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ne.type.NamedEntityMention;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.AnswerTokens;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.QuestionTokens;
import edu.cmu.deiis.types.Token;

/**
 * This classes reads the annotated name entities and used these name entites to score an existing
 * question.
 * 
 * since the name entities only finds the names rather than any other semantics, I've decided to
 * score it on my own understanding.
 * 
 * It was not easy to incorporate it into existing scoring services And again I did not quite get
 * what you mean in the documents by saying "integrate ... into your answer scoring component"
 * 
 * @author Jerry Sun
 * 
 */
public class NameEntityAnnotator extends JCasAnnotator_ImplBase {

  @SuppressWarnings("rawtypes")
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex neIndex = aJCas.getAnnotationIndex(NamedEntityMention.type);
    FSIndex questionTokensIndex = aJCas.getAnnotationIndex(QuestionTokens.type);
    FSIndex answerTokensIndex = aJCas.getAnnotationIndex(AnswerTokens.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(Question.type);
    FSIndex answerIndex = aJCas.getAnnotationIndex(Answer.type);
    // get all the iterators
    Iterator questionIterator = questionIndex.iterator();
    Iterator answerIterator = answerIndex.iterator();
    Iterator questionTokensIterator = questionTokensIndex.iterator();
    Iterator answerTokensIterator = answerTokensIndex.iterator();
    Iterator neIterator = neIndex.iterator();
    // store the entities
    List<String> questionNameEntity = new LinkedList<String>();
    List<String> answerNameEntity = new LinkedList<String>();

    Question question = (Question) questionIterator.next();
    Answer answer = (Answer) answerIterator.next();
    AnswerTokens answerTokens = (AnswerTokens) answerTokensIterator.next();
    // now compare the tokens
    while (neIterator.hasNext()) {
      NamedEntityMention nem = (NamedEntityMention) neIterator.next();
      //System.out.println(nem.getCoveredText()+"   "+nem.getMentionType());
      if (nem.getEnd() <= question.getEnd()) {
        if (nem.getCoveredText().startsWith("Q ")) {// this is the first token
          questionNameEntity.add(nem.getCoveredText().substring(2));
        } else if (nem.getMentionType().equals("PERSON")) {
          questionNameEntity.add(nem.getCoveredText());
        }
        
      } else {
        if (nem.getEnd() > answer.getEnd()) {
            if(answerNameEntity.size()>1){
              boolean start = false;
              
                
                for(int i=0; i< answerTokens.getTokens().size();i++){
                  Token answerToken = answerTokens.getTokens(i);                 
                  if(answerToken.getCoveredText().equals(answerNameEntity.get(1))){
                        start = true;
                }
                if(answerToken.getCoveredText().equals("by")&&start){
                  //swap tokens[0] and token[1]
                  answerNameEntity.add(answerNameEntity.remove(0));
                }
                }
              
            }
          
          int i=0;
          double score = 0;
          while(i<answerNameEntity.size()&&i<questionNameEntity.size()){
            if(answerNameEntity.get(i).equals(answerNameEntity.get(i))){
              score += 1/(questionNameEntity.size()*1.0);
            }
            i++;
          }
          AnswerScore as = new AnswerScore(aJCas);
          as.setBegin(answer.getBegin());
          as.setEnd(answer.getEnd());
          as.setScore(score);
          as.setCasProcessorId(this.getClass().getName());
          as.setAnswer(answer);
          as.addToIndexes();
          
          answer = (Answer) answerIterator.next();
          answerTokens =(AnswerTokens) answerTokensIterator.next();
          answerNameEntity.clear();
        }
        
        if (nem.getMentionType()!=null && nem.getMentionType().equals("PERSON")) {
          answerNameEntity.add(nem.getCoveredText());
        }

      }

    }
    int i=0;
    double score = 0;
    while(i<answerNameEntity.size()&&i<questionNameEntity.size()){
      if(answerNameEntity.get(i).equals(answerNameEntity.get(i))){
        score += 1/(questionNameEntity.size()*1.0);
      }
      i++;
    }
    AnswerScore as = new AnswerScore(aJCas);
    as.setBegin(answer.getBegin());
    as.setEnd(answer.getEnd());
    as.setScore(score);
    as.setCasProcessorId(this.getClass().getName());
    as.setAnswer(answer);
    as.addToIndexes();
    

  }
}
