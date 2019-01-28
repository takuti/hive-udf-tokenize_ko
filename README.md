Korean NLP on Hive
===

Tokenize Korean sentences on Hive.

```
tokenize_ko(String line [, const string mode = "discard", const array<string> stopTags, boolean outputUnknownUnigrams]) - returns tokenized strings in array<string>
```

Implementation is based on [Lucene Korean analyzer](https://lucene.apache.org/core/7_4_0/analyzers-nori/org/apache/lucene/analysis/ko/KoreanAnalyzer.html).

## Usage

```sh
mvn clean install
```

```sql
add jar hive-udf-tokenize_ko-0.0.1.jar;
create temporary function tokenize_ko as 'me.takuti.hive.nlp.tokenizer.TokenizeKoUDF';

select tokenize_ko("소설 무궁화꽃이 피었습니다.");
-- ["소설","무궁","화","꽃","피"]

select tokenize_ko("소설 무궁화꽃이 피었습니다.", null, "mixed");
-- ["소설","무궁화","무궁","화","꽃","피"]

select tokenize_ko("소설 무궁화꽃이 피었습니다.", null, "discard", array("E", "VV"));
-- ["소설","무궁","화","꽃","이"]

select tokenize_ko("Hello, world.", null, "none", array(), true);
-- ["h","e","l","l","o","w","o","r","l","d"]

select tokenize_ko("Hello, world.", null, "none", array(), false);
-- ["hello","world"]

select tokenize_ko("나는 C++ 언어를 프로그래밍 언어로 사랑한다.", null, "discard", array());
-- ["나","는","c","언어","를","프로그래밍","언어","로","사랑","하","ᆫ다"]

select tokenize_ko("나는 C++ 언어를 프로그래밍 언어로 사랑한다.", array("C++"), "discard", array());
-- ["나","는","c++","언어","를","프로그래밍","언어","로","사랑","하","ᆫ다"]
```

Note that other languages, English, Japanese and Chinese, are similarly [supported by Apache Hivemall](http://hivemall.incubator.apache.org/userguide/misc/tokenizer.html).