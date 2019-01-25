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
import java.util.List;
import java.util.Set;

import hivemall.nlp.analysis.ko.util.XpnDiscriminator;

/**
 * PosAppender for standard tokenizer.
 *
 * @author bibreen <bibreen@gmail.com>
 * @author amitabul <mousegood@gmail.com>
 */
public class StandardPosAppender extends PosAppender {
  static public Set<Appendable> appendableSet;

  static {
    appendableSet = new HashSet<Appendable>();

    // Configure Appendable HashSet
    // Assume that a word that is not in the dictionary (UNKNOWN) is a clan.

    // The mother (E) + the mother (E)
    appendableSet.add(new Appendable(PosIdManager.PosId.E, PosIdManager.PosId.E));
    // Root (XR) + E [+ E]*
    appendableSet.add(new Appendable(PosIdManager.PosId.XR, PosIdManager.PosId.E));
    // Adjective Derived Suffix (XSA) + E [+ E]* Adjective Derived Suffix (XSA)
    appendableSet.add(new Appendable(PosIdManager.PosId.VV, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VA, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VX, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VCP, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VCN, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.XSV, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.XSA, PosIdManager.PosId.E));
    // Cognition (N*) | General Adverb (MAG) | Root (XR) + Verb Derived Suffix (XSV)
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.MAG, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.XR, PosIdManager.PosId.XSV));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.XSV));
    // Cognition (N*) | General Adverbs (MAG) | Root (XR) + Adjective Derived Suffix (XSA)
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.MAG, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.XR, PosIdManager.PosId.XSA));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.XSA));
    // Censorship (N*) | Noun Derived Suffix (XSN) + Positive Assignment (VCP)
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.XSN, PosIdManager.PosId.VCP));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.VCP));
    // Cheong (N *) + Survey [+ Survey]*
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.J));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.J));
    // Cene prefix (XPN) + Clan (N*)
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NNG));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NR));
    appendableSet.add(new Appendable(PosIdManager.PosId.XPN, PosIdManager.PosId.NP));
    // Noun Derived Suffix (XSN) + Survey (J)
    appendableSet.add(new Appendable(PosIdManager.PosId.XSN, PosIdManager.PosId.J));
    // Maternity (E) + Survey (J) - When the mother is a noun-
    appendableSet.add(new Appendable(PosIdManager.PosId.E, PosIdManager.PosId.J));
    // mother (E) + auxiliary verb (VX) - if the mother is the connecting mother
    // 'Auxiliary verbs should be spelled out, but in some cases they may be pasted.'
    // (http://www.korean.go.kr/09_new/dic/rule/rule01_0503.jsp)
    // For the above reasons, neither mother nor auxiliary verbs are attached.
    // appendableSet.add (new Appendable (PosId.E, PosId.VX));
    // Adverb (MAG) + survey (J)
    appendableSet.add(new Appendable(PosIdManager.PosId.MAG, PosIdManager.PosId.J));
    // Survey (J) + Survey (J)
    appendableSet.add(new Appendable(PosIdManager.PosId.J, PosIdManager.PosId.J));
    // Foreign language (SL) + survey (J)
    appendableSet.add(new Appendable(PosIdManager.PosId.SL, PosIdManager.PosId.J));
    // Hanja (SH) + Survey (J)
    appendableSet.add(new Appendable(PosIdManager.PosId.SH, PosIdManager.PosId.J));
  }

  public StandardPosAppender(TokenizerOption option) {
    super(option);
  }

  @Override
  public boolean isAppendable(Pos left, Pos right) {
    if (right.getNode() != null && right.hasSpace()) {
      return false;
    }
    return appendableSet.contains(
        new Appendable(left.getEndPosId(), right.getStartPosId()));
  }

  @Override
  public boolean isSkippablePos(Pos pos) {
    // Exclusive symbols are excluded from token generation.
    PosIdManager.PosId posId = pos.getPosId();
    return posId == PosIdManager.PosId.SF ||
        posId.in(PosIdManager.PosId.SP, PosIdManager.PosId.SE);
  }

  @Override
  public LinkedList<Pos> getTokensFrom(Eojeol eojeol) {
    preprocessXpn(eojeol);
    LinkedList<Pos> output = getAdditionalPosesFrom(eojeol);
    insertEojeolPosTo(eojeol, output);
    return output;
  }

  /**
   * When starting with a non-independent cognitive prefix, combine the following common nouns, rhetoric, and pronouns with new nouns
   * Make up
   * - Rain / XPN + Normal / NNG -> Abnormal / NNG
   * Independent cognate prefixes (as defined in XpnDiscriminator) that contain compound nouns
   * Set and index the prefix and chess with the logic of the compound noun.
   * - King / XPN + Mandu / NNG -> King / XPN + Mandu / NNG
   * KIMMAN DUPLE / COMPOUND
   *
   * @param eojeol
   */
  private void preprocessXpn(Eojeol eojeol) {
    if (eojeol.getNumPoses() < 2) {
      return;
    }
    Pos first = eojeol.getPos(0);
    Pos second = eojeol.getPos(1);
    if (!first.isPosIdOf(PosIdManager.PosId.XPN) ||
        (!second.isPosIdOf(PosIdManager.PosId.NNG) &&
            !second.isPosIdOf(PosIdManager.PosId.NR) &&
            !second.isPosIdOf(PosIdManager.PosId.NP))) {
      return;
    }
    if (XpnDiscriminator.isIndependentXpn(first.getSurface())) {
      eojeol.setToCompoundNoun();
    } else {
      LinkedList<Pos> eojeolPosList = eojeol.getPosList();
      Pos xpn = eojeolPosList.poll();
      Pos noun = eojeolPosList.poll();
      Pos newNoun = xpn.append(noun, PosIdManager.PosId.NNG, 1);
      newNoun.setPositionLength(1);
      eojeolPosList.addFirst(newNoun);
    }
  }

  private LinkedList<Pos> getAdditionalPosesFrom(Eojeol eojeol) {
    LinkedList<Pos> poses = eojeol.getPosList();
    if (eojeol.hasCompoundNoun()) {
      LinkedList<Pos> output = new LinkedList<>();
      // TODO: Let's refactor code that's hard to understand
      Pos prevPos = null;
      int numAbsolutePos = 0;
      for (Pos pos: poses) {
        if (!isAbsolutePos(pos)) {
          break;
        }
        output.add(pos);
        numAbsolutePos += 1;
        if (prevPos == null) {
          prevPos = pos;
        } else {
          if (areBothSingleLengthNoun(prevPos, pos)) {
            prevPos = pos;
            continue;
          }
          Pos compound = prevPos.append(pos, PosIdManager.PosId.COMPOUND, 0);
          output.add(output.size() - 1, compound);
          prevPos = pos;
        }
      }
      if (numAbsolutePos >= 3) {
        output.add(1, generateWholeCompoundNoun(poses));
      }
      return output;
    } else {
      LinkedList<Pos> output = new LinkedList<>();
      for (Pos pos: poses) {
        if (isAbsolutePos(pos)) {
          pos.setPositionIncr(0);
          output.add(pos);
        }
        if (pos.isPosIdOf(PosIdManager.PosId.INFLECT)) {
          Pos firstPos = extractFirstPos(pos);
          if (isAbsolutePos(firstPos) &&
                  firstPos.getSurfaceLength() <= pos.getSurfaceLength()) {
            output.add(firstPos);
          }
        }
      }
      return output;
    }
  }

  private boolean areBothSingleLengthNoun(Pos p1, Pos p2) {
    return (p1.getPosId().in(PosIdManager.PosId.NNG, PosIdManager.PosId.NNP) &&
        p1.getSurfaceLength() == 1 &&
        p2.getPosId().in(PosIdManager.PosId.NNG, PosIdManager.PosId.NNP) &&
        p2.getSurfaceLength() == 1);
  }

  private Pos generateWholeCompoundNoun(LinkedList<Pos> poses) {
    Pos wholeCompoundNoun = poses.getFirst();
    for (Pos pos: poses.subList(1, poses.size())) {
      if (!isAbsolutePos(pos)) {
        break;
      }
      wholeCompoundNoun = wholeCompoundNoun.append(pos, PosIdManager.PosId.COMPOUND, 0);
    }
    return wholeCompoundNoun;
  }

  private Pos insertEojeolPosTo(Eojeol eojeol, LinkedList<Pos> eojeolTokens) {
    Pos eojeolPos;
    LinkedList<Pos> posList = eojeol.getPosList();
    if (posList.size() == 1) {
      if (eojeolTokens.isEmpty()) {
        eojeolTokens.add(posList.getFirst());
      }
      if (eojeolTokens.getFirst() != posList.getFirst()) {
        eojeolTokens.addFirst(posList.getFirst());
      }
      eojeolPos = eojeolTokens.getFirst();
      eojeolPos.setPositionIncr(1);
    } else {
      if (eojeol.hasCompoundNoun() && eojeolTokens.size() > 0) {
        int positionLength = recalcEojeolPositionLength(eojeolTokens);
        eojeolPos = new Pos(
            eojeol.getTerm(), PosIdManager.PosId.EOJEOL,
            eojeol.getStartOffset(), 0, positionLength);
        eojeolPos.setPos(concatMophemes(posList));
        if (eojeolTokens.size() < 2 ||
            !eojeolPos.equalsOffset(eojeolTokens.get(1))) {
          eojeolTokens.add(1, eojeolPos);
        }
      } else {
        eojeolPos = new Pos(
            eojeol.getTerm(), PosIdManager.PosId.EOJEOL, eojeol.getStartOffset(), 1, 1);
        eojeolPos.setPos(concatMophemes(posList));
        eojeolTokens.addFirst(eojeolPos);
      }
    }
    return eojeolPos;
  }

  private String concatMophemes(List<Pos> poses) {
    StringBuilder buff = new StringBuilder();
    for (int i = 0; i < poses.size(); i++) {
      if (i != 0) {
        buff.append("+");
      }
      buff.append(poses.get(i).getMophemes());
    }
    return buff.toString();
  }

  private int recalcEojeolPositionLength(LinkedList<Pos> eojeolTokens) {
    int positionLength = 0;
    for (Pos pos: eojeolTokens) {
      positionLength += pos.getPositionIncr();
    }
    return positionLength;
  }

  /**
   * Determine whether the morpheme can be used alone.
   *
   * @param pos Morpheme part.
   */
  private boolean isAbsolutePos(Pos pos) {
    if (option.useAdjectiveAndVerbOriginalForm) {
      return (pos.getPosId().in(PosIdManager.PosId.NNG, PosIdManager.PosId.NR) ||
          pos.isPosIdOf(PosIdManager.PosId.COMPOUND) ||
          pos.isPosIdOf(PosIdManager.PosId.MAG) ||
          pos.isPosIdOf(PosIdManager.PosId.MM) ||
          pos.isPosIdOf(PosIdManager.PosId.XR) ||
          pos.isPosIdOf(PosIdManager.PosId.SH) ||
          pos.isPosIdOf(PosIdManager.PosId.SL) ||
          pos.isPosIdOf(PosIdManager.PosId.SN) ||
          pos.isPosIdOf(PosIdManager.PosId.UNKNOWN) ||
          pos.isPosIdOf(PosIdManager.PosId.VA) ||
          pos.isPosIdOf(PosIdManager.PosId.VV) ||
          pos.isPosIdOf(PosIdManager.PosId.XPN) ||
          pos.isPosIdOf(PosIdManager.PosId.XSN)
      );
    } else {
      return (pos.getPosId().in(PosIdManager.PosId.NNG, PosIdManager.PosId.NR) ||
          pos.isPosIdOf(PosIdManager.PosId.COMPOUND) ||
          pos.isPosIdOf(PosIdManager.PosId.MAG) ||
          pos.isPosIdOf(PosIdManager.PosId.MM) ||
          pos.isPosIdOf(PosIdManager.PosId.XR) ||
          pos.isPosIdOf(PosIdManager.PosId.SH) ||
          pos.isPosIdOf(PosIdManager.PosId.SL) ||
          pos.isPosIdOf(PosIdManager.PosId.SN) ||
          pos.isPosIdOf(PosIdManager.PosId.UNKNOWN) ||
          pos.isPosIdOf(PosIdManager.PosId.XPN) ||
          pos.isPosIdOf(PosIdManager.PosId.XSN)
      );
    }
  }

  /**
   * Get the first pos from the Inflect Pos.
   *
   * @param inflectPos
   * @return pos morpheme part
   */
  private Pos extractFirstPos(Pos inflectPos) {
    if (!inflectPos.isPosIdOf(PosIdManager.PosId.INFLECT)) {
      return null;
    }
    String first = inflectPos.getExpression().split("\\+")[0];
    String[] datas = first.split("/");
    if (datas.length != 3) {
      return null;
    }
    String surface = datas[0];
    PosIdManager.PosId posId = PosIdManager.PosId.convertFrom(datas[1]);
    int startOffset = inflectPos.getStartOffset();
    return new Pos(surface, posId, startOffset, 0, 1);
  }
}
