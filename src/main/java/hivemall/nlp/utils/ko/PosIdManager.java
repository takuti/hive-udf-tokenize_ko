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
package hivemall.nlp.utils.ko;

/**
 * mecab-ko-dic의 PosId를 관리하는 클래스
 *
 * @author bibreen <bibreen@gmail.com>
 */
public final class PosIdManager {
  /**
   * PosId와 매핑되는 enum
   */
  public enum PosId {
    UNKNOWN(0),
    COMPOUND(1), INFLECT(2), PREANALYSIS(3),
    E(100), IC(110), J(120), MAG(130), MAJ(131), MM(140), NNG(150), NNP(151),
    NNB(152), NNBC(153), NP(154), NR(155), SF(160), SH(161), SL(162), SN(163),
    SP(164), SSC(165), SSO(166), SC(167), SY(168), SE(169), VA(170), VCN(171),
    VCP(172), VV(173), VX(174), XPN(181), XR(182),  XSA(183), XSN(184),
    XSV(185),
    EOJEOL(900);

    private int num;

    PosId(int num) {
      this.num = num;
    }

    public int getNum() {
      return num;
    }

    public static PosId convertFrom(int posIdNum) {
      switch(posIdNum) {
      case 0: return PosId.UNKNOWN;
      case 1: return PosId.COMPOUND;
      case 2: return PosId.INFLECT;
      case 3: return PosId.PREANALYSIS;
      case 100: return PosId.E;
      case 110: return PosId.IC;
      case 120: return PosId.J;
      case 130: return PosId.MAG;
      case 131: return PosId.MAJ;
      case 140: return PosId.MM;
      case 150: return PosId.NNG;
      case 151: return PosId.NNP;
      case 152: return PosId.NNB;
      case 153: return PosId.NNBC;
      case 154: return PosId.NP;
      case 155: return PosId.NR;
      case 160: return PosId.SF;
      case 161: return PosId.SH;
      case 162: return PosId.SL;
      case 163: return PosId.SN;
      case 164: return PosId.SP;
      case 165: return PosId.SSC;
      case 166: return PosId.SSO;
      case 167: return PosId.SC;
      case 168: return PosId.SY;
      case 169: return PosId.SE;
      case 170: return PosId.VA;
      case 171: return PosId.VCN;
      case 172: return PosId.VCP;
      case 173: return PosId.VV;
      case 174: return PosId.VX;
      case 181: return PosId.XPN;
      case 182: return PosId.XR;
      case 183: return PosId.XSA;
      case 184: return PosId.XSN;
      case 185: return PosId.XSV;
      case 900: return PosId.EOJEOL;
      default:
        if (200 <= posIdNum && posIdNum < 300) {
          return PosId.INFLECT;
        } else {
          return PosId.UNKNOWN;
        }
      }
    }

    public static PosId convertFrom(String tagString) {
      tagString = tagString.toUpperCase();
      try {
        if (tagString.charAt(0) == 'J') {
          return PosId.J;
        } else if (tagString.charAt(0) == 'E' && tagString.charAt(1) != 'J') {
          return PosId.E;
        } else {
          return PosId.valueOf(tagString);
        }
      } catch(Exception e) {
        return PosId.UNKNOWN;
      }
    }

    public boolean in(PosId start, PosId end) {
      return start.getNum() <= this.getNum()&& this.getNum() <= end.getNum();
    }
  }
}
