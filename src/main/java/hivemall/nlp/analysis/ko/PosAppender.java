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
 * TokenGenerator is an abstract class that is responsible for selecting a token-based part-of-speech and a part-of-speech selection algorithm.
 *
 * @author bibreen <bibreen@gmail.com>
 */
public abstract class PosAppender {
  protected TokenizerOption option;

  public PosAppender(TokenizerOption option) {
    this.option = option;
  }

  /**
   * Returns whether left PosId and right PosId are parts of speech that can be attached in the form of a word.
   */
  public abstract boolean isAppendable(Pos left, Pos right);
  /**
   * Returns true if the POS is excluded from indexing, false otherwise.
   */
  public abstract boolean isSkippablePos(Pos pos);
  /**
   * It returns the POS which is extracted from the POS list constituting the word by token.
   */
  public abstract LinkedList<Pos> getTokensFrom(Eojeol eojeol);
}
