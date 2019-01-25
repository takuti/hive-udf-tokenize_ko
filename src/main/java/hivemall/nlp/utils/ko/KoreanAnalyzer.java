/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package hivemall.nlp.utils.ko;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;

import java.io.IOException;

public class KoreanAnalyzer extends StopwordAnalyzerBase {

    public KoreanAnalyzer() {
        this(DefaultSetHolder.DEFAULT_STOP_SET);
    }

    public KoreanAnalyzer(CharArraySet stopwords) {
        super(stopwords);
    }

    public static CharArraySet getDefaultStopSet(){
        return DefaultSetHolder.DEFAULT_STOP_SET;
    }

    /**
     * Atomically loads DEFAULT_STOP_SET in a lazy fashion once the
     * outer class accesses the static final set the first time.
     */
    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET;

        static {
            try {
                DEFAULT_STOP_SET = loadStopwordSet(true, KoreanAnalyzer.class, "stopwords.txt", "#");
            } catch (IOException ex) {
                // default set should always be present as it is part of the distribution (JAR)
                throw new RuntimeException("Unable to load default stopword set");
            }
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        TokenizerOption option = new TokenizerOption();
        option.compoundNounMinLength = TokenGenerator.DEFAULT_COMPOUND_NOUN_MIN_LENGTH;

        Tokenizer tokenizer = new MeCabKoTokenizer(option, new StandardPosAppender(option));

        TokenStream stream = new KoreanTokenFilter(tokenizer);
        if (!stopwords.isEmpty()) {
            stream = new StopFilter(stream, stopwords);
        }

        return new TokenStreamComponents(tokenizer, stream);
    }
}
