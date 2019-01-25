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

import java.util.LinkedList;

/**
 * A class that takes a part-of-speech object (Pos) and constructs a word.
 *
 * @author bibreen <bibreen@gmail.com>
 */
class Eojeol {
  private PosAppender appender;
  private int compoundNounMinLength;
  private boolean hasCompoundNoun;

  private LinkedList<Pos> posList = new LinkedList<Pos>();
  private String term = "";

  Eojeol(PosAppender appender, int compoundNounMinLength) {
    this.appender = appender;
    this.compoundNounMinLength = compoundNounMinLength;
    this.hasCompoundNoun = false;
  }

  public boolean append(Pos pos) {
    if (isAppendable(pos)) {
      if (pos.isPosIdOf(PosIdManager.PosId.COMPOUND) &&
              pos.getSurfaceLength() >= compoundNounMinLength) {
        this.hasCompoundNoun = true;
        posList.addAll(TokenGenerator.getAnalyzedPoses(pos));
      } else {
        posList.add(pos);
      }
      term += pos.getSurface();
      return true;
    } else {
      return false;
    }
  }

  private boolean isAppendable(Pos pos) {
    return posList.isEmpty() || appender.isAppendable(posList.getLast(), pos);
  }

  /**
   * Combine Pos in Eojeol to generate a Pos to be Token.
   * @return If there is a token, it returns a list of Pos. If there is no token to be extracted, it returns null.
   */
  public LinkedList<Pos> generateTokens() {
    if (isSkippable()) {
      return null;
    }
    return appender.getTokensFrom(this);
  }

  public boolean isSkippable() {
    return posList.isEmpty() ||
            (posList.size() == 1 && appender.isSkippablePos(posList.get(0)));
  }

  public int getNumPoses() {
    return posList.size();
  }

  public Pos getPos(int index) {
    return posList.get(index);
  }

  public LinkedList<Pos> getPosList() {
    return posList;
  }

  public String getTerm() {
    return term;
  }

  public int getStartOffset() {
    return posList.getFirst().getStartOffset();
  }

  public boolean hasCompoundNoun() {
    return hasCompoundNoun;
  }

  public void setToCompoundNoun() {
    hasCompoundNoun = true;
  }

  public void clear() {
    posList.clear();
    term = "";
  }

  @Override
  public String toString() {
    return posList.toString();
  }
}
