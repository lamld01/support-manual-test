package com.lamld.supportmanualtest.server.utils;

import com.mifmif.common.regex.Generex;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {

  public static String generateRegexValue(String regex) {
    Generex generex = new Generex(regex);
    return generex.random();
  }
}
