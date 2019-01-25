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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * PosAppender for cognate and predicate indexing tokenizer.
 *
 * @author bibreen <bibreen@gmail.com>
 */
public class KeywordSearchPosAppender extends PosAppender {
  static public Set<Appendable> appendableSet;

  static {
    appendableSet = new HashSet<Appendable>();

    // Configuring an Appendable HashSet
    // Assume that a word that is not in the dictionary (UNKNOWN) is a clan.

    // Mother (E) + mother (E)
    appendableSet.add(new Appendable(PosIdManager.PosId.E, PosIdManager.PosId.E));
    // Verb (V *) + E [+ E]*
    appendableSet.add(new Appendable(PosIdManager.PosId.VV, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VA, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VX, PosIdManager.PosId.E));

    // Clan (N*) + Noun Derived Suffix (XSN)
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.XSN));

    // Cene prefix (XPN) + Clan (N*)
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NNG));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NNP));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NNB));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NNBC));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NP));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NR));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.COMPOUND));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.UNKNOWN));
  }

  public KeywordSearchPosAppender(TokenizerOption option) {
    super(option);
  }

  @Override
  public boolean isAppendable(Pos left, Pos right) {
    if (right.getNode() != null && right.hasSpace()) {
      return false;
    }
    if (left.getPosId() == PosIdManager.PosId.INFLECT &&
        !(left.getStartPosId() == PosIdManager.PosId.VA ||
            left.getStartPosId() == PosIdManager.PosId.VV)) {
      return false;
    }
    return appendableSet.contains(
        new Appendable(left.getEndPosId(), right.getStartPosId()));
  }

  @Override
  public boolean isSkippablePos(Pos pos) {
    PosIdManager.PosId posId = pos.getPosId();
    switch (posId) {
      case COMPOUND:
      case NNG:
      case NNP:
      case NNB:
      case NNBC:
      case NP:
      case NR:
      case SL:
      case SH:
      case SN:
      case XR:
        return false;
      case INFLECT:
        return !(pos.getStartPosId() == PosIdManager.PosId.VA ||
            pos.getStartPosId() == PosIdManager.PosId.VV);
      default:
        return true;
    }
  }

  @Override
  public LinkedList<Pos> getTokensFrom(Eojeol eojeol) {
    LinkedList<Pos> output = new LinkedList<Pos>();
    LinkedList<Pos> poses = eojeol.getPosList();
    if (poses.size() == 1) {
      output.add(poses.getFirst());
    } else {
      Pos eojeolPos = new Pos(
          eojeol.getTerm(), PosIdManager.PosId.EOJEOL, eojeol.getStartOffset(), 1, 1);
      output.add(eojeolPos);
    }
    return output;
  }

  /**
   * Determine whether it is a morpheme that can be used alone.
   *
   * @param pos The morpheme part.
   */
  private boolean isAbsolutePos(Pos pos) {
    return false;
  }
}
