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
 * 체언과 용언 위주의 인덱싱 tokenizer를 위한 PosAppender.
 *
 * @author bibreen <bibreen@gmail.com>
 */
public class KeywordSearchPosAppender extends PosAppender {
  static public Set<Appendable> appendableSet;

  static {
    appendableSet = new HashSet<Appendable>();

    // Appenable HashSet 구성
    // 사전에 없는 단어(UNKNOWN)은 체언이라고 가정한다.

    // 어미(E) + 어미(E)
    appendableSet.add(new Appendable(PosIdManager.PosId.E, PosIdManager.PosId.E));
    // 용언(V*) + E [+ E]*
    appendableSet.add(new Appendable(PosIdManager.PosId.VV, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VA, PosIdManager.PosId.E));
    appendableSet.add(new Appendable(PosIdManager.PosId.VX, PosIdManager.PosId.E));

    // 체언(N*) + 명사 파생 접미사(XSN)
    appendableSet.add(new Appendable(PosIdManager.PosId.NNG, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNP, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNB, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NNBC, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NP, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.NR, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.COMPOUND, PosIdManager.PosId.XSN));
    appendableSet.add(new Appendable(PosIdManager.PosId.UNKNOWN, PosIdManager.PosId.XSN));

    // 체언 접두사(XPN) + 체언(N*)
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
   * 단독으로 쓰일 수 있는 형태소인지를 판단한다.
   *
   * @param pos 형태소 품사.
   */
  private boolean isAbsolutePos(Pos pos) {
    return false;
  }
}
