package com.lamld.supportmanualtest.server.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    /**
     * Lấy danh sách giá trị của tất cả các key từ một đối tượng JSON.
     *
     * @param jsonObject Đối tượng JSON cần tìm kiếm.
     * @param keyName    Tên key cần tìm kiếm.
     * @param clazz      Class của kiểu dữ liệu cần trả về.
     * @param <T>       Kiểu dữ liệu tổng quát.
     * @return Danh sách các giá trị của các key được chỉ định.
     */
    public static <T> List<T> getValuesByKey(JSONObject jsonObject, String keyName, Class<T> clazz) {
        List<T> values = new ArrayList<>();
        extractValues(jsonObject, values, keyName, clazz);
        return values;
    }

    // Phương thức phụ để đệ quy lấy các giá trị
    private static <T> void extractValues(JSONObject jsonObject, List<T> values, String keyName, Class<T> clazz) {
        for (String key : jsonObject.keySet()) {
            if (key.equals(keyName)) {
                // Chuyển đổi giá trị về kiểu dữ liệu mong muốn
                values.add(clazz.cast(jsonObject.get(key))); // Thêm giá trị vào danh sách nếu key trùng khớp
            }

            // Nếu giá trị là JSONObject, gọi đệ quy
            if (jsonObject.get(key) instanceof JSONObject) {
                extractValues(jsonObject.getJSONObject(key), values, keyName, clazz);
            }

            // Nếu giá trị là JSONArray, lặp qua từng phần tử
            if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object item = jsonArray.get(i);
                    if (item instanceof JSONObject) {
                        extractValues((JSONObject) item, values, keyName, clazz);
                    }
                }
            }
        }
    }
}
