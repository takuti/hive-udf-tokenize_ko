package hivemall.nlp.analysis.ko;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MeCabKoStandardTokenizerTest.class,
  PosIdManagerTest.class,
  TokenGeneratorWithStandardPosAppenderTest.class,
  TokenGeneratorWithSimilarityMeasurePosAppenderTest.class,
  TokenGeneratorWithKeywordSearchPosAppenderTest.class})
public class AllTests {
}
