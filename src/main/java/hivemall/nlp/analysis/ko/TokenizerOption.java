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

/**
 * Tokenizer options
 */
public class TokenizerOption {
  public static final int NO_DECOMPOUND = 9999;

  /** mecab run options (ex: -d /usr/local/lib/mecab/dic/mecab-ko-dic/) */
  public String mecabArgs = "-d /usr/local/lib/mecab/dic/mecab-ko-dic/";
  /** The minimum length of a compound noun to be decomposed. */
  public int compoundNounMinLength = 3;
  /** Verb, adjective original search */
  public boolean useAdjectiveAndVerbOriginalForm = true;
  // boolean useHanjaRead = false;
}
