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
import java.util.List;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenizeKoUDFTest {

    private TokenizeKoUDF udf;

    @Before
    public void setUp() {
        this.udf = new TokenizeKoUDF();
    }

    @Test
    public void testOneArgument() throws UDFArgumentException, IOException {
        ObjectInspector[] argOIs = new ObjectInspector[1];
        argOIs[0] = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        udf.initialize(argOIs);
        udf.close();
    }

    @Test
    public void testEvaluateOneRow() throws IOException, HiveException {
        ObjectInspector[] argOIs = new ObjectInspector[1];
        argOIs[0] = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
        udf.initialize(argOIs);

        GenericUDF.DeferredObject[] args = new GenericUDF.DeferredObject[1];
        args[0] = new GenericUDF.DeferredObject() {
            public Text get() throws HiveException {
                return new Text("소설 무궁화꽃이 피었습니다.");
            }

            @Override
            public void prepare(int arg) throws HiveException {}
        };
        List<Text> tokens = udf.evaluate(args);

        Assert.assertNotNull(tokens);
        Assert.assertEquals(8, tokens.size());
        Assert.assertEquals("소설 무궁 무궁화 화 꽃이 꽃 피었습니다 피/VV", getString(tokens));

        udf.close();
    }

    private static String getString(List<Text> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Text token : tokens) {
            sb.append(token.toString()).append(" ");
        }
        return sb.toString().trim();
    }

}
