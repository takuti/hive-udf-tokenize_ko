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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hivemall.nlp.analysis.ko.KoreanAnalyzer;

import hivemall.utils.io.IOUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import javax.annotation.Nonnull;

@Description(name = "tokenize_ko",
        value = "_FUNC_(String line)"
                + " - returns tokenized strings in array<string>",
        extended = "select tokenize_ko(\"소설 무궁화꽃이 피었습니다.\");\n"
                + "\n"
                + "> [\"소설\",\"무궁\",\"무궁화\",\"화\",\"꽃이\",\"꽃\",\"피었습니다\",\"피/VV\"]\n")
@UDFType(deterministic = true, stateful = false)
public final class TokenizeKoUDF extends GenericUDF {

    private transient KoreanAnalyzer analyzer;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        final int arglen = arguments.length;
        if (arglen != 1) {
            throw new UDFArgumentException(
                    "Invalid number of arguments for `tokenize_ko`: " + arglen);
        }

        this.analyzer = null;

        return ObjectInspectorFactory.getStandardListObjectInspector(
                PrimitiveObjectInspectorFactory.writableStringObjectInspector);
    }

    @Override
    public List<Text> evaluate(DeferredObject[] arguments) throws HiveException {
        if (analyzer == null) {
            this.analyzer = new KoreanAnalyzer();
        }

        Object arg0 = arguments[0].get();
        if (arg0 == null) {
            return null;
        }

        String line = arg0.toString();

        final List<Text> tokens = new ArrayList<Text>(32);
        TokenStream stream = null;
        try {
            stream = analyzer.tokenStream("", line);
            if (stream != null) {
                analyzeTokens(stream, tokens);
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(analyzer);
            throw new HiveException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return tokens;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(analyzer);
    }

    private static void analyzeTokens(@Nonnull TokenStream stream, @Nonnull List<Text> results)
            throws IOException {
        // instantiate an attribute placeholder once
        CharTermAttribute termAttr = stream.getAttribute(CharTermAttribute.class);
        stream.reset();

        while (stream.incrementToken()) {
            String term = termAttr.toString();
            results.add(new Text(term));
        }
    }

    @Override
    public String getDisplayString(String[] children) {
        return "tokenize_ko(" + Arrays.toString(children) + ')';
    }

}
