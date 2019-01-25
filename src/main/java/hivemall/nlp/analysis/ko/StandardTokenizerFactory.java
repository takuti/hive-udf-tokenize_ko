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
 * Standard tokenizer factory constructor. It takes the following parameters.
 * - mecabArgs: mecab run option. The default value is "-d / usr / local / lib / mecab / dic / mecab-ko-dic /".
 * See the following URL for mecab execution options. http://mecab.googlecode.com/svn/trunk/mecab/doc/mecab.html
 * - compoundNounMinLength: The minimum length of the compound noun to be decomposed. The default value is 3.
 * - useAdjectiveAndVerbOriginalForm: Whether to search using verbs and adjective pie. The default value is true.
 *
 * <pre>
 * {@code
 * <fieldType name="text_ko" class="solr.TextField" positionIncrementGap="100">
 *   <analyzer type="index">
 *     <tokenizer class="hivemall.nlp.utils.ko.StandardTokenizerFactory"
 *                mecabArgs="-d /usr/local/lib/mecab/dic/mecab-ko-dic"
 *                compoundNounMinLength="3"
 *                useAdjectiveAndVerbOriginalForm="true"/>
 *   </analyzer>
 * </fieldType>
 * }
 * </pre>
 *
 * @author bibreen <bibreen@gmail.com>
 */
public class StandardTokenizerFactory extends TokenizerFactoryBase {
  public StandardTokenizerFactory(Map<String, String> args) {
    super(args);
  }

  @Override
  protected void setPosAppender() {
    posAppender = new StandardPosAppender(option);
  }
}
