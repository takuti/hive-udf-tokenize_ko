Korean NLP on Hive
===

This package depends on [Hivemall](https://github.com/apache/incubator-hivemall)'s NLP capability.

Before getting started, build the latest version of **hivemall-all-{HIVEMALL_VERSION}.jar** as documented on [Hivemall installation guide](https://hivemall.incubator.apache.org/userguide/getting_started/installation.html).

## Usage

1. Install [mecab-ko-dic](https://bitbucket.org/eunjeon/mecab-ko-dic)
  - Make sure the dictionary exists at `/usr/local/lib/mecab/dic/mecab-ko-dic`
2. Build [mecab-java-0.996](https://drive.google.com/drive/folders/0B4y35FiV1wh7fjQ5SkJETEJEYzlqcUY4WUlpZmR4dDlJMWI5ZUlXN2xZN2s2b0pqT3hMbTQ), then:
   ```sh
   sudo cp libMeCab.so /usr/local/lib
   mvn install:install-file -Dfile=MeCab.jar -DgroupId=org.chasen.mecab -DartifactId=mecab-java -Dversion=0.996 -Dpackaging=jar
   ```
3. `mvn clean install`
4. Go to Hive console:
   ```sql
   add jar hivemall-all-{HIVEMALL_VERSION}.jar; -- e.g., hivemall-all-0.5.1-incubating-SNAPSHOT.jar
   add jar hive-udf-tokenize_ko-0.0.1.jar;
   create temporary function tokenize_ko as 'hivemall.nlp.tokenizer.TokenizeKoUDF';
   select tokenize_ko("소설 무궁화꽃이 피었습니다.");
   -- ["소설","무궁","무궁화","화","꽃이","꽃","피었습니다","피/VV"]
   ```

> Implementation of Korean tokenizer is based on: [eunjeon/mecab-ko-lucene-analyzer](https://bitbucket.org/eunjeon/mecab-ko-lucene-analyzer).