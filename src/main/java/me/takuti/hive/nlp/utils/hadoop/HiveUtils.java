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
package me.takuti.hive.nlp.utils.hadoop;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public final class HiveUtils {

    private HiveUtils() {}

    public static boolean isVoidOI(@Nonnull final ObjectInspector oi) {
        String typeName = oi.getTypeName();
        return typeName.equals("void");
    }

    public static boolean isStringOI(@Nonnull final ObjectInspector oi) {
        String typeName = oi.getTypeName();
        return typeName.equals("string");
    }

    public static boolean isBooleanOI(@Nonnull final ObjectInspector oi) {
        String typeName = oi.getTypeName();
        return typeName.equals("boolean");
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Writable> T getConstValue(@Nonnull final ObjectInspector oi)
            throws UDFArgumentException {
        if (!ObjectInspectorUtils.isConstantObjectInspector(oi)) {
            throw new UDFArgumentException("argument must be a constant value: "
                    + TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
        }
        ConstantObjectInspector constOI = (ConstantObjectInspector) oi;
        Object v = constOI.getWritableConstantValue();
        return (T) v;
    }

    @Nullable
    public static String[] getConstStringArray(@Nonnull final ObjectInspector oi)
            throws UDFArgumentException {
        if (!ObjectInspectorUtils.isConstantObjectInspector(oi)) {
            throw new UDFArgumentException("argument must be a constant value: "
                    + TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
        }
        ConstantObjectInspector constOI = (ConstantObjectInspector) oi;
        if (constOI.getCategory() != Category.LIST) {
            throw new UDFArgumentException(
                    "argument must be an array: " + TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
        }
        final List<?> lst = (List<?>) constOI.getWritableConstantValue();
        if (lst == null) {
            return null;
        }
        final int size = lst.size();
        final String[] ary = new String[size];
        for (int i = 0; i < size; i++) {
            Object o = lst.get(i);
            if (o != null) {
                ary[i] = o.toString();
            }
        }
        return ary;
    }

    public static String getConstString(@Nonnull final ObjectInspector oi)
            throws UDFArgumentException {
        if (!isStringOI(oi)) {
            throw new UDFArgumentException("argument must be a Text value: "
                    + TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
        }
        Text v = getConstValue(oi);
        return v == null ? null : v.toString();
    }

    public static boolean getConstBoolean(@Nonnull final ObjectInspector oi)
            throws UDFArgumentException {
        if (!isBooleanOI(oi)) {
            throw new UDFArgumentException("argument must be a Boolean value: "
                    + TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
        }
        BooleanWritable v = getConstValue(oi);
        return v.get();
    }
}
