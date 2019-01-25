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

import java.util.Map;

/**
 * A tokenizer factory constructor for measuring document similarity. It takes the following parameters. (Experimental)
 *   - mecabArgs: mecab run option. The default value is "-d /usr/local/lib/mecab/dic/mecab-ko-dic/".
 *     See the following URL for mecab execution options. http://mecab.googlecode.com/svn/trunk/mecab/doc/mecab.html
 *   - compoundNounMinLength: The minimum length of the compound noun to be decomposed. The default value is 9999. (Not compound noun decomposition)
 *
 * <pre>
 * {@code
 * <fieldType name="text_ko" class="solr.TextField" positionIncrementGap="100">
 *   <analyzer type="index">
 *     <tokenizer class="hivemall.nlp.utils.ko.SimilarityMeasureTokenizerFactory"
 *                mecabArgs="-d /usr/local/lib/mecab/dic/mecab-ko-dic"
 *                compoundNounMinLength="9999"/>
 *   </analyzer>
 * </fieldType>
 * }
 * </pre>
 *
 * @author bibreen <bibreen@gmail.com>
 */
public class SimilarityMeasureTokenizerFactory extends TokenizerFactoryBase {
  public SimilarityMeasureTokenizerFactory(Map<String,String> args) {
    super(args);
  }

  protected void setDefaultOption() {
    option.compoundNounMinLength = TokenizerOption.NO_DECOMPOUND;
    option.useAdjectiveAndVerbOriginalForm = false;
  }

  @Override
  protected void setPosAppender() {
    posAppender = new SimilarityMeasurePosAppender(option);
  }
}
