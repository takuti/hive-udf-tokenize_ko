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
package hivemall.nlp.tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hivemall.nlp.utils.ko.MeCabKoTokenizer;
import hivemall.nlp.utils.ko.StandardPosAppender;
import hivemall.nlp.utils.ko.TokenGenerator;
import hivemall.nlp.utils.ko.TokenizerOption;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

@Description(name = "tokenize_ko",
        value = "_FUNC_(String line)"
                + " - returns tokenized strings in array<string>",
        extended = "select tokenize_ko(\"소설 무궁화꽃이 피었습니다.\");\n"
                + "\n"
                + "> [\"소설\",\"무궁\",\"무궁화\",\"화\",\"꽃이\",\"꽃\",\"피었습니다\",\"피/VV\"]\n")
@UDFType(deterministic = true, stateful = false)
public final class TokenizeKoUDF extends GenericUDF {

    private Tokenizer tokenizer;
    private TokenizerOption option;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        final int arglen = arguments.length;
        if (arglen != 1) {
            throw new UDFArgumentException(
                    "Invalid number of arguments for `tokenize_ko`: " + arglen);
        }

        this.tokenizer = null;

        this.option = new TokenizerOption();
        option.compoundNounMinLength = TokenGenerator.DEFAULT_COMPOUND_NOUN_MIN_LENGTH;

        return ObjectInspectorFactory.getStandardListObjectInspector(
                PrimitiveObjectInspectorFactory.writableStringObjectInspector);
    }

    @Override
    public List<Text> evaluate(DeferredObject[] arguments) throws HiveException {
        if (tokenizer == null) {
            this.tokenizer = new MeCabKoTokenizer(
                    option,
                    new StandardPosAppender(option));
        }

        Object arg0 = arguments[0].get();
        if (arg0 == null) {
            return null;
        }

        CharTermAttribute term = tokenizer.addAttribute(CharTermAttribute.class);
        final List<Text> tokens = new ArrayList<Text>();

        try {
            tokenizer.setReader(new StringReader(arg0.toString()));
            tokenizer.reset();
            while (tokenizer.incrementToken() == true) {
                String word = new String(term.buffer(), 0, term.length());
                tokens.add(new Text(word));
            }
            tokenizer.end();
        } catch (IOException e) {
            throw new HiveException("Failed to tokenize the sentence: " + e.getMessage());
        }

        return tokens;
    }

    @Override
    public void close() throws IOException {
        if (tokenizer != null) {
            tokenizer.close();
        }
    }

    @Override
    public String getDisplayString(String[] children) {
        return "tokenize_ko(" + Arrays.toString(children) + ')';
    }

}
