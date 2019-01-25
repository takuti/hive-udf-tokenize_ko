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

import org.chasen.mecab.Node;

/**
 * Part of speech (morpheme, part-of-speech ID, location, etc.) Information class
 *
 * @author bibreen <bibreen@gmail.com>
 */
public class Pos {
  private String surface;
  private String semanticClass;
  private String pos;
  private PosIdManager.PosId posId;
  private PosIdManager.PosId startPosId;
  private PosIdManager.PosId endPosId;
  private int startOffset;
  private int positionIncr;
  private int positionLength;
  private String expression;
  private Node node;

  // index_expression
  public static class ExpressionIndex {
    final static int TERM = 0;
    final static int TAG = 1;
    final static int SEMANTIC_CLASS = 2;
  }

  // feature
  public static class NodeIndex {
    final static int POS = 0;
    final static int SEMANTIC_CLASS = 1;
    final static int TYPE = 4;
    // when Inflect
    final static int START_POS = 5;
    final static int END_POS = 6;
    final static int EXPRESSION = 7;
  }

  public Pos(
      String surface,
      PosIdManager.PosId posId,
      int startOffset,
      int positionIncr,
      int positionLength) {

    this.surface = surface;
    this.posId = posId;
    startPosId = posId;
    endPosId = posId;
    this.startOffset = startOffset;
    this.positionIncr = positionIncr;
    this.positionLength = positionLength;
  }

  /**
   * A Pos constructor that uses node, the data structure of mecab.
   *
   * @param node Node
   * @param prevEndOffset End offset of previous Pos
   */
  public Pos(Node node, int prevEndOffset) {
    this(
        node.getSurface(),
        PosIdManager.PosId.convertFrom(node.getPosid()),
        prevEndOffset + node.getRlength() - node.getLength(),
        1, 1);
    this.node = node;
    parseFeatureString();
  }

  /**
   * Pos constructor that receives a string representing Pos.
   * expression is composed as follows.
   * '<surface>/<tag>/<semantic_class>'
   * ex) Pangyo/NNP/nomination
   */
  public Pos(String expression, int startOffset) {
    try {
      String[] datas = expression.split("/");
      this.surface = datas[ExpressionIndex.TERM];
      this.posId = PosIdManager.PosId.convertFrom(datas[ExpressionIndex.TAG]);
      this.pos = datas[ExpressionIndex.TAG];
      this.semanticClass = convertSemanticClass(datas[ExpressionIndex.SEMANTIC_CLASS]);
      startPosId = posId;
      endPosId = posId;
      this.startOffset = startOffset;
      this.positionIncr = 1;
      this.positionLength = 1;
    } catch (IndexOutOfBoundsException e) {
      throw new RuntimeException("Use mecab-ko-dic 2.0.0 or higher.");
    }
  }

  private void parseFeatureString() {
    String feature = node.getFeature();

    String features[] = node.getFeature().split(",");
    this.pos = features[NodeIndex.POS];
    this.semanticClass = convertSemanticClass(features[NodeIndex.SEMANTIC_CLASS]);

    String items[] = feature.split(",");
    if (posId == PosIdManager.PosId.INFLECT || posId == PosIdManager.PosId.PREANALYSIS) {
      this.startPosId = PosIdManager.PosId.convertFrom(items[NodeIndex.START_POS].toUpperCase());
      this.endPosId = PosIdManager.PosId.convertFrom(items[NodeIndex.END_POS].toUpperCase());
      expression = items[NodeIndex.EXPRESSION];
    } else if (posId == PosIdManager.PosId.COMPOUND){
      this.startPosId = PosIdManager.PosId.NNG;
      this.endPosId = PosIdManager.PosId.NNG;
      this.positionLength =
          getCompoundNounPositionLength(items[NodeIndex.EXPRESSION]);
      expression = items[NodeIndex.EXPRESSION];
    } else {
      this.startPosId = posId;
      this.endPosId = posId;
    }
  }

  private int getCompoundNounPositionLength(String indexExpression) {
    String[] tokens = indexExpression.split("\\+");
    return tokens.length;
  }

  public Pos append(Pos pos, PosIdManager.PosId posId, int positionIncr) {
    return new Pos(
            this.getSurface() + pos.getSurface(),
            posId, this.getStartOffset(),
            positionIncr, this.getPositionLength() + pos.getPositionLength());
  }

  public boolean equalsOffset(Pos pos) {
    return (this.getStartOffset() == pos.getStartOffset() &&
        this.getEndOffset() == pos.getEndOffset());
  }

  public Node getNode() {
    return node;
  }

  public PosIdManager.PosId getPosId() {
    return posId;
  }

  public PosIdManager.PosId getStartPosId() {
    return startPosId;
  }

  public PosIdManager.PosId getEndPosId() {
    return endPosId;
  }

  public String getSurface() {
    return surface;
  }

  public String getTokenString() {
    switch (posId) {
      case VV:
      case VA:
        return surface + '/' + posId.toString();
      default:
        return surface;
    }
  }

  public int getSurfaceLength() {
    return surface.length();
  }

  public String getMophemes() {
    return pos;
  }

  public String getSemanticClass() {
    return semanticClass;
  }

  public String getExpression() {
    return expression;
  }

  public int getStartOffset() {
    return startOffset;
  }

  public int getEndOffset() {
    return startOffset + surface.length();
  }

  public int getPositionIncr() {
    return positionIncr;
  }

  public int getPositionLength() {
    return positionLength;
  }

  public int getSpaceLength() {
    if (node == null) return 0;
    return node.getRlength() - node.getLength();
  }

  public int getLength() {
    return getSpaceLength() + getSurfaceLength();
  }

  public boolean isPosIdOf(PosIdManager.PosId posId) {
    return (this.posId == posId);
  }

  public boolean hasSpace() {
    return getSpaceLength() > 0;
  }

  public void setStartOffset(int val) {
    startOffset = val;
  }

  public void setPositionIncr(int val) {
    positionIncr = val;
  }

  public void setPositionLength(int val) {
    positionLength = val;
  }

  public void setPos(String pos) {
    this.pos = pos;
  }

  @Override
  public String toString() {
    return new String(
        surface + "/" +
        posId + "/" +
        semanticClass + "/" +
        positionIncr + "/" + positionLength + "/" +
        getStartOffset() + "/" + getEndOffset());
  }

  private static String convertSemanticClass(String semanticClass) {
    return semanticClass.equals("*") ? null : semanticClass;
  }
}
