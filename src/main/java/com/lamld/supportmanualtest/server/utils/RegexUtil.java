package com.lamld.supportmanualtest.server.utils;

import com.mifmif.common.regex.Generex;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@UtilityClass
@Log4j2
public class RegexUtil {

  public static String generateRegexValue(String regex) {
    try {
      log.debug("Generating regex value: {}", regex);
      // Kiểm tra regex có hợp lệ không
      if (regex == null || regex.trim().isEmpty()) {
        log.warn("Provided regex is null or empty");
        return ""; // Hoặc trả về giá trị mặc định nào đó
      }

      // Kiểm tra tính hợp lệ của regex
      Pattern.compile(regex); // Nếu regex không hợp lệ, sẽ ném ra PatternSyntaxException

      // Khởi tạo Generex
      Generex generex = new Generex(regex);

      // Lấy một giá trị ngẫu nhiên duy nhất từ regex
      String value = generex.random(); // Chỉ lấy 1 giá trị
      return value != null ? value : ""; // Trả về giá trị hoặc giá trị mặc định
    } catch (PatternSyntaxException e) {
      log.error("Invalid regex syntax: {}", e.getMessage());
      return ""; // Trả về giá trị mặc định nếu có lỗi
    } catch (StackOverflowError e) {
      log.error("StackOverflowError occurred: {}", e.getMessage());
      return ""; // Trả về giá trị mặc định nếu có lỗi
    } catch (Exception e) {
      log.error("Failed to generate regex value: {}", e.getMessage());
      return ""; // Trả về giá trị mặc định nếu có lỗi
    }
  }
}
