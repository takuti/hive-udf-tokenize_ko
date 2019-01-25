/*******************************************************************************
 * Copyright 2013 Yongwoon Lee, Yungho Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hivemall.nlp.analysis.ko;

import java.util.*;

import org.chasen.mecab.Node;
import org.chasen.mecab.MeCab;

/**
 * A class that creates a Pos list to be used in a Lucene tokenizer by receiving a node from MeCab.
 *
 * @author bibreen <bibreen@gmail.com>
 * @author amitabul <mousegood@gmail.com>
 */
public class TokenGenerator {
  public static final int NO_DECOMPOUND = 9999;
  public static final int DEFAULT_COMPOUND_NOUN_MIN_LENGTH = 3;

  private final PosAppender appender;
  private LinkedList<Pos> posList = new LinkedList<Pos>();
  private ListIterator<Pos> posIter;
  private int compoundNounMinLength;

  /**
   * TokenGenerator Constructor
   *
   * @param appender PosAppender
   * @param compoundNounMinLength The minimum length of the noun to decompose in the compound noun.
   * If no compound noun decomposition is required, enter TokenGenerator.NO_DECOMPOUND.
   * @param beginNode starting node
   */
  public TokenGenerator(
      PosAppender appender, int compoundNounMinLength, Node beginNode) {
    this.appender = appender;
    this.compoundNounMinLength = compoundNounMinLength;
    convertNodeListToPosList(beginNode);
    posIter = posList.listIterator();
  }

  private void convertNodeListToPosList(Node beginNode) {
    Node node = beginNode.getNext();
    Pos prevPos = new Pos("", PosIdManager.PosId.UNKNOWN, 0, 0, 0);
    while (!isEosNode(node)) {
      Pos curPos = new Pos(node, prevPos.getEndOffset());
      if (curPos.getPosId() == PosIdManager.PosId.PREANALYSIS) {
        posList.addAll(getAnalyzedPoses(curPos));
      } else {
        posList.add(curPos);
      }
      prevPos = curPos;
      node = node.getNext();
    }
  }

  static private boolean isEosNode(Node node) {
    return node == null || node.getStat() == MeCab.MECAB_EOS_NODE;
  }

  /**
   * It interprets the index expression string of mecab-ko-dic and returns the pos list.
   */
  static public LinkedList<Pos> getAnalyzedPoses(Pos pos) {
    LinkedList<Pos> output = new LinkedList<Pos>();
    String indexExp = pos.getExpression();
    if (indexExp == null) {
      output.add(pos);
      return output;
    }
    String[] posExps = indexExp.split("\\+");
    if (posExps.length == 1) {
      output.add(pos);
      return output;
    }

    for (String posExp: posExps) {
      output.add(new Pos(posExp, 0));
    }
    // Recalculate offset of disassembled POS
    Pos prevPos = null;
    for (Pos curPos: output) {
      if (prevPos == null) {
        curPos.setStartOffset(pos.getStartOffset());
        prevPos = curPos;
      } else {
        if (curPos.getPositionIncr() == 0) {
          curPos.setStartOffset(prevPos.getStartOffset());
        } else {
          curPos.setStartOffset(prevPos.getEndOffset());
          prevPos = curPos;
        }
      }
    }
    return output;
  }

  /**
   * Return the pos of the next word.
   * @return If the return value is null, it is the end of the generator.
   */
  public LinkedList<Pos> getNextEojeolTokens() {
    Eojeol eojeol = new Eojeol(appender, compoundNounMinLength);
    while (posIter.hasNext()) {
      Pos curPos = posIter.next();
      if (!eojeol.append(curPos)) {
        posIter.previous();
        LinkedList<Pos> poses = eojeol.generateTokens();
        if (poses != null) {
          return poses;
        } else {
          eojeol.clear();
        }
      }
    }
    // return last eojeol tokens
    return eojeol.generateTokens();
  }
}
