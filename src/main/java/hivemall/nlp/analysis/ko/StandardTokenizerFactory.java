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
 * 표준 tokenizer 팩토리 생성자. 다음과 같은 파라미터를 받는다.
 *   - mecabArgs: mecab 실행옵션. 디폴트 값은 "-d /usr/local/lib/mecab/dic/mecab-ko-dic/" 이다.
 *     mecab 실행 옵션은 다음의 URL을 참조. http://mecab.googlecode.com/svn/trunk/mecab/doc/mecab.html
 *   - compoundNounMinLength: 분해를 해야하는 복합명사의 최소 길이. 디폴트 값은 3이다.
 *   - useAdjectiveAndVerbOriginalForm: 동사와 형용사 원형을 사용하여 검색할지 여부. 디폴트 값은 true이다.
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
