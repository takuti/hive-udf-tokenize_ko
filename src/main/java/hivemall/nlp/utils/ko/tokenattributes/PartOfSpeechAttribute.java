package hivemall.nlp.utils.ko.tokenattributes;

import org.apache.lucene.util.Attribute;

public interface PartOfSpeechAttribute extends Attribute {
  public String partOfSpeech();
  public void setPartOfSpeech(String partOfSpeech);
}